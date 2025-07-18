package com.ejemplo.models;

public class CookieData {
    private String nombre;
    private String valor;

    public CookieData(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Cookie: " + nombre + " = " + valor;
    }
}
