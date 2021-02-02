package com.example.fintech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);
    }

    public void abrirActivityOperacao(View view) {
        startActivity(new Intent(this, Cadastro.class));
    }

    public void abrirActivityExtrato(View view) {
        startActivity(new Intent(this, Extrato.class));
    }

    public void abrirActivityPesquisar(View view) {
        startActivity(new Intent(this, Pesquisar.class));
    }

    public void abrirActivityLista(View view) {
        startActivity(new Intent(this, Lista.class));
    }
}
