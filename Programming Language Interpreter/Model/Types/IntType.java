package Model.Types;

public class IntType implements IType {

    public String toString() {
        return "int";
    }

    @Override
    public IValue defaultValue() {
        return new IntValue();
    }

    @Override
    public boolean equals(Object another) {
        return another instanceof IntType;
    }
}
