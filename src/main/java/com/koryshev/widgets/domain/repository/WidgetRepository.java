package com.koryshev.widgets.domain.repository;

import com.koryshev.widgets.domain.model.Widget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface WidgetRepository {

    Widget save(Widget widget);

    void deleteById(UUID id);

    void deleteAll();

    Optional<Widget> findById(UUID widgetId);

    Optional<Widget> findByZ(Integer z);

    Optional<Integer> findMaxZ();

    Page<Widget> findAll(Pageable pageable);

    void updateZ(Integer z);
}
