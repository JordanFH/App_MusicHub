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

public class Adaptador_ListViewArtistas extends ArrayAdapter<Artista> {

    Context contexto;

    public Adaptador_ListViewArtistas(@NonNull Context context, int resource, @NonNull List<Artista> objects) {
        super(context, resource, objects);
        contexto = getContext();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_artista, null);
        }


        Artista artista = getItem(position);
        TextView txtNombreArtista = v.findViewById(R.id.txtNombreArtista);
        TextView txtNumAlb_Can = v.findViewById(R.id.txtNumAlb_Can);
        ImageView imgIconoArtista = v.findViewById(R.id.imgIconoArtista);

        txtNombreArtista.setText(artista.getNombre());
        txtNumAlb_Can.setText(artista.getNumAlbumes_Canciones());
        imgIconoArtista.setImageURI(artista.getImagenArtista());

        imgIconoArtista.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return v;
    }

}
