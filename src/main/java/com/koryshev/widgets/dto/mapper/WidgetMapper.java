package com.koryshev.widgets.dto.mapper;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.dto.WidgetRequestDto;
import com.koryshev.widgets.dto.WidgetResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Maps {@link Widget} entities to and from various DTOs.
 *
 * @author Ivan Koryshev
 */
@Mapper(componentModel = "spring")
public interface WidgetMapper {

    Widget fromWidgetRequestDto(WidgetRequestDto dto);

    WidgetResponseDto toWidgetResponseDto(Widget widget);

    List<WidgetResponseDto> toWidgetResponseDto(List<Widget> widgets);
}
