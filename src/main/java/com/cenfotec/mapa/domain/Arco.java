package com.cenfotec.mapa.domain;

import java.io.Serializable;
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

    @OneToOne
    @JoinColumn(unique = true)
    private Nodo from;

    @OneToOne
    @JoinColumn(unique = true)
    private Nodo to;

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

    public Nodo getFrom() {
        return this.from;
    }

    public void setFrom(Nodo nodo) {
        this.from = nodo;
    }

    public Arco from(Nodo nodo) {
        this.setFrom(nodo);
        return this;
    }

    public Nodo getTo() {
        return this.to;
    }

    public void setTo(Nodo nodo) {
        this.to = nodo;
    }

    public Arco to(Nodo nodo) {
        this.setTo(nodo);
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
