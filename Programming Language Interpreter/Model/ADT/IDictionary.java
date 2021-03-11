package Model.ADT;

import java.util.HashMap;
import java.util.Map;

public interface IDictionary<Key, Value> {

    boolean variableAlreadyDefined(Key key);

    void add(Key key, Value value);

    Value lookup(Key key);

    void update(Key key, Value value);

    String toLogString();

    void delete(Key key);

    Map<Key, Value> getContent();

    Dictionary<Key,Value> deepCopy();
}
