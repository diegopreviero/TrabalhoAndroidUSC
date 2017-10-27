package estudae.com.br.starwarswiki.bd;

/**
 * Created by khaemhat on 14/10/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BancoDadosHelper extends SQLiteOpenHelper {

    public static final String BANCO = "STARWARS";
    public static final int VERSAO = 2;

    private String scriptSQLCreate[] = {
            "CREATE TABLE starwars (id INTEGER PRIMARY KEY, nome TEXT,  altura TEXT, massa TEXT, corcabelo TEXT, corpele TEXT, anonascimento TEXT, sexo TEXT);",
            "INSERT INTO starwars VALUES(1,'Kiko','173','96','Preta','Branca','1982','masculino');" };

    private String scriptSQLDelete = "drop table starwars";



    public BancoDadosHelper(Context context) {
        super(context, BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("aula", "Criando banco com sql");

        int qtdeScripts = scriptSQLCreate.length;

        // Executa cada sql passado como parâmetro
        for (int i = 0; i < qtdeScripts; i++) {
            String sql = scriptSQLCreate[i];
            Log.i("aula", sql);
            // Cria o banco de dados executando o script de criação
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("aula", "Atualizando da versão " + oldVersion + " para " + newVersion + ". Todos os registros serão deletados.");
        Log.i("aula", scriptSQLDelete);
        // Deleta as tabelas...
        db.execSQL(scriptSQLDelete);
        // Cria novamente...
        onCreate(db);
    }

    public List<Pessoa> listaPessoas() {
        List<Pessoa> pessoas = new ArrayList<Pessoa>();

        Cursor c = getWritableDatabase().query("starwars",
                new String[] { "id", "nome", "altura", "massa", "corcabelo","corpele","anonascimento", "sexo" },
                null, null, null, null, null);

        // se encontrou
        if (c.moveToFirst()) {
            do {
                Pessoa pessoa = new Pessoa();
                pessoa.id = c.getLong(0);
                pessoa.nome = c.getString(1);
                pessoa.altura = c.getString(2);
                pessoa.massa = c.getString(3);
                pessoa.corCabelo = c.getString(4);
                pessoa.corPele = c.getString(5);
                pessoa.anoNascimento = c.getString(6);
                pessoa.sexo = c.getString(7);
                pessoas.add(pessoa);
            } while (c.moveToNext());
        }
        c.close();

        return pessoas;
    }

    public void inserePessoa(Pessoa pessoa) {
        ContentValues valores = new ContentValues();
        valores.put("nome", pessoa.nome);
        valores.put("altura", pessoa.altura);
        valores.put("massa", pessoa.massa);
        valores.put("corcabelo", pessoa.corCabelo);
        valores.put("corpele", pessoa.corPele);
        valores.put("anonascimento", pessoa.anoNascimento);
        valores.put("sexo", pessoa.sexo);
        getWritableDatabase().insert("starwars", null, valores);
    }

    public void atualizaPessoa(Pessoa pessoa) {
        ContentValues valores = new ContentValues();
        valores.put("nome", pessoa.nome);
        valores.put("altura", pessoa.altura);
        valores.put("massa", pessoa.massa);
        valores.put("corcabelo", pessoa.corCabelo);
        valores.put("corpele", pessoa.corPele);
        valores.put("anonascimento", pessoa.anoNascimento);
        valores.put("sexo", pessoa.sexo);
        getWritableDatabase().update("starwars", valores, "id=" + pessoa.id, null);
    }

    public void apagaPessoa(Pessoa pessoa) {
        getWritableDatabase().delete("starwars", "id=" + pessoa.id, null);
    }

    public List<Pessoa> listaPessoas(String filtro) {
        List<Pessoa> pessoas = new ArrayList<Pessoa>();

        Cursor c = getWritableDatabase().query("starwars",
                new String[] { "id", "nome", "altura", "massa", "corcabelo","corpele","anonascimento", "sexo" },
                "nome like ?", new String[] { filtro }, null, null, null);

        // se encontrou
        if (c.moveToFirst()) {
            do {
                Pessoa pessoa = new Pessoa();
                pessoa.id = c.getLong(0);
                pessoa.nome = c.getString(1);
                pessoa.altura = c.getString(2);
                pessoa.massa = c.getString(3);
                pessoa.corCabelo = c.getString(4);
                pessoa.corPele = c.getString(5);
                pessoa.anoNascimento = c.getString(6);
                pessoa.sexo = c.getString(7);
                pessoas.add(pessoa);
            } while (c.moveToNext());
        }

        c.close();
        return pessoas;
    }

    public boolean isPessoa(String telefone) {
        Cursor rawQuery = getReadableDatabase().rawQuery(
                "SELECT telefone from starwars WHERE telefone = ? ",
                new String[] { telefone });
        int total = rawQuery.getCount();
        rawQuery.close();
        return total > 0;
    }
}
