package com.kruk.PersonalExpenses;

/**
 * Created by Vova on 24.05.2017.
 */
public class WrongParametersException extends Exception {

    private String parameter;
    public String getParameter(){return parameter;}
    public WrongParametersException(String message, String parameter){
        super(message);
        this.parameter = parameter;
    }

    @Override
    public String getMessage() {
        return super.getMessage()+"! - "+parameter;
    }
}