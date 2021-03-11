package Model.ADT;

import java.util.HashMap;
import java.util.Map;

public class FileTable<Key, Value > implements IDictionary<Key, Value> {

    HashMap<Key, Value> file_table;

    public FileTable() {
        this.file_table = new HashMap<>();
    }

    @Override
    public boolean variableAlreadyDefined(Key key) {
        return file_table.containsKey(key);
    }

    @Override
    public synchronized void add(Key key, Value value) {
        file_table.put(key,value);
    }

    @Override
    public Value lookup(Key key) {
        return file_table.get(key);
    }

    @Override
    public synchronized void update(Key key, Value value) {
        this.add(key,value);
    }

    @Override
    public String toLogString() {
        StringBuilder result = new StringBuilder();
        for (Key key: file_table.keySet()){
            result.append(key.toString()).append('\n'); // .append(" --> ").append(file_table.get(key).toString()).append('\n');
        }
        return result.toString();
    }

    @Override
    public void delete(Key key) {
        this.file_table.remove(key);
    }

    @Override
    public Map<Key, Value> getContent() {
        return file_table;
    }

    @Override
    public Dictionary<Key, Value> deepCopy() {
        Dictionary<Key, Value> copy = new Dictionary<>();
        for (Key key: this.file_table.keySet())
            copy.add(key, file_table.get(key));
        return copy;
    }
}
