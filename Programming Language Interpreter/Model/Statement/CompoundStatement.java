package Model.Statement;

import Model.ADT.IDictionary;
import Model.Exceptions.VariableAlreadyDefinedException;
import Model.ADT.IStack;
import Model.ProgramState;
import Model.Types.IType;

public class CompoundStatement implements IStatement{
    IStatement left;
    IStatement right;

    public CompoundStatement(IStatement left, IStatement right){
        this.left = left;
        this.right = right;
    }

    public ProgramState execute(ProgramState state) throws VariableAlreadyDefinedException {
        IStack<IStatement> stack = state.getStack();
        stack.push(right);
        stack.push(left);
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment) {
        return right.typeCheck(left.typeCheck(typeEnvironment));
    }

    public String toString() {
        return left.toString() + " | " + right.toString();
//                "(" + left.toString() + ";" + right.toString() + ")";
    }
}
