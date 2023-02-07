package AccesoDatos;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

import Modelo.Cancion;

public class DAO_Favoritos implements Serializable {
    ArrayList<Cancion> favoritos;
    private final String nombreDB;
    private final int version;

    public DAO_Favoritos() {
        this.favoritos = new ArrayList<>();
        nombreDB = "dbMusicHub";
        version = 1;
    }

    public boolean agregar(Activity oActividad, Cancion cancion) {
        boolean rpta = false;
        DBOpenHelperMusicHub openHelper = new DBOpenHelperMusicHub(oActividad, nombreDB, null, version);
        SQLiteDatabase oDB = openHelper.getWritableDatabase();

        if (oDB != null) {
            ContentValues campos = new ContentValues();
            campos.put("Nombre", cancion.getNombre());
            campos.put("Artista", cancion.getArtista());
            campos.put("Imagen", String.valueOf(cancion.getImagenAlbum()));
            campos.put("Data", cancion.getData());
            campos.put("Duracion", cancion.getDuracion());

            int fila = (int) oDB.insert("Favoritos", null, campos);
            if (fila > 0) {
                rpta = true;
                oDB.close();
            }
        }
        return rpta;
    }

    public boolean cargarLista(Fragment oFragmento) {
        boolean rpta = false;

        DBOpenHelperMusicHub openHelper = new DBOpenHelperMusicHub(oFragmento.requireActivity(), nombreDB, null, version);
        SQLiteDatabase oDB = openHelper.getReadableDatabase();
        try (Cursor registros = oDB.rawQuery("SELECT * FROM Favoritos", null)) {

            if (registros.moveToFirst()) {
                rpta = true;
                do {
                    String nombre = registros.getString(1);
                    String artista = registros.getString(2);
                    Uri imagenAlbum = Uri.parse(registros.getString(3));
                    String data = registros.getString(4);
                    long duracion = registros.getInt(5);

                    Cancion cancion = new Cancion(nombre, artista, imagenAlbum, data, duracion);
                    favoritos.add(cancion);

                } while (registros.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(oFragmento.requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return rpta;
    }

    public void eliminar(Activity oActividad, Cancion cancion)
    {
        DBOpenHelperMusicHub openHelper = new DBOpenHelperMusicHub(oActividad, nombreDB, null, version);
        SQLiteDatabase oDB = openHelper.getWritableDatabase();
        oDB.execSQL("DELETE FROM " + "Favoritos" + " WHERE " + "Nombre" + "= '" + cancion.getNombre() + "'");
        oDB.close();
    }

    public Cancion getCancion(int i) {
        return favoritos.get(i);
    }

    //cantidad de datos
    public int size() {
        return favoritos.size();
    }

    public ArrayList<Cancion> listar() {
        return new ArrayList<>(favoritos);
    }
}

