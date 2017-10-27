package estudae.com.br.starwarswiki;

/**
 * Created by khaemhat on 14/10/17.
 */

import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import estudae.com.br.starwarswiki.bd.Pessoa;

public class PessoaUtil {

    public static String acessar(String endereco)
    {
        try {

            URL url = new URL(endereco);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            Scanner scanner = new Scanner(is);
            String conteudo = scanner.useDelimiter("\\A").next();
            scanner.close();
            return conteudo;

        } catch (Exception e){

            Log.e("aula", e.getMessage());
            e.printStackTrace();

            return e.getMessage();
        }
    }

    public static List<Pessoa> pessoaConverter(String conteudo) throws JSONException {

        List<Pessoa> Pessoas = new ArrayList<Pessoa>();

        JSONObject jsonObject = new JSONObject(conteudo);
        JSONArray listaPessoas = jsonObject.getJSONArray("results");

        for (int i = 0; i < listaPessoas.length(); i++) {
            JSONObject j = listaPessoas.getJSONObject(i);
            Pessoa p = new Pessoa();
            p.nome = j.getString("name");
            p.altura = j.getString("height");
            p.massa = j.getString("mass");
            p.corCabelo = j.getString("hair_color");
            p.corPele = j.getString("skin_color");
            p.anoNascimento = j.getString("birth_year");
            p.sexo = j.getString("gender");
            Pessoas.add(p);
        }
        return Pessoas;
    }

}