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

    //altaCoche (Videojuego)
    public void altaVideojuego(int id, String titulo, String genero, String plataforma, double precio, boolean stock, LocalDate fechaLanzamiento, String texturas) {
        Videojuego nuevoVideojuego = (Videojuego) new Producto(id, titulo, genero, precio, stock, fechaLanzamiento);
        listaProductos.add(nuevoVideojuego);
    }

    //altaMoto (Figuras)
    public void altaFiguras(int id, String titulo, String genero, String plataforma, double precio, boolean stock, LocalDate fechaLanzamiento, double tamanno) {
        Figuras nuevasFiguras = (Figuras) new Producto(id, titulo, genero, precio, stock, fechaLanzamiento);
        listaProductos.add(nuevasFiguras);
    }

    //existeMatricula
    public boolean existeId(int id) {
        for (Producto unProducto : listaProductos) {
            if (unProducto.getId() == id) { //preguntar a sebas
                return true;
            }
        }
        return false;
    }

    //exportarXML
    public void exportarXML(File fichero) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //implementacion DOM -> web
        DOMImplementation dom = builder.getDOMImplementation();
        Document documento = dom.createDocument(null, "xml", null);

        //añado el nodo raiz (primera etiqueta)
        Element raiz = documento.createElement("Producto");
        documento.getDocumentElement().appendChild(raiz);

        Element nodoVideojuego = null;
        Element nodoDatos = null;
        Text texto = null;

        //voy a añadir una etiqueta dentro de vehiculo
        //en funcion de si es coche o moto
        for (Producto unProducto : listaProductos) {
            if (unProducto instanceof Videojuego) {
                nodoVideojuego = documento.createElement("Videojuego");
            } else {
                nodoVideojuego = documento.createElement("Figuras");
            }
            raiz.appendChild(nodoVideojuego);

            //dentro de la etiqueta vehiculo
            //tengo coche y moto
            //atributos comunes (matricula,marca,modelo,fechamatriculacion)

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

            //como hay un campo que depende del tipo de vehiculo
            //volvemos a comprobar
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
        //guardo los datos en fichero
        Source source = new DOMSource(documento);
        Result result = new StreamResult(fichero);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);

    }

    //importarXML
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
                nuevoVideojuego.setPlataforma(nodoVideojuego.getChildNodes().item(0).getTextContent());
                nuevoVideojuego.setGenero(nodoVideojuego.getChildNodes().item(1).getTextContent());
                nuevoVideojuego.setId(Integer.parseInt(nodoVideojuego.getChildNodes().item(2).getTextContent()));
                nuevoVideojuego.setFechaLanzamiento(LocalDate.parse(nodoVideojuego.getChildNodes().item(3).getTextContent()));
                nuevoVideojuego.setPrecio(Double.parseDouble(nodoVideojuego.getChildNodes().item(4).getTextContent()));
                nuevoVideojuego.setStock(Boolean.parseBoolean(nodoVideojuego.getChildNodes().item(5).getTextContent()));
                nuevoVideojuego.setTitulo(nodoVideojuego.getChildNodes().item(6).getTextContent());
                listaProductos.add(nuevoVideojuego);
            } else {
                if (nodoVideojuego.getTagName().equals("Figuras")) {
                    nuevasFiguras = new Figuras();
                    nuevasFiguras.setTamanno(Double.parseDouble(nodoVideojuego.getChildNodes().item(0).getTextContent()));
                    nuevasFiguras.setGenero(nodoVideojuego.getChildNodes().item(1).getTextContent());
                    nuevasFiguras.setId(Integer.parseInt(nodoVideojuego.getChildNodes().item(2).getTextContent()));
                    nuevasFiguras.setFechaLanzamiento(LocalDate.parse(nodoVideojuego.getChildNodes().item(3).getTextContent()));
                    nuevasFiguras.setPrecio(Double.parseDouble(nodoVideojuego.getChildNodes().item(4).getTextContent()));
                    nuevasFiguras.setStock(Boolean.parseBoolean(nodoVideojuego.getChildNodes().item(5).getTextContent()));
                    nuevasFiguras.setTitulo(nodoVideojuego.getChildNodes().item(6).getTextContent());
                    listaProductos.add(nuevasFiguras);
                }

            }
        }
    }

}

