package Exceptions;

public class FileReadingException extends RuntimeException {
    public FileReadingException(){
    }
    public FileReadingException(String path){
        super("Error while reading file: " + path);
    }
}
