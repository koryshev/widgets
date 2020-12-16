package com.koryshev.widgets.service;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.domain.repository.WidgetRepository;
import com.koryshev.widgets.dto.WidgetRequestDto;
import com.koryshev.widgets.dto.mapper.WidgetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Manages {@link Widget} entries using an in-memory implementation of @{link {@link WidgetRepository}}.
 *
 * @author Ivan Koryshev
 */
@Slf4j
@Profile("in-memory")
@Service
public class InMemoryWidgetService extends WidgetService {

    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    public InMemoryWidgetService(WidgetMapper widgetMapper, WidgetRepository widgetRepository) {
        super();
        super.setWidgetMapper(widgetMapper);
        super.setWidgetRepository(widgetRepository);
    }

    @Override
    public Widget create(WidgetRequestDto dto) {
        try {
            lock.writeLock().lock();
            return super.create(dto);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Widget update(UUID widgetId, WidgetRequestDto dto) {
        try {
            lock.writeLock().lock();
            return super.update(widgetId, dto);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(UUID widgetId) {
        try {
            lock.writeLock().lock();
            super.delete(widgetId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Widget findOne(UUID widgetId) {
        try {
            lock.readLock().lock();
            return super.findOne(widgetId);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Page<Widget> findAll(Integer page, Integer size) {
        try {
            lock.readLock().lock();
            return super.findAll(page, size);
        } finally {
            lock.readLock().unlock();
        }
    }
}
