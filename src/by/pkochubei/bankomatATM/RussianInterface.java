package by.pkochubei.bankomatATM;

import java.util.Scanner;
import java.util.function.Function;

public class RussianInterface extends ATM implements Exception {
    Scanner scanner = new Scanner(System.in);

    public void russianInterface() {
        System.out.println("===Вы являетесь клиентом нашего банка?===");
        System.out.println("1 - Да\n2 - Нет");
        switch (input(Integer::valueOf)) {
            case 1 -> loginAccount();
            case 2 -> createAccount();
            default -> {
                System.out.println("Некорректный ввод!");
                russianInterface();
            }
        }
    }

    //Вход в аккаунт
    public void loginAccount() {
        System.out.println("===Введите номер лицевого счета===");
        String accountNumber = scanner.nextLine();
        if (checkAccount(accountNumber)) {
            System.out.println("===Введите пин-код===");
            for (int i = 2; i >= 0; i--) {
                if (!checkPassword(input(Integer::valueOf))) {
                    if (i == 0) {
                        System.out.println("Счет заблокирован! Вы ввели 3 раза неверный пин-код");
                        break;
                    }
                    System.out.println("Введен неверный пин-код! Осталось " + i + " попытки");
                } else {
                    Operations(accountNumber);
                    break;
                }
            }
        } else {
            System.out.println("Пользователя не найдено");
        }
    }

    //Создание аккаунта для нового пользователя
    public void createAccount() {
        System.out.println("===Для создания аккаунта необходимо ввести персональные данные===");
        System.out.println("===Введите имя===");
        String firstName = scanner.nextLine();
        System.out.println("===Введите фамилию===");
        String lastName = scanner.nextLine();
        System.out.println("===Введите адрес электронной почты===");
        String mail = scanner.nextLine();
        String account = generateAccountNumber();
        int password = generatePassword();
        createNewUser(firstName, lastName, mail, account, password);
        for (User user : users) {
            if (user.getAccountNumber().equals(account)) {
                System.out.println(
                        "Ваше имя: " + user.getFirstName() +
                                "\nВаша фамилия: " + user.getLastName() +
                                "\nВаша электронная почта: " + user.getMail() +
                                "\nВаш номер банковского счета: " + user.getAccountNumber() +
                                "\nВаш пин-код: " + user.getPassword() + " Никому не показывайте ваш пин-код! Это ваша личная информация!");
                Operations(user.getAccountNumber());
            }
        }
    }

