package com.koryshev.widgets.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * A complete DTO representing a widget.
 *
 * @author Ivan Koryshev
 */
@Getter
@Setter
public class WidgetResponseDto {

    private UUID id;

    private Integer x;

    private Integer y;

    private Integer z;

    private Integer width;

    private Integer height;

    private Instant createdDate;

    private Instant lastModifiedDate;
}
