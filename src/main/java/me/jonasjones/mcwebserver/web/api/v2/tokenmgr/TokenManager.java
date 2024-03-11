package me.jonasjones.mcwebserver.web.api.v2.tokenmgr;

import lombok.Getter;
import me.jonasjones.mcwebserver.McWebserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.jonasjones.mcwebserver.web.api.v2.tokenmgr.TokenSaveManager.*;

public class TokenManager {
    @Getter
    private static ArrayList<Token> tokens = new ArrayList<>();

    public static String hashString(String input) throws NoSuchAlgorithmException {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Update the digest with the input string
            byte[] hashedBytes = digest.digest(input.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private static String generateToken() {
        try {
            // Generate random bytes using SecureRandom
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[16]; // 16 bytes for a 128-bit hash
            secureRandom.nextBytes(randomBytes);

            return hashString(new String(randomBytes));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static long convertExpirationDate(String expiresIn) {
        if (expiresIn == null || expiresIn.equals("0")) {
            return 0;
        }
        try {
            long timestamp = Long.parseLong(expiresIn);
            if (timestamp > Instant.now().getEpochSecond()) {
                // The input is already a future timestamp
                return timestamp;
            }
        } catch (NumberFormatException ignored) {
            // Input is not a valid timestamp, continue to parse the duration format
        }
        // Pattern to match the duration format (XyXdXh)
        Pattern pattern = Pattern.compile("(\\d+)?[yY]?(\\d+)?[dD]?(\\d+)?[hH]?");
        Matcher matcher = pattern.matcher(expiresIn);

        int years = 0, days = 0, hours = 0;

        // Check if the input matches the pattern
        if (matcher.matches()) {
            String yearsStr = matcher.group(1);
            String daysStr = matcher.group(2);
            String hoursStr = matcher.group(3);

            // Parse and add the corresponding values
            years = yearsStr != null ? Integer.parseInt(yearsStr) : 0;
            days = daysStr != null ? Integer.parseInt(daysStr) : 0;
            hours = hoursStr != null ? Integer.parseInt(hoursStr) : 0;
        }

        // Calculate the future timestamp based on the current timestamp and the parsed values
        long currentTimestamp = Instant.now().getEpochSecond();
        long futureTimestamp = currentTimestamp + (years * 365 * 24 * 60 * 60) + (days * 24 * 60 * 60) + (hours * 60 * 60);

        return futureTimestamp;
    }

    public static String convertToHumanReadable(long unixTimestamp) {
        if (unixTimestamp == 0) {
            return "Never";
        }
        // Convert Unix timestamp to LocalDateTime
        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Define a format for the human-readable date-time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime to a string
        return localDateTime.format(formatter) + " (UTC)";
    }

    public static String registerToken(String name, String expires) {
        tokens = readTokensFromFile();
        // check if token already exists
        for (Token tokenObj : tokens) {
            if (tokenObj.getName().equals(name)) {

                return "exists";
            }
        }
        try {
            String token = generateToken();
            String tokenStart = token.substring(0, 5);
            Token tokenObj = new Token(name, hashString(token), tokenStart, convertExpirationDate(expires));
            tokens.add(tokenObj);
            writeTokensToFile(tokens);
            return token;
        } catch (Exception e) {
            McWebserver.LOGGER.error("Error generating token: " + e.getMessage());
        }
        return "failed";
    }

    public static String listTokens() {
        tokens = readTokensFromFile();
        StringBuilder sb = new StringBuilder();
        if (tokens.size() == 0) {
            return "No active tokens.";
        }
        int longestName = 0;
        int longestExpires = 0;
        for (Token token : tokens) {
            if (token.getName().length() > longestName) {
                longestName = token.getName().length();
            }
            if (convertToHumanReadable(token.getExpires()).length() > longestExpires) {
                longestExpires = convertToHumanReadable(token.getExpires()).length();
            }
        }
        sb.append("Active Tokens:\n");
        sb.append("Name")
                .append(" ".repeat(Math.max(longestName - 4, 0)))
                .append(" | ").append("Expires")
                .append(" ".repeat(Math.max(longestExpires - 6, 0)))
                .append(" | TokenStart\n");
        sb.append("-".repeat(Math.max(longestName, 4)))
                .append(" | ")
                .append("-".repeat(Math.max(longestExpires, 7)))
                .append(" | ")
                .append("-".repeat(10))
                .append("\n");
        for (Token token : tokens) {
            String humanExpires = convertToHumanReadable(token.getExpires());
            sb.append(token.getName())
                    .append(" ".repeat(Math.max(longestName - token.getName().length(), 4 - token.getName().length())))
                    .append(" | ").append(humanExpires).append(" ".repeat(Math.max(longestExpires - humanExpires.length(), 7 - humanExpires.length())))
                    .append(" | ").append(token.getTokenStart()).append("...").append("\n");
        }
        return sb.toString();
    }

    public static Boolean deleteToken(String name) {
        tokens = readTokensFromFile();
        for (Token token : tokens) {
            if (token.getName().equals(name)) {
                tokens.remove(token);
                writeTokensToFile(tokens);
                return true;
            }
        }
        return false;
    }

    public static String getToken(String name) {
        tokens = readTokensFromFile();
        for (Token token : tokens) {
            if (token.getName().equals(name)) {
                return token.getTokenHash();
            }
        }
        return null;
    }

    public static String[] getTokenNames() {
        tokens = readTokensFromFile();
        String[] tokenNames = new String[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            tokenNames[i] = tokens.get(i).getName();
        }
        return tokenNames;
    }

    public static Boolean isTokenValid(String token) throws NoSuchAlgorithmException {
        tokens = readTokensFromFile();
        for (Token tokenObj : tokens) {
            if (tokenObj.getTokenHash().equals(hashString(token))) {
                if (tokenObj.getExpires() == 0) {
                    return true;
                }
                if (tokenObj.getExpires() > Instant.now().getEpochSecond()) {
                    return true;
                }
            }
        }
        return false;
    }
}
