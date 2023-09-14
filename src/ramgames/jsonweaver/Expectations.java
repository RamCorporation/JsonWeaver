package ramgames.jsonweaver;

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
    public static Set<Expectations> possibleNext(Expectations expectation) {
        return switch (expectation) {
            case COMMA -> Set.of(OPEN_BRACKET, OPEN_CURLY, KEY);
            case VALUE, CLOSE_BRACKET, CLOSE_CURLY -> Set.of(COMMA, CLOSE_BRACKET, CLOSE_CURLY);
            case COLON, OPEN_BRACKET -> Set.of(VALUE);
            case KEY -> Set.of(COLON);
            case OPEN_CURLY -> Set.of(KEY);
        };
    }
}
