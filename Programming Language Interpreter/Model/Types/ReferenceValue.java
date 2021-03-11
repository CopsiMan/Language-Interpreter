package Model.Types;

public class ReferenceValue implements IValue {
    int address;
    IType location_type;

    public ReferenceValue(int address, IType location_type) {
        this.address = address;
        this.location_type = location_type;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public IType getType() {
        return new ReferenceType(location_type);
    }

    public IType getLocationType() {
        return location_type;
    }

    @Override
    public boolean equals(Object another) {
        return false;
    }

    @Override
    public String toString() {
        return "(" + address + ", " + location_type.toString() + ")";
    }
}
