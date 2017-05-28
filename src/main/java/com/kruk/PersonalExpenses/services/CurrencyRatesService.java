package com.kruk.PersonalExpenses.services;

import com.google.gson.Gson;
import com.kruk.PersonalExpenses.dao.CurrencyRatesDAO;
import com.kruk.PersonalExpenses.model.CurrencyRate;
import com.kruk.PersonalExpenses.model.Expense;
import com.kruk.PersonalExpenses.model.fixerAPI.CurrencyRatesAPI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Vova on 23.05.2017.
 */
public class CurrencyRatesService {

    private CurrencyRatesDAO currencyRatesDAO;

    public CurrencyRatesService() throws SQLException {
        this.currencyRatesDAO = new CurrencyRatesDAO();
    }

    public CurrencyRatesService(CurrencyRatesDAO currencyRatesDAO) {
        this.currencyRatesDAO = currencyRatesDAO;
    }

    public List<CurrencyRate> getAll() throws SQLException {
        return currencyRatesDAO.getAll();
    }

    public void addCurrencyRate(CurrencyRate currencyRate) throws SQLException {
        currencyRatesDAO.addCurrencyRate(currencyRate);
    }

    public CurrencyRatesAPI getCurrencyRatesAPIonDate(String date,String currency){

        CurrencyRatesAPI currencyRatesAPI = null;

        Gson gson = new Gson();
        HttpClient client = new DefaultHttpClient();

        java.net.URI uri = null;
        try {
            uri = new URIBuilder("https://api.fixer.io/"+date+"?base="+currency)
                    .addParameter("limit", "15").addParameter("offset", "0")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpGet get = new HttpGet(uri);

        HttpClientContext context = HttpClientContext.create();

        HttpResponse response = null;
        String responseBody = null;
        try {
            response = client.execute(get, context);
            HttpEntity entity = response.getEntity();
            responseBody = EntityUtils.toString(entity);
        }catch (IOException e){
            e.printStackTrace();
        }

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            currencyRatesAPI = gson.fromJson(responseBody,
                    CurrencyRatesAPI.class);
        }

        return  currencyRatesAPI;
    }

    public boolean updateCurrencyRates(Date date, String currency) throws IllegalAccessException, SQLException, NoSuchFieldException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = df.format(date);

        CurrencyRatesAPI currencyRatesAPI = getCurrencyRatesAPIonDate(strDate,currency.toUpperCase());
        if(currencyRatesAPI == null) return false;
        List<CurrencyRate> currencyRateList = currencyRatesAPI.getCurrencyRatesList();

        for (CurrencyRate currencyRate : currencyRateList) {
            addCurrencyRate(currencyRate);
        }
        return true;
    }

    public List<CurrencyRate> getLastRateByCurrencyOnDate(String currency, Date date) throws SQLException {
        return currencyRatesDAO.getLastRateByCurrencyOnDate(currency,date);
    }

    public double getRate(String baseCurrency, String currency, Date date) throws SQLException, NoSuchFieldException, IllegalAccessException {
        List<CurrencyRate> currencyRateList = currencyRatesDAO.getRateOnDate(baseCurrency,date);//getLastRateByCurrencyOnDate(baseCurrency,date);
        if (currencyRateList.isEmpty()){
            if(!updateCurrencyRates(date,baseCurrency)) return 1;
            currencyRateList = getLastRateByCurrencyOnDate(baseCurrency,date);
            if (currencyRateList.isEmpty()) return 1;
        }

        CurrencyRate currencyRate = currencyRatesDAO.getLastCurrencyRate(baseCurrency, currency, date);

        if (currencyRate==null) return 1;

        return currencyRate.getRate();
    }

    public CurrencyRate findCurrencyRateInList(List<CurrencyRate> currencyRateList, String currency){
        return currencyRateList.stream()
                .filter(currencyRate->currencyRate.getCurrency().equals(currency))
                .findFirst().get();
    }

    public double calcTotalAmountInCurrency(List<Expense> list, String baseCurrency) throws NoSuchFieldException, IllegalAccessException, SQLException {
        double totalAmount = 0;
        for (Expense expense:list){
            totalAmount += expense.getAmount()*getRate(expense.getCurency(),baseCurrency,expense.getDate());
        }
        return totalAmount;
    }
}
