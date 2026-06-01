package type;

public class Type {
    public static class VoidType extends Type {
        private static VoidType voidType = new VoidType();

        private VoidType(){

        }

        public static VoidType getVoidType(){
            return voidType;
        }

        @Override
        public String toString() {
            return "void";
        }
    }

    public boolean isArrayTy() {
        return this instanceof ArrayType;
    }

    public boolean isIntType(){
        return this instanceof IntType;
    }
}
