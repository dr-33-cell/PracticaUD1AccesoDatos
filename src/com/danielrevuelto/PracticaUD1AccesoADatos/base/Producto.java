package com.danielrevuelto.PracticaUD1AccesoADatos.base;

import java.time.LocalDate;

public class Producto {

    private int id;
    private String titulo;
    private String genero;
    private double precio;
    private boolean stock;
    private LocalDate fechaLanzamiento;

    public Producto() {
    }

    public Producto(int id, String titulo, String genero, double precio, boolean stock, LocalDate fechaLanzamiento) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.precio = precio;
        this.stock = stock;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public void actualizarStock(int cantidad) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }
}
