package Beans;

import java.util.Date;

public class Operacao {
    private int id;
    private String tipo; // credito (1) ou debito(2)
    private String categoria; // salario (1), transferencias(2), educacao(3), lazer(4), moradia(5), saude(6), outros(7)
    private Date data;
    private float valor;

    public Operacao (String tipo, String categoria, Date data, float valor) {
        this.tipo = tipo;
        this.categoria = categoria;
        this.data = data;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
