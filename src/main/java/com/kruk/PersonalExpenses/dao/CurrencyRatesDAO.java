package com.kruk.PersonalExpenses.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kruk.PersonalExpenses.model.CurrencyRate;
import com.kruk.PersonalExpenses.model.Expense;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Vova on 23.05.2017.
 */
public class CurrencyRatesDAO {

    private final String url;
    private ConnectionSource source;
    private Dao<CurrencyRate, String> dao;

    public CurrencyRatesDAO() throws SQLException {
        url = "jdbc:h2:file:~/data/personal_expenses;INIT=create schema if not exists personal_expenses\\;runscript from 'classpath:/create.sql'";
        source = new JdbcConnectionSource(url);
        dao = DaoManager.createDao(source,CurrencyRate.class);
    }

    public CurrencyRatesDAO(String url) throws SQLException {
        this.url = url;
        this.source = new JdbcConnectionSource(url);
        this.dao = DaoManager.createDao(source,CurrencyRate.class);
    }

    public List<CurrencyRate> getAll() throws SQLException {
        return dao.queryForAll();
    }

    public void addCurrencyRate(CurrencyRate currencyRate) throws SQLException {
        dao.createIfNotExists(currencyRate);
    }

    public List<CurrencyRate> getLastRateByCurrencyOnDate(String baseCurrency, Date date) throws SQLException {

        List<CurrencyRate> currencyRateList;
        QueryBuilder qb = dao.queryBuilder();

        qb.limit(1L);
        qb.selectColumns("rate_date");
        qb.where().eq("base_currency",baseCurrency.toUpperCase()).and().le("rate_date",date);
        qb.groupBy("rate_date");
        qb.orderBy("rate_date",false);
        CurrencyRate currencyRateLastDate = (CurrencyRate) qb.queryForFirst();
        qb.reset();

        Date lastDate = currencyRateLastDate == null ? new Date() : currencyRateLastDate.getRateDate();

        currencyRateList = getRateOnDate(baseCurrency,lastDate);

        return currencyRateList;
    }

    public List<CurrencyRate> getRateOnDate(String baseCurrency, Date date) throws SQLException {
        List<CurrencyRate> currencyRateList;
        QueryBuilder qb = dao.queryBuilder();
        qb.where().eq("base_currency",baseCurrency.toUpperCase())
                .and()
                .eq("rate_date",date);
        currencyRateList = qb.query();

        return currencyRateList;
    }

    public CurrencyRate getLastCurrencyRate(String baseCurrency, String currency, Date date) throws SQLException {

        QueryBuilder qb = dao.queryBuilder();
        qb.limit(1L);
        qb.selectColumns("rate_date");
        qb.where()
                .eq("base_currency",baseCurrency.toUpperCase())
                .and()
                .eq("currency",currency.toUpperCase())
                .and()
                .le("rate_date",date);
        qb.groupBy("rate_date");
        qb.orderBy("rate_date",false);
        CurrencyRate currencyRateLastDate = (CurrencyRate) qb.queryForFirst();
        qb.reset();

        Date lastDate = currencyRateLastDate == null ? new Date() : currencyRateLastDate.getRateDate();
        qb.where()
                .eq("base_currency",baseCurrency.toUpperCase())
                .and()
                .eq("currency",currency.toUpperCase())
                .and()
                .eq("rate_date",lastDate);

        CurrencyRate currencyRate = (CurrencyRate) qb.queryForFirst();

        return currencyRate;
    }

    public void clearTable() throws SQLException {
        TableUtils.clearTable(source,CurrencyRate.class);
    }

}
