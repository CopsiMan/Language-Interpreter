package Model.Statement;

import Model.ADT.IDictionary;
import Model.Exceptions.NotABoolException;
import Model.Exceptions.VariableAlreadyDefinedException;
import Model.Expression.IExpression;
import Model.ProgramState;
import Model.Types.BoolType;
import Model.Types.BoolValue;
import Model.Types.IType;
import Model.Types.IValue;

public class IfStatement implements IStatement {
    IExpression expression;
    IStatement statement_if_true;
    IStatement statement_if_false;

    public IfStatement(IExpression expression, IStatement statement_if_true, IStatement statement_if_false) {
        this.expression = expression;
        this.statement_if_true = statement_if_true;
        this.statement_if_false = statement_if_false;
    }

    @Override
    public ProgramState execute(ProgramState state) throws VariableAlreadyDefinedException {
        IDictionary<String, IValue> symbols_table = state.getSymbolsTable();
        var stack = state.getStack();
        IValue result = expression.evaluate(symbols_table,  state.getHeapTable());
        if (result instanceof BoolValue) {
            boolean res = ((BoolValue) result).getValue();
            if (res) {
//                state = statement_if_true.execute(state);
                stack.push(statement_if_true);
            } else {
//                state = statement_if_false.execute(state);
                stack.push(statement_if_false);
            }
        } else throw new NotABoolException("If statement needs a boolean value for its clause");
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        IType expression_type = expression.typeCheck(typeEnvironment);
        if (expression_type.equals(new BoolType())) {
            statement_if_true.typeCheck(typeEnvironment.deepCopy());
            statement_if_false.typeCheck(typeEnvironment.deepCopy());
            return typeEnvironment;
        }
        throw new NotABoolException("If statement needs a boolean value for its clause but it got: " + expression.toString() + "-" + expression_type.toString());
    }

    @Override
    public String toString() {
        return "IF( " + expression.toString() + " ) THEN ( " + statement_if_true.toString() + " ) ELSE ( " + statement_if_false.toString() + " )";
    }
}
