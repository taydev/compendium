package dev.compendium.core.util;

import java.util.ArrayList;
import java.util.List;

// TODO: write documentation
public class ElementUtils {

    public static String abbreviate(String input) {
        if (input.length() <= 1) {
            return input;
        }
        String[] chunks = input.split(" ");
        StringBuilder abbreviation = new StringBuilder();
        for (String chunk : chunks) {
            abbreviation.append(chunk.charAt(0));
        }
        return abbreviation.toString().trim();
    }

    public static <T> List<T> matchingListFilter(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>();
        for (T t : a) {
            if (b.contains(t)) {
                result.add(t);
            }
        }
        return result;
    }
}
