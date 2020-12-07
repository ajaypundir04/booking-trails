package com.element.insurance.bookings.service;

import com.element.insurance.bookings.dto.TrailsEnum;
import com.element.insurance.bookings.entity.TrailEntity;
import com.element.insurance.bookings.repository.TrailRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ajay Singh Pundir
 * Handling Trails information.
 */
@Service
public class TrailService {

    private final TrailRepository trailRepository;
    private Map<TrailsEnum, TrailEntity> trails = new HashMap<>();

    public TrailService(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }

    @PostConstruct
    public void initTrails() {
        Map<TrailsEnum, TrailEntity> trailMap = new HashMap<>();
        trailRepository.findAll()
                .forEach(trail -> {
                    trailMap.put(TrailsEnum.valueOf(trail.getName()), trail);
                });
        trails = Collections.unmodifiableMap(trailMap);
    }

    /**
     * It will return all the trails present in db.
     *
     * @return @{@link Map<@TrailsEnum, @TrailEntity>} map of all the trails.
     */
    public Map<TrailsEnum, TrailEntity> getTrails() {
        return trails;
    }
}
