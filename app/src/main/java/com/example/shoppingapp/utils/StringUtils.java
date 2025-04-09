package com.example.shoppingapp.utils;

import java.text.Normalizer;

public class StringUtils {
    /**
     * Normalizes the given text by removing all whitespace and punctuation,
     * converting it to lowercase, and stripping diacritical marks (accents).
     *
     * @param input The text to normalize.
     * @return The normalized text.
     */
    public static String normalizeText(String input) {
        if (input == null) {
            return "";
        }
        // Remove whitespace and punctuation.
        String normalized = input.replaceAll("[\\s\\p{Punct}]", "");
        // Convert to lowercase.
        normalized = normalized.toLowerCase();
        // Normalize the string to decompose accented characters.
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        // Remove diacritical marks.
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized;
    }
}
