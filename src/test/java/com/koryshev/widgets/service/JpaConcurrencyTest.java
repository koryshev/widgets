package com.koryshev.widgets.service;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.domain.repository.WidgetRepository;
import com.koryshev.widgets.dto.WidgetRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.koryshev.widgets.util.TestData.createWidgetRequestDtoWithZIndex;
import static com.koryshev.widgets.util.TestData.createWidgetWithZIndex;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("jpa")
@SpringBootTest
class JpaConcurrencyTest {

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private WidgetService widgetService;

    @BeforeEach
    public void setup() {
        widgetRepository.deleteAll();
    }

    @Test
    void shouldUpdateWidgetAtomicallyWithConcurrentReadsSupport() throws Exception {
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
        AtomicBoolean updateFinished = new AtomicBoolean(false);
        executorService.submit(() -> {
            WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(2);
            widgetService.update(firstWidget.getId(), requestDto);
            updateFinished.set(true);
            log.info("Update finished");
        });

        // While update is in progress read operations must return old value
        while (!updateFinished.get()) {
            log.info("Verifying concurrent read");
            Widget shiftedWidget = widgetService.findOne(lastWidget.getId());
            // Verify that widget moved upwards in the parallel thread still has an old z-index
            // while update operation is in progress
            if (shiftedWidget.getZ() == widgetsNumber) {
                assertThat(updateFinished.get()).isFalse();
            } else {
                // For JPA repository, there is a short delay between update operation and setting the "updateFinished"
                // flag to true so this extra Thread.sleep is needed to make the test stable
                log.warn("Verifying of concurrent read failed, waiting 100ms and asserting if update is finished");
                Thread.sleep(100);
                assertThat(updateFinished.get()).isTrue();
            }
            Thread.sleep(50);
        }

        log.info("Asserting read after update is finished");
        List<Widget> widgets = widgetService.findAll(0, widgetsNumber + 1).getContent();
        assertThat(widgets).hasSize(widgetsNumber);
        assertThat(widgets.get(widgetsNumber - 1).getZ()).isEqualTo(widgetsNumber + 1);
    }
}
