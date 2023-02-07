package AccesoDatos;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import Modelo.Artista;

public class DAO_Artistas {

    ArrayList<Artista> listaArtistas;

    public DAO_Artistas() {
        this.listaArtistas = new ArrayList<>();
    }

    public boolean cargarLista(Fragment oFragmento) {
        boolean rpta = false;

        try {
            Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
            String orden = MediaStore.Audio.Media.ARTIST + " ASC";

            ContentResolver oCR = oFragmento.requireActivity().getContentResolver();
            Cursor oRegistros = oCR.query(uri, null, null, null, orden);

            if (oRegistros.moveToFirst()) {
                rpta = true;
                do {
                    long artistaId = oRegistros.getLong(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
                    String nombreArtista = oRegistros.getString(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
                    int numAlbumes = oRegistros.getInt(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
                    int numCanciones = oRegistros.getInt(oRegistros.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                    String snumAlbumes = numAlbumes == 1 ? numAlbumes + " Albúm" : numAlbumes + " Álbumes";
                    String sNumCanciones = numCanciones == 1 ? numCanciones + " Canción" : numCanciones + " Canciones";
                    String sNumAlb_Can = snumAlbumes + " • " + sNumCanciones;
                    //-------------------------------------------------------------------------------------------------------------
                    Uri artistaUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), artistaId);
                    Artista artista = new Artista(nombreArtista, sNumAlb_Can, artistaUri);
                    listaArtistas.add(artista);
                } while (oRegistros.moveToNext());
            }
            oRegistros.close();
        } catch (Exception e) {
            Log.d("Estado:", e.getMessage());
        }
        return rpta;
    }

    public Artista get(int i) {
        return listaArtistas.get(i);
    }

    public ArrayList<Artista> listar() {
        return new ArrayList<>(listaArtistas);
    }
}
