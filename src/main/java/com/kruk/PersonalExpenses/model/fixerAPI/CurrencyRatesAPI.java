package com.kruk.PersonalExpenses.model.fixerAPI;

import com.kruk.PersonalExpenses.model.CurrencyRate;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vova on 23.05.2017.
 */
public class CurrencyRatesAPI {

    private String base;

    private String date;

    private Rates rates;

    public String getBase() {
        return this.base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Date getDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date rateDate = null;

        try {
            rateDate = df.parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return rateDate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Rates getRates() {
        return this.rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    public class Rates {

        double AUD;

        double BGN;

        double BRL;

        double CAD;

        double CHF;

        double CNY;

        double CZK;

        double DKK;

        double GBP;

        double HKD;

        double HRK;

        double HUF;

        double IDR;

        double ILS;

        double INR;

        double JPY;

        double KRW;

        double MXN;

        double MYR;

        double NOK;

        double NZD;

        double PHP;

        double PLN;

        double RON;

        double RUB;

        double SEK;

        double SGD;

        double THB;

        double TRY;

        double USD;

        double ZAR;

        double EUR;

        public double getEUR() {
            return EUR;
        }

        public void setEUR(double EUR) {
            this.EUR = EUR;
        }

        public double getAUD() {
            return this.AUD;
        }

        public void setAUD(double AUD) {
            this.AUD = AUD;
        }

        public double getBGN() {
            return this.BGN;
        }

        public void setBGN(double BGN) {
            this.BGN = BGN;
        }

        public double getBRL() {
            return this.BRL;
        }

        public void setBRL(double BRL) {
            this.BRL = BRL;
        }

        public double getCAD() {
            return this.CAD;
        }

        public void setCAD(double CAD) {
            this.CAD = CAD;
        }

        public double getCHF() {
            return this.CHF;
        }

        public void setCHF(double CHF) {
            this.CHF = CHF;
        }

        public double getCNY() {
            return this.CNY;
        }

        public void setCNY(double CNY) {
            this.CNY = CNY;
        }

        public double getCZK() {
            return this.CZK;
        }

        public void setCZK(double CZK) {
            this.CZK = CZK;
        }

        public double getDKK() {
            return this.DKK;
        }

        public void setDKK(double DKK) {
            this.DKK = DKK;
        }

        public double getGBP() {
            return this.GBP;
        }

        public void setGBP(double GBP) {
            this.GBP = GBP;
        }

        public double getHKD() {
            return this.HKD;
        }

        public void setHKD(double HKD) {
            this.HKD = HKD;
        }

        public double getHRK() {
            return this.HRK;
        }

        public void setHRK(double HRK) {
            this.HRK = HRK;
        }

        public double getHUF() {
            return this.HUF;
        }

        public void setHUF(double HUF) {
            this.HUF = HUF;
        }

        public double getIDR() {
            return this.IDR;
        }

        public void setIDR(int IDR) {
            this.IDR = IDR;
        }

        public double getILS() {
            return this.ILS;
        }

        public void setILS(double ILS) {
            this.ILS = ILS;
        }

        public double getINR() {
            return this.INR;
        }

        public void setINR(double INR) {
            this.INR = INR;
        }

        public double getJPY() {
            return this.JPY;
        }

        public void setJPY(double JPY) {
            this.JPY = JPY;
        }

        public double getKRW() {
            return this.KRW;
        }

        public void setKRW(double KRW) {
            this.KRW = KRW;
        }

        public double getMXN() {
            return this.MXN;
        }

        public void setMXN(double MXN) {
            this.MXN = MXN;
        }

        public double getMYR() {
            return this.MYR;
        }

        public void setMYR(double MYR) {
            this.MYR = MYR;
        }

        public double getNOK() {
            return this.NOK;
        }

        public void setNOK(double NOK) {
            this.NOK = NOK;
        }

        public double getNZD() {
            return this.NZD;
        }

        public void setNZD(double NZD) {
            this.NZD = NZD;
        }

        public double getPHP() {
            return this.PHP;
        }

        public void setPHP(double PHP) {
            this.PHP = PHP;
        }

        public double getPLN() {
            return this.PLN;
        }

        public void setPLN(double PLN) {
            this.PLN = PLN;
        }

        public double getRON() {
            return this.RON;
        }

        public void setRON(double RON) {
            this.RON = RON;
        }

        public double getRUB() {
            return this.RUB;
        }

        public void setRUB(double RUB) {
            this.RUB = RUB;
        }

        public double getSEK() {
            return this.SEK;
        }

        public void setSEK(double SEK) {
            this.SEK = SEK;
        }

        public double getSGD() {
            return this.SGD;
        }

        public void setSGD(double SGD) {
            this.SGD = SGD;
        }

        public double getTHB() {
            return this.THB;
        }

        public void setTHB(double THB) {
            this.THB = THB;
        }

        public double getTRY() {
            return this.TRY;
        }

        public void setTRY(double TRY) {
            this.TRY = TRY;
        }

        public double getUSD() {
            return this.USD;
        }

        public void setUSD(double USD) {
            this.USD = USD;
        }

        public double getZAR() {
            return this.ZAR;
        }

        public void setZAR(double ZAR) {
            this.ZAR = ZAR;
        }
    }

    public List<CurrencyRate> getCurrencyRatesList() throws IllegalAccessException, NoSuchFieldException {

        List<CurrencyRate> currencyRatesList = new ArrayList<>();
        Date rateDate = getDate();

        Class[] classes = CurrencyRatesAPI.class.getDeclaredClasses();
        for(Class innerClass: classes){
            Field[] fields = innerClass.getDeclaredFields();
            for(Field field : fields){
                if(innerClass.getDeclaredField("this$0").equals(field)) continue;
                String currency = field.getName();
                double rate = (double)field.get(this.rates);
                CurrencyRate currencyRate = new CurrencyRate(rateDate, this.base, currency, rate);
                currencyRatesList.add(currencyRate);
            }
        }

        return currencyRatesList;
    }

}
