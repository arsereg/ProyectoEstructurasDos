package com.cenfotec.mapa.web.rest;
import com.cenfotec.mapa.domain.ShortestPath;
import com.cenfotec.mapa.domain.Vertex;
import com.cenfotec.mapa.domain.VertexDTO;
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
    public ResponseEntity<ShortestPath> shortestPath(@RequestParam String origin, @RequestParam String destination){
        return ResponseEntity.ok(graphService.findShortestRoute(origin, destination));
    }







}
