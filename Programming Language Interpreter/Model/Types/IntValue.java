package Model.Types;

public class IntValue implements IValue {
    private final int value;

    public IntValue() {
        value = 0;
    }

    public IntValue(int value){
        this.value = value;
    }

    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof IntValue)
            return value == ((IntValue) another).getValue();
        return false;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return "" + value;
    }
}
