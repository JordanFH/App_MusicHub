package com.example.app_musichub.Fragmento;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app_musichub.Actividad_MusicPlayer;
import com.example.app_musichub.MainActivity;
import com.example.app_musichub.R;

import java.util.ArrayList;

import AccesoDatos.DAO_Canciones;
import Modelo.Adaptador_ListViewCanciones;
import Modelo.Cancion;

public class Fragmento_Canciones extends Fragment {

    ListView lvCanciones;
    DAO_Canciones listaCanciones;
    Context contexto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fg_canciones, container, false);
        contexto = getActivity();
        lvCanciones = layout.findViewById(R.id.lvCanciones);
        mostrarListaCanciones();
        return layout;
    }

    ArrayList<Cancion> listaError;

    public void mostrarListaCanciones() {
        try {
            listaCanciones = new DAO_Canciones();

            if (listaCanciones.cargarLista(this)) {
                Adaptador_ListViewCanciones listViewAdapter = new Adaptador_ListViewCanciones(contexto, R.layout.list_item_cancion, listaCanciones.listar());
                lvCanciones.setAdapter(listViewAdapter);
            } else {
                listaError = new ArrayList<>();

                Uri uriImg = Uri.parse("android.resource://" + contexto.getPackageName() + "/" + R.drawable.advertencia_small);
                Cancion cancion_error = new Cancion("No hay canciones", "Click aquí para reiniciar la aplicación", uriImg);

                listaError.add(cancion_error);

                Adaptador_ListViewCanciones listViewAdapter = new Adaptador_ListViewCanciones(contexto, R.layout.list_item_cancion, listaError);
                lvCanciones.setAdapter(listViewAdapter);
            }
            lvCanciones.setOnItemClickListener((adapterView, view, i, l) -> {
                if (listaCanciones.listar().isEmpty()) {
                    contexto.startActivity(new Intent(contexto, MainActivity.class));
                    requireActivity().finish();
                } else {
                    contexto.startActivity(new Intent(contexto, Actividad_MusicPlayer.class)
                            .putParcelableArrayListExtra("canciones", listaCanciones.listar())
                            .putExtra("posCancion", i));
                }
            });

        } catch (Exception e) {
            Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("Error reproductor:", e.getMessage());
        }
    }

}