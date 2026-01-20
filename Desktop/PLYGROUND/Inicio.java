package com.example.playground;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Inicio extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.fontp);
        aplicarFuente(root, typeface);

        return root;
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