package com.example.trabalholam;

public class NotasDisciplinas
{
        public String UC;
        public int nota;

        public NotasDisciplinas(String UC, int nota) {
            this.UC = UC;
            this.nota = nota;
        }

        public int getNota() {
            return nota;
        }

        public String getUC(){
            return UC;
        }
}

