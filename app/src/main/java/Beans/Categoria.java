package Beans;

public class Categoria {
    private String tipo;
    private String nome;
    private float soma;

    public Categoria(String tipo, String nome, float soma) {
        this.tipo = tipo;
        this.nome = nome;
        this.soma = soma;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getSoma() {
        return soma;
    }

    public void setSoma(float soma) {
        this.soma = soma;
    }
}
