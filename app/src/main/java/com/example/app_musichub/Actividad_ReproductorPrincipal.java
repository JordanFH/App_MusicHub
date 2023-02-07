package com.example.app_musichub;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.app_musichub.Fragmento.Adaptador_ViewPager;
import com.example.app_musichub.Fragmento.Fragmento_Albumes;
import com.example.app_musichub.Fragmento.Fragmento_Artistas;
import com.example.app_musichub.Fragmento.Fragmento_Canciones;
import com.example.app_musichub.Fragmento.Fragmento_Favoritos;
import com.google.android.material.tabs.TabLayout;

public class Actividad_ReproductorPrincipal extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    long tiempo_btnBack;

    final int CODIGO_PERMISO = 10;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_reproductor_principal);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        Adaptador_ViewPager adaptadorViewPager = new Adaptador_ViewPager(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adaptadorViewPager.agregarFragmento(new Fragmento_Canciones(), "Canciones");
        adaptadorViewPager.agregarFragmento(new Fragmento_Albumes(), "Álbumes");
        adaptadorViewPager.agregarFragmento(new Fragmento_Artistas(), "Artistas");
        adaptadorViewPager.agregarFragmento(new Fragmento_Favoritos(), "Favoritos");

        viewPager.setAdapter(adaptadorViewPager);

        verificarPermisos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int orientacion = getResources().getConfiguration().orientation;

        if (orientacion == Configuration.ORIENTATION_PORTRAIT) {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void verificarPermisos() {
        int permisoLeerAlmacenamiento = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permisoEscribirAlmacenamiento = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (!(permisoLeerAlmacenamiento == PackageManager.PERMISSION_GRANTED && permisoEscribirAlmacenamiento == PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_PERMISO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODIGO_PERMISO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(this, Actividad_ReproductorPrincipal.class));
                finish();
            } else {
                Toast.makeText(this, "¡Permiso denegado!", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "¡No se pudo buscar canciones!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reproductor_principal, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent oIntento;

        switch (item.getItemId()) {
            case R.id.idBusqueda:
                startActivity(new Intent(this, Actividad_Busqueda.class));
                break;
            case R.id.idCerrarSesion:
                SharedPreferences.Editor oEditor = getSharedPreferences("usuarios", Context.MODE_PRIVATE).edit();
                oEditor.putInt("estado", 0);
                oEditor.apply();
                oIntento = new Intent(this, Actividad_Login.class);
                startActivity(oIntento);
                finish();
                break;
            case R.id.idSalir:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (tiempo_btnBack + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Presione atrás de nuevo para salir", Toast.LENGTH_SHORT).show();
        }
        tiempo_btnBack = System.currentTimeMillis();
    }
}