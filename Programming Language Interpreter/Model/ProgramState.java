package Model;

import Model.ADT.*;
import Model.Statement.IStatement;
import Model.Types.BoolValue;
import Model.Types.IValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.chrono.IsoChronology;

public class ProgramState {

    IStack<IStatement> stack;
    IDictionary<String, IValue> symbols_table;
    IList<IValue> output;
    IStatement statement;
    IDictionary<String, BufferedReader> file_table;
    HeapTable<Integer, IValue> heap_table;
    private static int global_id = 0;
    int private_id;

    public ProgramState(IStack<IStatement> stack, Dictionary<String, IValue> symbols_table, IList<IValue> output,IDictionary<String, BufferedReader> file_table,HeapTable<Integer, IValue> heap_table, IStatement statement) {
        this.private_id = getNextId();
        this.stack = stack;
        this.symbols_table = symbols_table;
        this.output = output;
        this.statement = statement;
        this.file_table = file_table;
        this.heap_table = heap_table;
        this.stack.push(statement);
    }

    private synchronized int getNextId() {
        global_id += 1;
        return global_id;
    }


    public boolean isCompleted() {
        return stack.isEmpty();
    }

    public ProgramState oneStep () throws IOException {
        if (stack.isEmpty())
            throw new RuntimeException("Program stack is empty!");
        IStatement statement = stack.pop();
        return statement.execute(this);
    }

    public IStack<IStatement> getStack() {
        return stack;
    }

    public String toString() {
        return "Execution Stack: " + stack.toString() + "\n" +
                "Id: " + private_id + '\n' +
                "Symbols Table: " + symbols_table.toString() + "\n" +
                "Files Table: " + file_table.toString() + "\n" +
                "Heap Table: " + heap_table.toString() + "\n" +
                "Output: " + output.toString() + "\n-----------";
    }

    public void setFileTable(IDictionary<String, BufferedReader> file_table) {
        this.file_table = file_table;
    }

    public IDictionary<String, BufferedReader> getFileTable() {
        return file_table;
    }

    public IDictionary<String, IValue> getSymbolsTable() {
        return symbols_table;
    }

    public IList<IValue> getOutput() {
        return output;
    }

    public void setStack(IStack<IStatement> stack) {
        this.stack = stack;
    }

    public void setSymbols_table(IDictionary<String, IValue> symbols_table) {
        this.symbols_table = symbols_table;
    }

    public void setOutput(IList<IValue> output) {
        this.output = output;
    }

    public IStatement getStatement() {
        return statement;
    }

    public void setStatement(IStatement statement) {
        this.statement = statement;
    }

    public String toLogString() {
        return  "Id: " + this.private_id + '\n' +
                "Execution Stack:\n" + stack.toLogString() +
                "Symbols Table:\n" + symbols_table.toLogString() +
                "Files Table:\n" + file_table.toLogString() +
                "Heap Table:\n" + heap_table.toLogString() +
                "Output:\n" + output.toLogString();
    }

    public HeapTable<Integer, IValue> getHeapTable() {
        return heap_table;
    }

    public void setHeapTable(HeapTable<Integer, IValue> heap_table) {
        this.heap_table = heap_table;
    }

    public int getId() {
        return private_id;
    }
}
