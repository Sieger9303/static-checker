package type;

public class IntType extends Type{
    private int numBits;
    public static IntType i32 = new IntType(32);

    public static IntType getI32() {
        return i32;
    }

    private IntType(int bits) {
        this.numBits = bits;
    }

    public int getNumBits() {
        return numBits;
    }

    @Override
    public String toString() {
        return "i" + this.numBits;
    }
}
