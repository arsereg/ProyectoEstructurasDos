package com.cenfotec.mapa.domain;

import com.cenfotec.mapa.service.dto.ArcoDTO;
import com.cenfotec.mapa.service.dto.NodoDTO;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Graph {

    private List<NodoDTO> nodos;
    private List<ArcoDTO> arcos;

}
