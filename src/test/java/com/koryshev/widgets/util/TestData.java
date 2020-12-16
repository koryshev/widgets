package com.koryshev.widgets.util;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.dto.WidgetPageRequestDto;
import com.koryshev.widgets.dto.WidgetRequestDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestData {

    public static WidgetRequestDto createWidgetRequestDtoWithZIndex(Integer z) {
        return WidgetRequestDto.builder()
                .x(0)
                .y(0)
                .z(z)
                .width(10)
                .height(10)
                .build();
    }

    public static WidgetPageRequestDto createWidgetPageRequestDto(
            Integer xBottomLeft, Integer yBottomLeft, Integer xTopRight, Integer yTopRight) {
        return WidgetPageRequestDto.builder()
                .xBottomLeft(xBottomLeft)
                .yBottomLeft(yBottomLeft)
                .xTopRight(xTopRight)
                .yTopRight(yTopRight)
                .build();
    }

    public static Widget createWidgetWithZIndex(Integer z) {
        return createWidget(0, 0, z, 10, 10);
    }

    public static Widget createWidget(Integer x, Integer y, Integer z, Integer width, Integer height) {
        return Widget.builder()
                .x(x)
                .y(y)
                .z(z)
                .width(width)
                .height(height)
                .build();
    }
}
