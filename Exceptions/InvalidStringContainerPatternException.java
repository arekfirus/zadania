package Exceptions;

public class InvalidStringContainerPatternException extends RuntimeException {
    public InvalidStringContainerPatternException(){
    }
    public InvalidStringContainerPatternException(String badPattern){
        super("Invalid Pattern : " + badPattern);
    }
}
