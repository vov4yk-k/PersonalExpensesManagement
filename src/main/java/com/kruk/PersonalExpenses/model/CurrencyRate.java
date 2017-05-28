package com.kruk.PersonalExpenses.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Vova on 23.05.2017.
 */
@DatabaseTable(tableName = "currency_rates")
public class CurrencyRate implements Comparable {

    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "rate_date")
    private Date rateDate;

    @DatabaseField(columnName = "base_currency")
    private String baseCurrency;

    @DatabaseField
    private String currency;

    @DatabaseField
    private double rate;

    public CurrencyRate(){

    }

    public CurrencyRate(Date rateDate, String baseCurrency, String currency, double rate) {
        this.rateDate = rateDate;
        this.baseCurrency = baseCurrency;
        this.currency = currency;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date  getRateDate() {
        return rateDate;
    }

    public void setRateDate(Date rateDate) {
        this.rateDate = rateDate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRate() {
        return baseCurrency.equals(currency) ? 1 : rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }


    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rateDate);
        stringBuilder.append(" ");
        stringBuilder.append(baseCurrency);
        stringBuilder.append(" ");
        stringBuilder.append(currency);
        stringBuilder.append(" ");
        stringBuilder.append(rate);
        return stringBuilder.toString();
    }
}
