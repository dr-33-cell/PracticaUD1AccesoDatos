package com.danielrevuelto.PracticaUD1AccesoADatos.gui;

import com.danielrevuelto.PracticaUD1AccesoADatos.base.Figuras;
import com.danielrevuelto.PracticaUD1AccesoADatos.base.Producto;
import com.danielrevuelto.PracticaUD1AccesoADatos.base.Videojuego;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProductoModelo {

    private ArrayList<Producto> listaProductos;

    public ProductoModelo() {
        listaProductos = new ArrayList<Producto>();
    }

    public ArrayList<Producto> obtenerProducto() {
        return listaProductos;
    }


    public void altaVideojuego(int id, String titulo, String genero, double precio, boolean stock, LocalDate fechaLanzamiento, String plataforma) {
        Videojuego nuevoVideojuego = new Videojuego(id, titulo, genero, precio, stock, fechaLanzamiento, plataforma);
        listaProductos.add(nuevoVideojuego);
    }


    public void altaFiguras(int id, String titulo, String genero, double precio, boolean stock, LocalDate fechaLanzamiento, double tamanno) {
        Figuras nuevasFiguras = new Figuras(id, titulo, genero, precio, stock, fechaLanzamiento, tamanno);
        listaProductos.add(nuevasFiguras);
    }


    public boolean existeId(int id) {
        for (Producto unProducto : listaProductos) {
            if (unProducto.getId() == id) {
                return true;
            }
        }
        return false;
    }


    public void exportarXML(File fichero) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        DOMImplementation dom = builder.getDOMImplementation();
        Document documento = dom.createDocument(null, "xml", null);


        Element raiz = documento.createElement("Producto");
        documento.getDocumentElement().appendChild(raiz);

        Element nodoVideojuego = null;
        Element nodoDatos = null;
        Text texto = null;


        for (Producto unProducto : listaProductos) {
            if (unProducto instanceof Videojuego) {
                nodoVideojuego = documento.createElement("Videojuego");
            } else {
                nodoVideojuego = documento.createElement("Figuras");
            }
            raiz.appendChild(nodoVideojuego);



            nodoDatos = documento.createElement("id");
            nodoVideojuego.appendChild(nodoDatos);

            texto = documento.createTextNode(String.valueOf(unProducto.getId()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("titulo");
            nodoVideojuego.appendChild(nodoDatos);

            texto = documento.createTextNode(unProducto.getTitulo());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("genero");
            nodoVideojuego.appendChild(nodoDatos);

            texto = documento.createTextNode(unProducto.getGenero());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("fecha-lanzamiento");
            nodoVideojuego.appendChild(nodoDatos);

            texto = documento.createTextNode(String.valueOf(unProducto.getFechaLanzamiento()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("precio");
            nodoVideojuego.appendChild(nodoDatos);

            texto = documento.createTextNode(String.valueOf(unProducto.getPrecio()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("stock");
            nodoVideojuego.appendChild(nodoDatos);

            texto = documento.createTextNode(String.valueOf(unProducto.isStock()));
            nodoDatos.appendChild(texto);


            if (unProducto instanceof Videojuego) {
                nodoDatos = documento.createElement("plataforma");
                nodoVideojuego.appendChild(nodoDatos);

                texto = documento.createTextNode(((Videojuego) unProducto).getPlataforma());
                nodoDatos.appendChild(texto);
            } else {
                nodoDatos = documento.createElement("tamanno");
                nodoVideojuego.appendChild(nodoDatos);

                texto = documento.createTextNode(String.valueOf(((Figuras) unProducto).getTamanno()));
                nodoDatos.appendChild(texto);
            }
        }

        Source source = new DOMSource(documento);
        Result result = new StreamResult(fichero);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);

    }


    public void importarXML(File fichero) throws ParserConfigurationException, IOException, SAXException {
        listaProductos = new ArrayList<Producto>();
        Videojuego nuevoVideojuego = null;
        Figuras nuevasFiguras = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.parse(fichero);

        NodeList listaElementos = documento.getElementsByTagName("*");

        for (int i = 0; i < listaElementos.getLength(); i++) {
            Element nodoVideojuego = (Element) listaElementos.item(i);

            if (nodoVideojuego.getTagName().equals("Videojuego")) {
                nuevoVideojuego = new Videojuego();
                nuevoVideojuego.setId(Integer.parseInt(nodoVideojuego.getChildNodes().item(0).getTextContent()));
                nuevoVideojuego.setTitulo(nodoVideojuego.getChildNodes().item(1).getTextContent());
                nuevoVideojuego.setGenero(nodoVideojuego.getChildNodes().item(2).getTextContent());
                nuevoVideojuego.setFechaLanzamiento(LocalDate.parse(nodoVideojuego.getChildNodes().item(3).getTextContent()));
                nuevoVideojuego.setPrecio(Double.parseDouble(nodoVideojuego.getChildNodes().item(4).getTextContent()));
                nuevoVideojuego.setStock(Boolean.parseBoolean(nodoVideojuego.getChildNodes().item(5).getTextContent()));
                nuevoVideojuego.setPlataforma(nodoVideojuego.getChildNodes().item(0).getTextContent());
                listaProductos.add(nuevoVideojuego);
            } else {
                if (nodoVideojuego.getTagName().equals("Figuras")) {
                    nuevasFiguras = new Figuras();
                    nuevasFiguras.setId(Integer.parseInt(nodoVideojuego.getChildNodes().item(0).getTextContent()));
                    nuevasFiguras.setTitulo(nodoVideojuego.getChildNodes().item(1).getTextContent());
                    nuevasFiguras.setGenero(nodoVideojuego.getChildNodes().item(2).getTextContent());
                    nuevasFiguras.setFechaLanzamiento(LocalDate.parse(nodoVideojuego.getChildNodes().item(3).getTextContent()));
                    nuevasFiguras.setPrecio(Double.parseDouble(nodoVideojuego.getChildNodes().item(4).getTextContent()));
                    nuevasFiguras.setStock(Boolean.parseBoolean(nodoVideojuego.getChildNodes().item(5).getTextContent()));
                    nuevasFiguras.setTamanno(Double.parseDouble(nodoVideojuego.getChildNodes().item(6).getTextContent()));
                    listaProductos.add(nuevasFiguras);
                }

            }
        }
    }

}

