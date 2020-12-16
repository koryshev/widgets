package com.koryshev.widgets.contoller;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.dto.WidgetRequestDto;
import com.koryshev.widgets.dto.WidgetResponseDto;
import com.koryshev.widgets.dto.mapper.WidgetMapper;
import com.koryshev.widgets.service.WidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Provides an API for widgets.
 *
 * @author Ivan Koryshev
 */
@RestController
@RequestMapping("/v1/widgets")
@RequiredArgsConstructor
public class WidgetController {

    private final WidgetService widgetService;

    private final WidgetMapper widgetMapper;

    /**
     * Creates a new widget from data specified in a DTO.
     *
     * @param dto the DTO containing widget details
     * @return the created widget
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WidgetResponseDto create(@RequestBody @Valid WidgetRequestDto dto) {
        Widget widget = widgetService.create(dto);
        return widgetMapper.toWidgetResponseDto(widget);
    }

    /**
     * Updates a widget with data specified in a DTO.
     *
     * @param widgetId the widget ID to update
     * @param dto      the DTO containing data to update
     */
    @PutMapping("/{widgetId}")
    public WidgetResponseDto update(@PathVariable UUID widgetId, @RequestBody @Valid WidgetRequestDto dto) {
        Widget widget = widgetService.update(widgetId, dto);
        return widgetMapper.toWidgetResponseDto(widget);
    }

    /**
     * Deletes a widget with the specified ID.
     *
     * @param widgetId the widget ID to delete
     */
    @DeleteMapping("/{widgetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID widgetId) {
        widgetService.delete(widgetId);
    }

    /**
     * Returns a widget with the specified ID.
     *
     * @param widgetId the widget ID to return
     * @return the widget
     */
    @GetMapping("/{widgetId}")
    public WidgetResponseDto get(@PathVariable UUID widgetId) {
        Widget widget = widgetService.findOne(widgetId);
        return widgetMapper.toWidgetResponseDto(widget);
    }

    /**
     * Returns a list of all widgets.
     *
     * @return the widgets list
     */
    @GetMapping
    public List<WidgetResponseDto> get() {
        List<Widget> widgets = widgetService.findAll();
        return widgetMapper.toWidgetResponseDto(widgets);
    }
}
