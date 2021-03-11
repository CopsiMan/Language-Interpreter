package Model.Expression;

import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.Types.IType;
import Model.Types.IValue;

public class ValueExpression implements IExpression {
    IValue value;
    public ValueExpression(IValue value) {
        this.value = value;
    }

    @Override
    public IValue evaluate(IDictionary<String, IValue> table, IHeap<Integer, IValue> heap_table) {
        return value;
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnvironment) {
        return value.getType();
    }

    public String toString () {
        return value.toString();
    }
}
