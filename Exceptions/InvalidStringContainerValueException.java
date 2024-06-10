package Exceptions;

public class InvalidStringContainerValueException extends RuntimeException{
    public InvalidStringContainerValueException(){
    }
    public InvalidStringContainerValueException(String value){
        System.out.println("Invalid value: " + value);
    }
}
