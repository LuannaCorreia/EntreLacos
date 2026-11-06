package entrelacos.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Match {
    private final Usuario usuario1;
    private final Usuario usuario2;
    private final LocalDateTime dataMatch;
    private final List<Mensagem> mensagens;

    public Match(Usuario usuario1, Usuario usuario2) {
        if (usuario1 == null || usuario2 == null) {
            throw new IllegalArgumentException("Os usuários do match são obrigatórios.");
        }

        if (usuario1.equals(usuario2)) {
            throw new IllegalArgumentException("Um usuário não pode ter match consigo mesmo.");
        }

        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.dataMatch = LocalDateTime.now();
        this.mensagens = new ArrayList<>();
    }
    public Usuario getUsuario1() {
        return usuario1;
    }
    public Usuario getUsuario2() {
        return usuario2;
    }
    public LocalDateTime getDataMatch() {
        return dataMatch;
    }
    public List<Mensagem> getMensagens() {
        return Collections.unmodifiableList(mensagens);
    }

    public void adicionarMensagem(Mensagem mensagem) {
        if (mensagem == null) {
            throw new IllegalArgumentException("A mensagem não pode ser nula.");
        }

        if(!participaDoMatch(mensagem.getRemetente())
                || !participaDoMatch(mensagem.getDestinatario())) {
            throw new IllegalArgumentException("A mensagem deve envolver os participantes do match.");
        }

        mensagens.add(mensagem);
    }
    public boolean participaDoMatch(Usuario usuario) {
        return usuario1.equals(usuario) || usuario2.equals(usuario);
    }
}