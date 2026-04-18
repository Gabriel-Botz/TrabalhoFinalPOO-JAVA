package exception;

public class DependenteException extends RuntimeException {
    public DependenteException(String mensagem) {
        super(mensagem);
    }
}