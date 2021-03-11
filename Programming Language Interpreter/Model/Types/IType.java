package Model.Types;

public interface IType {
    IValue defaultValue();
    boolean equals(Object another);
}
