package isutc.i32.isutcagendas.modelo;

import java.io.Serializable;
import java.util.Date;

public class Evento implements Serializable {
    private int codego;
    private String tipo, descricao;
    private Date dataInicio, dataTermino;

    public int getCodego() {
        return codego;
    }

    public void setCodego(int codego) {
        this.codego = codego;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }
}
