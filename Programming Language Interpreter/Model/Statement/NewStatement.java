package Model.Statement;

import Model.ADT.IDictionary;
import Model.Expression.IExpression;
import Model.ProgramState;
import Model.Types.IType;
import Model.Types.IValue;
import Model.Types.ReferenceType;
import Model.Types.ReferenceValue;

import java.io.IOException;

public class NewStatement implements  IStatement {

    String variable_name;
    IExpression expression;

    public NewStatement(String variable_name, IExpression expression) {
        this.variable_name = variable_name;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        var symbols_table = state.getSymbolsTable();
        if (!symbols_table.variableAlreadyDefined(variable_name))
            throw new RuntimeException("Variable" + variable_name + " is not defined in the symbols table");

        IValue variable_value = symbols_table.lookup(variable_name);
        if (!(variable_value.getType() instanceof ReferenceType))
            throw new RuntimeException("Value " + variable_value.toString() + " is not of reference type");

        IValue expression_value = expression.evaluate(symbols_table, state.getHeapTable());
        if (!(expression_value.getType().equals(((ReferenceValue) variable_value).getLocationType())))
            throw new RuntimeException("Type mismatch between expression " + expression_value.getType().toString() + " and " + ((ReferenceValue) variable_value).getLocationType().toString());


        var heap_table = state.getHeapTable();
        int address = heap_table.getNextFreeAddress();
        heap_table.add(address, expression_value);

        symbols_table.update(variable_name, new ReferenceValue(address, ((ReferenceValue) variable_value).getLocationType()));

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        IType variable_type = typeEnvironment.lookup(variable_name),
                expression_type = expression.typeCheck(typeEnvironment);
        if (variable_type.equals(new ReferenceType(expression_type)))
            return typeEnvironment;

        throw new RuntimeException("Type mismatch between expression " + variable_name + '-' + variable_type.toString() + " and " + expression.toString() + '-' + expression_type.toString());
    }

    @Override
    public String toString() {
        return "new(" + variable_name + ", " + expression.toString() + ")";
    }
}
