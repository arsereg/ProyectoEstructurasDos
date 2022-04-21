package com.cenfotec.mapa.service;

import com.cenfotec.mapa.domain.Graph;
import org.springframework.stereotype.Service;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;

@Service
public class GraphService {


    private final NodoService nodoService;

    private final ArcoService arcoService;

    private Graph graph;

    public GraphService(NodoService nodoService, ArcoService arcoService) {
        this.nodoService = nodoService;
        this.arcoService = arcoService;
        this.graph = new Graph(nodoService.findAll(), arcoService.findAll());

        int x = 0;
    }


}
