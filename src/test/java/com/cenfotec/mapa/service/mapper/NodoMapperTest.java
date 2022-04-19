package com.cenfotec.mapa.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NodoMapperTest {

    private NodoMapper nodoMapper;

    @BeforeEach
    public void setUp() {
        nodoMapper = new NodoMapperImpl();
    }
}
