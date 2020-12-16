package com.koryshev.widgets.domain.repository;

import com.koryshev.widgets.domain.model.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A JPA repository for accessing {@link Widget}.
 *
 * @author Ivan Koryshev
 */
@Repository
public interface JpaWidgetRepository extends WidgetRepository, JpaRepository<Widget, UUID> {

    @Override
    List<Widget> findAllByOrderByZAsc();

    @Override
    Optional<Widget> findByZ(Integer z);

    @Query("select max(w.z) from Widget w")
    Optional<Integer> findMaxZ();

    @Override
    @Modifying
    @Query("update Widget w set w.z = w.z + 1, w.lastModifiedDate = CURRENT_TIMESTAMP where w.z = :z")
    void updateZ(@Param(value = "z") Integer z);
}
