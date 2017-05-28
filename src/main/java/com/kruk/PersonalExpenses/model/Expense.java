package com.kruk.PersonalExpenses.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vova on 22.05.2017.
 */
@DatabaseTable(tableName = "expenses")
public class Expense implements Comparable<Expense> {

    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "exp_date")
    private Date date;

    @DatabaseField
    private double amount;

    @DatabaseField
    private String curency;

    @DatabaseField(columnName = "product_name")
    private String productName;

    public Expense(){

    }

    public Expense(Date date, double amount, String curency, String productName) {
        this.date = date;
        this.amount = amount;
        this.curency = curency;
        this.productName = productName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurency() {
        return curency;
    }

    public void setCurency(String curency) {
        this.curency = curency;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(df.format(date));
        stringBuilder.append(" ");
        stringBuilder.append(productName);
        stringBuilder.append(" ");
        stringBuilder.append(NumberFormat.getInstance().format(amount));
        stringBuilder.append(" ");
        stringBuilder.append(curency);

        return stringBuilder.toString();
    }

    @Override
    public int compareTo(Expense o) {
        if(getDate()==null||o.getDate()==null) return 0;
        return getDate().compareTo(o.getDate());
    }
}
