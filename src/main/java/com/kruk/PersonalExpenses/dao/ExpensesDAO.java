package com.kruk.PersonalExpenses.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kruk.PersonalExpenses.model.Expense;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Vova on 23.05.2017.
 */
public class ExpensesDAO {

    private final String url;
    private ConnectionSource source;
    private Dao<Expense, String> dao;

    public ExpensesDAO() throws SQLException {
        url = "jdbc:h2:file:~/data/personal_expenses;INIT=create schema if not exists personal_expenses\\;runscript from 'classpath:/create.sql'";
        source = new JdbcConnectionSource(url);
        dao = DaoManager.createDao(source,Expense.class);
    }

    public ExpensesDAO(String url) throws SQLException {
        this.url = url;
        source = new JdbcConnectionSource(url);
        dao = DaoManager.createDao(source, Expense.class);
    }

    public List<Expense> getAll() throws SQLException {
        /*GenericRawResults<String[]> rawResults =
                dao.queryRaw(
                        "select account_id,sum(amount) from orders group by account_id");*/
        return dao.queryForAll();
    }

    public TreeMap<Date,List<Expense>> getAllGroupedByDate() throws SQLException {
        TreeMap<Date,List<Expense>> groupedExpenses = new TreeMap<>();
        List<Expense> dates = dao.queryBuilder().selectColumns("exp_date").groupBy("exp_date").query();
        for (Expense dateExp:dates){
            Date currentDate = dateExp.getDate();
            List<Expense> expenseListOnDate = getListOnDate(currentDate);
            groupedExpenses.put(currentDate,expenseListOnDate);
        }
        return groupedExpenses;
    }

    public void addExpense(Expense expense) throws SQLException {
        dao.create(expense);
    }

    public void delete(String id) throws SQLException {
        dao.deleteById(id);
    }

    public void deleteByDate(Date date) throws SQLException {
        List<Expense> expenseListOnDate = getListOnDate(date);
        for (Expense v : expenseListOnDate) {
            delete(String.valueOf(v.getId()));
        }
    }

    public List<Expense> getListOnDate(Date date) throws SQLException {
        return dao.queryBuilder().where().eq("exp_date", date).query();
    }

    public void clearTable() throws SQLException {
        TableUtils.clearTable(source,Expense.class);
    }

}
