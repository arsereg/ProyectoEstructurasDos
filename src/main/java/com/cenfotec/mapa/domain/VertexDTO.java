package com.cenfotec.mapa.domain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;


@Data
public class VertexDTO implements Comparable<VertexDTO> {
    private String name;
    @EqualsAndHashCode.Exclude private List<String> edges;
    private boolean visited;
    @EqualsAndHashCode.Exclude private VertexDTO previosVertex;
    private double minDistance = Double.MAX_VALUE;

    public VertexDTO(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
    }

    public void addNeighbour(String edge) {
        this.edges.add(edge);
    }

    public List<String> getEdges() {
        return edges;
    }

    public void setEdges(List<String> edges) {
        this.edges = edges;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public VertexDTO getPreviosVertex() {
        return previosVertex;
    }

    public void setPreviosVertex(VertexDTO previosVertex) {
        this.previosVertex = previosVertex;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(VertexDTO otherVertex) {
        return Double.compare(this.minDistance, otherVertex.minDistance);
    }
}
