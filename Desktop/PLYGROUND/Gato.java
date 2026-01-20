package com.example.playground;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

public class Gato extends Fragment {

    Button[] botones = new Button[9];
    Button btnReiniciar;
    boolean turnoX = true;
    int jugadas = 0;

    public Gato() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_gato, container, false);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fontp);
        aplicarFuente(v, typeface);

        botones[0] = v.findViewById(R.id.b1);
        botones[1] = v.findViewById(R.id.b2);
        botones[2] = v.findViewById(R.id.b3);
        botones[3] = v.findViewById(R.id.b4);
        botones[4] = v.findViewById(R.id.b5);
        botones[5] = v.findViewById(R.id.b6);
        botones[6] = v.findViewById(R.id.b7);
        botones[7] = v.findViewById(R.id.b8);
        botones[8] = v.findViewById(R.id.b9);

        btnReiniciar = v.findViewById(R.id.btnReiniciar);

        // Asignar click a los 9 botones
        for (Button b : botones) {
            b.setOnClickListener(this::clickBoton);
        }

        // Click del botón reiniciar
        btnReiniciar.setOnClickListener(this::reiniciar);

        return v;
    }

    public void clickBoton(View view) {
        Button b = (Button) view;

        if (!b.getText().toString().equals("")) return;

        if (turnoX) {
            b.setText("X");
        } else {
            b.setText("O");
        }

        jugadas++;

        if (verificarGanador()) {
            if (turnoX) {
                Toast.makeText(getContext(), "Ganó X", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Ganó O", Toast.LENGTH_SHORT).show();
            }
            desactivarBotones();
        } else if (jugadas == 9) {
            Toast.makeText(getContext(), "Empate", Toast.LENGTH_SHORT).show();
        }

        turnoX = !turnoX;
    }

    private boolean verificarGanador() {
        String b1 = botones[0].getText().toString();
        String b2 = botones[1].getText().toString();
        String b3 = botones[2].getText().toString();
        String b4 = botones[3].getText().toString();
        String b5 = botones[4].getText().toString();
        String b6 = botones[5].getText().toString();
        String b7 = botones[6].getText().toString();
        String b8 = botones[7].getText().toString();
        String b9 = botones[8].getText().toString();

        if (!b1.equals("") && b1.equals(b2) && b2.equals(b3)) return true;
        if (!b4.equals("") && b4.equals(b5) && b5.equals(b6)) return true;
        if (!b7.equals("") && b7.equals(b8) && b8.equals(b9)) return true;

        if (!b1.equals("") && b1.equals(b4) && b4.equals(b7)) return true;
        if (!b2.equals("") && b2.equals(b5) && b5.equals(b8)) return true;
        if (!b3.equals("") && b3.equals(b6) && b6.equals(b9)) return true;

        if (!b1.equals("") && b1.equals(b5) && b5.equals(b9)) return true;
        if (!b3.equals("") && b3.equals(b5) && b5.equals(b7)) return true;

        return false;
    }

    private void desactivarBotones() {
        for (Button b : botones) {
            b.setEnabled(false);
        }
    }

    public void reiniciar(View view) {
        for (Button b : botones) {
            b.setText("");
            b.setEnabled(true);
        }
        turnoX = true;
        jugadas = 0;
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
