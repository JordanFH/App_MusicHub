package com.example.app_musichub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Actividad_Registro extends AppCompatActivity {

    EditText txtRegistroNombre, txtRegistroCorreo, txtRegistroUsuario, txtRegistroContrasena;
    TextView txtLinkLogin;
    Button btnRegistrarse;
    ProgressDialog oDialogo;
    RequestQueue oProceso;
    JsonObjectRequest oSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_registro);

        txtRegistroNombre = findViewById(R.id.txtRegistroNombre);
        txtRegistroCorreo = findViewById(R.id.txtRegistroCorreo);
        txtRegistroUsuario = findViewById(R.id.txtRegistroUsuario);
        txtRegistroContrasena = findViewById(R.id.txtRegistroContrasena);
        txtLinkLogin = findViewById(R.id.txtLinkLogin);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        txtLinkLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, Actividad_Login.class));
            finish();
        });
        btnRegistrarse.setOnClickListener(view -> crearUsuario());
    }

    private boolean hayCamposVacios() {
        String nombre = txtRegistroNombre.getText().toString();
        String correo = txtRegistroCorreo.getText().toString();
        String usuario = txtRegistroUsuario.getText().toString();
        String contrasena = txtRegistroContrasena.getText().toString();
        return nombre.isEmpty() || correo.isEmpty() || usuario.isEmpty() || contrasena.isEmpty();
    }

    private void crearUsuario() {

        String usuario = txtRegistroUsuario.getText().toString();
        String nombre = txtRegistroNombre.getText().toString();
        String correo = txtRegistroCorreo.getText().toString();
        String contrasena = txtRegistroContrasena.getText().toString();

        if (!hayCamposVacios()) {
            if (esCorreo(correo)) {
                oDialogo = new ProgressDialog(this);
                oDialogo.setMessage("Cargando datos...");
                oDialogo.setCancelable(false);
                oDialogo.show();
                String url = "https://jfxproh.000webhostapp.com/api/registro_musichub.php";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("codusu", usuario);
                parametros.put("nombrecompleto", nombre);
                parametros.put("correo", correo);
                parametros.put("clave", contrasena);

                oProceso = Volley.newRequestQueue(this);
                oSolicitud = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(parametros), response -> {
                    try {
                        String respuesta = response.getString("estado");

                        if (respuesta.equals("¡Cuenta registrada!")) {
                            startActivity(new Intent(this, Actividad_Login.class));
                            finish();
                        } else {
                            txtRegistroUsuario.requestFocus();
                        }
                        Toast.makeText(this, respuesta, Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "¡Correo no válido!", Toast.LENGTH_SHORT).show();
                txtRegistroCorreo.requestFocus();
            }
        } else {
            Toast.makeText(this, "¡Existen campos vacíos!", Toast.LENGTH_SHORT).show();

            if (nombre.isEmpty()) {
                txtRegistroNombre.requestFocus();
            } else if (correo.isEmpty()) {
                txtRegistroCorreo.requestFocus();
            } else if (usuario.isEmpty()) {
                txtRegistroUsuario.requestFocus();
            } else {
                txtRegistroContrasena.requestFocus();
            }
        }
    }

    private boolean esCorreo(String texto) {
        return Patterns.EMAIL_ADDRESS.matcher(texto).matches();
    }
}