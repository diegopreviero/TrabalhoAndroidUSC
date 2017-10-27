package estudae.com.br.starwarswiki;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;

import estudae.com.br.starwarswiki.bd.BancoDadosHelper;
import estudae.com.br.starwarswiki.bd.Pessoa;

public class ListaPessoasActivity extends AppCompatActivity {
    private ListView listaPessoas;
    private List<Pessoa> pessoas;
    private Pessoa pessoaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pessoas);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        carregaLista();

        listaPessoas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {
                Intent intent = new Intent(ListaPessoasActivity.this, PessoaActivity.class);
                intent.putExtra("pessoa", pessoas.get(posicao));
                startActivity(intent);
            }
        });

        listaPessoas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View view, int posicao, long id) {
                pessoaSelecionada = pessoas.get(posicao);
                return false;
            }
        });

        registerForContextMenu(listaPessoas);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.novo:
                Intent intent = new Intent(this, PessoaActivity.class);
                startActivity(intent);
                return false;

            case R.id.sincronizar:
                Toast.makeText(this, "Enviar", Toast.LENGTH_LONG).show();
                return false;


            case R.id.receber:
                new LendoServidorTask().execute();
                return false;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_pessoa_contexto, menu);
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.excluir:

                new AlertDialog.Builder(ListaPessoasActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar ?")
                        .setPositiveButton("Quero",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        BancoDadosHelper dao = new BancoDadosHelper(ListaPessoasActivity.this);
                                        dao.apagaPessoa(pessoaSelecionada);
                                        dao.close();
                                        carregaLista();
                                    }
                                }).setNegativeButton("Nao", null).show();

                return false;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void carregaLista() {
        BancoDadosHelper bd = new BancoDadosHelper(ListaPessoasActivity.this);
        pessoas = bd.listaPessoas();
        bd.close();
        ArrayAdapter<Pessoa> adapter = new ArrayAdapter<Pessoa>(this, android.R.layout.simple_list_item_1, pessoas);

        listaPessoas = (ListView) findViewById(R.id.lista);

        listaPessoas.setAdapter(adapter);
    }

    private class LendoServidorTask extends AsyncTask<Object, Object, String> {

        private ProgressDialog progress;

        @Override
        protected String doInBackground(Object... params) {

            String urlPessoas = Constants.URLSERVIDOR;
            String url = Uri.parse(urlPessoas).toString();
            String conteudo = PessoaUtil.acessar(url);

            Log.i("aula", conteudo);

            try {
                List<Pessoa> listaPessoasWeb = PessoaUtil.pessoaConverter(conteudo);
                BancoDadosHelper bd = new BancoDadosHelper( ListaPessoasActivity.this);
                for (Pessoa pessoa : listaPessoasWeb) {
                    bd.inserePessoa(pessoa);
                }
                bd.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return conteudo;
        }

        @Override
        protected void onPreExecute() {
            // executa algo antes de iniciar a tarefa
            super.onPreExecute();
            progress = ProgressDialog.show(ListaPessoasActivity.this,"Aguarde ...","Recebendo Informações de um lugar muito, muito distante.");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            carregaLista();
            progress.dismiss();
        }

    }

}
