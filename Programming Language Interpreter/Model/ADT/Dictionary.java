package Model.ADT;

import java.util.HashMap;
import java.util.Map;

public class Dictionary<Key, Value> implements IDictionary<Key, Value> {
    HashMap<Key, Value> dictionary;

    public Dictionary() {
        dictionary = new HashMap<Key, Value>();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Key, Value> entry : dictionary.entrySet()) {
            result.append(entry.getKey()).append(" -> ").append(entry.getValue()).append(";");
        }
        return "{" + result + "}";
    }

    @Override
    public boolean variableAlreadyDefined(Key key) {
        return dictionary.containsKey(key);
    }

    @Override
    public void add(Key key, Value value) {
        dictionary.put(key, value);
    }

    @Override
    public Value lookup(Key key) {
        return dictionary.get(key);
    }

    @Override
    public void update(Key key, Value value) {
        add(key, value);
    }

    @Override
    public String toLogString() {
        StringBuilder result = new StringBuilder();
        for (Key key: dictionary.keySet()){
            result.append(key.toString()).append(" --> ").append(dictionary.get(key).toString()).append('\n');
        }
        return result.toString();
    }

    @Override
    public void delete(Key key) {
        this.dictionary.remove(key);
    }

    @Override
    public Map<Key, Value> getContent() {
        return dictionary;
    }

    public Dictionary<Key, Value> deepCopy() {
        Dictionary<Key, Value> copy = new Dictionary<>();
        for (Key key: this.dictionary.keySet())
            copy.add(key, dictionary.get(key));
        return copy;
    }
}
