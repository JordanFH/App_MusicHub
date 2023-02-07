package Modelo;

import android.net.Uri;

public class Album {
    String nombre;
    String numCanciones;
    Uri imagenAlbum;

    public Album(String nombre, String numCanciones, Uri imagenAlbum) {
        this.nombre = nombre;
        this.numCanciones = numCanciones;
        this.imagenAlbum = imagenAlbum;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumCanciones() {
        return numCanciones;
    }

    public void setNumCanciones(String numCanciones) {
        this.numCanciones = numCanciones;
    }

    public Uri getImagenAlbum() {
        return imagenAlbum;
    }

    public void setImagenAlbum(Uri imagenAlbum) {
        this.imagenAlbum = imagenAlbum;
    }
}
