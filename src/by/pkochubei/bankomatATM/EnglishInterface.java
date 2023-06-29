package by.pkochubei.bankomatATM;

import java.util.Scanner;
import java.util.function.Function;

public class EnglishInterface extends ATM implements Exception {
    Scanner scanner = new Scanner(System.in);

    public void englishInterface() {
        System.out.println("===Are you a client of our bank?===");
        System.out.println("1 - Yes\n2 - No");
        switch (input(Integer::valueOf)) {
            case 1 -> loginAccount();
            case 2 -> createAccount();
            default -> {
                System.out.println("Invalid input!");
                englishInterface();
            }
        }
    }

    //Вход в аккаунт
    public void loginAccount() {
        System.out.println("===Enter your personal account number===");
        String accountNumber = scanner.nextLine();
        if (checkAccount(accountNumber)) {
            System.out.println("===Enter PIN===");
            for (int i = 2; i >= 0; i--) {
                if (!checkPassword(input(Integer::valueOf))) {
                    if (i == 0) {
                        System.out.println("Account blocked! You have entered an incorrect PIN code 3 times");
                        break;
                    }
                    System.out.println("Wrong PIN entered! " + i + " attempts left");
                } else {
                    Operations(accountNumber);
                    break;
                }
            }
        } else {
            System.out.println("User not found");
        }
    }

    //Создание аккаунта для нового пользователя
    public void createAccount() {
        System.out.println("===To create an account, you must enter personal data===");
        System.out.println("===Enter your name===");
        String firstName = scanner.nextLine();
        System.out.println("===Enter last name===");
        String lastName = scanner.nextLine();
        System.out.println("===Enter your email address===");
        String mail = scanner.nextLine();
        String account = generateAccountNumber();
        int password = generatePassword();
        createNewUser(firstName, lastName, mail, account, password);
        for (User user : users) {
            if (user.getAccountNumber().equals(account)) {
                System.out.println(
                        "Your name: " + user.getFirstName() +
                                "\nYour last name: " + user.getLastName() +
                                "\nYour email: " + user.getMail() +
                                "\nYour bank account number: " + user.getAccountNumber() +
                                "\nYour PIN: " + user.getPassword() + " Do not show your PIN code to anyone! This is your personal information!");
                Operations(user.getAccountNumber());
            }
        }
    }

