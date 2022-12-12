package com.example.trabalholam;

public class Horario {

    String diaSemana;
    String horaInicio;
    String horaFim;
    String codigoUC;
    String tipoAula;

    public Horario(String diaSemana, String horaInicio, String horaFim, String codigoUC, String tipoAula) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.codigoUC = codigoUC;
        this.tipoAula = tipoAula;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getCodigoUC() {
        return codigoUC;
    }

    public void setCodigoUC(String codigoUC) {
        this.codigoUC = codigoUC;
    }

    public String getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(String tipoAula) {
        this.tipoAula = tipoAula;
    }
}
