package Model.Statement;

import Model.ADT.IDictionary;
import Model.Exceptions.NotABoolException;
import Model.Expression.IExpression;
import Model.ProgramState;
import Model.Types.BoolType;
import Model.Types.BoolValue;
import Model.Types.IType;
import Model.Types.IValue;

import java.io.IOException;

public class WhileStatement implements IStatement {

    IExpression condition;
    IStatement statement;

    public WhileStatement(IExpression condition, IStatement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws RuntimeException, IOException {
        var symbols_table = state.getSymbolsTable();
        var heap = state.getHeapTable();

        IValue condition_value = condition.evaluate(symbols_table, heap);
        if (!(condition_value instanceof BoolValue))
            throw new RuntimeException("Condition: " + condition_value.toString() + " can not be evaluated to bool value!");

        if (!((BoolValue) condition_value).getValue())
            return null;

        var stack = state.getStack();
        stack.push(this);
        stack.push(statement);

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        IType condition_type = condition.typeCheck(typeEnvironment);
        if (condition_type.equals(new BoolType())) {
            statement.typeCheck(typeEnvironment.deepCopy());
            return typeEnvironment;
        }
        throw new NotABoolException("While statement needs a boolean value for its clause but it got: " + condition.toString() + "-" + condition_type.toString());
    }

    @Override
    public String toString() {
        return "while(" + condition.toString() + ") {" + statement.toString() + "}";
    }
}
