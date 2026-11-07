package entrelacos.app;

import entrelacos.excecao.ExcecaoNegocio;
import entrelacos.modelo.CatalogoInteresses;
import entrelacos.modelo.Match;
import entrelacos.modelo.Mensagem;
import entrelacos.modelo.Plano;
import entrelacos.modelo.PlanoConexao;
import entrelacos.modelo.PlanoEssencial;
import entrelacos.modelo.Usuario;
import entrelacos.servico.SistemaEncontros;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SistemaEncontros sistema = new SistemaEncontros();
    private static Usuario usuarioAtivo;

    public static void main(String[] args) {
        boolean executando = true;

        System.out.println("============================");
        System.out.println("SEJA BEM-VINDO AO ENTRELAÇOS");
        System.out.println("============================");

        while (executando) {
            exibirMenu();

            int opcao = lerInteiro("Escolha uma opção:");

            try {
                switch (opcao) {

                    case 1: cadastrarUsuario();
                            break;

                    case 2: selecionarUsuarioAtivo();
                            break;

                    case 3: listarPerfis();
                            break;

                    case 4: buscarPerfis();
                            break;

                    case 5: curtirPerfil();
                            break;

                    case 6: listarMatches();
                            break;

                    case 7: enviarMensagem();
                            break;

                    case 8: consultarMensagens();
                            break;

                    case 0: executando = false;
                            System.out.println("Obrigado por utilizar o EntreLaços!");
                            break;

                    default: System.out.println("Opção inválida. Tente novamente.");
                }

            } catch (ExcecaoNegocio | IllegalArgumentException e) {
                System.out.println("Operação não realizada: " + e.getMessage());
            }
        }

        scanner.close();
    }

    //MENU PRINCIPAL
    private static void exibirMenu() {
        System.out.println("\n========== MENU PRINCIPAL ==========");

        if (usuarioAtivo == null) {
            System.out.println("Usuário ativo: nenhum");
        } else {
            System.out.println("Usuário ativo: " + usuarioAtivo.getNome());
        }

        System.out.println("------------------------------------");
        System.out.println("1 - Cadastrar usuário");
        System.out.println("2 - Selecionar usuário ativo");
        System.out.println("3 - Listar perfis");
        System.out.println("4 - Buscar perfis");
        System.out.println("5 - Curtir perfil");
        System.out.println("6 - Consultar matches");
        System.out.println("7 - Enviar mensagem");
        System.out.println("8 - Consultar mensagens");
        System.out.println("0 - Sair");
        System.out.println("====================================");
    }

    //CADASTRO DE USUÁRIOS
    private static void cadastrarUsuario() {
        System.out.println("\n========== CADASTRO ==========");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        int idade = lerInteiro("Idade: ");

        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        System.out.println("\nEscolha seu plano:");
        System.out.println("1 - Essencial (2 curtidas)");
        System.out.println("2 - Conexão (5 curtidas)");

        Plano plano;

        int opcaoPlano = lerInteiro("Opção: ");

        if (opcaoPlano == 1) {
            plano = new PlanoEssencial();
        } else if (opcaoPlano == 2) {
            plano = new PlanoConexao();
        } else {
            throw new ExcecaoNegocio("Plano inválido.");
        }

        List<String> interesses = selecionarInteresses();

        Usuario novoUsuario = sistema.cadastrarUsuario(
                nome,
                email,
                idade,
                cidade,
                plano,
                interesses
        );

        System.out.println("\nUsuário cadastrado com sucesso!");
        System.out.println("ID: " + novoUsuario.getId());
        System.out.println("Nome: " + novoUsuario.getNome());
    }

    //SELEÇÃO DE INTERESSES
    private static List<String> selecionarInteresses() {
        List<String> interesses = new ArrayList<>();

        List<String> catalogo = CatalogoInteresses.getInteressesDisponiveis();

        System.out.println("\n========== INTERESSES ==========");

        for (int i = 0; i < catalogo.size(); i++) {
            System.out.println((i + 1) + " - " + catalogo.get(i));
        }

        int quantidade = lerInteiro(
                "\nQuantos interesses deseja selecionar? (0 a 3): "
        );

        if (quantidade < 0 || quantidade > 3) {
            throw new ExcecaoNegocio("Você pode selecionar no máximo 3 interesses.");
        }

        for (int i = 0; i < quantidade; i++) {
            int opcao = lerInteiro(
                    "Escolha o interesse " + (i + 1) + ": "
            );

            if (opcao < 1 || opcao > catalogo.size()) {
                throw new ExcecaoNegocio("Opção de interesse inválida.");
            }

            String interesse = catalogo.get(opcao - 1);

            if (interesses.contains(interesse)) {
                throw new ExcecaoNegocio("Você não pode selecionar o mesmo interesse duas vezes.");
            }

            interesses.add(interesse);
        }
        return interesses;
    }

    //SELEÇÃO DO USUÁRIO ATIVO
    private static void selecionarUsuarioAtivo() {
        List<Usuario> usuarios = sistema.listarUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("Não existem usuários cadastrados.");
            return;
        }

        System.out.println("\n========== USUÁRIOS CADASTRADOS ==========");

        for (Usuario usuario : usuarios) {
            System.out.println(
                    "ID: " + usuario.getId()
                            + " | Nome: " + usuario.getNome()
            );
        }

        int id = lerInteiro("\nInforme o ID do usuário: ");

        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                usuarioAtivo = usuario;

                System.out.println("Usuário ativo alterado para: " + usuario.getNome());

                return;
        }
    }
        throw new ExcecaoNegocio("ID de usuário não encontrado.");
}

    //LISTAGEM DE PERFIS
    private static void listarPerfis () {
        exigirUsuarioAtivo();

        List<Usuario> perfis = sistema.listarPerfis(usuarioAtivo);

        System.out.println("\n========== PERFIS DISPONÍVEIS ==========");

        if (perfis.isEmpty()) {
            System.out.println("Não existem outros perfis cadastrados.");
            return;
        }

        for (Usuario usuario : perfis) {
            exibirPerfil(usuario);
        }
    }

    //BUSCA DE PERFIS
    private static void buscarPerfis() {
        exigirUsuarioAtivo();

        System.out.println("\n========== BUSCAR PERFIS ==========");
        System.out.println("1 - Buscar por nome");
        System.out.println("2 - Buscar por nome e cidade");

        int opcao = lerInteiro("Escolha uma opção: ");

        List<Usuario> encontrados;

        if (opcao == 1) {
            System.out.print("Digite o nome: ");
            String nome = scanner.nextLine();

            encontrados = sistema.buscarUsuarios(nome);
        } else if (opcao == 2) {
            System.out.print("Digite o nome: ");
            String nome = scanner.nextLine();

            System.out.print("Digite a cidade: ");
            String cidade = scanner.nextLine();

            encontrados = sistema.buscarUsuarios(nome, cidade);
        } else {
            throw new ExcecaoNegocio("Opção de busca inválida.");
        }

        System.out.println("\n========== RESULTADOS ==========");

        boolean encontrou = false;

        for (Usuario usuario : encontrados) {
            if (!usuario.equals(usuarioAtivo)) {
                exibirPerfil(usuario);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum perfil encontrado.");
        }
    }

    //CURTIR PERFIL
    private static void curtirPerfil() {
        exigirUsuarioAtivo();

        List<Usuario> perfis = sistema.listarPerfis(usuarioAtivo);

        if (perfis.isEmpty()) {
            System.out.println("Não existem perfis disponíveis para curtir.");
            return;
        }

        System.out.println("\n========== CURTIR PERFIL ==========");

        for (Usuario usuario : perfis) {
            System.out.println(
                    "ID: " + usuario.getId()
                            + " | Nome: " + usuario.getNome()
            );
        }

        int id = lerInteiro("\nInforme o ID do perfil que deseja curtir: ");

        Usuario destinatario = null;

        for (Usuario usuario : perfis) {
            if (usuario.getId() == id) {
                destinatario = usuario;
                break;
            }
        }

        if (destinatario == null) {
            throw new ExcecaoNegocio("Perfil não encontrado.");
        }

        Match match = sistema.curtir(usuarioAtivo, destinatario);

        if (match != null) {
            System.out.println("\nÉ um match! Vocês curtiram um ao outro!");
        } else {
            System.out.println("\nCurtida enviada com sucesso!");
        }
    }

    //CONSULTA DE MATCHES
    private static void listarMatches() {
        exigirUsuarioAtivo();

        List<Match> matches = sistema.listarMatches(usuarioAtivo);

        System.out.println("\n========== SEUS MATCHES ==========");

        if (matches.isEmpty()) {
            System.out.println("Você ainda não possui matches.");
            return;
        }

        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            Usuario outroUsuario;

            if (match.getUsuario1().equals(usuarioAtivo)) {
                outroUsuario = match.getUsuario2();
            } else {
                outroUsuario = match.getUsuario1();
            }

            System.out.println(
                    (i + 1) + " - " + outroUsuario.getNome()
                            + " | " + outroUsuario.getCidade()
            );
        }
    }

    //ENVIO DE MENSAGENS
    private static void enviarMensagem() {
        Match match = selecionarMatch();

        if (match == null) {
            return;
        }

        Usuario destinatario = obterOutroParticipante(match);

        System.out.println("\nConversa com " + destinatario.getNome());

        System.out.print("Digite sua mensagem: ");
        String conteudo = scanner.nextLine();

        sistema.enviarMensagem(
                usuarioAtivo,
                destinatario,
                conteudo
        );

        System.out.println("Mensagem enviada com sucesso!");
    }

    //CONSULTA DE MENSAGENS
    private static void consultarMensagens() {
        Match match = selecionarMatch();

        if (match == null) {
            return;
        }

        Usuario outroUsuario = obterOutroParticipante(match);

        List<Mensagem> mensagens = sistema.listarMensagens(
                usuarioAtivo,
                outroUsuario
        );

        System.out.println(
                "\n========== CONVERSA COM "
                        + outroUsuario.getNome().toUpperCase()
                        + " =========="
        );

        if (mensagens.isEmpty()) {
            System.out.println("Ainda não existem mensagens nesta conversa.");
            return;
        }

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Mensagem mensagem : mensagens) {
            System.out.println(
                    "[" + mensagem.getDataEnvio().format(formato) + "] "
                            + mensagem.getRemetente().getNome()
                            + ": " + mensagem.getConteudo()
            );
        }
    }

    //SELEÇÃO DE MATCH PARA CONVERSA
    private static Match selecionarMatch() {
        exigirUsuarioAtivo();

        List<Match> matches = sistema.listarMatches(usuarioAtivo);

        if (matches.isEmpty()) {
            System.out.println("Você ainda não possui matches.");
            return null;
        }

        System.out.println("\n========== SELECIONE UMA CONVERSA ==========");

        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            Usuario outroUsuario = obterOutroParticipante(match);

            System.out.println(
                    (i + 1) + " - " + outroUsuario.getNome()
            );
        }

        int opcao = lerInteiro("Escolha uma conversa:");

        if (opcao < 1 || opcao > matches.size()) {
            throw new ExcecaoNegocio("Opção de conversa inválida.");
        }

        return matches.get(opcao - 1);
    }

    //IDENTIFICA O OUTRO PARTICIPANTE DO MATCH
    private static Usuario obterOutroParticipante(Match match) {
        if (match.getUsuario1().equals(usuarioAtivo)) {
            return match.getUsuario2();
        }

        return match.getUsuario1();
    }

    //EXIBIÇÃO DE PERFIL
    private static void exibirPerfil(Usuario usuario) {
        System.out.println("\n------------------------------------");
        System.out.println("ID: " + usuario.getId());
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Idade: " + usuario.getIdade());
        System.out.println("Cidade: " + usuario.getCidade());
        System.out.println("Plano: " + usuario.getPlano().getNomePlano());

        System.out.println("Interesses: ");

        if (usuario.getInteresses().isEmpty()) {
            System.out.println("Nenhum interesse informado.");
        } else {
            for (String interesse : usuario.getInteresses()) {
                System.out.println("- " + interesse);
            }
        }
        System.out.println("------------------------------------");
    }

    //VALIDAÇÃO DO USUÁRIO ATIVO
    private static void exigirUsuarioAtivo() {
        if (usuarioAtivo == null) {
            throw new ExcecaoNegocio("Selecione um usuário antes de realizar esta operação.");
        }
    }

    //LEITURA DE NÚMEROS INTEIROS
    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine();

            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }
}