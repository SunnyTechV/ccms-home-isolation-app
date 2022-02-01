package com.tecgvg.ccms.hi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tecgvg.ccms.hi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HomeIsolationsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomeIsolationsDTO.class);
        HomeIsolationsDTO homeIsolationsDTO1 = new HomeIsolationsDTO();
        homeIsolationsDTO1.setId(1L);
        HomeIsolationsDTO homeIsolationsDTO2 = new HomeIsolationsDTO();
        assertThat(homeIsolationsDTO1).isNotEqualTo(homeIsolationsDTO2);
        homeIsolationsDTO2.setId(homeIsolationsDTO1.getId());
        assertThat(homeIsolationsDTO1).isEqualTo(homeIsolationsDTO2);
        homeIsolationsDTO2.setId(2L);
        assertThat(homeIsolationsDTO1).isNotEqualTo(homeIsolationsDTO2);
        homeIsolationsDTO1.setId(null);
        assertThat(homeIsolationsDTO1).isNotEqualTo(homeIsolationsDTO2);
    }
}
