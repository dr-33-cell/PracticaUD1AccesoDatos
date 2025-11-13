package com.danielrevuelto.PracticaUD1AccesoADatos.gui;

import com.danielrevuelto.PracticaUD1AccesoADatos.base.Producto;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;

public class Vista {
    public JPanel panel1;
    public JRadioButton figurasRadioButton;
    public JRadioButton videojuegoRadioButton;
    public JComboBox generoComboBox;
    public JTextField tituloTxt;
    public JTextField precioTxt;
    public JButton nuevoButton;
    public JButton importarButton;
    public JButton exportarButton;
    public JList list1;
    public JTextField idTxt;
    public JLabel titulo;
    public DatePicker fechaLanzamientoDPicker;
    public JLabel Precio;
    public JCheckBox stockCheckBox;
    public JLabel formatoTxt;
    public JLabel id;
    public JLabel sizePlat;
    public JTextField sizePlatTxt;

    //lo declaro yo
    public JFrame frame;


    //para poner todos los datos en mi lista
    public DefaultListModel<Producto> dlmProducto;

    public Vista() {
        frame = new JFrame("VideojuegoMVC");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        initComponents();
    }

    public void initComponents() {
        dlmProducto = new DefaultListModel<Producto>();
        list1.setModel(dlmProducto);
    }
}

