package Exceptions;

public class DuplicatedElementOnListException extends RuntimeException {
    public DuplicatedElementOnListException(){

    }
    public DuplicatedElementOnListException(String value){
        super("Duplcated value: " + value);
    }
}
