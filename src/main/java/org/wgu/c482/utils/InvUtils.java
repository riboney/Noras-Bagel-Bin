package org.wgu.c482.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InvUtils {

    public static <T> Optional<T> findOneByCondition(List<T> array, Predicate<T> condition) {
        return array
                .stream()
                .filter(condition)
                .findAny();
    }

    public static <T> List<T> findAnyByCondition(List<T> array, Predicate<T> condition) {
        return array
                .stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public static boolean isInteger(String query){
        return query.chars().allMatch(Character::isDigit);
    }
}
