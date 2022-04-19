package com.cenfotec.mapa.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cenfotec.mapa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NodoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nodo.class);
        Nodo nodo1 = new Nodo();
        nodo1.setId(1L);
        Nodo nodo2 = new Nodo();
        nodo2.setId(nodo1.getId());
        assertThat(nodo1).isEqualTo(nodo2);
        nodo2.setId(2L);
        assertThat(nodo1).isNotEqualTo(nodo2);
        nodo1.setId(null);
        assertThat(nodo1).isNotEqualTo(nodo2);
    }
}
