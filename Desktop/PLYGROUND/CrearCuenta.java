package com.example.playground;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CrearCuenta extends AppCompatActivity {

    EditText etCorreoRegistro;
    EditText etContrasenaRegistro;
    EditText etContrasenaConfirmar;
    Button btnRegistrar;
    Button btnVolverLogin;

    Basesita basesita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        inicializarVistas();
        configurarInsets();
        configurarListeners();
    }

    private void inicializarVistas() {
        basesita = new Basesita(this);

        etCorreoRegistro = findViewById(R.id.etCorreoRegistro);
        etContrasenaRegistro = findViewById(R.id.etContrasenaRegistro);
        etContrasenaConfirmar = findViewById(R.id.etContrasenaConfirmar);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolverLogin = findViewById(R.id.btnVolverLogin);
    }

    private void configurarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void configurarListeners() {
        btnRegistrar.setOnClickListener(v -> intentarRegistrarUsuario());
        btnVolverLogin.setOnClickListener(v -> volverAlLogin());
    }

    private void intentarRegistrarUsuario() {
        String correo = obtenerTexto(etCorreoRegistro);
        String contrasena = obtenerTexto(etContrasenaRegistro);
        String contrasenaConfirmar = obtenerTexto(etContrasenaConfirmar);

        // Validar campos vacíos
        if (camposVacios(correo, contrasena, contrasenaConfirmar)) {
            mostrarMensaje("Por favor, llena todos los campos");
            etCorreoRegistro.requestFocus();
            return;
        }

        // Validar formato de correo
        if (!esCorreoValido(correo)) {
            mostrarMensaje("Por favor, ingresa un correo válido");
            etCorreoRegistro.requestFocus();
            return;
        }

        // Validar longitud mínima de contraseña
        if (contrasena.length() < 6) {
            mostrarMensaje("La contraseña debe tener al menos 6 caracteres");
            etContrasenaRegistro.requestFocus();
            return;
        }

        // Validar que las contraseñas coincidan
        if (!contrasena.equals(contrasenaConfirmar)) {
            mostrarMensaje("Las contraseñas no coinciden");
            etContrasenaConfirmar.requestFocus();
            return;
        }

        // Validar que el correo no exista
        if (basesita.usuarioExiste(correo)) {
            mostrarMensaje("Este correo ya está registrado");
            etCorreoRegistro.requestFocus();
            return;
        }

        // Crear y registrar usuario
        Usuario nuevoUsuario = new Usuario(correo, contrasena);
        long resultado = basesita.insertarUsuario(nuevoUsuario);

        if (resultado != -1) {
            mostrarMensaje("¡Registro exitoso! Ahora puedes iniciar sesión");
            limpiarCampos();

            // Esperar un momento antes de navegar usando Handler moderno
            new Handler(Looper.getMainLooper()).postDelayed(this::volverAlLogin, 1500);
        } else {
            mostrarMensaje("Error al registrar. Intenta nuevamente");
        }
    }

    private String obtenerTexto(EditText editText) {
        return editText.getText().toString().trim();
    }

    private boolean camposVacios(String... campos) {
        for (String campo : campos) {
            if (TextUtils.isEmpty(campo)) {
                return true;
            }
        }
        return false;
    }

    private boolean esCorreoValido(String correo) {
        return !TextUtils.isEmpty(correo) && Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }

    private void limpiarCampos() {
        etCorreoRegistro.setText("");
        etContrasenaRegistro.setText("");
        etContrasenaConfirmar.setText("");
    }

    private void volverAlLogin() {
        Intent intent = new Intent(CrearCuenta.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (basesita != null) {
            basesita.close();
        }
    }
}