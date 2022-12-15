package com.example.trabalholam;

public class Disciplina {

    private  int codUc;
    private String uc;

    public Disciplina(int codUc, String uc) {
        this.codUc = codUc;
        this.uc = uc;
    }

    public String getUc() {
        return uc;
    }

    public void setUc(String uc) {
        this.uc = uc;
    }

    public int getCodUc() {
        return codUc;
    }

    public void setCodUc(int codUc) {
        this.codUc = codUc;
    }
}
