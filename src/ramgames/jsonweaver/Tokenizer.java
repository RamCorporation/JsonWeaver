package ramgames.jsonweaver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public interface Tokenizer {

    static List<String> tokenizeString(String string) {
        int quoteCount = 0;
        StringBuilder builder = new StringBuilder();
        List<String> tokens = new ArrayList<>();
        for (var index = 0; index < string.length(); index++) {
            char charcoal = string.charAt(index);
            if (charcoal == '"') {
                quoteCount++;
                continue;
            }
            if (quoteCount % 2 == 1) {
                builder.append(charcoal);
                continue;
            }

            switch (charcoal) {
                case ')', '}', ']', ',', ':' -> {
                    if (!builder.toString().isEmpty()) tokens.add(builder.toString());
                    tokens.add(String.valueOf(charcoal));
                    builder = new StringBuilder();
                }
                case '(', '{', '[' -> {
                    if (!builder.toString().isEmpty()) tokens.add(builder.toString());
                    builder = new StringBuilder();
                    tokens.add(String.valueOf(charcoal));
                }
                case ' ' -> {
                    if (builder.toString().isEmpty()) continue;
                    tokens.add(builder.toString());
                    builder = new StringBuilder();
                }
                default -> builder.append(charcoal);
            }
        }
        return tokens;
    }



    static String readFile(File file) throws FileNotFoundException {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()) {
            String line = scanner.next();
            if(line.contains("//")) {
                line += "//";
                line = line.replaceAll("//.*?(?=\\r|\\n|$)|//.*$","");
            }
            lines.add(line);
        }
        StringBuilder stringBuilder = new StringBuilder();
        lines.forEach(line -> stringBuilder.append(line).append(" "));
        return stringBuilder.toString();
    }

}
