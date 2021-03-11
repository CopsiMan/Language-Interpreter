package Model.ADT;

import java.util.HashMap;
import java.util.Map;

public class HeapTable<Key, Value> implements IHeap<Key, Value> {
    HashMap<Key, Value> table;
    int nextFreeAddress;

    public HeapTable() {
        table = new HashMap<Key, Value>();
        nextFreeAddress = 1;
    }

    public int getNextFreeAddress() {
        int free = nextFreeAddress;
        nextFreeAddress += 1;
        return free;
    }

    @Override
    public Value get(Key key) {
        return table.get(key);
    }

    @Override
    public synchronized void update(Key key, Value value) {
        table.put(key,value);
    }

    @Override
    public synchronized void setContent(Map<Key, Value> content) {
        table = new HashMap<>(content);
    }

    @Override
    public Map<Key, Value> getContent() {
        return table;
    }

    @Override
    public boolean keyAlreadyDefined(Key key) {
        return table.containsKey(key);
    }

    @Override
    public synchronized void add(Key key, Value value) {
        table.put(key,value);
    }

    @Override
    public String toLogString() {
        StringBuilder result = new StringBuilder();
        for (Key key : table.keySet()) {
            result.append(key).append(" --> ").append(table.get(key).toString()).append('\n');
        }
        return result.toString();
    }
}
