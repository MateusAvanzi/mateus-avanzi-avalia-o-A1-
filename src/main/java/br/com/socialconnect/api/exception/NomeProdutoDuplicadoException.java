package br.com.socialconnect.api.exception;

public class NomeProdutoDuplicadoException extends RuntimeException {
    public NomeProdutoDuplicadoException(String nome) {
        super("Produto já cadastrado com o nome: " + nome);
    }
}
