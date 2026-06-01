import java.util.ArrayList;
import java.util.HashMap;

import type.*;

public class Value {
    private Value op1; // arith operand1
    private Value op2; // arith operand2
    private String op; // binop / unaryop
    private int intValue; // value of int
    private String symbolValue; // value of symbolic value
    private Type type; // the type of this value
    private boolean inputRelated; // whether this value is inputrelated
    private boolean isConst;
    private HashMap<Integer, Value> arrIdxValueMap; //index, value
    private int arraySize; // the size of an array
    private String valueName; // the name of this value
    private Scope scope; // which scope this value belongs to
    private Value parent;

    Value(Type type, String name, Scope scope, int intValue) {
        this.type = type;
        this.valueName = name;
        this.scope = scope;
        this.intValue = intValue;
        inputRelated = false;
        scope.put(name, this);
        this.op1 = null;
        this.op2 = null;
        this.op = null;
    }

    Value(Type type, String name, Scope scope, String symbolValue) {
        this.type = type;
        this.valueName = name;
        this.scope = scope;
        this.symbolValue = symbolValue;
        inputRelated = true;
        scope.put(name, this);
        this.op1 = null;
        this.op2 = null;
        this.op = null;
    }

    Value(Type type, String name, Scope scope) {
        this.type = type;
        this.valueName = name;
        this.scope = scope;
        if (type instanceof ArrayType) {
            arraySize = ((ArrayType) type).getNum_elements();
            arrIdxValueMap = new HashMap<>();
        } else {
            assert(false);
        }
    }

    Value(Value v1, Value v2, String op) {
        if (v1.isInputRelated() || v2.isInputRelated()) {
            inputRelated = true;
        }

        assert(v1.getType() == v2.getType());

        this.type = v1.getType();
        this.op = op;
        this.op1 = v1;
        this.op2 = v2;
    }

    Value(Value v1, String op) {
        if (v1.isInputRelated()) {
            inputRelated = true;
        }

        this.type = v1.getType();
        this.op = op;
        this.op1 = v1;
        this.op2 = null;
    }

    public boolean isInputRelated() {
        return inputRelated;
    }

    public boolean isConst() {
        return isConst;
    }

    /*
     * set element of an array
    */
    public boolean setElement(int i, Value v) {
        assert(type instanceof ArrayType);

        if (arraySize != 0) {
            if (i > arraySize) {
                return false;
            }
        }

        if (arrIdxValueMap.get(i) != null) {
            arrIdxValueMap.get(i).setParent(null);
        }

        arrIdxValueMap.put(i, v);
        v.setParent(this);
        return true;
    }

    public HashMap<Integer, Value> getLi() {
        return arrIdxValueMap;
    }

    public void setParent(Value v) {
        parent = v;
    }

    public Value getParent() {
        return parent;
    }

    /*
     * get element from an array
    */
    public Value getElement(int i) {
        assert(type instanceof ArrayType);

        if (arrIdxValueMap.containsKey(i)) {
            return arrIdxValueMap.get(i);
        }
        return null; // undefined behavior error !
    }

    public int getArraySize() {
        return arraySize;
    }

    public String getSymbolValue() {
        return symbolValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return valueName;
    }

    public Value getOp1() {
        return op1;
    }

    public Value getOp2() {
        return op2;
    }

    public String getOp() {
        return op;
    }

    public static Value copy(Value oldValue, Scope scope) {
        Value newValue = null;
        if (!oldValue.isInputRelated()) {
            if (oldValue.getType().isArrayTy()) {
                newValue = new Value(oldValue.getType(), oldValue.getName(), scope);
                for (int i = 0; i < oldValue.getArraySize(); i ++) {
                    if (oldValue.getElement(i) != null) {
                        Value newEleValue = copy(oldValue.getElement(i), scope);
                        newValue.setElement(i, newEleValue);
                    }
                }
            } else {
                newValue = new Value(oldValue.getType(), oldValue.getName(), scope, oldValue.getIntValue());
            }                
        } else {
            System.out.println(oldValue.getType() == null);
            // if (oldValue.getType().isArrayTy()) {
            //     newValue = new Value(oldValue.getType(), oldValue.getName(), scope);
            //     for (int i = 0; i < oldValue.getArraySize(); i ++) {
            //         if (oldValue.getElement(i) != null) {
            //             Value newEleValue = copy(oldValue.getElement(i), scope);
            //             newValue.setElement(i, newEleValue);
            //         }
            //     }
            // } else {
            if (oldValue.getOp1() == null) {
                assert(oldValue.getSymbolValue() != null);
                newValue = new Value(oldValue.getType(), oldValue.getName(), scope, oldValue.getSymbolValue());
            } else {
                Value newOp1 = Value.copy(oldValue.getOp1(), scope);
                String newOp = oldValue.getOp();
                if (oldValue.getOp2() != null) {
                    Value newOp2 = Value.copy(oldValue.getOp2(), scope);
                    newValue = new Value(newOp1, newOp2, newOp);
                } else {
                    newValue = new Value(newOp1, newOp);
                }
            }
            // }
        }

        return newValue;
    }

    public void print() {
        if (op1 != null) {
            op1.print();
        }

        if (op != null) {
            System.out.println(" " + op + " ");
        }

        if (op2 != null) {
            op2.print();
        }

        if (op1 == null && op2 == null) {
            if (type.isArrayTy()) {
                System.out.println(valueName + " has " + arrIdxValueMap.size() + " elements.");
            } else {
                if (inputRelated) {
                    System.out.println(symbolValue);
                } else {
                    System.out.println(intValue);
                }
            }
        }
        
    }

}
