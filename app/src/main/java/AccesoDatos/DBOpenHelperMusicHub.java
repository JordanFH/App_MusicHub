package AccesoDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelperMusicHub extends SQLiteOpenHelper {

    String tabla_favoritos = "CREATE TABLE [Favoritos] ( " +
            "[IdCancion] INTEGER  NOT NULL PRIMARY KEY," +
            "[Nombre] VARCHAR(80) UNIQUE NOT NULL," +
            "[Artista] VARCHAR(80)," +
            "[Imagen] VARCHAR(80)," +
            "[Data] VARCHAR(80) NOT NULL," +
            "[Duracion] INTEGER" +
            ")";

    public DBOpenHelperMusicHub(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase oDB) {
        oDB.execSQL(tabla_favoritos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase oDB, int i, int i1) {
        oDB.execSQL("DROP TABLE IF EXISTS Favoritos");
        oDB.execSQL(tabla_favoritos);
    }
}
