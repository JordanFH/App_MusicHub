package AccesoDatos;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

import Modelo.Cancion;

public class DAO_Canciones implements Serializable {

    ArrayList<Cancion> listaCanciones;

    public DAO_Canciones() {
        this.listaCanciones = new ArrayList<>();
    }

    public boolean cargarLista(Fragment oFragmento) {
        boolean rpta = false;

        try {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String seleccion = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
            String orden = MediaStore.Audio.Media.TITLE + " ASC";

            ContentResolver oCR = oFragmento.requireActivity().getContentResolver();
            Cursor oRegistros = oCR.query(uri, null, seleccion, null, orden);

            if (oRegistros.moveToFirst()) {
                rpta = true;
                do {
                    long albumId = oRegistros.getLong(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                    String nombreCancion = oRegistros.getString(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String nombreArtista = oRegistros.getString(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    long duracion = oRegistros.getLong(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    String data = oRegistros.getString(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    //-------------------------------------------------------------------------------------------------------------
                    Uri albumUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
                    Cancion cancion = new Cancion(nombreCancion, nombreArtista, albumUri, data, duracion);
                    listaCanciones.add(cancion);
                } while (oRegistros.moveToNext());
            }
            oRegistros.close();
        } catch (Exception e) {
            Log.d("Estado:", e.getMessage());
        }
        return rpta;
    }

    public boolean cargarLista(Activity oActividad) {
        boolean rpta = false;

        try {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String seleccion = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
            String orden = MediaStore.Audio.Media.TITLE + " ASC";

            ContentResolver oCR = oActividad.getContentResolver();
            Cursor oRegistros = oCR.query(uri, null, seleccion, null, orden);

            if (oRegistros.moveToFirst()) {
                rpta = true;
                do {
                    long albumId = oRegistros.getLong(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                    String nombreCancion = oRegistros.getString(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String nombreArtista = oRegistros.getString(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    long duracion = oRegistros.getLong(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    String data = oRegistros.getString(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    //-------------------------------------------------------------------------------------------------------------
                    Uri albumUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
                    Cancion cancion = new Cancion(nombreCancion, nombreArtista, albumUri, data, duracion);
                    listaCanciones.add(cancion);
                } while (oRegistros.moveToNext());
            }
            oRegistros.close();
        } catch (Exception e) {
            Log.d("Estado:", e.getMessage());
        }
        return rpta;
    }

    public Cancion get(int i) {
        return listaCanciones.get(i);
    }

    public ArrayList<Cancion> listar() {
        return new ArrayList<>(listaCanciones);
    }
}
