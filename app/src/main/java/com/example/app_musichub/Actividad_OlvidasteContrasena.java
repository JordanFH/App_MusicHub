package com.example.app_musichub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Actividad_OlvidasteContrasena extends AppCompatActivity {

    EditText txtCorreo;
    Button btnEnviarCorreo;
    ProgressDialog oDialogo;
    RequestQueue oProceso;
    JsonObjectRequest oSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_olvidaste_contrasena);

        txtCorreo = findViewById(R.id.txtEnviarCorreo);
        btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo);

        btnEnviarCorreo.setOnClickListener(view -> verificarCorreoRegistrado());
    }

    String mensaje;

    private void verificarCorreoRegistrado() {
        String correo = txtCorreo.getText().toString().trim();
        if (!correo.isEmpty()) {
            if (esCorreo(correo)) {
                oDialogo = new ProgressDialog(this);
                oDialogo.setMessage("Verificando correo...");
                oDialogo.setCancelable(false);
                oDialogo.show();
                String url = "https://jfxproh.000webhostapp.com/api/recuperar_credenciales_musichub.php";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("correo", correo);

                oProceso = Volley.newRequestQueue(this);
                oSolicitud = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(parametros), response -> {
                    try {
                        JSONArray oRegistros = response.getJSONArray("Usuarios");
                        JSONObject fila = oRegistros.getJSONObject(0);
                        String codusu = fila.getString("codusu");
                        String clave = fila.getString("clave");
                        // mensaje = "Usuario: " + codusu + " - Contraseña: " + clave;
                        mensaje = String.format("<html>" +
                                "  <head>" +
                                "    <style>" +
                                "      .colored {" +
                                "        color: rgb(255, 153, 0);" +
                                "      }" +
                                "      #body {" +
                                "        font-size: 14px;" +
                                "        font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto," +
                                "          Oxygen, Ubuntu, Cantarell, \"Open Sans\", \"Helvetica Neue\", sans-serif;" +
                                "      }" +
                                "      #credenciales h3{" +
                                "        display: inline;" +
                                "      }" +
                                "    </style>" +
                                "  </head>" +
                                "  <body>" +
                                "    <div id=\"body\">" +
                                "      <h2>Hola</h2>" +
                                "      <h3>Tus credenciales de acceso son las siguientes:</h3>" +
                                "      <div id=\"credenciales\">" +
                                "        <h3 class=\"colored\">• Usuario: </h3><h3>%1$s</h3>" +
                                "        <br>" +
                                "        <h3 class=\"colored\">• Contraseña: </h3><h3>%2$s</h3>" +
                                "      </div>" +
                                "    </div>" +
                                "  </body>" +
                                "</html>", codusu, clave);
                        enviarCorreo(correo);
                        oDialogo.hide();
                        startActivity(new Intent(Actividad_OlvidasteContrasena.this, Actividad_Login.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Espere, se está enviando el correo.", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(this, "¡Correo no registrado!", Toast.LENGTH_SHORT).show();
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
                txtCorreo.requestFocus();
            }
        } else {
            Toast.makeText(this, "¡Campo vacío!", Toast.LENGTH_SHORT).show();
            txtCorreo.requestFocus();
        }
    }

    private void enviarCorreo(String correoDestino) {
        try {
            if (mensaje != null) {
                sendEmailWithGmail(
                        "musichub.pe@gmail.com",
                        "!Sanramon12",
                        correoDestino,
                        "Credenciales de acceso a Music Hub",
                        mensaje);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean esCorreo(String texto) {
        return Patterns.EMAIL_ADDRESS.matcher(texto).matches();
    }

    /**
     * Send email with Gmail service.
     */
    private void sendEmailWithGmail(final String recipientEmail, final String recipientPassword,
                                    String to, String subject, String message) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(recipientEmail, recipientPassword);
            }
        });

        SenderAsyncTask task = new SenderAsyncTask(session, recipientEmail, to, subject, message);
        task.execute();
    }

    private class SenderAsyncTask extends AsyncTask<String, String, String> {

        private String from, to, subject, message;
        private Session session;

        public SenderAsyncTask(Session session, String from, String to, String subject, String message) {
            this.session = session;
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Message mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                mimeMessage.setSubject(subject);
                mimeMessage.setContent(message, "text/html; charset=utf-8");
                Transport.send(mimeMessage);
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return "¡Correo enviado!";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

}
