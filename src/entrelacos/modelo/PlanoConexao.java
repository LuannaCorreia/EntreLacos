package entrelacos.modelo;

public class PlanoConexao extends Plano {
    public static final int LIMITE_CURTIDAS = 5;

    public PlanoConexao() {
        super("Conexão");
    }
    @Override
    public int getLimiteCurtidas() {
        return LIMITE_CURTIDAS;
    }
}