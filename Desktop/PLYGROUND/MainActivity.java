package com.example.playground;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    EditText etCorreo;
    EditText etContrasena;
    Button btnIniciarSesion;
    Button btnCrearCuenta;

    private Basesita basesita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarVistas();
        configurarInsets();
        configurarListeners();
    }

    private void inicializarVistas() {
        basesita = new Basesita(this);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        btnCrearCuenta = findViewById(R.id.btn_crear_cuenta);
    }

    private void configurarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void configurarListeners() {
        btnIniciarSesion.setOnClickListener(v -> intentarIniciarSesion());
        btnCrearCuenta.setOnClickListener(v -> abrirCrearCuenta());
    }


    private void intentarIniciarSesion() {
        String correo = obtenerTexto(etCorreo);
        String contrasena = obtenerTexto(etContrasena);

        if (camposVacios(correo, contrasena)) {
            mostrarMensaje("Por favor, llena todos los campos");
            return;
        }

        if (validarCredenciales(correo, contrasena)) {
            mostrarMensaje("¡Bienvenido!");
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            mostrarMensaje("Correo o contraseña incorrectos");
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

    private boolean validarCredenciales(String correo, String contrasena) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = basesita.getReadableDatabase();

            String[] columnas = { Basesita.COL_PASSWORD };
            String seleccion = Basesita.COL_USUARIO + " = ?";
            String[] args = { correo };

            cursor = db.query(
                    Basesita.TABLA_USUARIOS,
                    columnas,
                    seleccion,
                    args,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                int passwordIndex = cursor.getColumnIndex(Basesita.COL_PASSWORD);
                if (passwordIndex != -1) {
                    String passwordGuardada = cursor.getString(passwordIndex);
                    return contrasena.equals(passwordGuardada);
                }
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private void abrirCrearCuenta() {
        Intent intent = new Intent(MainActivity.this, CrearCuenta.class);
        startActivity(intent);
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