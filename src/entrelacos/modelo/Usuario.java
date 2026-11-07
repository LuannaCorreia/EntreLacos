package entrelacos.modelo;

import entrelacos.excecao.ExcecaoNegocio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Usuario {
    private final int id;
    private final String nome;
    private final String email;
    private final int idade;
    private final String cidade;
    private final Plano plano;
    private final List<String> interesses;
    private final List<Usuario> curtidasEnviadas;

    public Usuario(int id, String nome, String email, int idade,
                  String cidade, Plano plano, List<String> interesses) {

        if (id <=0) {
            throw new IllegalArgumentException("O ID deve ser positivo");
        }

        if (nome == null || nome.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || cidade == null || cidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome, e-mail e cidade são obrigatórios.");
        }

        if (idade <=18) {
            throw new ExcecaoNegocio("O usuário deve ter pelo menos 18 anos.");
        }

        if (plano == null) {
            throw new IllegalArgumentException("O plano é obrigatório.");
        }

        if (interesses == null || interesses.size() > 3) {
            throw new ExcecaoNegocio("O usuário pode selecionar até 3 interesses.");
        }

        List<String> interessesValidos = new ArrayList<>();

        for (String interesse : interesses) {
            if (!CatalogoInteresses.validarInteresse(interesse)) {
                throw new ExcecaoNegocio("Interesse inválido: " + interesse);
            }

            String interesseFormatado = interesse.trim();

            for (String existente : interessesValidos) {
                if (existente.equalsIgnoreCase(interesseFormatado)) {
                    throw new ExcecaoNegocio("Não é permitido repetir interesses.");
                }
            }

            interessesValidos.add(interesseFormatado);
        }

        this.id = id;
        this.nome = nome.trim();
        this.email = email.trim();
        this.idade = idade;
        this.cidade = cidade.trim();
        this.plano = plano;
        this.interesses = interessesValidos;
        this.curtidasEnviadas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public int getIdade() {
        return idade;
    }

    public String getCidade() {
        return cidade;
    }

    public Plano getPlano() {
        return plano;
    }

    public List<String> getInteresses() {
        return Collections.unmodifiableList(interesses);
    }

    public List<Usuario> getCurtidasEnviadas() {
        return Collections.unmodifiableList(curtidasEnviadas);
    }

    public int getQuantidadeCurtidas() {
        return curtidasEnviadas.size();
    }

    public boolean jaCurtiu(Usuario usuario) {
        return curtidasEnviadas.contains(usuario);
    }

    public void registrarCurtidas(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException("O usuário é obrigatório.");
        }

        if (equals(usuario)) {
            throw new ExcecaoNegocio("Não é possível curtir o próprio perfil");
        }

        if (jaCurtiu(usuario)) {
            throw new ExcecaoNegocio("Você já curtiu esse usuário.");
        }

        if (getQuantidadeCurtidas() >= plano.getLimiteCurtidas()) {
            throw new ExcecaoNegocio("Você atingiu o limite de curtidas do seu plano.");
        }

        curtidasEnviadas.add(usuario);
    }

    @Override
    public boolean equals(Object objeto) {

        if(this == objeto) {
            return true;
        }

        if (!(objeto instanceof  Usuario outro)) {
            return false;
        }

        return id == outro.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
