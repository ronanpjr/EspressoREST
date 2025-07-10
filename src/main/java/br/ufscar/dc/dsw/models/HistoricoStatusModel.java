package br.ufscar.dc.dsw.models;

import br.ufscar.dc.dsw.models.enums.StatusSessao;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Objects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historico_status_sessao")
public class HistoricoStatusModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_historico", columnDefinition = "binary(16)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior")
    private StatusSessao statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false)
    private StatusSessao statusNovo;

    @Schema(type = "string", format = "date-time", description = "Data e hora da mudança de status")
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @JsonBackReference("sessao-historico")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sessao", nullable = false)
    private SessaoModel sessao;

    @PrePersist
    protected void onCreate() {
        this.dataHora = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public StatusSessao getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusSessao statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusSessao getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(StatusSessao statusNovo) {
        this.statusNovo = statusNovo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public SessaoModel getSessao() {
        return sessao;
    }

    public void setSessao(SessaoModel sessao) {
        this.sessao = sessao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoStatusModel that = (HistoricoStatusModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
