package Model.Statement;

import Model.ADT.IDictionary;
import Model.Expression.IExpression;
import Model.ProgramState;
import Model.Types.IType;
import Model.Types.IValue;
import Model.Types.StringType;
import Model.Types.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFile implements  IStatement {

    IExpression expression;

    public CloseReadFile(IExpression expression){
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws RuntimeException, IOException {
        var symbols_table = state.getSymbolsTable();
        IValue should_be_string = expression.evaluate(symbols_table, state.getHeapTable() );
        if(!(should_be_string instanceof StringValue))
            throw  new RuntimeException("Expression: " + expression.toString() + " cannot be evaluated to a string!");

        String file_name = ((StringValue) should_be_string).getValue();
        var file_table = state.getFileTable();
        if (!file_table.variableAlreadyDefined(file_name))
            throw new RuntimeException("File not found!");

        BufferedReader reader = file_table.lookup(file_name);
        reader.close();

        file_table.delete(file_name);

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        IType expression_type = expression.typeCheck(typeEnvironment);
        if(expression_type.equals(new StringType()))
            return typeEnvironment;

        throw  new RuntimeException("Expression: " + expression.toString() + " cannot be evaluated to a string!");
    }

    @Override
    public String toString() {
        return "closeFile(" + expression.toString() + ")";
    }
}
