package Modelo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Cancion implements Parcelable, Comparable<Cancion> {
    String nombre;
    String artista;
    Uri imagenAlbum;
    String data;
    long duracion;

    public Cancion(String nombre, String artista, Uri imagenAlbum, String data, long duracion) {
        this.nombre = nombre;
        this.artista = artista;
        this.imagenAlbum = imagenAlbum;
        this.data = data;
        this.duracion = duracion;
    }

    public Cancion(String nombre, String artista) {
        this.nombre = nombre;
        this.artista = artista;
    }

    public Cancion(String nombre, String artista, Uri imagenAlbum) {
        this.nombre = nombre;
        this.artista = artista;
        this.imagenAlbum = imagenAlbum;
    }

    protected Cancion(Parcel in) {
        nombre = in.readString();
        artista = in.readString();
        imagenAlbum = in.readParcelable(Uri.class.getClassLoader());
        data = in.readString();
        duracion = in.readLong();
    }

    public static final Creator<Cancion> CREATOR = new Creator<Cancion>() {
        @Override
        public Cancion createFromParcel(Parcel in) {
            return new Cancion(in);
        }

        @Override
        public Cancion[] newArray(int size) {
            return new Cancion[size];
        }
    };

    public void setImagenAlbum(Uri imagenAlbum) {
        this.imagenAlbum = imagenAlbum;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Uri getImagenAlbum() {
        return imagenAlbum;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public long getDuracion() {
        return duracion;
    }

    public void setDuracion(long duracion) {
        this.duracion = duracion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeString(artista);
        parcel.writeParcelable(imagenAlbum, i);
        parcel.writeString(data);
        parcel.writeLong(duracion);
    }

    @NonNull
    @Override
    public String toString() {
        return ("(" + nombre + ", " + artista + ")");
    }

    @Override
    public int compareTo(Cancion oCancion) {
        return toString().compareTo(oCancion.toString());
    }
}
