package br.ufscar.dc.dsw.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Objects;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "dica")
public class DicaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_dica", columnDefinition = "binary(16)")
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    @JsonBackReference("estrategia-dicas")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estrategia", nullable = false)
    private EstrategiaModel estrategia;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public EstrategiaModel getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaModel estrategia) {
        this.estrategia = estrategia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DicaModel that = (DicaModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}