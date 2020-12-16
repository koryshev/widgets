package com.koryshev.widgets.domain.repository;

import com.koryshev.widgets.domain.model.Widget;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * A JPA repository for accessing {@link Widget}.
 *
 * @author Ivan Koryshev
 */
@Profile("jpa")
@Repository
public interface JpaWidgetRepository extends WidgetRepository, JpaRepository<Widget, UUID> {

    @Override
    Optional<Widget> findByZ(Integer z);

    @Query("select max(w.z) from Widget w")
    Optional<Integer> findMaxZ();

    @Override
    @Modifying
    @Query("update Widget w set w.z = w.z + 1, w.lastModifiedDate = CURRENT_TIMESTAMP where w.z = :z")
    void shiftZ(@Param(value = "z") Integer z);

    @Override
    @Modifying
    @Query("update Widget w set w.z = :newValue, w.lastModifiedDate = CURRENT_TIMESTAMP where w.z = :oldValue")
    void updateZ(@Param(value = "oldValue") Integer oldValue, @Param(value = "newValue") Integer newValue);
}
