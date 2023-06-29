package by.pkochubei.bankomatATM;

import java.util.ArrayList;
import java.util.Random;

public class ATM extends User {
    private User bob = new User(2323, "Bob", "Smith", "2688700261", "BigBob@gmail.com", 5670.29, 1500, CurrencyType.BYN);
    private User tom = new User(8712, "Tom", "Jones", "8622973048", "Jones@gmail.com", 2103.14, 500, CurrencyType.USD);
    protected ArrayList<User> users;

    public ATM() {
        super();
        users = new ArrayList<>();
        users.add(bob);
        users.add(tom);
    }

    //Проверка существует ли такой номер счета в списке банкомата
    protected boolean checkAccount(String accountNumber) {
        boolean isHave = false;
        for (User user : users) {
            if (user.getAccountNumber().equals(accountNumber)) {
                isHave = true;
                break;
            }
        }
        return isHave;
    }

    //Проверка правильный ли пин-код введен
    protected boolean checkPassword(int password) {
        boolean isHave = false;
        for (User user : users) {
            if (user.getPassword() == password) {
                isHave = true;
                break;
            }
        }
        return isHave;
    }

    //Генерация номера счета
    protected String generateAccountNumber() {
        Random random = new Random();
        return Long.toString(random.nextLong(1000000000L, 9999999999L));
    }

    //Генерация пин-кода
    protected int generatePassword() {
        Random random = new Random();
        return random.nextInt(1000, 9999);
    }

    //Добавление нового пользователя в список
    protected void createNewUser(String firstName, String lastName, String mail, String account, int password) {
        users.add(new User(password, firstName, lastName, account, mail, 0, 1000, CurrencyType.BYN));
    }

    //Конвертирует баланс в ту валюту, которую выбрал пользователь
    protected void changeTheBalanceDependingOnTheTypeOfCurrency(User user, CurrencyType currencyType) {
        user.setBalance(user.getBalance()/(exchangeRate(currencyType)/exchangeRate(user.getCurrencyType())));
        user.setCurrencyType(currencyType);
    }

    //Возвращает курс валюты в зависимости какой тип валюты установлен
    protected double exchangeRate(CurrencyType type) {
        double usd = 2.925;
        double eur = 3.1324;
        double rub = 0.036414;
        double byn = 1;
        if (type.equals(CurrencyType.USD))
            return usd;
        else if (type.equals(CurrencyType.EUR))
            return eur;
        else if (type.equals(CurrencyType.RUB))
            return rub;
        else
            return byn;
    }

    @Override
    public String toString() {
        return "ATM{" +
                "users=" + users +
                '}';
    }
}
