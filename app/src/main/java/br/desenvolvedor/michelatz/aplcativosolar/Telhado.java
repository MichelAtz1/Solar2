package br.desenvolvedor.michelatz.aplcativosolar;

public class Telhado {

    public String inclinacao;
    public String area;

    public Telhado(String inclinacao, String area) {
        this.inclinacao = inclinacao;
        this.area = area;
    }

    public String getInclinacao() {
        return inclinacao;
    }

    public void setInclinacao(String inclinacao) {
        this.inclinacao = inclinacao;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
