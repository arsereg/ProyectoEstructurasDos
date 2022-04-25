package com.cenfotec.mapa.service;

import com.cenfotec.mapa.domain.Edge;
import com.cenfotec.mapa.domain.ShortestPath;
import com.cenfotec.mapa.domain.Vertex;
import com.cenfotec.mapa.domain.VertexDTO;
import com.cenfotec.mapa.service.dto.ArcoDTO;
import com.cenfotec.mapa.service.dto.NodoDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraphService {


    private final NodoService nodoService;
    private final ArcoService arcoService;
    private final DjikstraService djikstraService;

    List<NodoDTO> nodos;
    List<ArcoDTO> arcos;

    Map<String, Vertex> vertex; //Los nodos y arcos anteriores se utilizan como método de almacenamiento y no como estructura de datos


    public GraphService(NodoService nodoService, ArcoService arcoService, DjikstraService djikstraService) {
        this.nodoService = nodoService;
        this.arcoService = arcoService;
        this.djikstraService = djikstraService;
    }

    public Map<String, Vertex> getGraph(){
        return vertex;
    }

    public Vertex getNode(String name){
        return vertex.get(name);
    }

    public void initializeGraph() {
        this.vertex = new HashMap<>();
        this.nodos = nodoService.findAll();
        this.arcos = arcoService.findAll();
        this.nodos.forEach(n -> {
            Vertex node = new Vertex(n.getName());
//            node.setX(n.getX());
//            node.setY(n.getY());
            vertex.put(node.getName(), node);
        });

        this.nodos.forEach(nodoDTO -> {

            List<ArcoDTO> adyacentes = arcos.stream().filter(a -> a.connectsNode(nodoDTO.getId())).collect(Collectors.toList());
            Vertex node = vertex.get(nodoDTO.getName());
            adyacentes.forEach(nodoAdyacentePersistencia -> {
                if(nodoAdyacentePersistencia.getTos().stream().findFirst().isPresent()){
                    Vertex adyacente = vertex.get(nodoAdyacentePersistencia.getTos().stream().findFirst().get().getName());
                    node.addNeighbour(new Edge(nodoAdyacentePersistencia.getWeight(), node, adyacente));
                }
            });
        });
    }

    public ShortestPath findShortestRoute(String from, String to) {
        initializeGraph();
        djikstraService.computePath(vertex.get(from));
        ShortestPath result = new ShortestPath();
        result.setPath(djikstraService.getShortestPathTo(vertex.get(to)).stream().map(Vertex::getName).collect(Collectors.toList()));
        result.setWeight(djikstraService.getShortestPathTo(vertex.get(to)).stream().mapToDouble(Vertex::getMinDistance).sum());
        return result;
    }




}
