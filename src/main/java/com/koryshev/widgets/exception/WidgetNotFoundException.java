package com.koryshev.widgets.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a widget can't be found.
 *
 * @author Ivan Koryshev
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class WidgetNotFoundException extends RuntimeException {

    public WidgetNotFoundException() {
        super("Widget not found");
    }
}
