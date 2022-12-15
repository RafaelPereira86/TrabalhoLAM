package com.example.trabalholam;

public class InscricaoAl {
    private int ucID;
    private int alunoID;
    private int inscrID;

    public InscricaoAl(int ucID, int alunoID) {
        this.ucID = ucID;
        this.alunoID = alunoID;
    }

    public int getUcID() {
        return ucID;
    }

    public void setUcID(int ucID) {
        this.ucID = ucID;
    }

    public int getAlunoID() {
        return alunoID;
    }

    public void setAlunoID(int alunoID) {
        this.alunoID = alunoID;
    }

    public int getInscrID() {
        return inscrID;
    }

    public void setInscrID(int inscrID) {
        this.inscrID = inscrID;
    }
}
