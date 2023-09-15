package ramgames.jsonweaver;

import java.util.Collections;
import java.util.Set;

public enum Expectations {


    COMMA,
    VALUE,
    COLON,
    KEY,
    CLOSE_CURLY,
    OPEN_CURLY,
    OPEN_BRACKET,
    CLOSE_BRACKET
    ;

    public static Set<Expectations> getPossibleMatches(String string) {
        return switch (string) {
            case "{" -> Set.of(OPEN_CURLY);
            case "[" -> Set.of(OPEN_BRACKET);
            case "}" -> Set.of(CLOSE_CURLY);
            case "]" -> Set.of(CLOSE_BRACKET);
            case ":" -> Set.of(COLON);
            case "," -> Set.of(COMMA);
            default -> Set.of(KEY, VALUE);
        };
    }
    public static Set<Expectations> possibleHashNext(Expectations expectation) {
        return switch (expectation) {
            case COMMA -> Set.of(KEY);
            case VALUE, CLOSE_BRACKET, CLOSE_CURLY -> Set.of(COMMA, CLOSE_BRACKET, CLOSE_CURLY);
            case COLON -> Set.of(VALUE, OPEN_CURLY, OPEN_BRACKET);
            case OPEN_BRACKET -> Set.of(VALUE, CLOSE_BRACKET);
            case KEY -> Set.of(COLON);
            case OPEN_CURLY -> Set.of(KEY, CLOSE_CURLY);
        };
    }



    public static Set<Expectations> possibleArrayNext(Expectations expectation) {
        return switch (expectation) {
            case COMMA -> Set.of(OPEN_BRACKET, OPEN_CURLY, VALUE);
            case VALUE, CLOSE_BRACKET, CLOSE_CURLY -> Set.of(COMMA, CLOSE_BRACKET, CLOSE_CURLY);
            case COLON, OPEN_BRACKET -> Set.of(VALUE);
            case KEY -> Collections.EMPTY_SET;
            case OPEN_CURLY -> Set.of(KEY, CLOSE_CURLY);
        };
    }
}
