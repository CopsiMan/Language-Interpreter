package Model.Types;

public class BoolValue implements IValue {
    boolean value;

    public BoolValue() {
        this.value = false;
    }

    public BoolValue(boolean value) {
        this.value = value;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof BoolValue)
            return java.util.Objects.equals(value,((BoolValue) another).getValue());
        return false;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
