package me.jonasjones.mcwebserver.web.api.v2.tokenmgr;

import me.jonasjones.mcwebserver.McWebserver;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;

public class TokenSaveManager {
    private static final String TOKEN_FILE_PATH = String.valueOf(FabricLoader.getInstance().getConfigDir()) + "/mcwebserver_tokens.txt";

    public static Boolean isExpired(Token token) {
        if (token.getExpires() == 0) {
            return false;
        }
        return token.getExpires() < Instant.now().getEpochSecond();
    }
    public static ArrayList<Token> readTokensFromFile() {
        ArrayList<Token> tokenList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TOKEN_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4 && !parts[0].equals("null")) {
                    String name = parts[0];
                    String token = parts[1];
                    String tokenStart = parts[2];
                    long expires = Long.parseLong(parts[3]);

                    Token tokenObj = new Token(name, token, tokenStart, expires);

                    if (!isExpired(tokenObj)) {
                        tokenList.add(tokenObj);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            McWebserver.LOGGER.error("Error reading tokens from file: " + e.getMessage());
        }

        return tokenList;
    }

    public static void writeTokensToFile(ArrayList<Token> tokenList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TOKEN_FILE_PATH))) {
            for (Token token : tokenList) {
                String line = token.getName() + "|" + token.getTokenHash() + "|" + token.getTokenStart() + "|" + token.getExpires();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            McWebserver.LOGGER.error("Error writing tokens to file: " + e.getMessage());
        }
    }

    public static ArrayList<Token> readOrCreateTokenFile() {
        File file = new File(TOKEN_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                McWebserver.LOGGER.info("Created api token file.");
            } catch (IOException e) {
                McWebserver.LOGGER.error("Error creating api token file: " + e.getMessage());
            }
            return new ArrayList<>();
        } else {
            return readTokensFromFile();
        }
    }
}
