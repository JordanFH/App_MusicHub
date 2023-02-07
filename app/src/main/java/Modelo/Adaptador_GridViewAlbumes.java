package Modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app_musichub.R;

import java.util.List;

public class Adaptador_GridViewAlbumes extends ArrayAdapter<Album> {

    Context contexto;

    public Adaptador_GridViewAlbumes(@NonNull Context context, int resource, @NonNull List<Album> objects) {
        super(context, resource, objects);
        contexto = getContext();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item_album, null);
        }

        Album album = getItem(position);

        TextView txtAlbum = v.findViewById(R.id.txtAlbum);
        TextView txtNumeroCanciones = v.findViewById(R.id.txtNumeroCanciones);
        ImageView imgIconoAlbum = v.findViewById(R.id.imgIconoAlbum);

        txtAlbum.setText(album.getNombre());
        txtNumeroCanciones.setText(album.getNumCanciones());
        imgIconoAlbum.setImageURI(album.getImagenAlbum());

        imgIconoAlbum.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return v;
    }

}
