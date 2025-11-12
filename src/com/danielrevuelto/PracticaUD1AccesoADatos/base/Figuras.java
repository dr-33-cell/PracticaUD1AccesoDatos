package com.danielrevuelto.PracticaUD1AccesoADatos.base;

import java.time.LocalDate;

public class Figuras extends Producto {

    double tamanno;

    public  Figuras(){

    }

    public Figuras(int id, String titulo, String genero, double precio, boolean stock, LocalDate fechaLanzamiento, double tamanno) {
        super(id, titulo, genero, precio, stock, fechaLanzamiento);
        this.tamanno = tamanno;
    }

    public double getTamanno() {
        return tamanno;
    }

    public void setTamanno(double tamanno) {
        this.tamanno = tamanno;
    }

    @Override
    public String toString() {
        return "Figuras:" + getGenero()+" "+getPlataforma()
                +" "+getTitulo()+" " +getTamanno()
                +" "+getFechaLanzamiento()+" "+getId()+" "+getPrecio()+" "+getClass();
    }
}
