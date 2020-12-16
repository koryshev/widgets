package com.koryshev.widgets.util;

import com.koryshev.widgets.domain.model.Widget;
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

    public static Widget createWidgetWithZIndex(Integer z) {
        return Widget.builder()
                .x(0)
                .y(0)
                .z(z)
                .width(10)
                .height(10)
                .build();
    }
}
