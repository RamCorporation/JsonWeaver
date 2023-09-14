package ramgames.jsonweaver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

public interface JsonWeaver {

    int INT_TYPE = 1;
    int LONG_TYPE = 2;
    int BOOLEAN_TYPE = 3;
    int STRING_TYPE = 4;
    int ARRAY_TYPE = 5;
    int OBJECT_TYPE = 6;
    
    static JsonObject read(File file) throws FileNotFoundException {
        return process(TokenHolder.create(file));
    }
    static JsonObject read(String string) {
        return process(TokenHolder.create(string));
    }
    private static JsonObject process(TokenHolder tokenHolder) {
        JsonObject parentObject = new JsonObject();
        Set<Expectations> expected = Set.of(Expectations.OPEN_CURLY);

        String key = null;
        while (tokenHolder.hasNext()) {
            String token = tokenHolder.getNext();
            Set<Expectations> possibilities = Expectations.getPossibleMatches(token);
            Expectations type = null;
            for(Expectations expectation : possibilities) {
                if(expected.contains(expectation)) {
                    type = expectation;
                    break;
                }
            }
            if(type == null) throw new SyntaxException("failed to load json file due to improper syntax");
            expected = Expectations.possibleNext(type);
            switch (type) {
                case COMMA -> key = null;
                case VALUE -> {
                    System.out.println("saving key: "+key);
                    try {
                        parentObject.addInteger(key, Integer.parseInt(token));
                    } catch (NumberFormatException e) {
                        try {
                            parentObject.addLong(key, Long.parseLong(token));
                        } catch (NumberFormatException ex) {
                            try {
                                parentObject.addBoolean(key, Boolean.parseBoolean(token));
                            } catch (Exception exception) {
                                parentObject.addString(key, token);
                            }
                        }
                    }
                }
                case KEY -> key = token;
            }
        }
        return parentObject;
    }

}
