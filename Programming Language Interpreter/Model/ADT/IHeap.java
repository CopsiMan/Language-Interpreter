package Model.ADT;

import java.util.Map;

public interface IHeap<Key, Value> {
    boolean keyAlreadyDefined(Key key);

    void add(Key key, Value value);

    String toLogString();

    int getNextFreeAddress();

    Value get(Key key);

    void update(Key key, Value value);

    void setContent(Map<Key, Value> content);

    Map<Key,Value> getContent();

//    void delete(int key);
}
