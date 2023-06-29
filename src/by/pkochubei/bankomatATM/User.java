package by.pkochubei.bankomatATM;

import java.util.ArrayList;
import java.util.Date;


public class User {
    private int password;
    private String firstName;
    private String lastName;
    private String accountNumber;
    private String mail;
    private double balance;
    private int limit;
    private ArrayList<String> reportList;
    private CurrencyType currencyType;



    public User(int password, String firstName, String lastName, String accountNumber, String mail, double balance, int limit, CurrencyType currencyType) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountNumber = accountNumber;
        this.mail = mail;
        this.balance = balance;
        this.limit = limit;
        this.currencyType = currencyType;
        reportList = new ArrayList<>();
    }

    protected User() {
        reportList = new ArrayList<>();
    }

    protected void setReportList(int sum, TypeTransaction transaction, CurrencyType currencyType){
        reportList.add(new Date()+" / "+sum+" "+currencyType+" / "+transaction.toString());
    }

    protected void getReportList(){
        for (String list: reportList){
            System.out.println(list);
        }
    }

    protected void setPassword(int password) {
        this.password = password;
    }

    protected void setMail(String mail) {
        this.mail = mail;
    }

    protected int getPassword() {
        return password;
    }

    protected String getFirstName() {
        return firstName;
    }

    protected String getLastName() {
        return lastName;
    }

    protected String getAccountNumber() {
        return accountNumber;
    }

    protected String getMail() {
        return mail;
    }

    protected double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    protected int getLimit() {
        return limit;
    }

    protected void setLimit(int limit) {
        this.limit = limit;
    }

    protected CurrencyType getCurrencyType() {
        return currencyType;
    }

    protected void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

    @Override
    public String toString() {
        return "User{" +
                "password=" + password +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accountNumber=" + accountNumber +
                ", mail='" + mail + '\'' +
                '}';
    }
}
