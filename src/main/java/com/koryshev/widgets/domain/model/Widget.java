package com.koryshev.widgets.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import java.time.Instant;
import java.util.UUID;

/**
 * An entity containing widget details.
 *
 * @author Ivan Koryshev
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = @Index(columnList = "z"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Widget {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Integer y;

    @Column(nullable = false, unique = true)
    private Integer z;

    @Positive
    @Column(nullable = false)
    private Integer width;

    @Positive
    @Column(nullable = false)
    private Integer height;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;
}
