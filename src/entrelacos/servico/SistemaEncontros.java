package entrelacos.servico;

import entrelacos.excecao.ExcecaoNegocio;
import entrelacos.modelo.Match;
import entrelacos.modelo.Mensagem;
import entrelacos.modelo.Plano;
import entrelacos.modelo.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SistemaEncontros {
    private final List<Usuario> usuarios;
    private final List<Match> matches;
    private int proximoID;

    public SistemaEncontros() {
        this.usuarios = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.proximoID = 1;
    }

    //CADASTRO DE USUÁRIOS
    public  Usuario cadastrarUsuario(String nome, String email, int idade,
                                     String cidade, Plano plano,
                                     List<String> interesses) {
        if (email != null) {
            String emailformatado = email.trim();

            for (Usuario usuario : usuarios) {
                if (usuario.getEmail().equalsIgnoreCase(emailformatado)) {
                    throw new ExcecaoNegocio("Este e-mail já está cadastrado");
                }
            }
        }

        Usuario novoUsuario = new Usuario(
                proximoID,
                nome,
                email,
                idade,
                cidade,
                plano,
                interesses
        );

        usuarios.add(novoUsuario);
        proximoID++;

        return novoUsuario;
    }

    //LISTAGEM DE USUÁRIOS
    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(
                new ArrayList<>(usuarios)
        );
    }

    public List<Usuario> listarPerfis(Usuario usuarioAtivo) {
        validarUsuarioCadastrado(usuarioAtivo);

        List<Usuario> perfis = new ArrayList<>(usuarios);

        perfis.remove(usuarioAtivo);

        perfis.sort(
                Comparator.comparing(
                        Usuario::getNome,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        return Collections.unmodifiableList(perfis);
    }

    //BUSCA DE USUÁRIOS POR NOME
    public List<Usuario> buscarUsuarios(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ExcecaoNegocio("Informe um nome para realizar a busca");
        }

        List<Usuario> encontrados = new ArrayList<>();

        String termoBusca = nome.trim().toLowerCase(Locale.ROOT);

        for (Usuario usuario : usuarios) {
            String nomeUsuario = usuario.getNome().toLowerCase(Locale.ROOT);

            if (nomeUsuario.contains(termoBusca)) {
                encontrados.add(usuario);
            }
        }

        return Collections.unmodifiableList(encontrados);
    }

    //BUSCA DE USUÁRIOS POR NOME E CIDADE (SOBRECARGA)
    public List<Usuario> buscarUsuarios(String nome, String cidade) {
        if (nome == null || nome.trim().isEmpty()
                || cidade == null || cidade.trim().isEmpty()) {

            throw new ExcecaoNegocio("Informe o nome e a cidade para realizar a busca");
        }

        List<Usuario> encontrados = new ArrayList<>();

        String nomeBusca = nome.trim().toLowerCase(Locale.ROOT);
        String cidadeBusca = cidade.trim().toLowerCase(Locale.ROOT);

        for (Usuario usuario : usuarios) {
            boolean nomeEncontrado = usuario.getNome()
                    .toLowerCase(Locale.ROOT)
                    .contains(nomeBusca);

            boolean cidadeEncontrada = usuario.getCidade()
                    .toLowerCase(Locale.ROOT)
                    .contains(cidadeBusca);

            if (nomeEncontrado && cidadeEncontrada) {
                encontrados.add(usuario);
            }
        }

        return Collections.unmodifiableList(encontrados);
    }

    //REGISTRO DE CURTIDAS E CRIAÇÃO DE MATCHES
    public Match curtir(Usuario remetente, Usuario destinatario) {
        validarUsuarioCadastrado(remetente);
        validarUsuarioCadastrado(destinatario);

        remetente.registrarCurtidas(destinatario);

        if (destinatario.jaCurtiu(remetente)) {
            Match matchExistente = buscarMatch(remetente, destinatario);

            if (matchExistente != null) {
                return matchExistente;
            }

            Match novoMatch = new Match(remetente, destinatario);
            matches.add(novoMatch);
            return novoMatch;
        }
        return null;
    }

    //CONSULTA DE MATCHES
    public List<Match> listarMatches(Usuario usuario) {
        validarUsuarioCadastrado(usuario);
        List<Match> matchesUsuario = new ArrayList<>();

        for (Match match : matches) {
            if (match.participaDoMatch(usuario)) {
                matchesUsuario.add(match);
            }
        }
        return Collections.unmodifiableList(matchesUsuario);
    }

    //ENVIO DE MENSAGENS
    public Mensagem enviarMensagem(Usuario remetente,
                                   Usuario destinatario,
                                   String conteudo) {
        validarUsuarioCadastrado(remetente);
        validarUsuarioCadastrado(destinatario);

        if (remetente.equals(destinatario)) {
            throw new ExcecaoNegocio("Não é possível enviar mensagens para si mesmo.");
        }

        Match match = buscarMatch(remetente, destinatario);

        if (match == null) {
            throw new ExcecaoNegocio("Não é possível enviar mensagens sem um match.");
        }

        Mensagem mensagem = new Mensagem(
                conteudo,
                remetente,
                destinatario
        );

        match.adicionarMensagem(mensagem);
        return mensagem;
    }

    //CONSULTA DE MENSAGENS
    public List<Mensagem> listarMensagens(Usuario usuario,
                                          Usuario outroUsuario) {
        validarUsuarioCadastrado(usuario);
        validarUsuarioCadastrado(outroUsuario);

        Match match = buscarMatch(usuario, outroUsuario);

        if (match == null) {
            throw new ExcecaoNegocio("Não existe um match entre esses usuários");
        }

        return match.getMensagens();
    }

    //MÉTODOS AUXILIARES
    private Match buscarMatch(Usuario usuario1, Usuario usuario2) {
        for (Match match : matches) {
            boolean mesmoPar =
                    (match.getUsuario1().equals(usuario1)
                            && match.getUsuario2().equals(usuario2))
                    ||
                    (match.getUsuario1().equals(usuario2)
                            && match.getUsuario2().equals(usuario1));

            if (mesmoPar) {
                return match;
            }
        }
        return null;
    }

    private void validarUsuarioCadastrado(Usuario usuario) {
        if (usuario ==  null || !usuarios.contains(usuario)) {
            throw new ExcecaoNegocio("Usuário não encontrado no sistema.");
        }
    }
}









