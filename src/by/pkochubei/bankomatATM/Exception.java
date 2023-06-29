package by.pkochubei.bankomatATM;

import java.util.function.Function;

public interface Exception {
    <T> T input(Function<String , T> function);
}
