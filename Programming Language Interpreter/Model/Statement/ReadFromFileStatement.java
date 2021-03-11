package Model.Statement;

import Model.ADT.IDictionary;
import Model.Expression.IExpression;
import Model.ProgramState;
import Model.Types.*;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFromFileStatement implements  IStatement {

    IExpression expression;
    String variable_name;

    public ReadFromFileStatement(IExpression expression, String variable_name) {
        this.expression = expression;
        this.variable_name = variable_name;
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        var symbols_table = state.getSymbolsTable();
        if (!symbols_table.variableAlreadyDefined(variable_name))
            throw new RuntimeException("Variable " + variable_name + " is not defined!");

        IValue should_be_string = expression.evaluate(symbols_table, state.getHeapTable());
        if (!(should_be_string instanceof StringValue))
            throw new RuntimeException("Expression: " + expression.toString() + " cannot be evaluated to a string!");

        String file_name = ((StringValue) should_be_string).getValue();
        var file_table = state.getFileTable();
        if (!file_table.variableAlreadyDefined(file_name))
            throw new RuntimeException("File not found!");

        BufferedReader buffered_reader = file_table.lookup(file_name);
        String line = buffered_reader.readLine();
        int new_value;
        if (line == null)
            new_value = 0;
        else
            new_value = Integer.parseInt(line);

        symbols_table.update(variable_name, new IntValue(new_value));

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        IType variable_type = typeEnvironment.lookup(variable_name),
                expression_type = expression.typeCheck(typeEnvironment);
        if (variable_type.equals(new IntType()))
            if (expression_type.equals(new StringType()))
                return typeEnvironment;
            else
                throw new RuntimeException("Expression: " + expression.toString() + " cannot be evaluated to a string!");
        else
            throw new RuntimeException("Variable " + variable_name + "is not an int but " + variable_type.toString());
    }

    @Override
    public String toString() {
        return "readFromFile(" + expression.toString() + ", " + variable_name + ")";
    }
}
