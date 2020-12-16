package com.koryshev.widgets.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * A DTO for filtering widgets.
 *
 * @author Ivan Koryshev
 */
@Getter
@Setter
@Builder
public class WidgetPageRequestDto {

    @NotNull
    private Integer xBottomLeft;

    @NotNull
    private Integer yBottomLeft;

    @NotNull
    private Integer xTopRight;

    @NotNull
    private Integer yTopRight;
}
