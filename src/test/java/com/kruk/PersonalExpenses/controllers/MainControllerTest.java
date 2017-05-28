package com.kruk.PersonalExpenses.controllers;

import com.kruk.PersonalExpenses.Start;
import com.kruk.PersonalExpenses.dao.CurrencyRatesDAO;
import com.kruk.PersonalExpenses.dao.ExpensesDAO;
import com.kruk.PersonalExpenses.model.Expense;
import com.kruk.PersonalExpenses.services.CurrencyRatesService;
import com.kruk.PersonalExpenses.services.ExpensesService;
import org.h2.store.fs.FileUtils;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by Vova on 27.05.2017.
 */
public class MainControllerTest {

    private ExpensesDAO expensesDAO;
    private CurrencyRatesDAO currencyRatesDAO;
    private CurrencyRatesService currencyRatesService;
    private ExpensesService expensesService;
    private MainController mainController;
    private DateFormat df;

    @Before
    public void initTest() throws SQLException {
        String uri = "jdbc:h2:file:~/dataTest/personal_expenses;INIT=create schema if not exists personal_expenses\\;runscript from 'classpath:/create.sql'";
        expensesDAO = new ExpensesDAO(uri);
        expensesService = new ExpensesService(expensesDAO);

        currencyRatesDAO = new CurrencyRatesDAO(uri);
        currencyRatesService = new CurrencyRatesService(currencyRatesDAO);

        mainController = new MainController(expensesService,currencyRatesService);

        df = mainController.getDateFormat();
    }

    @After
    public void afterTest() throws SQLException {
        expensesDAO.clearTable();
        currencyRatesDAO.clearTable();
    }

    @Test
    public void add() throws Exception {

        String testLine = "add 2099-03-03 100 USD product TEST";
        Date date = df.parse("2099-03-03");
        Expense expense = new Expense(date, 100, "USD", mainController.formatProduct("product TEST"));

        mainController.add(testLine);
        Expense expectedExpense = expensesService.getListOnDate(date).listIterator().next();

        assertEquals(expectedExpense.getAmount(),expense.getAmount(),0.0001);
        assertEquals(expectedExpense.getCurency(),expense.getCurency());
        assertEquals(expectedExpense.getProductName(),expense.getProductName());
        assertEquals(expectedExpense.getDate(),expense.getDate());

        expensesService.deleteExpense(String.valueOf(expectedExpense.getId()));
        boolean deletedExpense = expensesService.getListOnDate(date).isEmpty();
        assertEquals(true,deletedExpense);

    }

    @Test
    public void list() throws Exception {

        List<Expense> expectedList = new ArrayList<>();
        for (int i = 1;i<=30;i++){
            double amount = Math.random()*100;
            String strDay = i<9 ? "0"+String.valueOf(i) : String.valueOf(i);
            expectedList.add(new Expense(df.parse("2099-03-"+strDay), amount, "USD", mainController.formatProduct("product TEST")));

            String testLine = "add 2099-03-"+strDay+" "+amount+" USD product TEST";
            mainController.add(testLine);
        }
        List<Expense> actualList = expensesService.getAll();
        for (int i = 0;i<30;i++){
            Expense expected = expectedList.get(i);
            Expense actual = actualList.get(i);
            assertEquals(expected.toString(),actual.toString());
        }

        for (int i = 1;i<=30;i++){
            double amount = Math.random()*100;
            String strDay = i<9 ? "0"+String.valueOf(i) : String.valueOf(i);
            expectedList.add(new Expense(df.parse("2099-03-"+strDay), amount, "EUR", mainController.formatProduct("product TEST2")));

            String testLine = "add 2099-03-"+strDay+" "+amount+" EUR product TEST2";
            mainController.add(testLine);
        }
        TreeMap<Date,List<Expense>> groupedExpensesEx = new TreeMap<>();
        for (int i = 1;i<=30;i++) {
            String strDay = i<9 ? "0"+String.valueOf(i) : String.valueOf(i);
            Date date = df.parse("2099-03-"+strDay);
            List<Expense> expenseList = expectedList.stream()
                                                    .filter(expected -> expected.getDate().equals(date))
                                                    .collect(Collectors.toList());
            groupedExpensesEx.put(date,expenseList);
        }
        String expectedGroupedStr = Start.formatList(groupedExpensesEx);
        TreeMap<Date,List<Expense>> groupedExpensesAc = mainController.list();
        String actualGroupedStr = Start.formatList(groupedExpensesAc);
        assertEquals(expectedGroupedStr,actualGroupedStr);
    }

    @Test
    public void clear() throws Exception {
        for (int day = 1; day<30;day++) {
            for (int i = 1; i <= 10; i++) {
                double amount = Math.random() * 100;
                String strDay = day < 9 ? "0" + String.valueOf(day) : String.valueOf(day);
                String testLine = "add 2099-03-" + strDay + " " + amount + " USD product TEST";
                mainController.add(testLine);
            }
        }
        TreeMap<Date, List<Expense>> expenses = mainController.list();
        assertEquals(false,expenses.isEmpty());

        for (int day = 1; day<30;day++) {
            String strDay = day < 9 ? "0" + String.valueOf(day) : String.valueOf(day);
            String testLine = "clear 2099-03-" + strDay;
            mainController.clear(testLine);
        }
        expenses = mainController.list();
        assertEquals(true,expenses.isEmpty());
    }

    @Test
    public void total() throws Exception {
        for (int day = 1; day<=10;day++) {
                String strDay = day < 9 ? "0" + String.valueOf(day) : String.valueOf(day);

                String testLine = "add 2017-03-" + strDay + " " + (day*10) + " USD product TEST";
                mainController.add(testLine);

                testLine = "add 2017-03-" + strDay + " " + (day*15) + " EUR product TEST";
                mainController.add(testLine);

                testLine = "add 2017-03-" + strDay + " " + (day*20) + " PLN product TEST";
                mainController.add(testLine);
        }
        /*
        Date            Amount(USD) Amount(EUR) Amount(PLN) Rate(USD-GBP) Rate(EUR-GBP) Rate(PLN-GBP)     Total
        2017-03-01          10          15          20          0.81221     0.8555          0.19915       24,9376
        2017-03-02          20          30          40          0.81377     0.8556          0.19925       49,9134
        2017-03-03          30          45          60          0.81737     0.86355         0.20079       75,42825
        2017-03-04          40          60          80          0.81737     0.86355         0.20079       100,571
        2017-03-05          50          75          100         0.81737     0.86355         0.20079       125,71375
        2017-03-06          60          90          120         0.81477     0.863           0.20027       150,5886
        2017-03-07          70          105         140         0.81988     0.8671          0.2013        176,6191
        2017-03-08          80          120         160         0.82184     0.86753         0.20114       202,0332
        2017-03-09          90          135         180         0.82128     0.86653         0.20072       227,02635
        2017-03-10          100         150         200         0.82265     0.8725          0.20169       253,478
                                                                                                          1386,30925
         */
        double total = mainController.total("total GBP");
        assertEquals(1386.30925,total,0.001);
    }

}