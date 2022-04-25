package com.cenfotec.mapa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Arco.
 */
@Entity
@Table(name = "arco")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Arco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "weight")
    private Double weight;

    @ManyToMany
    @JoinTable(name = "rel_arco__from", joinColumns = @JoinColumn(name = "arco_id"), inverseJoinColumns = @JoinColumn(name = "from_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "incomings", "goings" }, allowSetters = true)
    private Set<Nodo> froms = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rel_arco__to", joinColumns = @JoinColumn(name = "arco_id"), inverseJoinColumns = @JoinColumn(name = "to_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "incomings", "goings" }, allowSetters = true)
    private Set<Nodo> tos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Arco id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeight() {
        return this.weight;
    }

    public Arco weight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Set<Nodo> getFroms() {
        return this.froms;
    }

    public void setFroms(Set<Nodo> nodos) {
        this.froms = nodos;
    }

    public Arco froms(Set<Nodo> nodos) {
        this.setFroms(nodos);
        return this;
    }

    public Arco addFrom(Nodo nodo) {
        this.froms.add(nodo);
        nodo.getIncomings().add(this);
        return this;
    }

    public Arco removeFrom(Nodo nodo) {
        this.froms.remove(nodo);
        nodo.getIncomings().remove(this);
        return this;
    }

    public Set<Nodo> getTos() {
        return this.tos;
    }

    public void setTos(Set<Nodo> nodos) {
        this.tos = nodos;
    }

    public Arco tos(Set<Nodo> nodos) {
        this.setTos(nodos);
        return this;
    }

    public Arco addTo(Nodo nodo) {
        this.tos.add(nodo);
        nodo.getGoings().add(this);
        return this;
    }

    public Arco removeTo(Nodo nodo) {
        this.tos.remove(nodo);
        nodo.getGoings().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Arco)) {
            return false;
        }
        return id != null && id.equals(((Arco) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Arco{" +
            "id=" + getId() +
            ", weight=" + getWeight() +
            "}";
    }
}
