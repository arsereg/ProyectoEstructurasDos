package com.cenfotec.mapa.web.rest;

import com.cenfotec.mapa.domain.Graph;
import com.cenfotec.mapa.domain.Node;
import com.cenfotec.mapa.domain.Vertex;
import com.cenfotec.mapa.service.GraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GraphResource {

    private final GraphService graphService;

    public GraphResource(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/graph")
    public ResponseEntity<Map<String, Vertex>> getWholeGraph(){
        return ResponseEntity.ok(graphService.getGraph());
    }


    @GetMapping("/graph/shortest-path")
    public ResponseEntity<List<Vertex>> shortestPath(@RequestParam String origin, @RequestParam String destination){
        List<Vertex> path = graphService.findShortestRoute(origin, destination);
        return ResponseEntity.ok(path);
    }







}
