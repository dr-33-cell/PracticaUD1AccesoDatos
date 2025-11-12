package com.danielrevuelto.PracticaUD1AccesoADatos.base;

import java.time.LocalDate;

public class Videojuego extends Producto {

    String plataforma;

    public Videojuego() {

    }



    public Videojuego(int id, String titulo, String genero, double precio, boolean stock, LocalDate fechaLanzamiento, String plataforma) {
        super(id, titulo, genero, precio, stock, fechaLanzamiento);
        this.plataforma = plataforma;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    @Override
    public String toString() {
        return "Stickers:" + getGenero()+" "+getPlataforma() +" "+getTitulo()+" "+getFechaLanzamiento()+" "+getId()+" "+getPrecio()+" "+getClass();
    }
}
