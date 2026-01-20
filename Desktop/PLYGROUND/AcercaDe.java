package com.example.playground;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class AcercaDe extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private ListView lvAcercaDe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_acerca_de, container, false);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fontp);
        aplicarFuente(view, typeface);

        lvAcercaDe = view.findViewById(R.id.lv_acerca_de);

        ArrayList<HashMap<String, Object>> datos = new ArrayList<>();

        agregarItem(datos, R.drawable.smile, "Nombre de la app: PLAYGROUND");
        agregarItem(datos, R.drawable.checkmark, "Versión: 1.0.0");
        agregarItem(datos, R.drawable.saturn, "Descripción: Aplicación creada para jugar 4 juegos famosos.");
        agregarItem(datos, R.drawable.dandelion, "Desarrollador: Sebastián Gael Moreno Rosales");
        agregarItem(datos, R.drawable.paleheart, "Contacto: bastian333@gmail.com");

        String[] from = {"icono", "texto"};
        int[] to = {R.id.iv_icono_acerca, R.id.tv_texto_acerca};

        SimpleAdapter adapter = new SimpleAdapter(
                getContext(),
                datos,
                R.layout.item_acerca_de,
                from,
                to
        );

        lvAcercaDe.setAdapter(adapter);

        return view;
    }

    private void agregarItem(ArrayList<HashMap<String, Object>> lista,
                             int resIcono,
                             String texto) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("icono", resIcono);
        map.put("texto", texto);
        lista.add(map);
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