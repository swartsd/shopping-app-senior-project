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

        // Remove emojis by matching common Unicode emoji ranges.
        normalized = normalized.replaceAll("[\\x{1F600}-\\x{1F64F}]", ""); // Emoticons
        normalized = normalized.replaceAll("[\\x{1F300}-\\x{1F5FF}]", ""); // Misc Symbols and Pictographs
        normalized = normalized.replaceAll("[\\x{1F680}-\\x{1F6FF}]", ""); // Transport & Map symbols
        normalized = normalized.replaceAll("[\\x{2600}-\\x{26FF}]", "");   // Misc symbols
        normalized = normalized.replaceAll("[\\x{2700}-\\x{27BF}]", "");   // Dingbats

        // Convert to lowercase.
        normalized = normalized.toLowerCase();
        // Normalize the string to decompose accented characters.
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        // Remove diacritical marks.
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized;
    }
}
