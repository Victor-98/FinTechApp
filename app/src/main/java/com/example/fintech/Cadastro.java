package com.example.fintech;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Beans.Operacao;
import DAO.DAO;

public class Cadastro extends AppCompatActivity {

    private ArrayAdapter<CharSequence> creditoAdapter;
    private ArrayAdapter<CharSequence> debitoAdapter;
    private final Calendar operacaoDataCalendar = Calendar.getInstance();
    private boolean isDateSelected;

    private void updateLabel(EditText editText) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(operacaoDataCalendar.getTime()));
    }

    // Cuida de listeners relacionados a abertura da janela de DatePickerDialog, e salvamento da data no calendário / edit de data.
    private void carregarDatePicker() {
        final EditText dataOperacaoEdit = findViewById(R.id.dataOperacaoEdit);
        dataOperacaoEdit.setInputType(InputType.TYPE_NULL);

        // Ao definir a data no datepicker, atualiza no objeto do calendário, e também atualiza a label.
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                // TODO Auto-generated method stub
                operacaoDataCalendar.set(Calendar.YEAR, year);
                operacaoDataCalendar.set(Calendar.MONTH, month);
                operacaoDataCalendar.set(Calendar.DAY_OF_MONTH, day);
                isDateSelected = true;
                updateLabel(dataOperacaoEdit);
            }
        };

        // Ao clicar no editText, abre o datepicker
        dataOperacaoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Cadastro.this, date, operacaoDataCalendar
                        .get(Calendar.YEAR), operacaoDataCalendar.get(Calendar.MONTH),
                        operacaoDataCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();

        isDateSelected = false; // Inicializa variável de controle do calendário.


        // Carregar dados que serão preenchidos no spinner de categoria de operação.
        // Credito
        this.creditoAdapter = ArrayAdapter.createFromResource(this, R.array.categoria_credito, android.R.layout.simple_spinner_item); // Cria um adaptador para preencher as listas de dropdown com as informações das categorias de crédito.
        this.creditoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Indicar que o layout será usado numa lista de dropdown.

        // Debito
        this.debitoAdapter = ArrayAdapter.createFromResource(this, R.array.categoria_debito, android.R.layout.simple_spinner_item); // Cria um adaptador para preencher as listas de dropdown com as informações das categorias de débito.
        this.debitoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Indicar que o layout será usado numa lista de dropdown.

        setContentView(R.layout.activity_cadastro); // setContentView precisa estar acima da findViewByID, para não existir problemas ao encontrar o elemento de interface.
        carregarDatePicker(); // Carrega informações do datepicker do cadastro da operação.
    }

    // Chamado ao selecionar débito no RadioButton de Cadastro de Operação
    public void selecionarDebito(View view) {
        Spinner categoriaSpinner = (Spinner) findViewById(R.id.categoriaSpinner);
        categoriaSpinner.setAdapter(this.debitoAdapter);
    }

    // Chamado ao selecionar crédito no RadioButton de Cadastro de Operação
    public void selecionarCredito(View view) {
        Spinner categoriaSpinner = (Spinner) findViewById(R.id.categoriaSpinner);
        categoriaSpinner.setAdapter(this.creditoAdapter);
    }

    public void onCriarOperacao(View view) {

        // Tipo de Operação Selecionada (RadioGroup)
        RadioGroup radioGroup = findViewById(R.id.tipoOperacaoRadioGroup);
        int radioSelectedID = radioGroup.getCheckedRadioButtonId();

        // Caso não há um radio button selecionado
        if (radioSelectedID == -1) {
            Toast toast = Toast.makeText(getApplicationContext(), "Você deve escolher um tipo de operação.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Categoria da Operação
        Spinner categoriaOperacaoSpinner = findViewById(R.id.categoriaSpinner);
        if (categoriaOperacaoSpinner.getSelectedItem() == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Você deve escolher uma categoria de operação.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Data da Operação
        if (!isDateSelected) {
            Toast toast = Toast.makeText(getApplicationContext(), "Você deve escolher uma data da operação.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Verifica se a data é válida.
        Date dateNow = java.util.Calendar.getInstance().getTime();
        Date dateSelected = operacaoDataCalendar.getTime();
        // Se a data de hoje for antes da data selecionada. (Data selecionada está no futuro)
        if (dateNow.before(dateSelected)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Você deve escolher uma data passada.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Verifica se o valor está preenchido
        EditText editTextValor = findViewById(R.id.valorOperacaoEdit);
        String valorOperacaoString = editTextValor.getText().toString();
        if (valorOperacaoString.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Você deve preencher um valor.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Finalmente, os dados são recebidos em seus formatos apropriados.
        RadioButton tipoOperacaoRadio = (RadioButton)(findViewById(radioSelectedID));
        String tipoOperacaoString = (String) tipoOperacaoRadio.getText();
        String categoriaOperacaoString = (String) categoriaOperacaoSpinner.getSelectedItem().toString();
        Float valorOperacaoFloat = Float.valueOf(valorOperacaoString);

        Operacao operacao = new Operacao(tipoOperacaoString, categoriaOperacaoString, dateSelected, valorOperacaoFloat);

        DAO db = new DAO(this);
        db.criarOperacao(operacao);

        // Operacao criada com sucesso.
        Toast toast = Toast.makeText(getApplicationContext(), "Operação criada com sucesso!.", Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
}