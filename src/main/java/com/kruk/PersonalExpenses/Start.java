package com.kruk.PersonalExpenses;

import com.kruk.PersonalExpenses.controllers.MainController;
import com.kruk.PersonalExpenses.model.Expense;


import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;


/**
 * Created by Vova on 22.05.2017.
 */

enum Commands {
    add(5), list(1), clear(2), total(2), quit(1);
    private final int parameters;
    Commands(int parameters){
        this.parameters = parameters;
    }
    public int getParametersCount(){
        return this.parameters;
    }
}

public class Start {

    public static void main(String[] args) {
        try {
            MainController mainController = new MainController();
            StartApp(mainController);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void StartApp(MainController mainController) throws SQLException, ParseException, NoSuchFieldException, IllegalAccessException {

        Scanner console = new Scanner(System.in);

        boolean finished = false;
        System.out.println("Please enter the command");
        do{
            String response = console.nextLine();
            if(response.length()==0) continue;
            Commands command = getCommand(response);

            try {
                verifyParameters(response, command);
                verifyCurrency(command,response,mainController);
            } catch (WrongParametersException e) {
                System.out.println(e.getMessage());
                continue;
            }

            String newLine = System.getProperty("line.separator");
            System.out.print(newLine);

            switch (command){
                case add:
                    TreeMap<Date, List<Expense>> expensesAfterAdd = mainController.add(response);
                    showList(expensesAfterAdd);
                    break;
                case list:
                    TreeMap<Date, List<Expense>> expensesList = mainController.list();
                    showList(expensesList);
                    break;
                case clear:
                    TreeMap<Date, List<Expense>> expensesAfterClear = mainController.clear(response);
                    showList(expensesAfterClear);
                    break;
                case total:
                    double amount = mainController.total(response);
                    String[] str = response.split(" ");
                    String currency = str[1];
                    System.out.format("%9.2f", amount);
                    System.out.println(" "+currency.toUpperCase());
                    System.out.print(newLine);
                    break;
                case quit:
                    finished = true;
                    break;
            }
        }while (!finished);
    }

    public static Commands getCommand(String line){
        String[] str = line.split(" ");
        String strCommand = str[0];

        for (Commands command : Commands.values()) {
            if (command.name().equalsIgnoreCase(strCommand))
                return command;
        }
        return null;
    }

    public static void verifyParameters(String parameters, Commands command) throws WrongParametersException {

        String[] str = parameters.split(" ");

        if(command == null){
            throw new WrongParametersException("Wrong command",str[0]);
        }

        switch (command) {
            case add:
                verifyAdd(str,command.getParametersCount());
                break;
            case list:
                if(str.length<command.getParametersCount()) throw new WrongParametersException("Not enough parameters",String.valueOf(str));
                break;
            case clear:
                verifyClear(str,command.getParametersCount());
                break;
            case total:
                if(str.length<command.getParametersCount()) throw new WrongParametersException("Not enough parameters",String.valueOf(str));
                break;
        }
    }

    public static void verifyAdd(String[] parameters, int count) throws WrongParametersException {

        if(parameters.length<count) throw new WrongParametersException("Not enough parameters",String.join(",", parameters));

        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date expDate = df.parse(parameters[1]);
        } catch (ParseException e) {
            throw new WrongParametersException("Wrong date format",parameters[1]);
        }

        try {
            Double num = Double.parseDouble(parameters[2]);
        }catch (NumberFormatException e){
            throw new WrongParametersException("Ammount is incorrect",parameters[2]);
        }
    }

    public static void verifyClear(String[] parameters, int count) throws WrongParametersException {

        if(parameters.length<count) throw new WrongParametersException("Not enough parameters",String.valueOf(parameters));

        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date expDate = df.parse(parameters[1]);
        } catch (ParseException e) {
            throw new WrongParametersException("Wrong date format", parameters[1]);
        }
    }

    public static void showList(TreeMap<Date, List<Expense>> list){
        System.out.print(formatList(list));
    }

    public static void verifyCurrency(Commands command, String parameters,MainController mainController) throws SQLException, WrongParametersException, IllegalAccessException, NoSuchFieldException, ParseException {
        String[] str = parameters.split(" ");
        if(command == Commands.add){
            DateFormat dateFormat = mainController.getDateFormat();
            mainController.verifyCurrency(str[3],dateFormat.parse(str[1]));
        }else if (command == Commands.total){
            mainController.verifyCurrency(str[1]);
        }
    }

    public static String formatPL(String s){
        return String.format("%1$3s",s);
    }

    public static String formatList(TreeMap<Date, List<Expense>> list){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        NumberFormat numberFormat = NumberFormat.getInstance();
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach((date,expenseList)->{
            stringBuilder.append(formatPL(""));
            stringBuilder.append(dateFormat.format(date));
            expenseList.forEach((expense -> {
                stringBuilder.append('\n');
                stringBuilder.append(formatPL(""));
                stringBuilder.append(expense.getProductName());
                stringBuilder.append(" ");
                stringBuilder.append(numberFormat.format(expense.getAmount()));
                stringBuilder.append(" ");
                stringBuilder.append(expense.getCurency());
            }));
            stringBuilder.append('\n');
            stringBuilder.append('\n');
        });

        return stringBuilder.toString();
    }
}
