package ramgames.jsonweaver;

import java.util.ArrayList;
import java.util.List;

public class JsonArray {
    
    private static final List<Object> contents = new ArrayList<>();

    public void addInteger(Integer object) {
        contents.add(object);
    }

    public void addLong(Long object) {
        contents.add(object);
    }

    public void addBoolean(Boolean object) {
        contents.add(object);
    }

    public void addString(String object) {
        contents.add(object);
    }

    public void addJsonArray(JsonArray object) {
        contents.add(object);
    }

    public void addJsonObject(JsonObject object) {
        contents.add(object);
    }
    
    public int getLength() {
        return contents.size();
    }

    public int getInteger(int key) {
        if(contents.get(key) instanceof Integer value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'Integer'", key));
    }

    public long getLong(int key) {
        if(contents.get(key) instanceof Long value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'Long'", key));
    }

    public boolean getBoolean(int key) {
        if(contents.get(key) instanceof Boolean value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'Boolean'", key));
    }

    public String getString(int key) {
        if(contents.get(key) instanceof String value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'String'", key));
    }

    public JsonArray getJsonArray(int key) {
        if(contents.get(key) instanceof JsonArray value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'JsonArray'", key));
    }

    public JsonObject getJsonObject(int key) {
        if(contents.get(key) instanceof JsonObject value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'JsonObject'", key));
    }
    
}
