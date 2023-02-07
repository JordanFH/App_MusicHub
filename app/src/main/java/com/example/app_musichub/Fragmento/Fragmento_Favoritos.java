package com.example.app_musichub.Fragmento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_musichub.Actividad_MusicPlayer;
import com.example.app_musichub.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import AccesoDatos.DAO_Favoritos;
import Modelo.Adaptador_ListViewCanciones;
import Modelo.Cancion;

public class Fragmento_Favoritos extends Fragment {

    ListView lvFavoritos;
    TextView txtFavoritosDefault;
    DAO_Favoritos listaFavoritos;
    Context contexto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fg_favoritos, container, false);
        contexto = getActivity();
        lvFavoritos = layout.findViewById(R.id.lvFavoritos);
        txtFavoritosDefault = layout.findViewById(R.id.txtFavoritosDefault);
        mostrarListaFavoritos();
        return layout;
    }

    public void mostrarListaFavoritos() {
        try {
            listaFavoritos = new DAO_Favoritos();
            ArrayList<Cancion> temp = new ArrayList<>();

            if (!listaFavoritos.cargarLista(this)) {
                Toast.makeText(contexto, "No hay ningún registro", Toast.LENGTH_SHORT).show();
                txtFavoritosDefault.setVisibility(View.VISIBLE);
            } else {

                temp.addAll(listaFavoritos.listar());
                Set<Cancion> set = new HashSet<>(temp);

                temp.clear();
                temp.addAll(set);
                Collections.sort(temp);

                Adaptador_ListViewCanciones listViewAdapter = new Adaptador_ListViewCanciones(contexto, R.layout.list_item_cancion, temp);
                lvFavoritos.setAdapter(listViewAdapter);
                txtFavoritosDefault.setVisibility(View.GONE);
            }

            lvFavoritos.setOnItemClickListener((adapterView, view, i, l) -> {
                if (!listaFavoritos.listar().isEmpty()) {
                    contexto.startActivity(new Intent(contexto, Actividad_MusicPlayer.class)
                            .putParcelableArrayListExtra("canciones", temp)
                            .putExtra("posCancion", i));
                }
            });

        } catch (Exception e) {
            Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("Error reproductor:", e.getMessage());
        }
    }

}