package com.koryshev.widgets.service;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.domain.repository.WidgetRepository;
import com.koryshev.widgets.dto.WidgetPageRequestDto;
import com.koryshev.widgets.dto.WidgetRequestDto;
import com.koryshev.widgets.dto.mapper.WidgetMapper;
import com.koryshev.widgets.exception.WidgetNotFoundException;
import com.koryshev.widgets.util.WidgetUtil;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.awt.*;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

/**
 * Manages {@link Widget} entries.
 *
 * @author Ivan Koryshev
 */
@Slf4j
@NoArgsConstructor
public class WidgetService {

    @Setter
    private WidgetMapper widgetMapper;

    @Setter
    private WidgetRepository widgetRepository;

    /**
     * Creates a new widget from data specified in a DTO.
     *
     * @param dto the DTO containing widget details
     * @return the created widget
     */
    public Widget create(WidgetRequestDto dto) {
        log.info("Creating widget, z-index {}", dto.getZ());
        Widget widget = widgetMapper.fromWidgetRequestDto(dto);

        if (widget.getZ() == null) {
            Integer z = widgetRepository.findMaxZ().orElse(0) + 1;
            log.info("Z-index for new widget is not specified, setting value automatically to {}", z);
            widget.setZ(z);
        } else {
            shift(dto.getZ());
        }

        widget = widgetRepository.save(widget);
        log.info("Created widget {}", widget.getId());
        return widget;
    }

    /**
     * Updates a widget with data specified in a DTO.
     *
     * @param widgetId the widget ID to update
     * @param dto      the DTO containing data to update
     * @return the updated widget
     * @throws WidgetNotFoundException if such widget doesn't exist
     */
    public Widget update(UUID widgetId, WidgetRequestDto dto) {
        log.info("Updating widget {}, z-index {}", widgetId, dto.getZ());
        Widget widget = widgetRepository.findById(widgetId)
                .orElseThrow(() -> {
                    log.warn("Attempted to update widget {}, which was not found", widgetId);
                    return new WidgetNotFoundException();
                });

        if (dto.getZ() == null) {
            Integer z = widgetRepository.findMaxZ().orElse(0) + 1;
            log.info("Updating z-index for widget {}, setting value automatically to {}", widgetId, z);
            dto.setZ(z);
            widgetRepository.updateZ(widget.getZ(), dto.getZ());
        } else if (!widget.getZ().equals(dto.getZ())) {
            log.info("Updating z-index for widget {}, from {} to {}", widgetId, widget.getZ(), dto.getZ());
            shift(dto.getZ());
            widgetRepository.updateZ(widget.getZ(), dto.getZ());
        }

        widget.setX(dto.getX());
        widget.setY(dto.getY());
        widget.setZ(dto.getZ());
        widget.setWidth(dto.getWidth());
        widget.setHeight(dto.getHeight());
        widget = widgetRepository.save(widget);
        log.info("Updated widget {}", widget.getId());
        return widget;
    }

    /**
     * Deletes a widget with the specified ID.
     *
     * @param widgetId the widget ID to delete
     * @throws WidgetNotFoundException if such widget doesn't exist
     */
    public void delete(UUID widgetId) {
        log.info("Deleting widget {}", widgetId);
        widgetRepository.findById(widgetId)
                .orElseThrow(() -> {
                    log.warn("Attempted to delete widget {}, which was not found", widgetId);
                    return new WidgetNotFoundException();
                });

        widgetRepository.deleteById(widgetId);
    }

    /**
     * Returns a widget with the specified ID.
     *
     * @param widgetId the widget ID to return
     * @return the widget
     * @throws WidgetNotFoundException if such widget doesn't exist
     */
    public Widget findOne(UUID widgetId) {
        log.info("Getting widget {}", widgetId);
        Widget widget = widgetRepository.findById(widgetId)
                .orElseThrow(() -> {
                    log.warn("Attempted to get widget {}, which was not found", widgetId);
                    return new WidgetNotFoundException();
                });
        log.info("Returning widget {}", widgetId);
        return widget;
    }

    /**
     * Returns a list of widgets based on the specified filter.
     *
     * @return the widgets list
     */
    public Page<Widget> findAll(Integer page, Integer size, WidgetPageRequestDto dto) {
        log.info("Getting all widgets, page {}, size {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "z");

        if (dto == null) {
            return findAll(pageable);
        }

        // Find center, width and height for the specified rectangle
        Integer x = (dto.getXBottomLeft() + dto.getXTopRight()) / 2;
        Integer y = (dto.getYBottomLeft() + dto.getYTopRight()) / 2;
        Integer width = dto.getXTopRight() - dto.getXBottomLeft();
        Integer height = dto.getYTopRight() - dto.getYBottomLeft();

        // Filter widgets by width and height
        List<Widget> widgets =
                widgetRepository.findAllByWidthLessThanEqualAndHeightLessThanEqualOrderByZAsc(width, height);
        if (widgets.isEmpty()) {
            log.info("No matching widgets found, returning empty page");
            return Page.empty(pageable);
        }
        log.info("Filtered {} widgets by width and height", widgets.size());

        // Apply filtering
        Rectangle rectangle = WidgetUtil.createRectangle(x, y, width, height);
        List<Widget> filteredWidgets = widgets.stream()
                .filter(widget -> {
                    Rectangle widgetRectangle = WidgetUtil.createRectangle(
                            widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
                    return WidgetUtil.rectangleContains(rectangle, widgetRectangle);
                })
                .collect(toList());
        if (filteredWidgets.isEmpty()) {
            log.info("No matching widgets found, returning empty page");
            return Page.empty(pageable);
        }
        log.info("Filtered {} widgets for specified rectangle", filteredWidgets.size());

        // Apply pagination
        int total = filteredWidgets.size();
        int from = (int) pageable.getOffset();
        int to = from + pageable.getPageSize();

        if (from > total) {
            log.info("No matching widgets found, returning empty page");
            return Page.empty(pageable);
        }
        if (to > total) {
            to = total;
        }

        List<Widget> content = filteredWidgets.subList(from, to);
        log.info("Returning {} widgets", content.size());
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Returns a list of all widgets.
     *
     * @return the widgets list
     */
    private Page<Widget> findAll(Pageable pageable) {
        Page<Widget> widgets = widgetRepository.findAll(pageable);
        log.info("Returning {} widgets", widgets.getNumberOfElements());
        return widgets;
    }

    /**
     * Shifts existing widgets upwards, if needed.
     *
     * @param z the z-index specified in the request
     */
    private void shift(Integer z) {
        widgetRepository.findByZ(z)
                .ifPresentOrElse(foundWidget -> {
                    shift(z + 1);
                    log.info("Shifting upwards existing widget with z-index {}", z);
                    widgetRepository.shiftZ(z);
                }, () -> log.info("No widgets found with z-index {}", z));
    }
}
