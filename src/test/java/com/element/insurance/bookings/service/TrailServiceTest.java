package com.element.insurance.bookings.service;

import com.element.insurance.bookings.dto.TrailsEnum;
import com.element.insurance.bookings.entity.TrailEntity;
import com.element.insurance.bookings.repository.TrailRepository;
import com.element.insurance.bookings.util.TestUtil;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrailServiceTest {

    private TrailRepository trailRepository = Mockito.mock(TrailRepository.class);
    private TrailService trailService = new TrailService(trailRepository);

    @Test
    public void initTrailsMapTest() {
        List<TrailEntity> trailEntities = new ArrayList<>();
        trailEntities.add(TestUtil.trailEntity());
        Mockito.when(trailRepository.findAll()).thenReturn(trailEntities);
        trailService.initTrails();
        Map<TrailsEnum, TrailEntity> trails = trailService.getTrails();
        Assert.assertNotNull(trails);
        Assert.assertThat(trails.get(TrailsEnum.Shire), Matchers.equalTo(TestUtil.trailEntity()));
    }
}
