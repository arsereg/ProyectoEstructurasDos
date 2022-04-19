package com.cenfotec.mapa.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.cenfotec.mapa.domain.Nodo} entity.
 */
public class NodoDTO implements Serializable {

    private Long id;

    private String name;

    private Double x;

    private Double y;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NodoDTO)) {
            return false;
        }

        NodoDTO nodoDTO = (NodoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nodoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NodoDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", x=" + getX() +
            ", y=" + getY() +
            "}";
    }
}
