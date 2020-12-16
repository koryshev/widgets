package com.koryshev.widgets.domain.repository;

import com.koryshev.widgets.domain.model.Widget;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WidgetRepository {

    Widget save(Widget widget);

    void deleteById(UUID id);

    void deleteAll();

    Optional<Widget> findById(UUID widgetId);

    Optional<Widget> findByZ(Integer z);

    Optional<Integer> findMaxZ();

    List<Widget> findAllByOrderByZAsc();

    void updateZ(Integer z);
}
