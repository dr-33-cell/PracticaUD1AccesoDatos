package com.danielrevuelto.PracticaUD1AccesoADatos.gui;

import com.danielrevuelto.PracticaUD1AccesoADatos.base.Figuras;
import com.danielrevuelto.PracticaUD1AccesoADatos.base.Producto;
import com.danielrevuelto.PracticaUD1AccesoADatos.base.Videojuego;
import com.danielrevuelto.PracticaUD1AccesoADatos.util.Util;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.Properties;

public class ProductoControlador implements ActionListener, ListSelectionListener, WindowListener {

    private Vista vista;
    private ProductoModelo modelo;
    private File ultimaRutaExportada;

    public ProductoControlador(Vista vista, ProductoModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        try {
            cargarDatosConfiguracion();
        } catch (IOException e) {
            System.out.println("No existe fichero de configuracion");
        }

        //listener al arrancar el controlador
        addActionListener(this);
        addWindowListener(this);
        addListSelectionListener(this);
    }

    private boolean hayCamposVacios() {
        int indiceSeleccionado = vista.comboBox1.getSelectedIndex();
        if (vista.figurasRadioButton.getText().isEmpty() ||
                vista.videojuego.getText().isEmpty() ||
                vista.precioTxt.getText().isEmpty() ||
                vista.tituloTxt.getText().isEmpty() ||
                vista.nuevoButton.getText().isEmpty() ||
                vista.importarButton.getText().isEmpty() ||
                vista.exportarButton.getText().isEmpty())


        if (indiceSeleccionado != -1) {
            System.out.println("El índice seleccionado es: " + indiceSeleccionado);

        } else {
            System.out.println("No hay ningún elemento seleccionado.");
        }{
            return true;
        }
        return false;
    }

    private void limpiarCampos() {
        vista.figurasRadioButton.setText(null);
        vista.videojuego.setText(null);
        vista.nuevoButton.setText(null);
        vista.exportarButton.setText(null);
        vista.importarButton.setText(null);
        vista.tituloTxt.setText(null);
        vista.precioTxt.setText(null);
    }

    //listener botones
    private void addActionListener(ActionListener listener) {
        vista.importarButton.addActionListener(listener);
        vista.exportarButton.addActionListener(listener);
        vista.nuevoButton.addActionListener(listener);
        vista.figurasRadioButton.addActionListener(listener);
        vista.videojuego.addActionListener(listener);
        vista.comboBox1.addActionListener(listener);
    }

    //listener ventana (boton cerrar)
    private void addWindowListener(WindowListener listener) {
        vista.frame.addWindowListener(listener);
    }

    //listener de la lista
    private void addListSelectionListener(ListSelectionListener listener) {
        vista.list1.addListSelectionListener(listener);
    }

    public void refrescar() {
        vista.dlmProducto.clear();
        //modelo.obtenerVehiculos -> contiene la lista de vehiculos
        for (Producto unVehiculo:modelo.obtenerProducto()) {
            vista.dlmProducto.addElement(unVehiculo);
        }
    }

    private void cargarDatosConfiguracion() throws IOException {
        Properties configuracion = new Properties();
        configuracion.load(new FileReader("vehiculos.conf"));
        ultimaRutaExportada= new File(configuracion.getProperty("ultimaRutaExportada"));
    }

    private void actualizarDatosConfiguracion(File ultimaRutaExportada) {
        this.ultimaRutaExportada=ultimaRutaExportada;
    }

    private void guardarConfiguracion() throws IOException {
        Properties configuracion=new Properties();
        configuracion.setProperty("ultimaRutaExportada"
                ,ultimaRutaExportada.getAbsolutePath());
        configuracion.store(new PrintWriter("vehiculos.conf")
                ,"Datos configuracion vehiculos");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand=e.getActionCommand();

        switch (actionCommand) {
            case "Nuevo":
                if (hayCamposVacios()) {
                    Util.mensajeError("Los siguientes campos estan vacios " +
                            "\n Matricula\nMarca\nModelo\nFecha matriculacion" +
                            "\n"+vista.kmsPlazasLbl.getText());
                    break;
                }
                if (modelo.existeId(Integer.parseInt(vista.idTxt.getText()))) {
                    Util.mensajeError("Ya existe un videojuego con ese id" +
                            "\n"+vista.idTxt.getText());
                    break;
                }
                if (vista.videojuego.isSelected()) {
                    modelo.altaVideojuego(vista.matriculaTxt.getText(),vista.marcaTxt.getText(),
                            vista.modeloTxt.getText(),vista.fechaMatriculacionDPicker.getDate()
                            , Integer.parseInt(vista.kmsPlazasTxt.getText()));
                } else {
                    modelo.altaFiguras(vista.matriculaTxt.getText(),vista.marcaTxt.getText(),
                            vista.modeloTxt.getText(),vista.fechaMatriculacionDPicker.getDate()
                            , Double.parseDouble(vista.kmsPlazasTxt.getText()));
                }
                limpiarCampos();
                refrescar();
                break;
            case "Importar":
                JFileChooser selectorFichero = Util.crearSelectorFichero(ultimaRutaExportada
                        ,"Archivos XML","xml");
                int opt =selectorFichero.showOpenDialog(null);
                if (opt==JFileChooser.APPROVE_OPTION) {
                    try {
                        modelo.importarXML(selectorFichero.getSelectedFile());
                    } catch (ParserConfigurationException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SAXException ex) {
                        ex.printStackTrace();
                    }
                    refrescar();
                }
                break;
            case "Exportar":
                JFileChooser selectorFichero2=Util.crearSelectorFichero(ultimaRutaExportada
                        ,"Archivos XML","xml");
                int opt2=selectorFichero2.showSaveDialog(null);
                if (opt2==JFileChooser.APPROVE_OPTION) {
                    try {
                        modelo.exportarXML(selectorFichero2.getSelectedFile());
                    } catch (ParserConfigurationException ex) {
                        ex.printStackTrace();
                    } catch (TransformerException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case "Figuras":
                vista.titulo.setText("Nombre");
                break;
            case "Videojuego":
                vista.titulo.setText("Saga");
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int resp= Util.mensajeConfirmacion("¿Desea cerrar la ventana?","Salir");
        if (resp== JOptionPane.OK_OPTION) {
            try {
                guardarConfiguracion();
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            Producto productoSeleccionado = vista.list1.getSelectedValue();
            vista.precioTxt.setText(productoSeleccionado.getPrecio());
            vista.tituloTxt.setText(productoSeleccionado.getTitulo());
            vista.checkBox1.setText(productoSeleccionado.isStock());
            vista.idTxt.setText(productoSeleccionado.getId());
            vista.fechaLanzamientoDPicker.setDate(productoSeleccionado.getFechaLanzamiento());
            if (productoSeleccionado instanceof Producto) {
                vista.videojuego.doClick();
                vista.tituloTxt.setText((Videojuego) productoSeleccionado).getPlataforma();
            } else {
                vista.figurasRadioButton.doClick();
                vista.tituloTxt.setText((Figuras)productoSeleccionado).getTamanno()));
            }
        }
    }

    //no los uso

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

}
