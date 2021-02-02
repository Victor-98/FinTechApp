package Beans;

import java.util.Date;

public class Pesquisa {
    Date dataIni;
    Date dataFim;
    String tipo;

    public Pesquisa(Date dataIni, Date dataFim, String tipo) {
        this.dataIni = dataIni;
        this.dataFim = dataFim;
        this.tipo = tipo;
    }

    public Date getDataIni() {
        return dataIni;
    }

    public void setDataIni(Date dataIni) {
        this.dataIni = dataIni;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
