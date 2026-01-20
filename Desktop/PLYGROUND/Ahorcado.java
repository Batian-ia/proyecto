package com.example.playground;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.*;
import java.util.Random;

public class Ahorcado extends Fragment {

    EditText txtLetra;
    Button btnAdivinar, btnReiniciar;
    ImageView imgAhorcado;
    TextView txtProgreso;

    String[] palabras = {"papa", "manzana", "perro", "gato", "casa", "silla", "mesa", "puerta", "ventana", "coche", "arbol", "flor", "cielo", "mar", "rio", "montaña", "camino", "libro", "cuaderno", "lapiz",
            "pluma", "mochila", "escuela", "maestro", "alumno", "pizarra", "teclado", "pantalla", "raton", "celular",
            "reloj", "tiempo", "fuego", "agua", "aire", "tierra", "sol", "luna", "estrella", "nube",
            "lluvia", "viento", "trueno", "rayo", "bosque", "jardin", "semilla", "fruta", "verdura", "carne",
            "queso", "pan", "leche", "huevo", "arroz", "sopa", "tacos", "salsa", "azucar", "sal",
            "dulce", "amargo", "feliz", "triste", "rapido", "lento", "grande", "pequeño", "alto", "bajo",
            "izquierda", "derecha", "cerca", "lejos", "dentro", "fuera", "abrir", "cerrar", "subir", "bajar"};

    String palabraActual;
    String progreso;
    int intentos = 6;

    int[] imagenes = {
            R.drawable.ahorcado0,
            R.drawable.ahorcado1,
            R.drawable.ahorcado2,
            R.drawable.ahorcado3,
            R.drawable.ahorcado4,
            R.drawable.ahorcado5,
            R.drawable.ahorcado6
    };

    Random random = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_ahorcado, container, false);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fontp);
        aplicarFuente(vista, typeface);

        txtLetra = vista.findViewById(R.id.txtLetra);
        btnAdivinar = vista.findViewById(R.id.btnAdivinar);
        btnReiniciar = vista.findViewById(R.id.btnReiniciar);
        imgAhorcado = vista.findViewById(R.id.imgAhorcado);
        txtProgreso = vista.findViewById(R.id.txtProgreso);

        seleccionarNuevaPalabra();

        btnAdivinar.setOnClickListener(v -> adivinarLetra());
        btnReiniciar.setOnClickListener(v -> reiniciarJuego());

        return vista;
    }

    private void seleccionarNuevaPalabra() {
        palabraActual = palabras[random.nextInt(palabras.length)];
        progreso = "_".repeat(palabraActual.length());
        txtProgreso.setText(progreso);
        intentos = 6;
        imgAhorcado.setImageResource(R.drawable.ahorcado0);
        btnAdivinar.setEnabled(true);
    }

    private void adivinarLetra() {

        String letra = txtLetra.getText().toString();

        if (letra.isEmpty()) return;

        if (!letra.matches("[a-z]")) {
            Toast.makeText(getContext(), "Solo minúsculas", Toast.LENGTH_SHORT).show();
            txtLetra.setText("");
            return;
        }

        boolean acierto = false;
        char[] nuevo = progreso.toCharArray();

        for (int i = 0; i < palabraActual.length(); i++) {
            if (palabraActual.charAt(i) == letra.charAt(0)) {
                nuevo[i] = letra.charAt(0);
                acierto = true;
            }
        }

        progreso = String.valueOf(nuevo);
        txtProgreso.setText(progreso);

        if (!acierto) {
            intentos--;
            imgAhorcado.setImageResource(imagenes[6 - intentos]);

            if (intentos > 0) {
                Toast.makeText(getContext(), "Fallaste, quedan " + intentos + " intentos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Perdiste! la palabra era " + palabraActual, Toast.LENGTH_LONG).show();
                btnAdivinar.setEnabled(false);
            }
        } else {
            if (progreso.equals(palabraActual)) {
                Toast.makeText(getContext(), "Ganaste", Toast.LENGTH_SHORT).show();
                btnAdivinar.setEnabled(false);
            }
        }

        txtLetra.setText("");
    }

    private void reiniciarJuego() {
        seleccionarNuevaPalabra();
        Toast.makeText(getContext(), "Juego Reiniciado", Toast.LENGTH_SHORT).show();
    }

    private void aplicarFuente(View view, Typeface typeface) {

        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                aplicarFuente(group.getChildAt(i), typeface);
            }
        }
    }
}
