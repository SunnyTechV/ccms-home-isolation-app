package com.tecgvg.ccms.hi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tecgvg.ccms.hi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IsolationsDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IsolationsDetails.class);
        IsolationsDetails isolationsDetails1 = new IsolationsDetails();
        isolationsDetails1.setId(1L);
        IsolationsDetails isolationsDetails2 = new IsolationsDetails();
        isolationsDetails2.setId(isolationsDetails1.getId());
        assertThat(isolationsDetails1).isEqualTo(isolationsDetails2);
        isolationsDetails2.setId(2L);
        assertThat(isolationsDetails1).isNotEqualTo(isolationsDetails2);
        isolationsDetails1.setId(null);
        assertThat(isolationsDetails1).isNotEqualTo(isolationsDetails2);
    }
}
