package Model.Types;

public class BoolType implements IType{
    @Override
    public IValue defaultValue() {
        return new BoolValue();
    }

    @Override
    public boolean equals(Object another) {
        return another instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }
}
