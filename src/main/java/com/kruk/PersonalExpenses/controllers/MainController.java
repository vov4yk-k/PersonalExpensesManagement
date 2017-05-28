package com.kruk.PersonalExpenses.controllers;

import com.kruk.PersonalExpenses.WrongParametersException;
import com.kruk.PersonalExpenses.model.CurrencyRate;
import com.kruk.PersonalExpenses.model.Expense;
import com.kruk.PersonalExpenses.services.CurrencyRatesService;
import com.kruk.PersonalExpenses.services.ExpensesService;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Vova on 23.05.2017.
 */
public class MainController {

    private ExpensesService expensesService;
    private CurrencyRatesService currencyRatesService;
    private DateFormat dateFormat;

    public MainController() throws SQLException {
        expensesService = new ExpensesService();
        currencyRatesService = new CurrencyRatesService();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public MainController(ExpensesService expensesService, CurrencyRatesService currencyRatesService) {
        this.expensesService = expensesService;
        this.currencyRatesService = currencyRatesService;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public TreeMap<Date, List<Expense>> add(String parameters) throws SQLException, ParseException {
        String[] str = parameters.split(" ");
        Date date = dateFormat.parse(str[1]);
        Double amount = Double.parseDouble(str[2]);
        String currency = str[3];

        StringBuilder sb = new StringBuilder();
        for (int i = 4;i<str.length; i++)
            sb.append(str[i]+" ");

        Expense expense = new Expense(date,amount,currency.toUpperCase(),formatProduct(sb.toString()));
        expensesService.addExpense(expense);
        return list();
    }

    public TreeMap<Date, List<Expense>> list() throws SQLException {
        return expensesService.getAllGroupedByDate();
    }

    public TreeMap<Date, List<Expense>> clear(String parameters) throws SQLException, ParseException {
        String[] str = parameters.split(" ");
        Date date = dateFormat.parse(str[1]);
        expensesService.deleteExpenseByDate(date);
        return list();
    }

    public double total(String parameters) throws ParseException, SQLException, NoSuchFieldException, IllegalAccessException {
        String[] str = parameters.split(" ");
        String baseCurrency = str[1];
        List<Expense> expenses = expensesService.getAll();
        return currencyRatesService.calcTotalAmountInCurrency(expenses,baseCurrency.toUpperCase());
    }

    public void verifyCurrency(String currency) throws SQLException, NoSuchFieldException, IllegalAccessException, WrongParametersException {
        verifyCurrency(currency,new Date());
    }

    public void verifyCurrency(String currency,Date date) throws WrongParametersException, IllegalAccessException, SQLException, NoSuchFieldException {
        List<CurrencyRate> currencyRateList = currencyRatesService.getLastRateByCurrencyOnDate(currency.toUpperCase(),date);
        if(currencyRateList.isEmpty()){
            if(!currencyRatesService.updateCurrencyRates(date,currency.toUpperCase())) throw new WrongParametersException("No rates for currency",currency);
            currencyRateList = currencyRatesService.getLastRateByCurrencyOnDate(currency.toUpperCase(),date);
            if(currencyRateList.isEmpty()) throw new WrongParametersException("No rates for currency",currency);
        }
        Iterator<CurrencyRate> iterator = currencyRateList.listIterator();
        CurrencyRate currencyRate = iterator.next();
        if(currencyRate == null || currencyRate.getRate()==0) throw new WrongParametersException("No rates for currency",currency);
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public String formatProduct(String product){
        product = product.trim();

        int first = product.indexOf("\"");
        if(first == -1)
            first = product.indexOf(8220);

        int last = product.lastIndexOf("\"");
        if(last == -1)
            last = product.lastIndexOf(8221);

        if(first==0 && last == product.length()-1)
            product = product.substring(1,product.length()-1);

        return capitalize(product);
    }

    public static String capitalizeWord(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final char firstChar = str.charAt(0);
        final char newChar = Character.toTitleCase(firstChar);
        if (firstChar == newChar) {
            // already capitalized
            return str;
        }

        char[] newChars = new char[strLen];
        newChars[0] = newChar;
        str.getChars(1,strLen, newChars, 1);
        return String.valueOf(newChars);
    }

    public static String capitalize(String str){
        String[] strArr = str.split(" ");
        StringBuilder sb = new StringBuilder();
        for(String s:strArr){
            sb.append(capitalizeWord(s.trim()));
            sb.append(" ");
        }
        return  sb.toString().trim();
    }

}
