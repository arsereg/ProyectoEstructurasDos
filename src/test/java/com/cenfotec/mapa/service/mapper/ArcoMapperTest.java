package com.cenfotec.mapa.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArcoMapperTest {

    private ArcoMapper arcoMapper;

    @BeforeEach
    public void setUp() {
        arcoMapper = new ArcoMapperImpl();
    }
}
