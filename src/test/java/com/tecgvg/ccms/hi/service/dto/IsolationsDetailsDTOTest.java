package com.tecgvg.ccms.hi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tecgvg.ccms.hi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IsolationsDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IsolationsDetailsDTO.class);
        IsolationsDetailsDTO isolationsDetailsDTO1 = new IsolationsDetailsDTO();
        isolationsDetailsDTO1.setId(1L);
        IsolationsDetailsDTO isolationsDetailsDTO2 = new IsolationsDetailsDTO();
        assertThat(isolationsDetailsDTO1).isNotEqualTo(isolationsDetailsDTO2);
        isolationsDetailsDTO2.setId(isolationsDetailsDTO1.getId());
        assertThat(isolationsDetailsDTO1).isEqualTo(isolationsDetailsDTO2);
        isolationsDetailsDTO2.setId(2L);
        assertThat(isolationsDetailsDTO1).isNotEqualTo(isolationsDetailsDTO2);
        isolationsDetailsDTO1.setId(null);
        assertThat(isolationsDetailsDTO1).isNotEqualTo(isolationsDetailsDTO2);
    }
}
