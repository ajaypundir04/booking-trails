package com.element.insurance.bookings.repository;

import com.element.insurance.bookings.entity.TrailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrailRepository extends JpaRepository<TrailEntity, Long> {

    Optional<TrailEntity> findByName(String name);

}
