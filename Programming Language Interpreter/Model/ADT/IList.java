package Model.ADT;

import Model.Types.IValue;

import java.util.List;

public interface IList<T> {
    void add(T t);

    String toLogString();

    List<T> getList();
}
