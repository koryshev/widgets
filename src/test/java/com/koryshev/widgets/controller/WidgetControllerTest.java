package com.koryshev.widgets.controller;

import com.koryshev.widgets.domain.model.Widget;
import com.koryshev.widgets.domain.repository.WidgetRepository;
import com.koryshev.widgets.dto.WidgetPageResponseDto;
import com.koryshev.widgets.dto.WidgetRequestDto;
import com.koryshev.widgets.dto.WidgetResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static com.koryshev.widgets.util.TestData.createWidgetRequestDtoWithZIndex;
import static com.koryshev.widgets.util.TestData.createWidgetWithZIndex;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WidgetControllerTest {

    private static final String API_BASE_PATH = "/v1/widgets/";

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        widgetRepository.deleteAll();
    }

    @Test
    void shouldCreateWidget() {
        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(1);

        HttpEntity<WidgetRequestDto> entity = new HttpEntity<>(requestDto);
        ResponseEntity<WidgetResponseDto> response = restTemplate.exchange(
                API_BASE_PATH, HttpMethod.POST, entity, WidgetResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getZ()).isEqualTo(1);
        assertThat(response.getBody().getCreatedDate()).isNotNull();
        assertThat(response.getBody().getLastModifiedDate()).isNotNull();

        assertThat(widgetRepository.findById(response.getBody().getId())).isNotEmpty();
    }

    @Test
    void shouldCreateWidgetWhenZIndexNotSpecified() {
        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(null);

        HttpEntity<WidgetRequestDto> entity = new HttpEntity<>(requestDto);
        ResponseEntity<WidgetResponseDto> response = restTemplate.exchange(
                API_BASE_PATH, HttpMethod.POST, entity, WidgetResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getZ()).isEqualTo(1);

        assertThat(widgetRepository.findById(response.getBody().getId())).isNotEmpty();
    }

    @Test
    void shouldNotCreateWidgetWithInvalidHeight() {
        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(1);
        requestDto.setHeight(0);

        HttpEntity<WidgetRequestDto> entity = new HttpEntity<>(requestDto);
        ResponseEntity<Void> response = restTemplate.exchange(
                API_BASE_PATH, HttpMethod.POST, entity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotCreateWidgetWithInvalidWidth() {
        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(1);
        requestDto.setWidth(0);

        HttpEntity<WidgetRequestDto> entity = new HttpEntity<>(requestDto);
        ResponseEntity<Void> response = restTemplate.exchange(
                API_BASE_PATH, HttpMethod.POST, entity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUpdateWidget() {
        Widget widget = createWidgetWithZIndex(1);
        widget = widgetRepository.save(widget);

        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(2);

        HttpEntity<WidgetRequestDto> entity = new HttpEntity<>(requestDto);
        ResponseEntity<WidgetResponseDto> response = restTemplate.exchange(
                API_BASE_PATH + widget.getId(), HttpMethod.PUT, entity, WidgetResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getZ()).isEqualTo(2);
    }

    @Test
    void shouldUpdateWidgetWhenZIndexNotSpecified() {
        Widget widget = createWidgetWithZIndex(1);
        widget = widgetRepository.save(widget);

        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(null);

        HttpEntity<WidgetRequestDto> entity = new HttpEntity<>(requestDto);
        ResponseEntity<WidgetResponseDto> response = restTemplate.exchange(
                API_BASE_PATH + widget.getId(), HttpMethod.PUT, entity, WidgetResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getZ()).isEqualTo(2);
    }

    @Test
    void shouldNotUpdateWidgetByNonExistingId() {
        WidgetRequestDto requestDto = createWidgetRequestDtoWithZIndex(1);

        HttpEntity<WidgetRequestDto> entity = new HttpEntity<>(requestDto);
        ResponseEntity<Void> response = restTemplate.exchange(
                API_BASE_PATH + UUID.randomUUID(), HttpMethod.PUT, entity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDeleteWidgetById() {
        Widget widget = createWidgetWithZIndex(1);
        widget = widgetRepository.save(widget);

        ResponseEntity<Void> response = restTemplate.exchange(
                API_BASE_PATH + widget.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(widgetRepository.findById(widget.getId())).isEmpty();
    }

    @Test
    void shouldNotDeleteWidgetByNonExistingId() {
        ResponseEntity<Void> response = restTemplate.exchange(
                API_BASE_PATH + UUID.randomUUID(), HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldGetWidgetById() {
        Widget widget = createWidgetWithZIndex(1);
        widget = widgetRepository.save(widget);

        ResponseEntity<WidgetResponseDto> response = restTemplate.exchange(
                API_BASE_PATH + widget.getId(), HttpMethod.GET, null, WidgetResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(widget.getId());
        assertThat(response.getBody().getZ()).isEqualTo(1);
    }

    @Test
    void shouldNotGetWidgetByNonExistingId() {
        ResponseEntity<Void> response = restTemplate.exchange(
                API_BASE_PATH + UUID.randomUUID(), HttpMethod.GET, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldGetAllWidgetsWithDefaultPagination() {
        Widget widget1 = createWidgetWithZIndex(0);
        Widget widget2 = createWidgetWithZIndex(2);
        Widget widget3 = createWidgetWithZIndex(1);
        widgetRepository.save(widget1);
        widgetRepository.save(widget2);
        widgetRepository.save(widget3);

        ResponseEntity<WidgetPageResponseDto> response = restTemplate.exchange(
                API_BASE_PATH, HttpMethod.GET, null, WidgetPageResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(3);
        assertThat(response.getBody().getNumber()).isEqualTo(0);
        assertThat(response.getBody().getSize()).isEqualTo(10);

        List<WidgetResponseDto> widgets = response.getBody().getContent();
        assertThat(widgets).hasSize(3);
        // assert sort order
        assertThat(widgets.get(0).getZ()).isEqualTo(0);
        assertThat(widgets.get(1).getZ()).isEqualTo(1);
        assertThat(widgets.get(2).getZ()).isEqualTo(2);
    }

    @Test
    void shouldGetAllWidgetsWithPagination() {
        Widget widget1 = createWidgetWithZIndex(0);
        Widget widget2 = createWidgetWithZIndex(2);
        Widget widget3 = createWidgetWithZIndex(1);
        widgetRepository.save(widget1);
        widgetRepository.save(widget2);
        widgetRepository.save(widget3);

        ResponseEntity<WidgetPageResponseDto> response = restTemplate.exchange(
                API_BASE_PATH + "?page=0&size=2", HttpMethod.GET, null, WidgetPageResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(3);
        assertThat(response.getBody().getNumber()).isEqualTo(0);
        assertThat(response.getBody().getSize()).isEqualTo(2);

        List<WidgetResponseDto> widgets = response.getBody().getContent();
        assertThat(widgets).hasSize(2);
        // assert sort order
        assertThat(widgets.get(0).getZ()).isEqualTo(0);
        assertThat(widgets.get(1).getZ()).isEqualTo(1);
    }
}
