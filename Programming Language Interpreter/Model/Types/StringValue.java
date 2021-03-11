package Model.Types;

public class StringValue implements IValue {
    private final String value;

    public StringValue (){
        this.value = "";
    }

    public StringValue (String value) {
        this.value = value;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof StringValue)
            return value.equals(((StringValue) another).getValue());
        return false;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
