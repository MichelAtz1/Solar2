package br.desenvolvedor.michelatz.aplcativosolar;

/**
 * Created by Michel Atz on 03/07/2018.
 */

public class Dados {
    private String dado;

    public Dados() {
    }

    public Dados(String dado) {
        super();
        this.dado = dado;
    }

    public String getDado() {
        return dado;
    }

    public void setDado(String dado) {
        this.dado = dado;
    }

    @Override
    public String toString() {
        return dado;
    }
}
