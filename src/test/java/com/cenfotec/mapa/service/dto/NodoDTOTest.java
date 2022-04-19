package com.cenfotec.mapa.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.cenfotec.mapa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NodoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NodoDTO.class);
        NodoDTO nodoDTO1 = new NodoDTO();
        nodoDTO1.setId(1L);
        NodoDTO nodoDTO2 = new NodoDTO();
        assertThat(nodoDTO1).isNotEqualTo(nodoDTO2);
        nodoDTO2.setId(nodoDTO1.getId());
        assertThat(nodoDTO1).isEqualTo(nodoDTO2);
        nodoDTO2.setId(2L);
        assertThat(nodoDTO1).isNotEqualTo(nodoDTO2);
        nodoDTO1.setId(null);
        assertThat(nodoDTO1).isNotEqualTo(nodoDTO2);
    }
}
