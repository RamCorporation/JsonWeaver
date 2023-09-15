package ramgames.jsonweaver;

import ramgames.jsonweaver.exceptions.SyntaxException;
import ramgames.jsonweaver.objects.JsonArray;
import ramgames.jsonweaver.objects.JsonHash;
import ramgames.jsonweaver.objects.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface JsonWeaver {

    int INT_TYPE = 1;
    int DOUBLE_TYPE = 2;
    int BOOLEAN_TYPE = 3;
    int STRING_TYPE = 4;
    int ARRAY_TYPE = 5;
    int OBJECT_TYPE = 6;
    
    static JsonObject read(File file) throws FileNotFoundException {
        return read(Tokenizer.readFile(file));
    }
    static JsonObject read(String string) {
        return process(Tokenizer.tokenizeString(string));
    }

    private static JsonObject process(List<String> tokens) {
        if (tokens.get(0).equals("{")) return processHash(tokens);
        return processArray(tokens);
    }
    private static JsonHash processHash(List<String> tokens) {
        JsonHash parentHash = new JsonHash();
        Set<Expectations> expected = Set.of(Expectations.OPEN_CURLY);

        String key = null;
        int index = 0;
        int indents = 0;
        List<String> reservedTokens = new ArrayList<>();
        while (tokens.size() > index) {
            String token = tokens.get(index++);
            Set<Expectations> possibilities = Expectations.getPossibleMatches(token);
            Expectations type = null;
            for(Expectations expectation : possibilities) {
                if(expected.contains(expectation)) {
                    type = expectation;
                    break;
                }
            }
            if(type == null) throw new SyntaxException("failed to load json file due to improper syntax");
            if (!reservedTokens.isEmpty() && reservedTokens.get(0).equals("[")) expected = Expectations.possibleArrayNext(type);
            else expected = Expectations.possibleHashNext(type);
            if(indents > 1) {
                reservedTokens.add(token);
                switch (type) {
                    case OPEN_CURLY, OPEN_BRACKET -> indents++;
                    case CLOSE_CURLY, CLOSE_BRACKET -> {
                        indents--;
                        if(indents == 1) {
                            JsonObject jsonObject = process(reservedTokens);
                            reservedTokens = new ArrayList<>();
                            if(jsonObject instanceof JsonArray) parentHash.putJsonArray(key, (JsonArray) jsonObject);
                            else parentHash.putJsonObject(key, (JsonHash) jsonObject);
                        }
                    }
                }

                continue;
            }
            switch (type) {
                case COMMA -> key = null;
                case VALUE -> {
                    try {
                        parentHash.putInteger(key, Integer.parseInt(token));
                    } catch (NumberFormatException e) {
                        try {
                            parentHash.putDouble(key, Double.parseDouble(token));
                        } catch (NumberFormatException ex) {
                            if(token.equals("true") || token.equals("false")) parentHash.putBoolean(key, Boolean.parseBoolean(token));
                            else parentHash.putString(key, token);
                        }
                    }
                }
                case KEY -> key = token;
                case OPEN_CURLY, OPEN_BRACKET -> {
                    indents++;
                    if(indents != 1) reservedTokens.add(token);
                }
            }
        }
        return parentHash;
    }
    private static JsonArray processArray(List<String> tokens) {
        JsonArray parentArray = new JsonArray();
        Set<Expectations> expected = Set.of(Expectations.OPEN_BRACKET);

        int index = 0;
        int indents = 0;
        List<String> reservedTokens = new ArrayList<>();
        while (tokens.size() > index) {
            String token = tokens.get(index++);
            Set<Expectations> possibilities = Expectations.getPossibleMatches(token);
            Expectations type = null;
            for(Expectations expectation : possibilities) {
                if(expected.contains(expectation)) {
                    type = expectation;
                    break;
                }
            }
            if(type == null) throw new SyntaxException("failed to load json file due to improper syntax");
            if (!reservedTokens.isEmpty() && reservedTokens.get(0).equals("{")) expected = Expectations.possibleHashNext(type);
            else expected = Expectations.possibleArrayNext(type);
            if(indents > 1) {
                reservedTokens.add(token);
                switch (type) {
                    case OPEN_CURLY, OPEN_BRACKET -> indents++;
                    case CLOSE_CURLY, CLOSE_BRACKET -> {
                        indents--;
                        if(indents == 1) {
                            JsonObject jsonObject = process(reservedTokens);
                            reservedTokens = new ArrayList<>();
                            if(jsonObject instanceof JsonArray) parentArray.addJsonArray((JsonArray) jsonObject);
                            else parentArray.addJsonObject((JsonHash) jsonObject);
                        }
                    }
                }

                continue;
            }
            switch (type) {
                case VALUE -> {
                    try {
                        parentArray.addInteger(Integer.parseInt(token));
                    } catch (NumberFormatException e) {
                        try {
                            parentArray.addDouble(Double.parseDouble(token));
                        } catch (NumberFormatException ex) {
                            if(token.equals("true") || token.equals("false")) parentArray.addBoolean(Boolean.parseBoolean(token));
                            else parentArray.addString(token);
                        }
                    }
                }
                case OPEN_CURLY, OPEN_BRACKET -> {
                    indents++;
                    if(indents != 1) reservedTokens.add(token);
                }
            }
        }
        return parentArray;
    }

    static void write(File file, JsonObject jsonObject) throws IOException {
        write(file, jsonObject, 4, false);
    }

    static void write(File file, JsonObject jsonObject, int indentSize) throws IOException {
        write(file, jsonObject, indentSize, false);
    }

    static void write(File file, JsonObject jsonObject, int indentSize, boolean sortAlphabetically) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        String jsonString = (sortAlphabetically && jsonObject instanceof JsonHash jsonHash) ? jsonHash.toSortedString() : jsonObject.toString();
        List<String> tokens = Tokenizer.tokenizeString(jsonString);
        String indentThickness = " ".repeat(indentSize);
        final int[] indent = {0};
        final StringBuilder[] line = {new StringBuilder()};
        tokens.forEach(token -> {
            Set<Expectations> expectation = Expectations.getPossibleMatches(token);
            switch (expectation.iterator().next()) {
                case OPEN_BRACKET, OPEN_CURLY -> {
                    line[0].append(token);
                    indent[0]++;
                }
                case CLOSE_BRACKET, CLOSE_CURLY -> {
                    indent[0]--;
                    line[0].append(token);
                }
                case COMMA -> {
                    line[0].append(token);
                    try {
                        fileWriter.write(line[0].toString());
                    } catch (IOException e) {
                        System.err.printf("failed to write line \"%s\" due to: %s\n", line[0], e.getMessage());
                    }
                    line[0] = new StringBuilder(indentThickness.repeat(indent[0]));
                }
                default -> line[0].append(token);
            }
        });
        try {
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("failed to close writer due to: " +e.getMessage());
        }
    }
}
