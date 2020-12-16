package com.koryshev.widgets.util;

import lombok.experimental.UtilityClass;

import java.awt.*;


@UtilityClass
public class WidgetUtil {

    public static Rectangle createRectangle(Integer x, Integer y, Integer width, Integer height) {
        int xTopLeft = x - width / 2;
        int yTopLeft = y + height / 2;
        return new Rectangle(xTopLeft, yTopLeft, width, height);
    }

    public static boolean rectangleContains(Rectangle rectangle, Rectangle widget) {
        return (widget.getX() >= rectangle.getX()
                && widget.getY() <= rectangle.getY()
                && (widget.getX() + widget.getWidth()) <= (rectangle.getX() + rectangle.getWidth())
                && (widget.getY() + widget.getHeight()) <= (rectangle.getY() + rectangle.getHeight()));
    }
}
