package com.cenfotec.mapa.domain;

import com.cenfotec.mapa.service.dto.ArcoDTO;
import com.cenfotec.mapa.service.dto.NodoDTO;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class Graph {

    private Set<Node> nodes = new HashSet<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

}
