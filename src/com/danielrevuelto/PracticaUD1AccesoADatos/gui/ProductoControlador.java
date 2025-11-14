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
        StringBuilder camposVacios = new StringBuilder();

        if (vista.tituloTxt.getText().isEmpty()){
            camposVacios.append("\n-Titulo del producto");
        }
        if(vista.precioTxt.getText().isEmpty()){
            camposVacios.append("\n-Precio del producto");
        }
        if (vista.fechaLanzamientoDPicker.getDate()== null){
            camposVacios.append("\n-Fecha de Lanzamiento del producto");
        }
        if(vista.idTxt.getText().isEmpty()){
            camposVacios.append("\n-Id del producto");
        }
        if (vista.sizePlatTxt.getText().isEmpty()){
            camposVacios.append("\n-Tamaño/Plataforma del producto");
            }
        if (camposVacios.length()>0){
            Util.mensajeError("Los siguientes campos estan vacios: "+ camposVacios.toString());
            return true;
        }

            return false;
    }

    private void limpiarCampos() {
        vista.tituloTxt.setText("");
        vista.precioTxt.setText("");
        vista.idTxt.setText("");
        vista.fechaLanzamientoDPicker.setDate(null);
        vista.stockCheckBox.setSelected(false);
        vista.generoComboBox.setSelectedIndex(0);
        vista.videojuegoRadioButton.setSelected(false);
        vista.figurasRadioButton.setSelected(false);
    }

    //listener botones
    private void addActionListener(ActionListener listener) {
        vista.importarButton.addActionListener(listener);
        vista.exportarButton.addActionListener(listener);
        vista.nuevoButton.addActionListener(listener);
        vista.figurasRadioButton.addActionListener(listener);
        vista.videojuegoRadioButton.addActionListener(listener);
        vista.generoComboBox.addActionListener(listener);
        vista.stockCheckBox.addActionListener(listener);
        vista.limpiarButton.addActionListener(listener);
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
        //modelo.obtenerProducto -> contiene la lista de productos
        for (Producto unProducto:modelo.obtenerProducto()) {
            vista.dlmProducto.addElement(unProducto);
        }
    }

    private void cargarDatosConfiguracion() throws IOException {
        Properties configuracion = new Properties();
        configuracion.load(new FileReader("productos.conf"));
        ultimaRutaExportada= new File(configuracion.getProperty("ultimaRutaExportada"));
    }

    private void actualizarDatosConfiguracion(File ultimaRutaExportada) {
        this.ultimaRutaExportada=ultimaRutaExportada;
    }

    private void guardarConfiguracion() throws IOException {
        Properties configuracion=new Properties();
        configuracion.setProperty("ultimaRutaExportada"
                ,ultimaRutaExportada.getAbsolutePath());
        configuracion.store(new PrintWriter("productos.conf")
                ,"Datos configuracion productos");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand=e.getActionCommand();

        switch (actionCommand) {
            case "Nuevo":
                if (hayCamposVacios()) {

                    break;
                }

                int id = 0;
                try{
                    id = Integer.parseInt(vista.idTxt.getText());
                } catch (NumberFormatException ne){
                    Util.mensajeError("Introduce bien el ID (numerito)");
                    break;
                }
                 id = Integer.parseInt(vista.idTxt.getText());
                double precio = Double.parseDouble(vista.precioTxt.getText());

                if (id < 0 || precio < 0) {
                    Util.mensajeError("El precio y el Id no pueden ser números negativos");
                    break;
                }

                if (modelo.existeId(id)) {
                    Util.mensajeError("Ya existe un producto con ese id" +
                            "\n"+vista.idTxt.getText());
                    break;
                }

                try {
                    if (vista.videojuegoRadioButton.isSelected()) {
                        modelo.altaVideojuego(id, vista.tituloTxt.getText(), vista.generoComboBox.getSelectedItem().toString(),
                                (Double.parseDouble(vista.precioTxt.getText())), vista.stockCheckBox.isSelected(), vista.fechaLanzamientoDPicker.getDate(), vista.tituloTxt.getText());
                    } else {
                        modelo.altaFiguras(id, vista.tituloTxt.getText(), vista.generoComboBox.getSelectedItem().toString(),
                                Double.parseDouble(vista.precioTxt.getText()), vista.stockCheckBox.isSelected(), vista.fechaLanzamientoDPicker.getDate(), Double.parseDouble(vista.precioTxt.getText()));
                    }
                }catch (NumberFormatException ne) {
                    Util.mensajeError("Introduce decimales");
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
                vista.sizePlat.setText("Tamaño");
                break;
            case "Videojuego":
                vista.titulo.setText("Saga");
                vista.sizePlat.setText("Plataforma");
                break;
            case "Limpiar":
                limpiarCampos();
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int resp = Util.mensajeConfirmacion("¿Desea cerrar la ventana?", "Salir");
        if (resp == JOptionPane.OK_OPTION) {
            try {
                guardarConfiguracion();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar configuración");
            } finally {
                System.exit(0);
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            Producto productoSeleccionado = (Producto) vista.list1.getSelectedValue();
            vista.precioTxt.setText(String.valueOf(productoSeleccionado.getPrecio()));
            vista.tituloTxt.setText(productoSeleccionado.getTitulo());
            vista.stockCheckBox.setText(String.valueOf(productoSeleccionado.isStock()));
            vista.idTxt.setText(String.valueOf(productoSeleccionado.getId()));
            vista.fechaLanzamientoDPicker.setDate(productoSeleccionado.getFechaLanzamiento());
            if (productoSeleccionado instanceof Videojuego) {
                vista.videojuegoRadioButton.doClick();
                vista.tituloTxt.setText(String.valueOf(((Videojuego) productoSeleccionado).getPlataforma()));
            } else {
                vista.figurasRadioButton.doClick();
                vista.tituloTxt.setText(String.valueOf(((Figuras)productoSeleccionado).getTamanno()));
            }
        }
    }



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
