package ramgames.jsonweaver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class TokenHolder {
    private final List<String> tokens = new ArrayList<>();

    private int scannerIndex = 0;

    private TokenHolder() {

    }

    public static TokenHolder create(File file) throws FileNotFoundException{
        return create(readFile(file));
    }
    public static TokenHolder create(String string) {
        return tokenizeString(string);
    }

    public void addToken(String token) {
        this.tokens.add(token);
    }

    public boolean containsSequence(String... tokenSequence) {
        List<String> tokenList = Arrays.asList(tokenSequence);
        if(tokenList.size() > tokens.size()) return false;
        if(tokenList.size() == tokens.size()) return tokenList == tokens;
        for(var i = 0; i < (tokens.size()-tokenList.size()); i++) {
            for (var l = 0; l < tokenList.size(); l++) {
                if (!Objects.equals(tokenList.get(l), tokens.get(l + i))) break;
                if(l+1 == tokenList.size()) return true;
            }
        }
        return false;
    }

    public void shiftToNextOccurrence(String token) {
        scannerIndex++;
        while(!tokens.get(scannerIndex).equals(token)) {
            if (scannerIndex + 1 == tokens.size()) break;
            scannerIndex++;
        }
    }

    public boolean hasNext() {
        return scannerIndex+1 < tokens.size();
    }

    public String getNext() {
        return tokens.get(scannerIndex++);
    }

    public void setScannerIndex(int i) {
        this.scannerIndex = i;
    }


    private static TokenHolder tokenizeString(String string) {
        int quoteCount = 0;
        StringBuilder builder = new StringBuilder();
        TokenHolder tokenHolder = new TokenHolder();
        for(var index = 0; index < string.length(); index++) {
            char charcoal = string.charAt(index);
            char nextCoal = index+1 < string.length() ? string.charAt(index+1) : '▌';
            if(charcoal == '"') {
                quoteCount++;
                continue;
            }
            if(quoteCount % 2 == 1) {
                builder.append(charcoal);
                continue;
            }

            switch(charcoal) {
                case ')', '}', ']',',',':' -> {
                    if(!builder.toString().isEmpty()) tokenHolder.addToken(builder.toString());
                    tokenHolder.addToken(String.valueOf(charcoal));
                    builder = new StringBuilder();
                }
                case '(','{','[' -> {
                    if(!builder.toString().isEmpty()) tokenHolder.addToken(builder.toString());
                    builder = new StringBuilder();
                    if(nextCoal == charcoal+1) {
                        tokenHolder.addToken(charcoal+String.valueOf((char) (charcoal +1)));
                        index+=1;
                    } else tokenHolder.addToken(String.valueOf(charcoal));
                }
                case ' ' -> {
                    if(builder.toString().isEmpty()) continue;
                    tokenHolder.addToken(builder.toString());
                    builder = new StringBuilder();
                }
                default -> builder.append(charcoal);
            }
        }
        System.out.println(tokenHolder.tokens);
        return tokenHolder;
    }

    private static String readFile(File file) throws FileNotFoundException {
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