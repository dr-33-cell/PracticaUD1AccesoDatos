package com.danielrevuelto.PracticaUD1AccesoADatos;

import com.danielrevuelto.PracticaUD1AccesoADatos.gui.ProductoControlador;
import com.danielrevuelto.PracticaUD1AccesoADatos.gui.ProductoModelo;
import com.danielrevuelto.PracticaUD1AccesoADatos.gui.Vista;

public class Principal {
    public static void main(String[] args) {

        Vista vista = new Vista();
        ProductoModelo productoModelo = new ProductoModelo();
        ProductoControlador productoControlador = new ProductoControlador(vista, productoModelo);
    }
}
