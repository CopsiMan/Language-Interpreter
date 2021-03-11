package Model.ADT;

import java.util.ArrayDeque;
import java.util.Deque;

public class Stack<T> implements  IStack<T> {
    Deque<T> stack;

    public Stack() {
        stack = new ArrayDeque<T>();
    }

    @Override
    public void push(T t) {
        stack.push(t);
    }

    @Override
    public T pop() {
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toLogString() {
//        return stack.toString() + '\n';
        StringBuilder result = new StringBuilder();
        for (T elem : stack) {
            String str = elem.toString();
//            str = str.replaceAll("\\[|\\]", " ");
//            String[] split = str.split("\\|");
//            for (String s : split) {
//                s = s.trim();
            result.append(str).append('\n');
//            }
        }

        return result.toString() + '\n';

//        StringBuilder result = new StringBuilder();
//        for (T t: stack){
//            result.append(t.toString()).append('\n');
//        }
//        return result.toString();
    }

    @Override
    public T top() {
        return stack.peek();
    }

    @Override
    public Deque<T> getStack() {
        return stack;
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
