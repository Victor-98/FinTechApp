package com.example.fintech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import Beans.Operacao;
import DAO.DAO;

public class Extrato extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_extrato);

        // Atualizar saldo
        DAO db = new DAO(this);
        float saldo = db.getSaldo();
        TextView saldoTv = findViewById(R.id.saldoText);
        saldoTv.setText("Saldo : $" + saldo);

        // Popular lista de extrato
        LinearLayout extratoLinearLayout = findViewById(R.id.extratoLinearLayout);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        List<Operacao> opList = db.getExtrato();
        for (Operacao op : opList) {
            // Se o valor for positivo, colocar um '+' na frente
            String valorString = String.valueOf(op.getValor());
            if (op.getTipo().equals("Crédito"))
                valorString = "+" + valorString;
            else
                valorString = "-" + valorString;

            TextView tv = new TextView(this);
            tv.setText("Operação #" + op.getId() + "\n" + op.getTipo() + " / " + op.getCategoria() + "\nData : " + dateFormat.format(op.getData()) + "\nValor : " + valorString);

            // Define o tamanho fixo de layout (altura)
            // RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            tv.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            tv.setHeight(350);
            tv.setTextSize(20f);
            // tv.setLayoutParams(params);

            extratoLinearLayout.addView(tv);
        }
    }
}