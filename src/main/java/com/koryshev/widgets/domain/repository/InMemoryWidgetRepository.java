package com.koryshev.widgets.domain.repository;

import com.koryshev.widgets.domain.model.Widget;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

/**
 * An in-memory repository for accessing {@link Widget}.
 *
 * @author Ivan Koryshev
 */
@Profile("in-memory")
@Repository
public class InMemoryWidgetRepository implements WidgetRepository {

    private final Map<UUID, Widget> repository = new HashMap<>();
    private final TreeMap<Integer, Widget> orderedRepository = new TreeMap<>();

    @Override
    public Widget save(Widget widget) {
        if (widget.getId() == null) {
            UUID id = UUID.randomUUID();
            Instant createdDate = Instant.now();
            widget.setId(id);
            widget.setCreatedDate(createdDate);
        }

        Instant lastModifiedDate = Instant.now();
        widget.setLastModifiedDate(lastModifiedDate);

        repository.put(widget.getId(), widget);
        orderedRepository.put(widget.getZ(), widget);
        return widget;
    }

    @Override
    public void deleteById(UUID id) {
        Widget widget = repository.remove(id);
        orderedRepository.remove(widget.getZ());
    }

    @Override
    public void deleteAll() {
        repository.clear();
        orderedRepository.clear();
    }

    @Override
    public Optional<Widget> findById(UUID widgetId) {
        return Optional.ofNullable(repository.get(widgetId));
    }

    @Override
    public Optional<Widget> findByZ(Integer z) {
        return Optional.ofNullable(orderedRepository.get(z));
    }

    @Override
    public Optional<Integer> findMaxZ() {
        if (orderedRepository.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(orderedRepository.lastKey());
    }

    @Override
    public Page<Widget> findAll(Pageable pageable) {
        List<Widget> widgets = new ArrayList<>(orderedRepository.values());

        int total = widgets.size();
        int from = (int) pageable.getOffset();
        int to = from + pageable.getPageSize();

        if (from > total) {
            return Page.empty();
        }
        if (to > total) {
            to = total;
        }

        List<Widget> content = widgets.subList(from, to);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public void shiftZ(Integer z) {
        updateZ(z, z + 1);
    }

    @Override
    public void updateZ(Integer oldValue, Integer newValue) {
        Widget widget = orderedRepository.remove(oldValue);

        widget.setZ(newValue);
        widget.setLastModifiedDate(Instant.now());

        orderedRepository.put(widget.getZ(), widget);
        repository.put(widget.getId(), widget);
    }
}
