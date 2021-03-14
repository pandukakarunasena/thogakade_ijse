package middlewares;

public class CustomException extends Exception{
    public static void exception(Exception ex){
        ex.getMessage();
    }
}
