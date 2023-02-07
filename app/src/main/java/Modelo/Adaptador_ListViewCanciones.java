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

public class Adaptador_ListViewCanciones extends ArrayAdapter<Cancion> {

    Context contexto;

    public Adaptador_ListViewCanciones(@NonNull Context context, int resource, @NonNull List<Cancion> objects) {
        super(context, resource, objects);
        contexto = getContext();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_cancion, null);
        }

        Cancion cancion = getItem(position);
        TextView txtCancion = v.findViewById(R.id.txtCancion);
        TextView txtArtista = v.findViewById(R.id.txtArtista);
        ImageView imgIconoMusica = v.findViewById(R.id.imgIconoMusica);

        txtCancion.setText(cancion.getNombre());
        txtArtista.setText(cancion.getArtista());
        imgIconoMusica.setImageURI(cancion.getImagenAlbum());

        imgIconoMusica.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return v;
    }
}