package ramgames.jsonweaver.objects;

import ramgames.jsonweaver.exceptions.KeyNotFoundException;
import ramgames.jsonweaver.exceptions.TypeException;

import java.util.*;

import static ramgames.jsonweaver.JsonWeaver.*;


public class JsonHash extends JsonObject {


    private final LinkedHashMap<String, Object> contents = new LinkedHashMap<>();

    public boolean isEmpty() {
        return this.contents.isEmpty();
    }

    public Set<String> keySet() {
        return contents.keySet();
    }

    public List<String> sortedKeyList() {
        List<String> sortedSet = new ArrayList<>();
        Set<String> tempKeySet = Set.copyOf(contents.keySet());
        for (String key : tempKeySet) {
            System.out.println(sortedSet);
            if (sortedSet.isEmpty()) {
                sortedSet.add(key);
                continue;
            }
            boolean added = false;
            for(int i = 0; i < sortedSet.size(); i++) {
                String contestant = sortedSet.get(i);
                int bound = Math.min(contestant.length(), key.length());
                boolean finished = false;
                for(int l = 0; l < bound; l++) {
                    char contest = contestant.charAt(l);
                    char keyChar = key.charAt(l);
                    if(keyChar < contest) {
                        sortedSet.add(i, key);
                        added = true;
                        finished = true;
                        break;
                    }
                    if(keyChar > contest) {
                        break;
                    }
                }
                if(finished) break;

            }
            if(!added) sortedSet.add(key);
        }
        return sortedSet;
    }

    public void putInteger(String key, Integer object) {
        contents.put(key, object);
    }

    public void putDouble(String key, Double object) {
        contents.put(key, object);
    }

    public void putBoolean(String key, Boolean object) {
        contents.put(key, object);
    }

    public void putString(String key, String object) {
        contents.put(key, object);
    }

    public void putJsonArray(String key, JsonArray object) {
        contents.put(key, object);
    }

    public void putJsonHash(String key, JsonHash object) {
        contents.put(key, object);
    }

    public boolean containsElement(String key) {
        return contents.containsKey(key);
    }

    public int getSize() {
        return contents.size();
    }

    public boolean containsElement(String key, int type) {
        if(!containsElement(key)) return false;
        return switch (type) {
            case INT_TYPE -> contents.get(key) instanceof Integer;
            case DOUBLE_TYPE -> contents.get(key) instanceof Double;
            case BOOLEAN_TYPE -> contents.get(key) instanceof Boolean;
            case STRING_TYPE -> contents.get(key) instanceof String;
            case ARRAY_TYPE -> contents.get(key) instanceof JsonArray;
            case OBJECT_TYPE -> contents.get(key) instanceof JsonHash;

            default -> throw new ArrayIndexOutOfBoundsException(String.format("unknown type element type '%s'", type));
        };
    }

    public int getInteger(String key) {
        if(!containsElement(key)) throw new KeyNotFoundException(String.format("JsonObject does not contain the key '%s'", key));
        if(contents.get(key) instanceof Integer value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'Integer'", key));
    }

    public long getDouble(String key) {
        if(!containsElement(key)) throw new KeyNotFoundException(String.format("JsonObject does not contain the key '%s'", key));
        if(contents.get(key) instanceof Long value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'Long'", key));
    }

    public boolean getBoolean(String key) {
        if(!containsElement(key)) throw new KeyNotFoundException(String.format("JsonObject does not contain the key '%s'", key));
        if(contents.get(key) instanceof Boolean value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'Boolean'", key));
    }

    public String getString(String key) {
        if(!containsElement(key)) throw new KeyNotFoundException(String.format("JsonObject does not contain the key '%s'", key));
        if(contents.get(key) instanceof String value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'String'", key));
    }

    public JsonArray getJsonArray(String key) {
        if(!containsElement(key)) throw new KeyNotFoundException(String.format("JsonObject does not contain the key '%s'", key));
        if(contents.get(key) instanceof JsonArray value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'JsonArray'", key));
    }

    public JsonHash getJsonObject(String key) {
        if(!containsElement(key)) throw new KeyNotFoundException(String.format("JsonObject does not contain the key '%s'", key));
        if(contents.get(key) instanceof JsonHash value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'JsonObject'", key));
    }

    @Override
    public String toString() {
        return buildToString(this.keySet().stream().toList());
    }

    public String toSortedString() {
        return buildToString(this.sortedKeyList());
    }

    private String buildToString(List<String> keys) {
        if(keys.isEmpty()) return "{}";
        StringBuilder stringBuilder = new StringBuilder("{");
        keys.forEach((key) -> {
            Object object = this.contents.get(key);
            stringBuilder.append('"').append(key).append("\": ");
            if (object instanceof Integer value) stringBuilder.append(value).append(", ");
            else if (object instanceof Double value) stringBuilder.append(value).append(", ");
            else if (object instanceof Boolean value) stringBuilder.append(value).append(", ");
            else if (object instanceof String value) stringBuilder.append("\"").append(value).append("\", ");
            else if (object instanceof JsonHash value) stringBuilder.append(value).append(", ");
            else if (object instanceof JsonArray value) stringBuilder.append(value).append(", ");
        });
        stringBuilder.setLength(stringBuilder.length()-2);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
