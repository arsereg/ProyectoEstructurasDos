package com.cenfotec.mapa.domain;

import lombok.Data;

import java.util.List;

@Data
public class ShortestPath {

    List<String> nodos;
    List<String> arcos;
    double weight;
}
