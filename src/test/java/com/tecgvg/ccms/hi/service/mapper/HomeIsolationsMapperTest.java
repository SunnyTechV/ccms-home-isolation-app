package com.tecgvg.ccms.hi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HomeIsolationsMapperTest {

    private HomeIsolationsMapper homeIsolationsMapper;

    @BeforeEach
    public void setUp() {
        homeIsolationsMapper = new HomeIsolationsMapperImpl();
    }
}
