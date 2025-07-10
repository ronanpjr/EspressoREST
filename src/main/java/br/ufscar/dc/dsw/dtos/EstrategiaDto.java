package br.ufscar.dc.dsw.dtos;

import java.util.*;

public class EstrategiaDto {
    private UUID id;
    private String nome;
    private String descricao;
    private List<DicaDto> dicas = new ArrayList<>();
    private List<ExemploDto> exemplos = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public List<DicaDto> getDicas() { return dicas; }
    public void setDicas(List<DicaDto> dicas) { this.dicas = dicas; }
    public List<ExemploDto> getExemplos() { return exemplos; }
    public void setExemplos(List<ExemploDto> exemplos) { this.exemplos = exemplos; }
}