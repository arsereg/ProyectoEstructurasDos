package com.cenfotec.mapa.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.cenfotec.mapa.domain.Arco} entity.
 */
public class ArcoDTO implements Serializable {

    private Long id;

    private Double weight;

    private NodoDTO from;

    private NodoDTO to;

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

    public NodoDTO getFrom() {
        return from;
    }

    public void setFrom(NodoDTO from) {
        this.from = from;
    }

    public NodoDTO getTo() {
        return to;
    }

    public void setTo(NodoDTO to) {
        this.to = to;
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

    // prettier-ignore
    @Override
    public String toString() {
        return "ArcoDTO{" +
            "id=" + getId() +
            ", weight=" + getWeight() +
            ", from=" + getFrom() +
            ", to=" + getTo() +
            "}";
    }
}
