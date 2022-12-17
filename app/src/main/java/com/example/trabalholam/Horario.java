package com.example.trabalholam;

public class Horario {

    int numAluno;
    int codigoUC;
    int diaSemana;
    int horaInicio;
    int horaFim;
    String tipoAula;

    public Horario(int numAluno, int codigoUC, int diaSemana, int horaInicio, int horaFim, String tipoAula) {
        this.numAluno = numAluno;
        this.codigoUC = codigoUC;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.tipoAula = tipoAula;
    }
    public Horario(){
        this.numAluno = 0;
        this.codigoUC = 0;
        this.diaSemana = 0;
        this.horaInicio = 0;
        this.horaFim = 0;
        this.tipoAula = "";

    }

    public int getNumAluno() {
        return numAluno;
    }

    public void setNumAluno(int numAluno) {
        this.numAluno = numAluno;
    }

    public int getCodigoUC() {
        return codigoUC;
    }

    public void setCodigoUC(int codigoUC) {
        this.codigoUC = codigoUC;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(int horaFim) {
        this.horaFim = horaFim;
    }

    public String getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(String tipoAula) {
        this.tipoAula = tipoAula;
    }
}
