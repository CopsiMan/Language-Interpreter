package Model.Types;

public class StringType implements IType {
    @Override
    public IValue defaultValue() {
        return new StringValue();
    }

    @Override
    public boolean equals(Object another) {
        return another instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }
}
