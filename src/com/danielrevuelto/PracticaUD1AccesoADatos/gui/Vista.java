package com.danielrevuelto.PracticaUD1AccesoADatos.gui;

import com.danielrevuelto.PracticaUD1AccesoADatos.base.Producto;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;

public class Vista {
     JPanel panel1;
     JRadioButton figurasRadioButton;
     JRadioButton videojuego;
     JComboBox comboBox1;
     JTextField textoTitulo;
     JTextField textoPrecio;
     JButton nuevoButton;
     JButton importarButton;
     JButton exportarButton;
     JList list1;
     JTextField idTxt;
     JLabel titulo;
    private DatePicker fechaLanzamientoDPicker;

    //lo declaro yo
    public JFrame frame;


    //para poner todos los datos en mi lista
    public DefaultListModel<Producto> dlmVideojuego;

    public Vista(){
        frame = new JFrame("VideojuegoMVC");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        initComponents();
    }
    public void initComponents() {
        dlmVideojuego=new DefaultListModel<Producto>();
        list1.setModel(Producto);
    }
    }
}
