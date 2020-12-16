package com.koryshev.widgets.domain.repository;

import com.koryshev.widgets.domain.model.Widget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.koryshev.widgets.util.TestData.createWidgetWithZIndex;
import static org.assertj.core.api.Assertions.assertThat;

class InMemoryWidgetRepositoryTest {

    private final InMemoryWidgetRepository repository = new InMemoryWidgetRepository();

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveWidget() {
        Widget widget = createWidgetWithZIndex(1);
        widget = repository.save(widget);

        assertThat(widget.getId()).isNotNull();
        assertThat(widget.getCreatedDate()).isNotNull();
        assertThat(widget.getLastModifiedDate()).isNotNull();
        assertThat(repository.findById(widget.getId())).isNotEmpty();
    }

    @Test
    void shouldDeleteWidget() {
        Widget widget = createWidgetWithZIndex(1);
        widget = repository.save(widget);

        assertThat(widget.getId()).isNotNull();
        assertThat(repository.findById(widget.getId())).isNotEmpty();

        repository.deleteById(widget.getId());
        assertThat(repository.findById(widget.getId())).isEmpty();
    }

    @Test
    void shouldFindWidgetByZIndex() {
        Widget widget = createWidgetWithZIndex(1);
        widget = repository.save(widget);

        assertThat(widget.getId()).isNotNull();
        assertThat(repository.findById(widget.getId())).isNotEmpty();

        assertThat(repository.findByZ(1)).isNotEmpty();
    }

    @Test
    void shouldFindMaxZIndex() {
        Widget widget1 = createWidgetWithZIndex(1);
        Widget widget2 = createWidgetWithZIndex(2);
        Widget widget3 = createWidgetWithZIndex(3);
        repository.save(widget1);
        repository.save(widget2);
        repository.save(widget3);

        assertThat(repository.findMaxZ()).contains(3);
    }

    @Test
    void shouldFindAllWidgets() {
        Widget widget1 = createWidgetWithZIndex(1);
        Widget widget2 = createWidgetWithZIndex(2);
        Widget widget3 = createWidgetWithZIndex(3);
        repository.save(widget1);
        repository.save(widget2);
        repository.save(widget3);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "z");

        assertThat(repository.findAll(pageable).getTotalElements()).isEqualTo(3);
        assertThat(repository.findAll(pageable).getContent()).hasSize(3);
    }

    @Test
    void shouldShiftZIndex() {
        Widget widget = createWidgetWithZIndex(1);
        repository.save(widget);

        repository.shiftZ(1);

        assertThat(repository.findByZ(2)).isNotEmpty();
    }


    @Test
    void shouldUpdateZIndex() {
        Widget widget = createWidgetWithZIndex(1);
        repository.save(widget);

        repository.updateZ(1, 10);

        assertThat(repository.findByZ(10)).isNotEmpty();
    }
}
