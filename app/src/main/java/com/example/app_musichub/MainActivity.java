package com.example.app_musichub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                if (existeUsuario()) {
                    startActivity(new Intent(getApplicationContext(), Actividad_ReproductorPrincipal.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), Actividad_Login.class));
                }
                finish();
            }
        }.start();
    }

    private boolean existeUsuario() {
        SharedPreferences oFlujo = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        int estado = oFlujo.getInt("estado", 0);
        return estado != 0;
    }
}