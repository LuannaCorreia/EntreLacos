package entrelacos.modelo;

import java.util.List;

public class CatalogoInteresses {
    private static final List<String> INTERESSES_DISPONIVEIS = List.of(
            "Música",
            "Cinema",
            "Esportes",
            "Leitura",
            "Viagens",
            "Tecnologia",
            "Gastronomia",
            "Fotografia",
            "Jogos",
            "Arte"
    );

    public static List<String> getInteressesDisponiveis() {
        return INTERESSES_DISPONIVEIS;
    }

    public static boolean validarInteresse(String interesse) {
        if (interesse == null) {
            return false;
        }

        for (String item : INTERESSES_DISPONIVEIS) {
            if (item.equalsIgnoreCase(interesse.trim())) {
                return true;
            }
        }
        return false;
    }
}
