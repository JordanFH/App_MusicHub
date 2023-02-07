package AccesoDatos;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import Modelo.Album;

public class DAO_Albumes {

    ArrayList<Album> listaAlbumes;

    public DAO_Albumes() {
        this.listaAlbumes = new ArrayList<>();
    }

    public boolean cargarLista(Fragment oFragmento) {
        boolean rpta = false;

        try {
            Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            String orden = MediaStore.Audio.Media.ALBUM + " ASC";

            ContentResolver oCR = oFragmento.requireActivity().getContentResolver();
            Cursor oRegistros = oCR.query(uri, null, null, null, orden);

            if (oRegistros.moveToFirst()) {
                rpta = true;
                do {
                    long albumId = oRegistros.getLong(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
                    String nombreAlbum = oRegistros.getString(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
                    int numCanciones = oRegistros.getInt(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
                    String sNumCanciones = numCanciones == 1 ? numCanciones + " Canción" : numCanciones + " Canciones";
                    //-------------------------------------------------------------------------------------------------------------
                    Uri albumUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
                    Album album = new Album(nombreAlbum, sNumCanciones, albumUri);
                    listaAlbumes.add(album);
                } while (oRegistros.moveToNext());
            }
            oRegistros.close();
        } catch (Exception e) {
            Log.d("Estado:", e.getMessage());
        }
        return rpta;
    }

    public Album get(int i) {
        return listaAlbumes.get(i);
    }

    public ArrayList<Album> listar() {
        return new ArrayList<>(listaAlbumes);
    }
}