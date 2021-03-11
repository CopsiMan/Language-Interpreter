package Model.Statement;

import Model.ADT.IDictionary;
import Model.Exceptions.VariableAlreadyDefinedException;
import Model.ProgramState;
import Model.Types.IType;

public class NopStatement implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws VariableAlreadyDefinedException {
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        return typeEnvironment;
    }
}
