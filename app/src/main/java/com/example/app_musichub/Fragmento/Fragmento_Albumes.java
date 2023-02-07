package com.example.app_musichub.Fragmento;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.app_musichub.MainActivity;
import com.example.app_musichub.R;

import java.util.ArrayList;

import AccesoDatos.DAO_Albumes;
import Modelo.Adaptador_GridViewAlbumes;
import Modelo.Album;

public class Fragmento_Albumes extends Fragment {

    GridView gdAlbumes;
    DAO_Albumes listaAlbumes;
    Context contexto;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fg_albumes, container, false);
        contexto = getActivity();
        gdAlbumes = layout.findViewById(R.id.gdAlbumes);
        mostrarListaAlbumes();

        return layout;
    }

    ArrayList<Album> listaError;

    public void mostrarListaAlbumes() {
        try {
            listaAlbumes = new DAO_Albumes();

            if (listaAlbumes.cargarLista(this)) {
                Adaptador_GridViewAlbumes gridViewAdapter = new Adaptador_GridViewAlbumes(contexto, R.layout.grid_item_album, listaAlbumes.listar());
                gdAlbumes.setAdapter(gridViewAdapter);
            } else {
                listaError = new ArrayList<>();

                Uri uriImg = Uri.parse("android.resource://" + contexto.getPackageName() + "/" + R.drawable.advertencia_small);
                Album album_error = new Album("No hay álbumes", "Click para reiniciar", uriImg);

                listaError.add(album_error);

                Adaptador_GridViewAlbumes gridViewAdapter = new Adaptador_GridViewAlbumes(contexto, R.layout.grid_item_album, listaError);
                gdAlbumes.setAdapter(gridViewAdapter);
            }
            gdAlbumes.setOnItemClickListener((adapterView, view, i, l) -> {
                if (listaAlbumes.listar().isEmpty()) {
                    contexto.startActivity(new Intent(contexto, MainActivity.class));
                    requireActivity().finish();
                } else {
                    Album album_click = listaAlbumes.get(i);
                    Toast.makeText(contexto, album_click.getNombre() + " - " + album_click.getNumCanciones(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("Error reproductor:", e.getMessage());
        }
    }
}