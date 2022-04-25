package com.cenfotec.mapa.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(exclude="adjacentNodes")
public class Node {

    private String name;
    private double x;
    private double y;


    private List<Node> shortestPath = new LinkedList<>();
    private Double distance = Double.MAX_VALUE;
    Map<Node, Double> adjacentNodes = new HashMap<>();

    public void addDestination(Node destination, double distance) {
        adjacentNodes.put(destination, distance);
    }

    public Node(String name) {
        this.name = name;
    }

}
