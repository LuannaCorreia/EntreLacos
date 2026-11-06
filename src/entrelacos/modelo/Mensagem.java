package entrelacos.modelo;

import java.time.LocalDateTime;

public class Mensagem {
    private final String conteudo;
    private final LocalDateTime dataEnvio;
    private final Usuario remetente;
    private final Usuario destinatario;

    public Mensagem(String conteudo, Usuario remetente, Usuario destinatario) {
        if(conteudo == null || conteudo.trim().isEmpty()) {
            throw new IllegalArgumentException("A mensagem não pode estar vazia.");
        }
        if (remetente == null || destinatario == null) {
            throw new IllegalArgumentException("Os participantes são obrigatórios.");
        }

        this.conteudo = conteudo.trim();
        this.dataEnvio = LocalDateTime.now();
        this.remetente = remetente;
        this.destinatario = destinatario;
    }

    public String getConteudo() {
        return conteudo;
    }
    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }
    public Usuario getRemetente() {
        return remetente;
    }
    public Usuario getDestinatario(){
        return destinatario;
    }
}
