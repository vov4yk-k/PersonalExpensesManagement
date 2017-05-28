package com.kruk.PersonalExpenses.services;

import com.kruk.PersonalExpenses.dao.ExpensesDAO;
import com.kruk.PersonalExpenses.model.Expense;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Vova on 22.05.2017.
 */
public class ExpensesService {

    private ExpensesDAO expensesDAO;

    public ExpensesService() throws SQLException {
        this.expensesDAO = new ExpensesDAO();
    }

    public ExpensesService(ExpensesDAO expensesDAO) {
        this.expensesDAO = expensesDAO;
    }

    public List<Expense> getAll() throws SQLException {
        return expensesDAO.getAll();
    }

    public void addExpense(Expense expense) throws SQLException {
        expensesDAO.addExpense(expense);
    }

    public void deleteExpense(String id) throws SQLException {
        expensesDAO.delete(id);
    }

    public TreeMap<Date,List<Expense>> getAllGroupedByDate() throws SQLException {
        return expensesDAO.getAllGroupedByDate();
    }

    public void deleteExpenseByDate(Date date) throws SQLException {
        expensesDAO.deleteByDate(date);
    }

    public List<Expense> getListOnDate(Date date) throws SQLException {
        return expensesDAO.getListOnDate(date);
    }
}
