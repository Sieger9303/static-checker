package type;

import java.util.ArrayList;
public class FunctionType extends Type {
    private Type retTy;
    private ArrayList<Type> paramsType;

    public FunctionType(Type retTy, ArrayList<Type> paramsType) {
        this.retTy = retTy;
        this.paramsType = paramsType;
    }

    public Type getRetTType() {
        return retTy;
    }

    public ArrayList<Type> getParamsTypes() {
        return paramsType;
    }
}
