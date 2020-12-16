package com.koryshev.widgets.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A DTO representing a page with a list of widgets.
 *
 * @author Ivan Koryshev
 */
@Getter
@Setter
@Builder
public class WidgetPageResponseDto {
    private Integer totalElements;
    private Integer number;
    private Integer size;
    private List<WidgetResponseDto> content;
}
