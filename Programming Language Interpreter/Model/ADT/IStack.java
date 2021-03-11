package Model.ADT;

import Model.Statement.IStatement;

import java.util.Deque;

public interface IStack<T> {
    void push(T t);
    T pop();
    boolean isEmpty();

    String toLogString();

    T top();

    Deque<T> getStack();
}
