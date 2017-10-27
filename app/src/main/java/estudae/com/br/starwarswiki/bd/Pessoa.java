package estudae.com.br.starwarswiki.bd;

import java.io.Serializable;

/**
 * Created by khaemhat on 14/10/17.
 */

public class Pessoa implements Serializable{

    private static final long serialVersionUID = 1L;

    public Long id;
    public String nome;
    public String altura;
    public String massa;
    public String corCabelo;
    public String corPele;
    public String anoNascimento;
    public String sexo;

    @Override
    public String toString() {
        return this.nome;
    }
}
