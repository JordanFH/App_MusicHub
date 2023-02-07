package com.example.app_musichub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import AccesoDatos.DAO_Canciones;
import Modelo.Adaptador_ListViewCanciones;
import Modelo.Cancion;

public class Actividad_Busqueda extends AppCompatActivity {

    TextInputEditText txtBusqueda;
    ListView lvBusqueda;
    DAO_Canciones listaCanciones;
    ArrayList<Cancion> seleccionadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_busqueda);

        Objects.requireNonNull(getSupportActionBar()).hide();
        lvBusqueda = findViewById(R.id.lvBusqueda);
        txtBusqueda = findViewById(R.id.txtBusqueda);

        listaCanciones = new DAO_Canciones();
        seleccionadas = new ArrayList<>();

        if (listaCanciones.cargarLista(this)) {
            Adaptador_ListViewCanciones listViewAdapter = new Adaptador_ListViewCanciones(getApplicationContext(), R.layout.list_item_cancion, listaCanciones.listar());
            lvBusqueda.setAdapter(listViewAdapter);
            seleccionadas.clear();
            seleccionadas.addAll(listaCanciones.listar());
        }

        txtBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    Adaptador_ListViewCanciones listViewAdapter = new Adaptador_ListViewCanciones(getApplicationContext(), R.layout.list_item_cancion, filtrar(editable.toString()));
                    lvBusqueda.setAdapter(listViewAdapter);
                    seleccionadas.clear();
                    seleccionadas.addAll(filtrar(editable.toString()));
                } else {
                    Adaptador_ListViewCanciones listViewAdapter = new Adaptador_ListViewCanciones(getApplicationContext(), R.layout.list_item_cancion, listaCanciones.listar());
                    lvBusqueda.setAdapter(listViewAdapter);
                    seleccionadas.clear();
                    seleccionadas.addAll(listaCanciones.listar());
                }
            }
        });

        lvBusqueda.setOnItemClickListener((adapterView, view, i, l) -> {
            ArrayList<Cancion> unico = new ArrayList<>();
            unico.add(seleccionadas.get(i));

            startActivity(new Intent(this, Actividad_MusicPlayer.class)
                    .putParcelableArrayListExtra("canciones", unico)
                    .putExtra("posCancion", 0));
        });

    }

    public ArrayList<Cancion> filtrar(String texto) {
        ArrayList<Cancion> filtrarLista = new ArrayList<>();

        Set<Cancion> set = new HashSet<>(filtrarLista);

        for (Cancion cancion : listaCanciones.listar()) {
            if (cancion.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                set.add(cancion);
            }
        }

        filtrarLista.clear();
        filtrarLista.addAll(set);
        Collections.sort(filtrarLista);

        return filtrarLista;
    }
}