package ramgames.jsonweaver.objects;

import ramgames.jsonweaver.exceptions.TypeException;

import java.util.ArrayList;
import java.util.List;

public class JsonArray extends JsonObject{
    
    private final List<Object> contents = new ArrayList<>();

    public boolean isEmpty() {
        return this.contents.isEmpty();
    }

    public void addInteger(Integer object) {
        contents.add(object);
    }

    public void addDouble(Double object) {
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

    public void addJsonObject(JsonHash object) {
        contents.add(object);
    }

    public void addInteger(int i, Integer object) {
        contents.add(i, object);
    }

    public void addDouble(int i, Double object) {
        contents.add(i, object);
    }

    public void addBoolean(int i, Boolean object) {
        contents.add(i, object);
    }

    public void addString(int i, String object) {
        contents.add(i, object);
    }

    public void addJsonArray(int i, JsonArray object) {
        contents.add(i, object);
    }

    public void addJsonObject(int i, JsonHash object) {
        contents.add(i, object);
    }
    
    public int getLength() {
        return contents.size();
    }

    public int getInteger(int key) {
        if(contents.get(key) instanceof Integer value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'Integer'", key));
    }

    public long getDouble(int key) {
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

    public JsonHash getJsonObject(int key) {
        if(contents.get(key) instanceof JsonHash value) return value;
        throw new TypeException(String.format("The key '%s' is not of type 'JsonObject'", key));
    }

    @Override
    public String toString() {
        if(contents.isEmpty()) return "[]";
        StringBuilder stringBuilder = new StringBuilder("[");
        contents.forEach((object) -> {
            if (object instanceof Integer value) stringBuilder.append(value).append(", ");
            else if (object instanceof Double value) stringBuilder.append(value).append(", ");
            else if (object instanceof Boolean value) stringBuilder.append(value).append(", ");
            else if (object instanceof String value) stringBuilder.append("\"").append(value).append("\", ");
            else if (object instanceof JsonHash value) stringBuilder.append(value).append(", ");
            else if (object instanceof JsonArray value) stringBuilder.append(value).append(", ");
        });
        stringBuilder.setLength(stringBuilder.length()-2);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
