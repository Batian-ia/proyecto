package com.example.playground;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Random;

public class Mazo extends Fragment {

    ImageButton[] botones = new ImageButton[9];
    Button restart;

    int topoActual = -1;
    boolean ganado = false;

    Handler handler = new Handler();
    Random random = new Random();

    Runnable moverTopo = new Runnable() {
        @Override
        public void run() {
            if (!ganado) {
                mostrarTopo();
                handler.postDelayed(this, 700);
            }
        }
    };

    public Mazo() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mazo, container, false);

        botones[0] = view.findViewById(R.id.bt1);
        botones[1] = view.findViewById(R.id.bt2);
        botones[2] = view.findViewById(R.id.bt3);
        botones[3] = view.findViewById(R.id.bt4);
        botones[4] = view.findViewById(R.id.bt5);
        botones[5] = view.findViewById(R.id.bt6);
        botones[6] = view.findViewById(R.id.bt7);
        botones[7] = view.findViewById(R.id.bt8);
        botones[8] = view.findViewById(R.id.bt9);

        restart = view.findViewById(R.id.restart);

        for (int i = 0; i < 9; i++) {
            final int index = i;
            botones[i].setOnClickListener(v -> {
                if (index == topoActual && !ganado) {
                    ganado = true;
                    botones[index].setImageResource(R.drawable.hit);
                    Toast.makeText(getContext(), "Ganaste", Toast.LENGTH_SHORT).show();
                }
            });
        }

        restart.setOnClickListener(v -> reiniciarJuego());

        handler.post(moverTopo);

        return view;
    }

    void mostrarTopo() {
        if (topoActual != -1) {
            botones[topoActual].setImageResource(R.drawable.hole);
        }

        topoActual = random.nextInt(9);
        botones[topoActual].setImageResource(R.drawable.mole);
    }

    void reiniciarJuego() {
        ganado = false;

        if (topoActual != -1) {
            botones[topoActual].setImageResource(R.drawable.hole);
        }

        topoActual = -1;

        Toast.makeText(getContext(), "Juego Reiniciado", Toast.LENGTH_SHORT).show();

        handler.post(moverTopo);
    }
}