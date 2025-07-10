package br.ufscar.dc.dsw.dtos;

import java.util.UUID;

public class DicaDto {
    private UUID id;
    private String dica;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getDica() { return dica; }
    public void setDica(String dica) { this.dica = dica; }
}