package Model.Statement;

import Model.ADT.IDictionary;
import Model.Expression.IExpression;
import Model.ProgramState;
import Model.Types.IType;
import Model.Types.IValue;
import Model.Types.ReferenceType;
import Model.Types.ReferenceValue;

import java.io.IOException;

public class WriteHeapStatement implements IStatement {
    IExpression expression, variable_name;
    public WriteHeapStatement(IExpression variable_name, IExpression expression) {
        this.variable_name = variable_name;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws RuntimeException, IOException {
        var symbols_table = state.getSymbolsTable();
        var heap =  state.getHeapTable();
        IValue variable_value = variable_name.evaluate(symbols_table, heap);

//        System.out.println(variable_name.toString());

        if (!(variable_value.getType() instanceof ReferenceType))
            throw new RuntimeException("Variable type is: " + variable_value.getType().toString() + " instead of reference type");
        int address = ((ReferenceValue) variable_value).getAddress();
        if (!(heap.keyAlreadyDefined(address)))
        throw new RuntimeException("Address: " + address + " not defined in the heap table");

        IValue expression_value = expression.evaluate(symbols_table,heap);
        if (!(expression_value.getType().equals(((ReferenceType) variable_value.getType()).getInner())))
            throw new RuntimeException("Type mismatch between: " + expression_value.getType().toString() + " and " + ((ReferenceValue) variable_value).getLocationType().toString());

        heap.update(address, expression_value);
//        System.out.println(variable_value.toString());
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        IType variable_type = variable_name.typeCheck(typeEnvironment),
                expression_type = expression.typeCheck(typeEnvironment);

        if (!variable_type.equals(new ReferenceType(expression_type)))
            throw new RuntimeException("Parameters type mismatch: " + expression.toString() + '-' + expression_type.toString() + " and " + variable_name.toString() + '-' + variable_type.toString());

        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "writeHeap(" + variable_name.toString() + ", " + expression.toString()+ ")";
    }
}
