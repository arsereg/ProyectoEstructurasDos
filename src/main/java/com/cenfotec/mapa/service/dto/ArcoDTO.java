package com.cenfotec.mapa.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.cenfotec.mapa.domain.Arco} entity.
 */
public class ArcoDTO implements Serializable {

    private Long id;

    private Double weight;

    private Set<NodoDTO> froms = new HashSet<>();

    private Set<NodoDTO> tos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Set<NodoDTO> getFroms() {
        return froms;
    }

    public void setFroms(Set<NodoDTO> froms) {
        this.froms = froms;
    }

    public Set<NodoDTO> getTos() {
        return tos;
    }

    public void setTos(Set<NodoDTO> tos) {
        this.tos = tos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArcoDTO)) {
            return false;
        }

        ArcoDTO arcoDTO = (ArcoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, arcoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public boolean connectsNode(Long id) {
        for (NodoDTO nodoDTO : this.getFroms()) {
            if (nodoDTO.getId().equals(id)) {
                return true;
            }
        }
        for (NodoDTO nodoDTO : this.getTos()) {
            if (nodoDTO.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArcoDTO{" +
            "id=" + getId() +
            ", weight=" + getWeight() +
            ", froms=" + getFroms() +
            ", tos=" + getTos() +
            "}";
    }
}
