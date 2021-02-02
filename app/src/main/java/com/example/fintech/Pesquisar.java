package com.example.fintech;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Beans.Operacao;
import Beans.Pesquisa;
import DAO.DAO;

public class Pesquisar extends AppCompatActivity {
    private ArrayAdapter<CharSequence> pesquisaAdapter;
    private final Calendar dataIniCalendar = Calendar.getInstance();
    private final Calendar dataFimCalendar = Calendar.getInstance();
    private boolean isDataIniSelected;
    private boolean isDataFimSelected;

    private void updateLabel(EditText editText, Calendar calendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

    private void carregarDataIniPicker() {
        final EditText dataIniEdit = findViewById(R.id.dataIniPesquisarEdit);
        dataIniEdit.setInputType(InputType.TYPE_NULL);

        // Ao definir a data no datepicker, atualiza no objeto do calendário, e também atualiza a label.
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                // TODO Auto-generated method stub
                dataIniCalendar.set(Calendar.YEAR, year);
                dataIniCalendar.set(Calendar.MONTH, month);
                dataIniCalendar.set(Calendar.DAY_OF_MONTH, day);
                isDataIniSelected = true;
                updateLabel(dataIniEdit, dataIniCalendar);
            }
        };

        // Ao clicar no editText, abre o datepicker
        dataIniEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Pesquisar.this, date, dataIniCalendar
                        .get(Calendar.YEAR), dataIniCalendar.get(Calendar.MONTH),
                        dataIniCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void carregarDataFimPicker() {
        final EditText dataFimEdit = findViewById(R.id.dataFimPesquisarEdit);
        dataFimEdit.setInputType(InputType.TYPE_NULL);

        // Ao definir a data no datepicker, atualiza no objeto do calendário, e também atualiza a label.
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                // TODO Auto-generated method stub
                dataFimCalendar.set(Calendar.YEAR, year);
                dataFimCalendar.set(Calendar.MONTH, month);
                dataFimCalendar.set(Calendar.DAY_OF_MONTH, day);
                isDataFimSelected = true;
                updateLabel(dataFimEdit, dataFimCalendar);
            }
        };

        // Ao clicar no editText, abre o datepicker
        dataFimEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Pesquisar.this, date, dataFimCalendar
                        .get(Calendar.YEAR), dataFimCalendar.get(Calendar.MONTH),
                        dataFimCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_pesquisar);

        // Inicia as datas como "não definidas".
        isDataIniSelected = false;
        isDataFimSelected = false;

        this.pesquisaAdapter = ArrayAdapter.createFromResource(this, R.array.pesquisa_filtro, android.R.layout.simple_spinner_item); // Cria um adaptador para preencher as listas de dropdown com as informações da pesquisa.
        this.pesquisaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Indicar que o layout será usado numa lista de dropdown.

        Spinner pesquisarSpinner = (Spinner) findViewById(R.id.pesquisarSpinner);
        pesquisarSpinner.setAdapter(this.pesquisaAdapter); // Define a lista de elementos para o spinner de pesquisa.



        carregarDataIniPicker();
        carregarDataFimPicker();
    }

    public void onPesquisar(View view) {
        // Data da Operação
        if (!isDataIniSelected || !isDataFimSelected) {
            Toast toast = Toast.makeText(getApplicationContext(), "Você deve escolher as datas da pesquisa de operações.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Tipo da Pesquisa
        Spinner pesquisaSpinner = findViewById(R.id.pesquisarSpinner);
        if (pesquisaSpinner.getSelectedItem() == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Você deve escolher um tipo de operação.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Datas válidas
        Date dataAntes = dataIniCalendar.getTime();
        Date dataDepois = dataFimCalendar.getTime();
        // Se a data de hoje for antes da data selecionada. (Data selecionada está no futuro)
        if (dataDepois.before(dataAntes)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Você deve escolher uma data final posterior a data inicial.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String tipoOperacaoString = (String) pesquisaSpinner.getSelectedItem().toString();
        Pesquisa pesquisa = new Pesquisa(dataAntes, dataDepois, tipoOperacaoString);

        LinearLayout pesquisaLinearLayout = findViewById(R.id.pesquisaLinearLayout);
        pesquisaLinearLayout.removeAllViews();
        /*
        for (int i = 0; i < pesquisaLinearLayout.getChildCount(); i++) {
            View v = pesquisaLinearLayout.getChildAt(i);
            pesquisaLinearLayout.removeAllViews();
        }
        */
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DAO db = new DAO(this);
        List<Operacao> lista = db.getResultadoPesquisa(pesquisa);
        for (Operacao op : lista) {
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

            pesquisaLinearLayout.addView(tv);
        }
    }
}