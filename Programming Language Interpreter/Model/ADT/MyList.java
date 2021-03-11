package Model.ADT;

import Model.Types.IValue;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T>{
    ArrayList<T> list = new ArrayList<>();
    @Override
    public synchronized void add(T t) {
        list.add(t);
    }

    @Override
    public String toLogString() {
        StringBuilder result = new StringBuilder();
        for (T t: list){
            result.append(t.toString()).append('\n');
        }
        return result.toString();
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (T t: list) {
            result.append(t.toString()).append(", ");
        }
        result.append("]");
        return result.toString();
    }
}
