package Model.Statement;

import Model.ADT.IDictionary;
import Model.Expression.IExpression;
import Model.ProgramState;
import Model.Types.IType;
import Model.Types.IValue;
import Model.Types.StringType;
import Model.Types.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenReadFileStatement implements IStatement {

    IExpression expression;

    public OpenReadFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws RuntimeException {
        var file_table = state.getFileTable();
        var symbols_table = state.getSymbolsTable();
        IValue value = expression.evaluate(symbols_table,  state.getHeapTable());
        if (value.getType() instanceof StringType) {
            String file_path = ((StringValue) value).getValue();
            if (!file_table.variableAlreadyDefined(file_path)) {
                try {
                    FileReader file_reader = new FileReader(file_path);
                    BufferedReader buffered_reader = new BufferedReader(file_reader);
                    file_table.add(file_path, buffered_reader);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("File not found!");
                }
            } else {
                throw new RuntimeException("This file is already in the file-table");
            }
        } else {
            throw new RuntimeException("The expression is not StringType");
        }
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
        return "openFile(" + expression.toString() + ")";
    }
}
