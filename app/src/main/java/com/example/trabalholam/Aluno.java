package com.example.trabalholam;

public class Aluno {
    private int number;
    private  String password;
    private String token;

    public Aluno(int number, String password, String token){
        this.number = number;
        this.password = password;
        this.token = token;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
