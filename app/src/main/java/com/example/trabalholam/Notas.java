package com.example.trabalholam;

public class Notas {

    private String uc;
    private String nota;

    public Notas(String uc, String nota) {
        this.uc = uc;
        this.nota = nota;
    }

    public String getUc() {
        return uc;
    }

    public void setUc(String uc) {
        this.uc = uc;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
