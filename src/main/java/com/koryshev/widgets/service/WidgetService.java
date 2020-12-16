package com.koryshev.widgets.service;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.domain.repository.WidgetRepository;
import com.koryshev.widgets.dto.WidgetRequestDto;
import com.koryshev.widgets.exception.WidgetNotFoundException;
import com.koryshev.widgets.dto.mapper.WidgetMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Manages {@link Widget} entries.
 *
 * @author Ivan Koryshev
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetMapper widgetMapper;

    private final WidgetRepository widgetRepository;

    /**
     * Creates a new widget from data specified in a DTO.
     *
     * @param dto the DTO containing widget details
     * @return the created widget
     */
    @Transactional
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
    @Transactional
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
        } else if (!widget.getZ().equals(dto.getZ())) {
            log.info("Updating z-index for widget {}, from {} to {}", widgetId, widget.getZ(), dto.getZ());
            shift(dto.getZ());
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
     * Returns a list of all widgets.
     *
     * @return the widgets list
     */
    public List<Widget> findAll() {
        log.info("Getting all widgets");
        List<Widget> widgets = widgetRepository.findAllByOrderByZAsc();
        log.info("Returning {} widgets", widgets.size());
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
                    log.info("Found existing widget with z-index {}, shifting it upwards", z);
                    shift(z + 1);
                    widgetRepository.updateZ(z);
                }, () -> log.info("No widgets found with z-index {}, shifting finished", z));
    }
}