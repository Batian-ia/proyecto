package com.example.playground;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inicializarVistas();
        configurarToolbar();
        configurarNavigationDrawer();

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_inicio);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false); // desactiva este callback
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);
    }

    void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("PLAYGROUND");
        }
    }

    void configurarNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_nav,
                R.string.close_nav
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        if (id == R.id.nav_inicio) {
            navController.navigate(R.id.inicio);


        } else if (id == R.id.nav_memorama) {
            navController.navigate(R.id.memorama);


        } else if (id == R.id.nav_gato) {
            navController.navigate(R.id.gato);


        } else if (id == R.id.nav_acerca_de) {
            navController.navigate(R.id.acercade);


        } else if (id == R.id.nav_compartir) {
            compartirApp();

        } else if (id == R.id.nav_juegomazo) {
            navController.navigate(R.id.mazo);

        } else if (id == R.id.nav_ahorcado) {
            navController.navigate(R.id.ahorcado);

        } else if (id == R.id.nav_cerrar_sesion) {
            cerrarSesion();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    void compartirApp() {
        Intent compartir = new Intent(Intent.ACTION_SEND);
        compartir.setType("text/plain");
        compartir.putExtra(Intent.EXTRA_SUBJECT, "Mi Proyecto");
        compartir.putExtra(Intent.EXTRA_TEXT,
                "Tal vez sea el momento en el que puedas probar Mi Proyectito");
        startActivity(Intent.createChooser(compartir, "Compartir app mediante"));
    }

    void cerrarSesion() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {

                    mostrarMensaje("Sesión cerrada");

                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}