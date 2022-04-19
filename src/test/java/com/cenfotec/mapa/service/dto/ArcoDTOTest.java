package com.cenfotec.mapa.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.cenfotec.mapa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArcoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArcoDTO.class);
        ArcoDTO arcoDTO1 = new ArcoDTO();
        arcoDTO1.setId(1L);
        ArcoDTO arcoDTO2 = new ArcoDTO();
        assertThat(arcoDTO1).isNotEqualTo(arcoDTO2);
        arcoDTO2.setId(arcoDTO1.getId());
        assertThat(arcoDTO1).isEqualTo(arcoDTO2);
        arcoDTO2.setId(2L);
        assertThat(arcoDTO1).isNotEqualTo(arcoDTO2);
        arcoDTO1.setId(null);
        assertThat(arcoDTO1).isNotEqualTo(arcoDTO2);
    }
}
