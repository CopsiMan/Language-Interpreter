package Model.Statement;

import Model.ADT.IDictionary;
import Model.ADT.IList;
import Model.Exceptions.VariableAlreadyDefinedException;
import Model.Expression.IExpression;
import Model.Types.IType;
import Model.Types.IValue;
import Model.ProgramState;

public class PrintStatement  implements  IStatement {
    IExpression expression;

    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    public String toString() {
        return "print(" + expression.toString() + ")";
    }

    public ProgramState execute(ProgramState state) throws VariableAlreadyDefinedException {
        IList<IValue> output = state.getOutput();
        IDictionary<String, IValue> symbols_table = state.getSymbolsTable();
        output.add(expression.evaluate(symbols_table, state.getHeapTable()));
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        expression.typeCheck(typeEnvironment);
        return typeEnvironment;
    }
}
