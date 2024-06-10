package Exceptions;

public class FileWritingException extends RuntimeException{
    public FileWritingException(){
    }
    public FileWritingException (String path){
        super("Error writing data to file: " + path);
    }
}
