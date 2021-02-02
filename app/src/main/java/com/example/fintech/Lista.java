package com.example.fintech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import Beans.Categoria;
import DAO.DAO;

public class Lista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_lista);

        LinearLayout listaLinearLayout = findViewById(R.id.listaLinearLayout);
        DAO db = new DAO(this);
        List<Categoria> lista = db.getCategorias();
        for (Categoria c : lista) {
            // Se o valor for positivo, colocar um '+' na frente
            String valorString;
            if (c.getTipo().equals("Crédito"))
                valorString = "+" + String.valueOf(c.getSoma());
            else
                valorString = "-" + String.valueOf(c.getSoma());

            TextView tv = new TextView(this);
            tv.setText("" + c.getNome() + "\n" + c.getTipo() + "\nTotal : " + valorString);

            // Define o tamanho fixo de layout (altura)
            // RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            tv.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            tv.setHeight(350);
            tv.setTextSize(20f);
            // tv.setLayoutParams(params);

            listaLinearLayout.addView(tv);
        }
    }
}