package Model.Expression;

import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.Types.IType;
import Model.Types.IValue;
import Model.Types.ReferenceType;
import Model.Types.ReferenceValue;

public class ReadHeapExpression implements IExpression {
    IExpression expression;

    public ReadHeapExpression(IExpression expression){
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "readHeap(" + expression.toString()+ ")";
    }

    @Override
    public IValue evaluate(IDictionary<String, IValue> table, IHeap<Integer, IValue> heap_table) {
        IValue expression_value = expression.evaluate(table, heap_table);
        if (!(expression_value instanceof ReferenceValue)) {
            throw new RuntimeException("Expression: " + expression.toString() + " cannot be evaluated to a reference value!");
        }
        int address = ((ReferenceValue) expression_value).getAddress();
        if(!(heap_table.keyAlreadyDefined(address)))
            throw new RuntimeException("Address: " + address + " not defined in the heap table");
        // System.out.println(heap_table.keyAlreadyDefined(address));

        return heap_table.get(address);

    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnvironment) {
            IType type = expression.typeCheck(typeEnvironment);
            if (type instanceof ReferenceType)
                return ((ReferenceType) type).getInner();

            throw new RuntimeException("The readHeap argument is not of reference type but actually: " + expression.toString() + '-' + type.toString());
    }
}
