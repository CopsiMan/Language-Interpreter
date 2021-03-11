package Model.Statement;

import Model.ADT.IDictionary;
import Model.ADT.IStack;
import Model.Exceptions.AssignmentVariablesDoNotMatch;
import Model.Exceptions.VariableAlreadyDefinedException;
import Model.Expression.IExpression;
import Model.Types.IType;
import Model.Types.IValue;
import Model.ProgramState;

public class AssignmentStatement implements IStatement {
    String variable_id;
    IExpression expression;
    public AssignmentStatement(String variable_id, IExpression expression) {
        this.variable_id = variable_id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws VariableAlreadyDefinedException {
        IStack<IStatement> stack = state.getStack();
        IDictionary<String , IValue> symbols_table = state.getSymbolsTable();
        if (symbols_table.variableAlreadyDefined(variable_id)) {
            IValue value = expression.evaluate(symbols_table, state.getHeapTable());
            IType type = symbols_table.lookup(variable_id).getType();
            if (value.getType().equals(type)) {
                symbols_table.update(variable_id, value);
            } else
                throw new AssignmentVariablesDoNotMatch("Declared type of variable " + variable_id + " and type of  the assigned expression do not match");
        }
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        IType variable_id_type = typeEnvironment.lookup(variable_id),
                expression_type = expression.typeCheck(typeEnvironment);

        if (variable_id_type.equals(expression_type))
            return typeEnvironment;

        throw new RuntimeException("Operands type mismatch: " + variable_id + '-' + variable_id_type.toString() + " and " + expression.toString() + '-' + expression_type.toString());

    }

    @Override
    public String toString() {
        return variable_id + " = " + expression.toString();
    }
}