    //Операции со счетом
    public void Operations(String accountNumber) {
        System.out.println("===Операции над счетом===");
        System.out.println(
                """
                        1 - Просмотреть баланс
                        2 - Снятие наличных
                        3 - Пополнение баланса
                        4 - Выполнить перевод
                        5 - Изменить пин-код
                        6 - Изменить адрес электронной почты
                        7 - Изменение лимитов
                        8 - Отчет по счету
                        0 - Выход""");
        switch (input(Integer::valueOf)) {
            case 1 -> {
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        setCurrency(user);
                        System.out.println("Ваш баланс: " + String.format("%.2f", user.getBalance()) + " " + user.getCurrencyType());
                        accountBalanceTooLow(user);
                        break;
                    }
                }
                Operations(accountNumber);
            }
            case 2 -> {
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        setCurrency(user);
                        System.out.print("Введите сумму для снятия наличных: ");
                        int sum = input(Integer::valueOf);
                        if (sum < 0) {
                            System.out.println("Некорректный ввод!");
                            break;
                        } else if (sum <= user.getLimit()) {
                            if (sum <= user.getBalance()) {
                                user.setBalance(user.getBalance() - sum);
                                setReportList(sum, TypeTransaction.WITHDRAWAL, user.getCurrencyType());
                                System.out.println("После операции ваш баланс составляет: " + String.format("%.2f", user.getBalance()) + " " + user.getCurrencyType());
                                accountBalanceTooLow(user);
                            } else {
                                System.out.println("Операция невозможна. На счете недостаточно средств");
                            }
                        } else {
                            System.out.println("Операция невозможна. Сумма превышает ваш текущий лимит.");
                        }
                        break;
                    }
                }
                Operations(accountNumber);
            }
            case 3 -> {
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        setCurrency(user);
                        System.out.print("Введите сумму для пополнения счета: ");
                        int sum = input(Integer::valueOf);
                        if (sum < 0) {
                            System.out.println("Некорректный ввод!");
                            break;
                        }
                        if (sum <= user.getLimit()) {
                            user.setBalance(user.getBalance() + sum);
                            setReportList(sum, TypeTransaction.REPLENISHMENT, user.getCurrencyType());
                            System.out.println("После операции ваш баланс составляет: " + String.format("%.2f", user.getBalance()));
                        } else {
                            System.out.println("Операция невозможна. Сумма превышает ваш текущий лимит.");
                        }
                        break;
                    }
                }
                Operations(accountNumber);
            }
            case 4 -> {
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        boolean isHave = false;
                        System.out.print("Введите номер счета на который хотите сделать перевод: ");
                        String otherAccountNumber = scanner.nextLine();
                        for (User otherUser : users) {
                            if (otherUser.getAccountNumber().equals(otherAccountNumber)) {
                                isHave = true;
                                System.out.println("Информация об аккаунте на который будет сделан перевод");
                                System.out.println("Имя: " + otherUser.getFirstName());
                                System.out.println("Фамилия: " + otherUser.getLastName());
                                System.out.println("Номер счета: " + otherUser.getAccountNumber());
                                System.out.println(
                                        """
                                                Вы действительно хотите сделан перевод на этот счет?
                                                1 - Да
                                                2 - Нет""");
                                switch (input(Integer::valueOf)) {
                                    case 1 -> {
                                        setCurrency(user);
                                        changeTheBalanceDependingOnTheTypeOfCurrency(otherUser, user.getCurrencyType());
                                        System.out.print("Введите сумму для перевода: ");
                                        int sum = input(Integer::valueOf);
                                        if (user.getBalance() >= sum && sum > 0) {
                                            if (sum <= user.getLimit()) {
                                                user.setBalance(user.getBalance() - sum);
                                                otherUser.setBalance(otherUser.getBalance() + sum);
                                                System.out.println("После операции ваш баланс составляет: " + String.format("%.2f", user.getBalance()));
                                                accountBalanceTooLow(user);
                                                setReportList(sum, TypeTransaction.TRANSFER, user.getCurrencyType());
                                            } else {
                                                System.out.println("Операция невозможна. Сумма превышает ваш текущий лимит.");
                                            }
                                        } else {
                                            System.out.println("Некорректный ввод!");
                                        }
                                    }
                                    case 2 -> System.out.println("Операция отменена!");
                                }
                                break;
                            }
                        }
                        if (!isHave) {
                            System.out.println("Пользователя с таким номер счета не найдено!");
                        }
                    }
                }
                Operations(accountNumber);
            }
            case 5 -> {
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        System.out.print("Введите действующий пин-код: ");
                        int nowPin = input(Integer::valueOf);
                        if (user.getPassword() == nowPin) {
                            System.out.print("Введите новый пин-код: ");
                            int firstPin = input(Integer::valueOf);
                            if (firstPin < 0) {
                                System.out.println("Некорректный ввод!");
                            }
                            if (firstPin > 9999) {
                                System.out.println("Некорректный ввод. Пин-код должен состоять из 4 символов");
                                break;
                            }
                            System.out.print("Повторите действие: ");
                            int secondPin = input(Integer::valueOf);
                            if (secondPin < 0) {
                                System.out.println("Некорректный ввод!");
                            }
                            if (firstPin == secondPin) {
                                user.setPassword(secondPin);
                                System.out.println("Ваш новый пин-код: " + user.getPassword());
                            } else {
                                System.out.println("Пин-код не совпадает");
                            }
                        } else if (nowPin < 0) {
                            System.out.println("Некорректный ввод!");
                        } else {
                            System.out.println("Пин-код неверен!");
                        }
                        break;
                    }
                }
                Operations(accountNumber);
            }
            case 6 -> {
                System.out.print("Введите ваш новый адрес электронной почты: ");
                String mail = scanner.nextLine();
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        user.setMail(mail);
                        System.out.println("Ваш новый адрес электронной почты: " + user.getMail());
                        break;
                    }
                }
                Operations(accountNumber);
            }
            case 7 -> {
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        System.out.println("Ваш лимит: " + user.getLimit());
                        System.out.print("Введите новый лимит: ");
                        int newLimit = input(Integer::valueOf);
                        if (newLimit > 0) {
                            user.setLimit(newLimit);
                            System.out.println("После операции ваш новый лимит: " + user.getLimit());
                        } else {
                            System.out.println("Некорректный ввод!");
                        }
                        break;
                    }
                }
                Operations(accountNumber);
            }
            case 8 -> {
                System.out.println("Дата / Сумма / Тип транзакции");
                getReportList();
                Operations(accountNumber);
            }
            case 0 -> System.out.println("Досвидания!");
            default -> {
                System.out.println("Некорректный ввод! Повтотрите попытку");
                Operations(accountNumber);
            }
        }

    }

    protected void setCurrency(User user) {
        System.out.println("===Выберите тип валюты===");
        System.out.println(
                """
                        1 - BYN
                        2 - USD
                        3 - EUR
                        4 - RUB
                        """);
        switch (input(Integer::valueOf)) {
            case 1 -> changeTheBalanceDependingOnTheTypeOfCurrency(user, CurrencyType.BYN);
            case 2 -> changeTheBalanceDependingOnTheTypeOfCurrency(user, CurrencyType.USD);
            case 3 -> changeTheBalanceDependingOnTheTypeOfCurrency(user, CurrencyType.EUR);
            case 4 -> changeTheBalanceDependingOnTheTypeOfCurrency(user, CurrencyType.RUB);
            default -> System.out.println("Некорректный ввод!");
        }
    }

    protected void accountBalanceTooLow(User user) {
        if (user.getBalance() < 100) {
            System.out.println("Ваш баланс критически низкий!!!");
        }
    }

    //Функция проверяющая ввод на корректность
    @Override
    public <T> T input(Function<String, T> function) {
        try {
            return function.apply(scanner.nextLine());
        } catch (java.lang.Exception e) {
            return function.apply(String.valueOf(-1));
        }
    }
}
