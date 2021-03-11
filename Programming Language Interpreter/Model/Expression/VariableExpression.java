package Model.Expression;

import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.Types.IType;
import Model.Types.IValue;

public class VariableExpression implements IExpression {
    String variable_name;

    public VariableExpression(String variable_name){
        this.variable_name = variable_name;
    }

    @Override
    public IValue evaluate(IDictionary<String, IValue> table, IHeap<Integer, IValue> heap_table) {
        if (!(table.variableAlreadyDefined(variable_name)))
            throw new RuntimeException("Variable:" + variable_name + " is not defined");
        return table.lookup(variable_name);
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnvironment) {
        return typeEnvironment.lookup(variable_name);
    }

    @Override
    public String toString() {
        return variable_name;
    }
}
