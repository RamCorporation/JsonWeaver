package ramgames.jsonweaver;

import java.util.HashMap;
import java.util.Set;

import static ramgames.jsonweaver.JsonWeaver.*;


public class JsonObject {

    private final HashMap<String, Object> contents = new HashMap<>();

    public Set<String> keySet() {
        return contents.keySet();
    }

    public void addInteger(String key, Integer object) {
        contents.put(key, object);
    }

    public void addLong(String key, Long object) {
        contents.put(key, object);
    }

    public void addBoolean(String key, Boolean object) {
        contents.put(key, object);
    }

    public void addString(String key, String object) {
        contents.put(key, object);
    }

    public void addJsonArray(String key, JsonArray object) {
        contents.put(key, object);
    }

    public void addJsonObject(String key, JsonObject object) {
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
            case LONG_TYPE -> contents.get(key) instanceof Long;
            case BOOLEAN_TYPE -> contents.get(key) instanceof Boolean;
            case STRING_TYPE -> contents.get(key) instanceof String;
            case ARRAY_TYPE -> contents.get(key) instanceof JsonArray;
            case OBJECT_TYPE -> contents.get(key) instanceof JsonObject;

            default -> throw new ArrayIndexOutOfBoundsException(String.format("unknown type element type '%s'", type));
        };
    }

    public int getInteger(String key) {
        if(!containsElement(key)) throw new KeyNotFoundException(String.format("JsonObject does not contain the key '%s'", key));
        if(contents.get(key) instanceof Integer value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'Integer'", key));
    }

    public long getLong(String key) {
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

    public JsonObject getJsonObject(String key) {
        if(!containsElement(key)) throw new KeyNotFoundException(String.format("JsonObject does not contain the key '%s'", key));
        if(contents.get(key) instanceof JsonObject value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'JsonObject'", key));
    }

}
