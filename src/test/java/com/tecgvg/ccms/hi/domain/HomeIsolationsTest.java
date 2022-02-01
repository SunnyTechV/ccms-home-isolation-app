package com.tecgvg.ccms.hi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tecgvg.ccms.hi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HomeIsolationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomeIsolations.class);
        HomeIsolations homeIsolations1 = new HomeIsolations();
        homeIsolations1.setId(1L);
        HomeIsolations homeIsolations2 = new HomeIsolations();
        homeIsolations2.setId(homeIsolations1.getId());
        assertThat(homeIsolations1).isEqualTo(homeIsolations2);
        homeIsolations2.setId(2L);
        assertThat(homeIsolations1).isNotEqualTo(homeIsolations2);
        homeIsolations1.setId(null);
        assertThat(homeIsolations1).isNotEqualTo(homeIsolations2);
    }
}
