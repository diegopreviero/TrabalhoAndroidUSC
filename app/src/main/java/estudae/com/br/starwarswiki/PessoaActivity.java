package estudae.com.br.starwarswiki;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import estudae.com.br.starwarswiki.bd.BancoDadosHelper;
import estudae.com.br.starwarswiki.bd.Pessoa;

public class PessoaActivity extends AppCompatActivity {

    private int ano, mes, dia;
    private ImageView imgPessoa;

    private EditText txtNome;
    private EditText txtAltura;
    private EditText txtMassa;
    private EditText txtCorCabelo;
    private EditText txtCorPele;
    private EditText txtAnoNascimento;
    private EditText txtSexo;
    private Pessoa pessoa;

    private Button btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa);

        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar_child);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtAltura = (EditText) findViewById(R.id.txtAltura);
        txtMassa = (EditText) findViewById(R.id.txtMassa);
        txtCorCabelo = (EditText) findViewById(R.id.txtCorCabelo);
        txtCorPele = (EditText) findViewById(R.id.txtCorPele);
        txtAnoNascimento = (EditText) findViewById(R.id.txtAnoNascimento);
        txtSexo = (EditText) findViewById(R.id.txtSexo);

        Intent intent = getIntent();
        if(intent != null){
            pessoa = (Pessoa) intent.getSerializableExtra("pessoa");
            if(pessoa != null){
                this.txtNome.setText(pessoa.nome);
                this.txtAltura.setText(pessoa.altura);
                this.txtMassa.setText(pessoa.massa);
                this.txtCorCabelo.setText(pessoa.corCabelo);
                this.txtCorPele.setText(pessoa.corPele);
                this.txtAnoNascimento.setText(pessoa.anoNascimento);
                this.txtSexo.setText(pessoa.sexo);

            }else{
                pessoa = new Pessoa();
            }
        }

        btnCadastro = (Button) findViewById(R.id.btnCadastro);
        btnCadastro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                pessoa.nome = txtNome.getText().toString();
                pessoa.altura = txtAltura.getText().toString();
                pessoa.massa = txtMassa.getText().toString();
                pessoa.corCabelo = txtCorCabelo.getText().toString();
                pessoa.corPele = txtCorPele.getText().toString();
                pessoa.anoNascimento = txtAnoNascimento.getText().toString();
                pessoa.sexo = txtSexo.getText().toString();

                BancoDadosHelper bd = new BancoDadosHelper(PessoaActivity.this);
                if(pessoa.id == null){
                    bd.inserePessoa(pessoa);
                }else{
                    bd.atualizaPessoa(pessoa);
                }
                bd.close();

                finish();

            }
        });
    }
}
