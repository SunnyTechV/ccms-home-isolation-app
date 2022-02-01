package com.tecgvg.ccms.hi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IsolationsDetailsMapperTest {

    private IsolationsDetailsMapper isolationsDetailsMapper;

    @BeforeEach
    public void setUp() {
        isolationsDetailsMapper = new IsolationsDetailsMapperImpl();
    }
}
