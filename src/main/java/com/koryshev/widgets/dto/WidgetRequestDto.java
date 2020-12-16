package com.koryshev.widgets.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * A DTO for creating or updating a widget.
 *
 * @author Ivan Koryshev
 */
@Getter
@Setter
@Builder
public class WidgetRequestDto {

    @NotNull
    private Integer x;

    @NotNull
    private Integer y;

    private Integer z;

    @NotNull
    @Positive
    private Integer width;

    @NotNull
    @Positive
    private Integer height;
}
