package com.example.playground;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;

public class Memorama extends Fragment {

    Button Reiniciar;

    ImageButton[] botones = new ImageButton[16];
    int[] imagenes = {
            R.drawable.rose, R.drawable.rose,
            R.drawable.pepper, R.drawable.pepper,
            R.drawable.cactus, R.drawable.cactus,
            R.drawable.strawberry, R.drawable.strawberry,
            R.drawable.coconut, R.drawable.coconut,
            R.drawable.blueflower, R.drawable.blueflower,
            R.drawable.pineapple, R.drawable.pineapple,
            R.drawable.pumpkin, R.drawable.pumpkin
    };

    ArrayList<Integer> cartas = new ArrayList<>();
    int primera = -1, segunda = -1;
    boolean bloqueo = false;
    int paresEncontrados = 0;

    public Memorama() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_memorama, container, false);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fontp);
        aplicarFuente(root, typeface);

        for(int i = 0; i < 16; i++){
            botones[i] = root.findViewById(
                    getResources().getIdentifier("btn" + (i+1), "id", requireContext().getPackageName()));
            cartas.add(imagenes[i]);
        }

        mezclarCartas();

        for(int i = 0; i < 16; i++){
            final int index = i;
            botones[i].setOnClickListener(v -> seleccionarCarta(index));
        }

        Reiniciar = root.findViewById(R.id.btnreinicio);
        Reiniciar.setOnClickListener(v -> reiniciarJuego());

        return root;
    }

    private void mezclarCartas(){
        Collections.shuffle(cartas);
    }

    private void seleccionarCarta(int i){
        if(bloqueo || !botones[i].isEnabled()) return;

        botones[i].setImageResource(cartas.get(i));

        if(primera == -1){
            primera = i;
        } else if(segunda == -1 && i != primera){
            segunda = i;
            bloqueo = true;
            verificarPareja();
        }
    }

    private void verificarPareja(){
        Handler handler = new Handler();

        if(cartas.get(primera).equals(cartas.get(segunda))){
            botones[primera].setEnabled(false);
            botones[segunda].setEnabled(false);
            paresEncontrados++;

            if(paresEncontrados == 8){
                Toast.makeText(getContext(), "Ganaste", Toast.LENGTH_SHORT).show();
            }

            resetSeleccion();
            bloqueo = false;

        } else {
            Toast.makeText(getContext(), "Carta Incorrecta", Toast.LENGTH_SHORT).show();

            handler.postDelayed(() -> {
                botones[primera].setImageResource(R.drawable.smile);
                botones[segunda].setImageResource(R.drawable.smile);
                resetSeleccion();
                bloqueo = false;
            }, 1000);
        }
    }

    private void resetSeleccion(){
        primera = -1;
        segunda = -1;
    }

    private void reiniciarJuego(){
        paresEncontrados = 0;
        cartas.clear();

        for(int i = 0; i < 16; i++){
            cartas.add(imagenes[i]);
            botones[i].setImageResource(R.drawable.smile);
            botones[i].setEnabled(true);
        }

        mezclarCartas();
        resetSeleccion();

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