package Model.Expression;

import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.Types.IType;
import Model.Types.IValue;

public interface IExpression {
    IValue evaluate(IDictionary<String, IValue> table, IHeap<Integer, IValue> heap_table);
    IType typeCheck (IDictionary<String, IType> typeEnvironment);
}
