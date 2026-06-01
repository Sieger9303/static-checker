package type;

public class ArrayType extends Type{
    private Type contained;
    private int num_elements;
    private int intContains;

    public Type getEleType() {
        return contained;
    }

    public int getNum_elements() {
        return num_elements;
    }

    public int getIntContains() {
        return intContains;
    }

    public ArrayType(Type contained, int num_elements) {
        this.contained = contained;
        this.num_elements = num_elements;
        if (contained.isIntType()) {
            intContains = num_elements;
        } else {
            intContains = ((ArrayType) contained).getIntContains() * num_elements;
        }
    }

    @Override
    public String toString() {
        return "[" + num_elements + " x " + contained.toString() + "]";
    }
}
