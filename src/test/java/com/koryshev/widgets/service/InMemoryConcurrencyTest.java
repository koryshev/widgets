package com.koryshev.widgets.service;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.domain.repository.WidgetRepository;
import com.koryshev.widgets.dto.WidgetRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.koryshev.widgets.util.TestData.createWidgetRequestDtoWithZIndex;
import static com.koryshev.widgets.util.TestData.createWidgetWithZIndex;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class InMemoryConcurrencyTest {

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private WidgetService widgetService;

    @BeforeEach
    public void setup() {
        widgetRepository.deleteAll();
    }

    @Test
    void shouldUpdateWidgetAtomicallyWithBlockingConcurrentReads() throws Exception {
        // Insert widgets
        int widgetsNumber = 1000;
        Widget firstWidget = widgetRepository.save(createWidgetWithZIndex(1));
        Widget lastWidget = widgetRepository.save(createWidgetWithZIndex(widgetsNumber));
        for (int i = 2; i < widgetsNumber; i++) {
            Widget widget = createWidgetWithZIndex(i);
            widgetRepository.save(widget);
        }

        // Submit update for firstWidget that will shift all other widgets upwards
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(2);
            widgetService.update(firstWidget.getId(), requestDto);
            log.info("Update finished");
        });

        // Thread.sleep is added so that the update operation had a chance to acquire a lock before we try to read
        Thread.sleep(5);
        log.info("Verifying that concurrent read is blocked until update is finished");
        Widget shiftedWidget = widgetService.findOne(lastWidget.getId());
        assertThat(shiftedWidget.getZ()).isEqualTo(widgetsNumber + 1);

        log.info("Asserting read after update is finished");
        List<Widget> widgets = widgetService.findAll(0, widgetsNumber + 1, null).getContent();
        assertThat(widgets).hasSize(widgetsNumber);
        assertThat(widgets.get(widgetsNumber - 1).getZ()).isEqualTo(widgetsNumber + 1);
    }
}
