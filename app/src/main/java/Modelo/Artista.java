package Modelo;

import android.net.Uri;

public class Artista {
    String nombre;
    String numAlbumes_Canciones;
    Uri imagenArtista;

    public Artista() {
    }

    public Artista(String nombre, String numAlbumes_Canciones, Uri imagenArtista) {
        this.nombre = nombre;
        this.numAlbumes_Canciones = numAlbumes_Canciones;
        this.imagenArtista = imagenArtista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumAlbumes_Canciones() {
        return numAlbumes_Canciones;
    }

    public void setNumAlbumes_Canciones(String numAlbumes_Canciones) {
        this.numAlbumes_Canciones = numAlbumes_Canciones;
    }

    public Uri getImagenArtista() {
        return imagenArtista;
    }

    public void setImagenArtista(Uri imagenArtista) {
        this.imagenArtista = imagenArtista;
    }
}
