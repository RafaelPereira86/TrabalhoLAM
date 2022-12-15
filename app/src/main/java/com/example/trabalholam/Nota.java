package com.example.trabalholam;

public class Nota {
    private int codUc;
    private int nAluno;
    private String uc;
    private int nota;

    public Nota(String uc, int nota, int nAluno) {
        this.uc = uc;
        this.nota = nota;
        this.nAluno = nAluno;
    }

    public String getUc() {
        return uc;
    }

    public void setUc(String uc) {
        this.uc = uc;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public int getnAluno() {
        return nAluno;
    }

    public void setnAluno(int nAluno) {
        this.nAluno = nAluno;
    }

    public int getCodUc() {
        return codUc;
    }

    public void setCodUc(int codUc) {
        this.codUc = codUc;
    }
}
