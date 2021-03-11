package Model.Types;

public class ReferenceType implements IType {
    IType inner;

    public ReferenceType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return inner;
    }

    @Override
    public IValue defaultValue() {
        return new ReferenceValue(0, inner);
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof ReferenceType) {
            return inner.equals(((ReferenceType) another).getInner());
        } else return false;
    }

    @Override
    public String toString() {
        return "reference(" + inner.toString() + ")";
    }
}
