package Model.Statement;

import Model.ADT.IDictionary;
import Model.Exceptions.VariableAlreadyDefinedException;
import Model.ProgramState;
import Model.Types.IType;

import java.io.IOException;
import java.lang.reflect.Type;

public interface IStatement {
    ProgramState execute (ProgramState state) throws RuntimeException, IOException;
    IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnvironment);
}
