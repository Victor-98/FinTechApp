package DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Beans.Categoria;
import Beans.Operacao;
import Beans.Pesquisa;

public class DAO extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fintech";
    private static final int DATABASE_VERSION = 1;

    public DAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;

        // Create table tipo
        query = "CREATE TABLE tipo ("
                + "id_tipo INTEGER PRIMARY KEY,"
                + "nome TEXT"
                + ")";
        db.execSQL(query);

        // Insert into table tipo
        query = "INSERT INTO tipo (nome) VALUES ('Crédito')";
        db.execSQL(query);
        query = "INSERT INTO tipo (nome) VALUES ('Débito')";
        db.execSQL(query);

        // Create table categoria
        query = "CREATE TABLE categoria ("
                + "id_categoria INTEGER PRIMARY KEY,"
                + "nome TEXT"
                + ")";
        db.execSQL(query);

        // Insert into table categoria
        query = "INSERT INTO categoria (nome) VALUES ('Salário')";
        db.execSQL(query);
        query = "INSERT INTO categoria (nome) VALUES ('Transferências')";
        db.execSQL(query);
        query = "INSERT INTO categoria (nome) VALUES ('Educação')";
        db.execSQL(query);
        query = "INSERT INTO categoria (nome) VALUES ('Lazer')";
        db.execSQL(query);
        query = "INSERT INTO categoria (nome) VALUES ('Moradia')";
        db.execSQL(query);
        query = "INSERT INTO categoria (nome) VALUES ('Saúde')";
        db.execSQL(query);
        query = "INSERT INTO categoria (nome) VALUES ('Outros')";
        db.execSQL(query);

        query = "CREATE TABLE operacao ("
              + "id_operacao INTEGER PRIMARY KEY,"
              + "id_tipo INTEGER,"
              + "id_categoria INTEGER,"
              + "data TEXT,"
              + "valor REAL,"
              + "FOREIGN KEY (id_tipo) REFERENCES tipoOperacao(id_tipo),"
              + "FOREIGN KEY (id_categoria) REFERENCES categoriaOperacao(id_categoria)"
              + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS operacao");
        db.execSQL("DROP TABLE IF EXISTS tipo");
        db.execSQL("DROP TABLE IF EXISTS categoria");
        onCreate(db);
    }

    public void criarOperacao(Operacao operacao) {
        String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(operacao.getData());

        SQLiteDatabase db = this.getWritableDatabase();
        String query;
        query = "INSERT INTO operacao (" +
                "    id_tipo," +
                "    id_categoria," +
                "    data," +
                "    valor) " +
                "SELECT " +
                "    (SELECT id_tipo FROM tipo WHERE nome = '" + operacao.getTipo() + "')," +
                "    (SELECT id_categoria FROM categoria WHERE nome = '" + operacao.getCategoria() + "')," +
                "    '" + dateString + "' AS dataOperacao," +
                "    '" + operacao.getValor() + "' AS valorOperacao";
        db.execSQL(query);
    }

    // Faz um SELECT no banco de dados, e recebe os extratos necessários, retornando uma lista de operações
    public List<Operacao> getExtrato() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query;
        query = "SELECT o.id_operacao AS id_operacao, " +
                "       t.nome AS tipo, " +
                "       c.nome AS categoria, " +
                "       o.data AS data, " +
                "       o.valor AS valor " +
                "FROM operacao o " +
                "INNER JOIN categoria c ON o.id_categoria = c.id_categoria " +
                "INNER JOIN tipo t ON o.id_tipo = t.id_tipo " +
                "ORDER BY o.data DESC " +
                "LIMIT 15";
        Cursor c = db.rawQuery(query, null);
        List<Operacao> lista = new ArrayList<>();
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id_operacao"));
            String tipo = c.getString(c.getColumnIndex("tipo"));
            String categoria = c.getString(c.getColumnIndex("categoria"));
            String dataString = c.getString(c.getColumnIndex("data"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = formatter.parse(dataString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            float valor = c.getFloat(c.getColumnIndex("valor"));
            Operacao op = new Operacao(tipo, categoria, date, valor);
            op.setId(id);
            lista.add(op);
        }
        return lista;
    }

    public List<Operacao> getResultadoPesquisa(Pesquisa pesquisa) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query;
        query = "SELECT o.id_operacao AS id_operacao, " +
                "       t.nome AS tipo, " +
                "       c.nome AS categoria, " +
                "       o.data AS data, " +
                "       o.valor AS valor " +
                "FROM operacao o " +
                "INNER JOIN categoria c ON o.id_categoria = c.id_categoria " +
                "INNER JOIN tipo t ON o.id_tipo = t.id_tipo " +
                "WHERE " +
                "data BETWEEN ? AND ?";
        // Se houver um tipo a ser pesquisado, anexar na query.
        if ( !pesquisa.getTipo().equals("Ambos") )
            query += " AND tipo = ?";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dataIniString = dateFormat.format(pesquisa.getDataIni());
        String dataFimString = dateFormat.format(pesquisa.getDataFim());

        Cursor c;
        if ( !pesquisa.getTipo().equals("Ambos") )
            c = db.rawQuery(query, new String[] { dataIniString, dataFimString, pesquisa.getTipo() });
        else
            c = db.rawQuery(query, new String[] { dataIniString, dataFimString });


        List<Operacao> lista = new ArrayList<>();
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id_operacao"));
            String tipo = c.getString(c.getColumnIndex("tipo"));
            String categoria = c.getString(c.getColumnIndex("categoria"));
            String dataString = c.getString(c.getColumnIndex("data"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = formatter.parse(dataString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            float valor = c.getFloat(c.getColumnIndex("valor"));
            Operacao op = new Operacao(tipo, categoria, date, valor);
            op.setId(id);
            lista.add(op);
        }

        return lista;
    }

    public List<Categoria> getCategorias() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query;
        query = "SELECT " +
                "       c.nome AS categoria, " +
                "       t.nome AS tipo, " +
                "       SUM(o.valor) AS valor " +
                "FROM operacao o " +
                "INNER JOIN categoria c ON o.id_categoria = c.id_categoria " +
                "INNER JOIN tipo t ON o.id_tipo = t.id_tipo " +
                "GROUP BY categoria " +
                "ORDER BY valor DESC ";
        Cursor c = db.rawQuery(query, null);
        List<Categoria> list = new ArrayList<>();
        while (c.moveToNext()) {
            String categoriaString = c.getString(c.getColumnIndex("categoria"));
            String tipoString = c.getString(c.getColumnIndex("tipo"));
            float valorString = c.getFloat(c.getColumnIndex("valor"));

            Categoria categoria = new Categoria(tipoString, categoriaString, valorString);
            list.add(categoria);
        }
        return list;
    }

    public float getSaldo() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query;
        query = "SELECT " +
                "    ( " +
                "        SELECT sum(valor) " +
                "        FROM operacao o " +
                "        INNER JOIN tipo t ON o.id_tipo = t.id_tipo " +
                "        WHERE t.nome = 'Crédito' " +
                "    ) AS credito, " +
                "    ( " +
                "        SELECT sum(valor) " +
                "        FROM operacao o " +
                "        INNER JOIN tipo t ON o.id_tipo = t.id_tipo " +
                "        WHERE t.nome = 'Débito' " +
                "    ) AS debito";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() == 0 || !c.moveToFirst()) // Se não retornar resultado, então o saldo é 0.
            return 0;
        c.moveToFirst();
        // c.moveToNext(); // Utilizar apenas uma vez, como possuimos só um valor
        float credito = c.getFloat(c.getColumnIndex("credito"));
        float debito = c.getFloat(c.getColumnIndex("debito"));
        return credito-debito;
    }
}
