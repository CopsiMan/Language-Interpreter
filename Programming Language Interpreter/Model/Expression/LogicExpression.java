package Model.Expression;

import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.Exceptions.NotABoolException;
import Model.Exceptions.NotAnIntException;
import Model.Types.*;

public class LogicExpression implements IExpression {
    IExpression lhs;
    IExpression rhs;
    String operator;

    public LogicExpression(IExpression lhs, IExpression rhs, String operator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    @Override
    public IValue evaluate(IDictionary<String, IValue> table, IHeap<Integer, IValue> heap_table) {
        IValue left_value, right_value;
        left_value = lhs.evaluate(table,heap_table );
        if (left_value.getType().equals(new BoolType())) {
            right_value = rhs.evaluate(table,heap_table );
            if (right_value.getType().equals(new BoolType())) {
                BoolValue left_bool = (BoolValue) left_value;
                BoolValue right_bool = (BoolValue) right_value;
                boolean left, right;
                left = left_bool.getValue();
                right = right_bool.getValue();
                switch (operator) {
                    case "&" -> {
                        return new BoolValue(left && right);
                    }
                    case "|" -> {
                        return new BoolValue(left || right);
                    }
                }
            } else
                throw new NotABoolException(" Right-hand side operator " + right_value.toString() + " is not a bool");
        } else if (left_value.getType().equals(new IntType())) {
            right_value = rhs.evaluate(table,heap_table );
            if (right_value.getType().equals(new IntType())) {
                IntValue left_int = (IntValue) left_value;
                IntValue right_int = (IntValue) right_value;
                int left, right;
                left = left_int.getValue();
                right = right_int.getValue();
                switch (operator) {
                    case "<" -> {
                        return new BoolValue(left < right);
                    }
                    case "<=" -> {
                        return new BoolValue(left <= right);
                    }
                    case "==" -> {
                        return new BoolValue(left == right);
                    }
                    case "!=" -> {
                        return new BoolValue(left != right);
                    }
                    case ">" -> {
                        return new BoolValue(left > right);
                    }
                    case ">=" -> {
                        return new BoolValue(left >= right);
                    }
                }
            } else
                throw new NotAnIntException(" Right-hand side operator " + right_value.toString() + " is not an int");
        } else
            throw new RuntimeException(" Left-hand side operator " + left_value.toString() + " is not a bool or an int.");
        return null;
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnvironment) {
        IType lhs_type = lhs.typeCheck(typeEnvironment),
                rhs_type = rhs.typeCheck(typeEnvironment);

        if (!lhs_type.equals(rhs_type))
            throw new RuntimeException("Operands type mismatch: " + lhs.toString() + '-' + lhs_type.toString() + " and " + rhs.toString() + '-' + rhs_type.toString());

        return new BoolType();

    }

    @Override
    public String toString() {
        return lhs.toString() + " " + operator + " " + rhs.toString();
    }
}
