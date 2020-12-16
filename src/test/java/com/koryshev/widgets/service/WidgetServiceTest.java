package com.koryshev.widgets.service;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.domain.repository.WidgetRepository;
import com.koryshev.widgets.dto.WidgetRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.koryshev.widgets.util.TestData.createWidgetRequestDtoWithZIndex;
import static com.koryshev.widgets.util.TestData.createWidgetWithZIndex;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WidgetServiceTest {

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private WidgetService widgetService;

    @BeforeEach
    public void setup() {
        widgetRepository.deleteAll();
    }

    @Test
    void shouldCreateWidget() {
        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(1);

        Widget widget = widgetService.create(requestDto);

        assertThat(widgetRepository.findById(widget.getId())).isNotEmpty();
    }

    @Test
    void shouldCreateWidgetWithMultipleShifts() {
        Widget widget1 = createWidgetWithZIndex(1);
        Widget widget2 = createWidgetWithZIndex(2);
        Widget widget3 = createWidgetWithZIndex(3);
        widgetRepository.save(widget1);
        widgetRepository.save(widget2);
        widgetRepository.save(widget3);

        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(2);

        Widget newWidget = widgetService.create(requestDto);

        List<Widget> widgets = widgetService.findAll(0, 10).getContent();
        assertThat(widgets.get(0).getZ()).isEqualTo(1);
        assertThat(widgets.get(0).getId()).isEqualTo(widget1.getId());
        assertThat(widgets.get(1).getZ()).isEqualTo(2);
        assertThat(widgets.get(1).getId()).isEqualTo(newWidget.getId());
        assertThat(widgets.get(2).getZ()).isEqualTo(3);
        assertThat(widgets.get(2).getId()).isEqualTo(widget2.getId());
        assertThat(widgets.get(3).getZ()).isEqualTo(4);
        assertThat(widgets.get(3).getId()).isEqualTo(widget3.getId());
    }

    @Test
    void shouldCreateWidgetWithoutShifts() {
        Widget widget1 = createWidgetWithZIndex(1);
        Widget widget2 = createWidgetWithZIndex(5);
        Widget widget3 = createWidgetWithZIndex(6);
        widgetRepository.save(widget1);
        widgetRepository.save(widget2);
        widgetRepository.save(widget3);

        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(2);

        Widget newWidget = widgetService.create(requestDto);

        List<Widget> widgets = widgetService.findAll(0, 10).getContent();
        assertThat(widgets.get(0).getZ()).isEqualTo(1);
        assertThat(widgets.get(0).getId()).isEqualTo(widget1.getId());
        assertThat(widgets.get(1).getZ()).isEqualTo(2);
        assertThat(widgets.get(1).getId()).isEqualTo(newWidget.getId());
        assertThat(widgets.get(2).getZ()).isEqualTo(5);
        assertThat(widgets.get(2).getId()).isEqualTo(widget2.getId());
        assertThat(widgets.get(3).getZ()).isEqualTo(6);
        assertThat(widgets.get(3).getId()).isEqualTo(widget3.getId());
    }

    @Test
    void shouldCreateWidgetWithOneShift() {
        Widget widget1 = createWidgetWithZIndex(1);
        Widget widget2 = createWidgetWithZIndex(2);
        Widget widget3 = createWidgetWithZIndex(4);
        widgetRepository.save(widget1);
        widgetRepository.save(widget2);
        widgetRepository.save(widget3);

        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(2);

        Widget newWidget = widgetService.create(requestDto);

        List<Widget> widgets = widgetService.findAll(0, 10).getContent();
        assertThat(widgets.get(0).getZ()).isEqualTo(1);
        assertThat(widgets.get(0).getId()).isEqualTo(widget1.getId());
        assertThat(widgets.get(1).getZ()).isEqualTo(2);
        assertThat(widgets.get(1).getId()).isEqualTo(newWidget.getId());
        assertThat(widgets.get(2).getZ()).isEqualTo(3);
        assertThat(widgets.get(2).getId()).isEqualTo(widget2.getId());
        assertThat(widgets.get(3).getZ()).isEqualTo(4);
        assertThat(widgets.get(3).getId()).isEqualTo(widget3.getId());
    }

    @Test
    void shouldUpdateWidget() {
        Widget widget = createWidgetWithZIndex(1);
        widget = widgetRepository.save(widget);

        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(2);

        widgetService.update(widget.getId(), requestDto);

        Optional<Widget> updatedWidgetOptional = widgetRepository.findById(widget.getId());
        assertThat(updatedWidgetOptional).isNotEmpty();
        assertThat(updatedWidgetOptional.get().getZ()).isEqualTo(2);
    }

    @Test
    void shouldUpdateWidgetWithMultipleShifts() {
        Widget widget1 = createWidgetWithZIndex(1);
        Widget widget2 = createWidgetWithZIndex(2);
        Widget widget3 = createWidgetWithZIndex(3);
        widgetRepository.save(widget1);
        widgetRepository.save(widget2);
        widgetRepository.save(widget3);

        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(2);

        widgetService.update(widget1.getId(), requestDto);

        List<Widget> widgets = widgetService.findAll(0, 10).getContent();
        assertThat(widgets.get(0).getZ()).isEqualTo(2);
        assertThat(widgets.get(0).getId()).isEqualTo(widget1.getId());
        assertThat(widgets.get(1).getZ()).isEqualTo(3);
        assertThat(widgets.get(1).getId()).isEqualTo(widget2.getId());
        assertThat(widgets.get(2).getZ()).isEqualTo(4);
        assertThat(widgets.get(2).getId()).isEqualTo(widget3.getId());
    }

    @Test
    void shouldUpdateWidgetWithoutShifts() {
        Widget widget1 = createWidgetWithZIndex(1);
        Widget widget2 = createWidgetWithZIndex(5);
        Widget widget3 = createWidgetWithZIndex(6);
        widgetRepository.save(widget1);
        widgetRepository.save(widget2);
        widgetRepository.save(widget3);

        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(2);

        widgetService.update(widget1.getId(), requestDto);

        List<Widget> widgets = widgetService.findAll(0, 10).getContent();
        assertThat(widgets.get(0).getZ()).isEqualTo(2);
        assertThat(widgets.get(0).getId()).isEqualTo(widget1.getId());
        assertThat(widgets.get(1).getZ()).isEqualTo(5);
        assertThat(widgets.get(1).getId()).isEqualTo(widget2.getId());
        assertThat(widgets.get(2).getZ()).isEqualTo(6);
        assertThat(widgets.get(2).getId()).isEqualTo(widget3.getId());
    }

    @Test
    void shouldUpdateWidgetWithOneShift() {
        Widget widget1 = createWidgetWithZIndex(1);
        Widget widget2 = createWidgetWithZIndex(2);
        Widget widget3 = createWidgetWithZIndex(4);
        widgetRepository.save(widget1);
        widgetRepository.save(widget2);
        widgetRepository.save(widget3);

        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(2);

        widgetService.update(widget1.getId(), requestDto);

        List<Widget> widgets = widgetService.findAll(0, 10).getContent();
        assertThat(widgets.get(0).getZ()).isEqualTo(2);
        assertThat(widgets.get(0).getId()).isEqualTo(widget1.getId());
        assertThat(widgets.get(1).getZ()).isEqualTo(3);
        assertThat(widgets.get(1).getId()).isEqualTo(widget2.getId());
        assertThat(widgets.get(2).getZ()).isEqualTo(4);
        assertThat(widgets.get(2).getId()).isEqualTo(widget3.getId());
    }

    @Test
    void shouldDeleteWidget() {
        Widget widget = createWidgetWithZIndex(1);
        widget = widgetRepository.save(widget);

        widgetService.delete(widget.getId());

        assertThat(widgetRepository.findById(widget.getId())).isEmpty();
    }

    @Test
    void shouldFindOneWidget() {
        Widget widget = createWidgetWithZIndex(1);
        widget = widgetRepository.save(widget);

        assertThat(widgetService.findOne(widget.getId())).isNotNull();
    }

    @Test
    void shouldFindAllWidgets() {
        Widget widget1 = createWidgetWithZIndex(0);
        Widget widget2 = createWidgetWithZIndex(2);
        Widget widget3 = createWidgetWithZIndex(1);
        widgetRepository.save(widget1);
        widgetRepository.save(widget2);
        widgetRepository.save(widget3);

        List<Widget> widgets = widgetService.findAll(0, 10).getContent();

        assertThat(widgets).hasSize(3);
        // assert sort order
        assertThat(widgets.get(0).getZ()).isEqualTo(0);
        assertThat(widgets.get(1).getZ()).isEqualTo(1);
        assertThat(widgets.get(2).getZ()).isEqualTo(2);
    }
}
