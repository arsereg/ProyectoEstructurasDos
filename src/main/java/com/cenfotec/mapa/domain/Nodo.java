package com.cenfotec.mapa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Nodo.
 */
@Entity
@Table(name = "nodo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Nodo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;

    @ManyToMany(mappedBy = "froms")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "froms", "tos" }, allowSetters = true)
    private Set<Arco> incomings = new HashSet<>();

    @ManyToMany(mappedBy = "tos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "froms", "tos" }, allowSetters = true)
    private Set<Arco> goings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Nodo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Nodo name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getX() {
        return this.x;
    }

    public Nodo x(Double x) {
        this.setX(x);
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return this.y;
    }

    public Nodo y(Double y) {
        this.setY(y);
        return this;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Set<Arco> getIncomings() {
        return this.incomings;
    }

    public void setIncomings(Set<Arco> arcos) {
        if (this.incomings != null) {
            this.incomings.forEach(i -> i.removeFrom(this));
        }
        if (arcos != null) {
            arcos.forEach(i -> i.addFrom(this));
        }
        this.incomings = arcos;
    }

    public Nodo incomings(Set<Arco> arcos) {
        this.setIncomings(arcos);
        return this;
    }

    public Nodo addIncoming(Arco arco) {
        this.incomings.add(arco);
        arco.getFroms().add(this);
        return this;
    }

    public Nodo removeIncoming(Arco arco) {
        this.incomings.remove(arco);
        arco.getFroms().remove(this);
        return this;
    }

    public Set<Arco> getGoings() {
        return this.goings;
    }

    public void setGoings(Set<Arco> arcos) {
        if (this.goings != null) {
            this.goings.forEach(i -> i.removeTo(this));
        }
        if (arcos != null) {
            arcos.forEach(i -> i.addTo(this));
        }
        this.goings = arcos;
    }

    public Nodo goings(Set<Arco> arcos) {
        this.setGoings(arcos);
        return this;
    }

    public Nodo addGoing(Arco arco) {
        this.goings.add(arco);
        arco.getTos().add(this);
        return this;
    }

    public Nodo removeGoing(Arco arco) {
        this.goings.remove(arco);
        arco.getTos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Nodo)) {
            return false;
        }
        return id != null && id.equals(((Nodo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Nodo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", x=" + getX() +
            ", y=" + getY() +
            "}";
    }
}
