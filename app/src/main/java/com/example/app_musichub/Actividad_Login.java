package com.example.app_musichub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Modelo.Usuario;

public class Actividad_Login extends AppCompatActivity {

    EditText txtUsuario, txtContrasena;
    TextView txtLinkOlvidasteContrasena, txtLinkRegistro;
    Button btnIniciarSesion;
    ProgressDialog oDialogo;
    RequestQueue oProceso;
    JsonObjectRequest oSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_login);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        txtLinkOlvidasteContrasena = findViewById(R.id.txtLinkOlvidasteContrasena);
        txtLinkRegistro = findViewById(R.id.txtLinkRegistro);

        btnIniciarSesion.setOnClickListener(view -> verificarUsuario());
        txtLinkOlvidasteContrasena.setOnClickListener(view -> startActivity(new Intent(this, Actividad_OlvidasteContrasena.class)));
        txtLinkRegistro.setOnClickListener(view -> startActivity(new Intent(this, Actividad_Registro.class)));
    }

    private boolean hayCamposVacios() {
        String usuario = txtUsuario.getText().toString();
        String contrasena = txtContrasena.getText().toString();
        return usuario.isEmpty() || contrasena.isEmpty();
    }

    private void verificarUsuario() {

        String usuario = txtUsuario.getText().toString();
        String contrasena = txtContrasena.getText().toString();

        if (!hayCamposVacios()) {
            oDialogo = new ProgressDialog(this);
            oDialogo.setMessage("Cargando datos...");
            oDialogo.setCancelable(false);
            oDialogo.show();
            String url = "https://jfxproh.000webhostapp.com/api/login_musichub.php";
            HashMap<String, String> parametros = new HashMap<>();
            parametros.put("codusu", usuario);
            parametros.put("clave", contrasena);

            oProceso = Volley.newRequestQueue(this);
            oSolicitud = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(parametros), response -> {
                try {
                    JSONArray oRegistros = response.getJSONArray("Usuarios");
                    JSONObject fila = oRegistros.getJSONObject(0);
                    Usuario oUsuario = new Usuario();
                    oUsuario.setEstado(fila.getString("estado"));

                    if (oUsuario.verificarCredencial()) {
                        oUsuario.setCodusu(fila.getString("codusu"));
                        oUsuario.setNombre(fila.getString("nombrecompleto"));
                        oUsuario.setCorreo(fila.getString("correo"));
                        oUsuario.setClave(fila.getString("clave"));
                        oUsuario.setTipo(fila.getInt("tipousuario"));

                        SharedPreferences oFlujo = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
                        SharedPreferences.Editor oEditor = oFlujo.edit();
                        oEditor.putString("nombre", oUsuario.getNombre());
                        oEditor.putString("usuario", oUsuario.getCodusu());
                        oEditor.putInt("estado", 1);
                        oEditor.apply();

                        startActivity(new Intent(this, Actividad_ReproductorPrincipal.class));
                        finish();
                        Toast.makeText(this, oUsuario.mostrarDatos(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, oUsuario.getEstado(), Toast.LENGTH_SHORT).show();
                    }
                    oDialogo.hide();
                } catch (JSONException e) {
                    Toast.makeText(this, "Error API: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    oDialogo.hide();
                }
            }, error -> {
                Toast.makeText(this, "Error de conectividad: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                oDialogo.hide();
            });
            oProceso.add(oSolicitud);
            // Líneas de código se ejecutan antes de onResponse
        } else {
            Toast.makeText(this, "¡Existen campos vacíos!", Toast.LENGTH_SHORT).show();

            if (usuario.isEmpty()) {
                txtUsuario.requestFocus();
            } else {
                txtContrasena.requestFocus();
            }

        }
    }
}