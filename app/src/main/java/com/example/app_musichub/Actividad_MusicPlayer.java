package com.example.app_musichub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Objects;

import AccesoDatos.DAO_Favoritos;
import Modelo.Cancion;

public class Actividad_MusicPlayer extends AppCompatActivity {

    TextView txtCancionReproductor, txtArtistaReproductor, txtTiempoActual, txtTiempoTotal;
    CheckBox ckFavorito, ckAleatorio, ckRepetir;
    SeekBar barraReproduccion;
    Button btnAtrasReproductor, btnPlayPausaReproductor, btnAdelanteReproductor;
    LinearLayout linear_music_player;
    ShapeableImageView imgAlbumReproductor;

    static MediaPlayer mediaPlayer;
    ArrayList<Cancion> canciones;
    int posCancion;
    Thread actualizarBarraReproduccion;
    Cancion cancion_rep;
    DAO_Favoritos oDAOFavoritos;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_music_player);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Reproduciendo ahora");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtCancionReproductor = findViewById(R.id.txtCancionReproductor);
        txtArtistaReproductor = findViewById(R.id.txtArtistaReproductor);
        imgAlbumReproductor = findViewById(R.id.imgAlbumReproductor);
        ckFavorito = findViewById(R.id.ckFavorito);
        ckAleatorio = findViewById(R.id.ckAleatorio);
        ckRepetir = findViewById(R.id.ckRepetir);
        barraReproduccion = findViewById(R.id.barraReproduccion);
        txtTiempoActual = findViewById(R.id.txtTiempoActual);
        txtTiempoTotal = findViewById(R.id.txtTiempoTotal);
        btnAtrasReproductor = findViewById(R.id.btnAtrasReproductor);
        btnPlayPausaReproductor = findViewById(R.id.btnPlayPausaReproductor);
        btnAdelanteReproductor = findViewById(R.id.btnAdelanteReproductor);
        linear_music_player = findViewById(R.id.linear_music_player);

        txtCancionReproductor.setSelected(true);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        try {
            Intent i = getIntent();
            canciones = i.getParcelableArrayListExtra("canciones"); // getParcelableArrayExtra
            posCancion = i.getIntExtra("posCancion", 0);

            oDAOFavoritos = new DAO_Favoritos();

            cancion_rep = canciones.get(posCancion);
            txtCancionReproductor.setText(cancion_rep.getNombre());
            txtArtistaReproductor.setText(cancion_rep.getArtista());
            imgAlbumReproductor.setImageURI(cancion_rep.getImagenAlbum());
            imgAlbumReproductor.setScaleType(ImageView.ScaleType.CENTER_CROP);

            ckFavorito.setOnCheckedChangeListener((compoundButton, b) -> {
                try {
                    if (ckFavorito.isChecked()) {
                        if (oDAOFavoritos.agregar(this, cancion_rep)) {
                            Toast.makeText(this, "¡Favorito agregado!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "¡Error al agregar!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        oDAOFavoritos.eliminar(this, cancion_rep);
                    }
                } catch (Exception e) {
                    Log.e("Error Reproductor", e.getMessage());
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(cancion_rep.getData()));
            mediaPlayer.start();

            actualizarBarraReproduccion = new Thread() {
                @Override
                public void run() {
                    int tiempoTotal = mediaPlayer.getDuration();
                    int tiempoActual = 0;

                    while (tiempoActual < tiempoTotal) {
                        try {
                            tiempoActual = mediaPlayer.getCurrentPosition();
                            barraReproduccion.setProgress(tiempoActual);
                            sleep(500);
                        } catch (InterruptedException | IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            barraReproduccion.setMax(mediaPlayer.getDuration());
            actualizarBarraReproduccion.start();
            barraReproduccion.getProgressDrawable().setColorFilter(getResources().getColor(R.color.primary_dark), PorterDuff.Mode.MULTIPLY);
            barraReproduccion.getThumb().setColorFilter(getResources().getColor(R.color.primary_dark), PorterDuff.Mode.SRC_IN);
            barraReproduccion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(barraReproduccion.getProgress());
                    if (barraReproduccion.getProgress() == mediaPlayer.getDuration()) {
                        btnAdelanteReproductor.performClick();
                    }
                }
            });

            txtTiempoTotal.setText(actualizarTiempoTxt(mediaPlayer.getDuration()));

            final Handler handler = new Handler();
            final int delay = 1000;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtTiempoActual.setText(actualizarTiempoTxt(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, delay);
                }
            }, delay);

        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }

        btnPlayPausaReproductor.setOnClickListener(view -> onBtnPlayClick());
        btnAtrasReproductor.setOnClickListener(view -> onBtnAtrasClick());
        btnAdelanteReproductor.setOnClickListener(view -> onBtnAdelanteClick());
        mediaPlayer.setOnCompletionListener(mediaPlayer -> btnAdelanteReproductor.performClick());
    }

    void onBtnPlayClick() {
        if (mediaPlayer.isPlaying()) {
            btnPlayPausaReproductor.setBackgroundResource(R.drawable.ic_play);
            mediaPlayer.pause();
        } else {
            btnPlayPausaReproductor.setBackgroundResource(R.drawable.ic_pausa);
            mediaPlayer.start();
        }
    }

    void onBtnAdelanteClick() {
        mediaPlayer.stop();
        mediaPlayer.release();
        posCancion = ((posCancion + 1) % canciones.size());

        cancion_rep = canciones.get(posCancion);
        txtCancionReproductor.setText(cancion_rep.getNombre());
        txtArtistaReproductor.setText(cancion_rep.getArtista());
        imgAlbumReproductor.setImageURI(cancion_rep.getImagenAlbum());
        imgAlbumReproductor.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(cancion_rep.getData()));
        mediaPlayer.start();
        btnPlayPausaReproductor.setBackgroundResource(R.drawable.ic_pausa);
        startAnimation();

        txtTiempoTotal.setText(actualizarTiempoTxt(mediaPlayer.getDuration()));
        barraReproduccion.setMax(mediaPlayer.getDuration());
        barraReproduccion.setProgress(0);
        mediaPlayer.seekTo(0);
    }

    void onBtnAtrasClick() {
        mediaPlayer.stop();
        mediaPlayer.release();
        posCancion = ((posCancion - 1) < 0) ? (canciones.size() - 1) : (posCancion - 1);

        cancion_rep = canciones.get(posCancion);
        txtCancionReproductor.setText(cancion_rep.getNombre());
        txtArtistaReproductor.setText(cancion_rep.getArtista());
        imgAlbumReproductor.setImageURI(cancion_rep.getImagenAlbum());
        imgAlbumReproductor.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(cancion_rep.getData()));
        mediaPlayer.start();
        btnPlayPausaReproductor.setBackgroundResource(R.drawable.ic_pausa);
        startAnimation();

        txtTiempoTotal.setText(actualizarTiempoTxt(mediaPlayer.getDuration()));
        barraReproduccion.setMax(mediaPlayer.getDuration());
        barraReproduccion.setProgress(0);
        mediaPlayer.seekTo(0);
    }

    void startAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgAlbumReproductor, "rotation", 0f, 360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    private String actualizarTiempoTxt(int duracion) {
        String tiempo = "";

        int min = duracion / 1000 / 60;
        int seg = duracion / 1000 % 60;

        tiempo += min + ":";

        if (seg < 10) {
            tiempo += 0;
        }
        tiempo += seg;

        return tiempo;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int orientacion = getResources().getConfiguration().orientation;

        if (orientacion == Configuration.ORIENTATION_PORTRAIT) {
            linear_music_player.setOrientation(LinearLayout.VERTICAL);
        } else {
            linear_music_player.setOrientation(LinearLayout.HORIZONTAL);
        }
    }
}