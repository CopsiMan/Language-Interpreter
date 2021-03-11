package Model.Statement;

import Model.ADT.Dictionary;
import Model.ADT.IDictionary;
import Model.ADT.Stack;
import Model.ProgramState;
import Model.Types.IType;

import java.io.IOException;

public class ForkStatement implements IStatement {
    private final IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws RuntimeException, IOException {
        return new ProgramState(new Stack<IStatement>(), state.getSymbolsTable().deepCopy(), state.getOutput(), state.getFileTable(), state.getHeapTable(), statement);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        statement.typeCheck(typeEnvironment.deepCopy());
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "fork(" + this.statement.toString() + ")";
    }
}
