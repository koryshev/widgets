package com.koryshev.widgets.service;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.domain.repository.WidgetRepository;
import com.koryshev.widgets.dto.WidgetPageRequestDto;
import com.koryshev.widgets.dto.WidgetRequestDto;
import com.koryshev.widgets.dto.mapper.WidgetMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Manages {@link Widget} entries using a JPA implementation of @{link {@link WidgetRepository}}.
 *
 * @author Ivan Koryshev
 */
@Profile("jpa")
@Service
@Transactional
public class JpaWidgetService extends WidgetService {

    public JpaWidgetService(WidgetMapper widgetMapper, WidgetRepository widgetRepository) {
        super();
        super.setWidgetMapper(widgetMapper);
        super.setWidgetRepository(widgetRepository);
    }

    @Override
    public Widget create(WidgetRequestDto dto) {
        return super.create(dto);
    }

    @Override
    public Widget update(UUID widgetId, WidgetRequestDto dto) {
        return super.update(widgetId, dto);
    }

    @Override
    public void delete(UUID widgetId) {
        super.delete(widgetId);
    }

    @Override
    public Widget findOne(UUID widgetId) {
        return super.findOne(widgetId);
    }

    @Override
    public Page<Widget> findAll(Integer page, Integer size, WidgetPageRequestDto dto) {
        return super.findAll(page, size, dto);
    }
}
