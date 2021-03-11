package Model.Statement;

import Model.*;
import Model.ADT.IDictionary;
import Model.ADT.IStack;
import Model.Exceptions.VariableAlreadyDefinedException;
import Model.Types.*;

public class VariableDeclarationStatement implements IStatement {
    String variable_name;
    IType type;

    public VariableDeclarationStatement(String variable_name, IType type) {
        this.variable_name = variable_name;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state) throws VariableAlreadyDefinedException {
        IStack<IStatement> stack = state.getStack();
        IDictionary<String, IValue> symbols_table = state.getSymbolsTable();
        if (symbols_table.variableAlreadyDefined(variable_name))
            throw new VariableAlreadyDefinedException("Variable " + variable_name + " is already defined");
        else
            symbols_table.add(variable_name, type.defaultValue());
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        typeEnvironment.add(variable_name,type);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return type.toString() + " " + variable_name;
    }
}
