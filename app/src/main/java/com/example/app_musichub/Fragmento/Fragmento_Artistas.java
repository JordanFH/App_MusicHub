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

import com.example.app_musichub.MainActivity;
import com.example.app_musichub.R;

import java.util.ArrayList;

import AccesoDatos.DAO_Artistas;
import Modelo.Adaptador_ListViewArtistas;
import Modelo.Artista;

public class Fragmento_Artistas extends Fragment {

    ListView lvArtistas;
    DAO_Artistas listaArtistas;
    Context contexto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fg_artistas, container, false);
        contexto = getActivity();
        lvArtistas = layout.findViewById(R.id.lvArtistas);
        mostrarListaArtistas();

        return layout;
    }

    ArrayList<Artista> listaError;

    public void mostrarListaArtistas() {
        try {
            listaArtistas = new DAO_Artistas();

            if (listaArtistas.cargarLista(this)) {
                Adaptador_ListViewArtistas listViewAdapter = new Adaptador_ListViewArtistas(contexto, R.layout.list_item_artista, listaArtistas.listar());
                lvArtistas.setAdapter(listViewAdapter);
            } else {
                listaError = new ArrayList<>();

                Uri uriImg = Uri.parse("android.resource://" + contexto.getPackageName() + "/" + R.drawable.advertencia_small);
                Artista artista_error = new Artista("No hay artistas", "Click aquí para reiniciar la aplicación", uriImg);

                listaError.add(artista_error);

                Adaptador_ListViewArtistas listViewAdapter = new Adaptador_ListViewArtistas(contexto, R.layout.list_item_artista, listaError);
                lvArtistas.setAdapter(listViewAdapter);
            }
            lvArtistas.setOnItemClickListener((adapterView, view, i, l) -> {
                if (listaArtistas.listar().isEmpty()) {
                    contexto.startActivity(new Intent(contexto, MainActivity.class));
                    requireActivity().finish();
                } else {
                    Artista artista_click = listaArtistas.get(i);
                    Toast.makeText(contexto, artista_click.getNombre() + " - " + artista_click.getNumAlbumes_Canciones(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("Error reproductor:", e.getMessage());
        }
    }

}