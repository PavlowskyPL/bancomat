package by.pkochubei.bankomatATM;

import java.util.Scanner;
import java.util.function.Function;

public class Menu implements Exception {

    private final RussianInterface russianInterface = new RussianInterface();
    private final EnglishInterface englishInterface = new EnglishInterface();

    public void menu() {
        System.out.println("===Choose language===\n===Выберите язык===");
        System.out.println("1 - English\n2 - Russian");
        switch (input(Integer::valueOf)) {
            case 1 -> englishInterface.englishInterface();
            case 2 -> russianInterface.russianInterface();
            default -> menu();
        }
    }

    @Override
    public <T> T input(Function<String, T> function) {
        Scanner scanner = new Scanner(System.in);
        try {
            return function.apply(scanner.nextLine());
        } catch (java.lang.Exception e) {
            return function.apply(String.valueOf(-1));
        }
    }
}