    //Операции со счетом
    public void Operations(String accountNumber) {
        System.out.println("===Operations on the account===");
        System.out.println(
                """
                        1 - View balance
                        2 - Cash withdrawal
                        3 - Balance replenishment
                        4 - Execute translation
                        5 - Change pin code
                        6 - Change email address
                        7 - Changing limits
                        8 - Account report
                        0 - Exit""");
        switch (input(Integer::valueOf)) {
            case 1 -> {
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        setCurrency(user);
                        System.out.println("Your balance: " + String.format("%.2f", user.getBalance()) + " " + user.getCurrencyType());
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
                        System.out.print("Enter the amount to withdraw cash: ");
                        int sum = input(Integer::valueOf);
                        if (sum < 0) {
                            System.out.println("Invalid input!");
                            break;
                        } else if (sum <= user.getLimit()) {
                            if (sum <= user.getBalance()) {
                                user.setBalance(user.getBalance() - sum);
                                setReportList(sum, TypeTransaction.WITHDRAWAL, user.getCurrencyType());
                                System.out.println("After the operation, your balance is: " + String.format("%.2f", user.getBalance()) + " " + user.getCurrencyType());
                                accountBalanceTooLow(user);
                            } else {
                                System.out.println("Operation is not possible. There are not enough funds on the account");
                            }
                        } else {
                            System.out.println("Operation is not possible. The amount exceeds your current limit.");
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
                        System.out.print("Enter the amount to replenish the account: ");
                        int sum = input(Integer::valueOf);
                        if (sum < 0) {
                            System.out.println("Invalid input!");
                            break;
                        }
                        if (sum <= user.getLimit()) {
                            user.setBalance(user.getBalance() + sum);
                            setReportList(sum, TypeTransaction.REPLENISHMENT, user.getCurrencyType());
                            System.out.println("After the operation, your balance is: " + String.format("%.2f", user.getBalance()));
                        } else {
                            System.out.println("Operation is not possible. The amount exceeds your current limit.");
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
                        System.out.print("Enter the account number to which you want to transfer: ");
                        String otherAccountNumber = scanner.nextLine();
                        for (User otherUser : users) {
                            if (otherUser.getAccountNumber().equals(otherAccountNumber)) {
                                isHave = true;
                                System.out.println("Information about the account to which the transfer will be made");
                                System.out.println("Name: " + otherUser.getFirstName());
                                System.out.println("Surname: " + otherUser.getLastName());
                                System.out.println("Account number: " + otherUser.getAccountNumber());
                                System.out.println(
                                        """
                                                Do you really want a transfer made to this account?
                                                1 - Yes
                                                2 - No""");
                                switch (input(Integer::valueOf)) {
                                    case 1 -> {
                                        setCurrency(user);
                                        changeTheBalanceDependingOnTheTypeOfCurrency(otherUser, user.getCurrencyType());
                                        System.out.print("Enter amount to transfer: ");
                                        int sum = input(Integer::valueOf);
                                        if (user.getBalance() >= sum && sum > 0) {
                                            if (sum <= user.getLimit()) {
                                                user.setBalance(user.getBalance() - sum);
                                                otherUser.setBalance(otherUser.getBalance() + sum);
                                                System.out.println("After the operation, your balance is: " + String.format("%.2f", user.getBalance()));
                                                accountBalanceTooLow(user);
                                                setReportList(sum, TypeTransaction.TRANSFER, user.getCurrencyType());
                                            } else {
                                                System.out.println("Operation is not possible. The amount exceeds your current limit.");
                                            }
                                        } else {
                                            System.out.println("Invalid input!");
                                        }
                                    }
                                    case 2 -> System.out.println("Operation cancelled!");
                                }
                                break;
                            }
                        }
                        if (!isHave) {
                            System.out.println("No user found with this account number!");
                        }
                    }
                }
                Operations(accountNumber);
            }
            case 5 -> {
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        System.out.print("Enter a valid pin code: ");
                        int nowPin = input(Integer::valueOf);
                        if (user.getPassword() == nowPin) {
                            System.out.print("Enter a new pin code: ");
                            int firstPin = input(Integer::valueOf);
                            if (firstPin < 0) {
                                System.out.println("Invalid input!");
                            }
                            if (firstPin > 9999) {
                                System.out.println("Invalid input. PIN must be 4 characters long");
                                break;
                            }
                            System.out.print("Repeat action: ");
                            int secondPin = input(Integer::valueOf);
                            if (secondPin < 0) {
                                System.out.println("Invalid input!");
                            }
                            if (firstPin == secondPin) {
                                user.setPassword(secondPin);
                                System.out.println("Your new pin: " + user.getPassword());
                            } else {
                                System.out.println("PIN code does not match");
                            }
                        } else if (nowPin < 0) {
                            System.out.println("Invalid input!");
                        } else {
                            System.out.println("PIN code is incorrect!");
                        }
                        break;
                    }
                }
                Operations(accountNumber);
            }
            case 6 -> {
                System.out.print("Enter your new email address: ");
                String mail = scanner.nextLine();
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        user.setMail(mail);
                        System.out.println("Your new email address: " + user.getMail());
                        break;
                    }
                }
                Operations(accountNumber);
            }
            case 7 -> {
                for (User user : users) {
                    if (user.getAccountNumber().equals(accountNumber)) {
                        System.out.println("Your limit: " + user.getLimit());
                        System.out.print("Enter a new limit: ");
                        int newLimit = input(Integer::valueOf);
                        if (newLimit > 0) {
                            user.setLimit(newLimit);
                            System.out.println("After the operation, your new limit: " + user.getLimit());
                        } else {
                            System.out.println("Invalid input!");
                        }
                        break;
                    }
                }
                Operations(accountNumber);
            }
            case 8 -> {
                System.out.println("Date / Amount / Type of transaction");
                getReportList();
                Operations(accountNumber);
            }
            case 0 -> System.out.println("Goodbye!");
            default -> {
                System.out.println("Invalid input! Try again");
                Operations(accountNumber);
            }
        }

    }

    protected void setCurrency(User user) {
        System.out.println("===Select currency type===");
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
            default -> System.out.println("Invalid input!");
        }
    }

    protected void accountBalanceTooLow(User user) {
        if (user.getBalance() < 100) {
            System.out.println("Your balance is critically low!!!");
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