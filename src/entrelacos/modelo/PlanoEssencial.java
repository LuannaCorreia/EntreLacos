package entrelacos.modelo;

public class PlanoEssencial extends Plano {
    public static final int LIMITE_CURTIDAS = 2;

    public PlanoEssencial() {
        super("Essencial");
    }
    @Override
    public int getLimiteCurtidas() {
        return LIMITE_CURTIDAS;
    }
}
