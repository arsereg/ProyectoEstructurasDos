package com.cenfotec.mapa.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cenfotec.mapa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArcoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Arco.class);
        Arco arco1 = new Arco();
        arco1.setId(1L);
        Arco arco2 = new Arco();
        arco2.setId(arco1.getId());
        assertThat(arco1).isEqualTo(arco2);
        arco2.setId(2L);
        assertThat(arco1).isNotEqualTo(arco2);
        arco1.setId(null);
        assertThat(arco1).isNotEqualTo(arco2);
    }
}
