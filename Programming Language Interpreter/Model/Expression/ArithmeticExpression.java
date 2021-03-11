package Model.Expression;

import Model.ADT.IDictionary;
import Model.ADT.IHeap;
import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.NotAnIntException;
import Model.Types.IType;
import Model.Types.IValue;
import Model.Types.IntType;
import Model.Types.IntValue;

public class ArithmeticExpression implements IExpression {
    IExpression rhs;
    IExpression lhs;
    char operator; //1-plus, 2-minus, 3-multiply, 4-divide

    public ArithmeticExpression(char op, IExpression left, IExpression right) {
        lhs = left;
        rhs = right;
        operator = op;
    }

    @Override
    public IValue evaluate(IDictionary<String, IValue> table, IHeap<Integer, IValue> heap_table) {
        IValue left, right;
        left = lhs.evaluate(table,heap_table );
        if (left.getType().equals(new IntType())) {
            right = rhs.evaluate(table,heap_table );
            if (right.getType().equals(new IntType())) {
                IntValue left_int = (IntValue) left;
                IntValue right_int = (IntValue) right;
                int operand_left, operand_right;
                operand_left = left_int.getValue();
                operand_right = right_int.getValue();
                switch (this.operator) {
                    case '+' -> {
                        return new IntValue(operand_left + operand_right);
                    }
                    case '-' -> {
                        return new IntValue(operand_left - operand_right);
                    }
                    case '*' -> {
                        return new IntValue(operand_left * operand_right);
                    }
                    case '/' -> {
                        if (operand_right == 0)
                            throw new DivisionByZeroException("Trying to divide " + operand_left + " by 0");
                        else return new IntValue(operand_left + operand_right);
                    }
                }
            } else throw new NotAnIntException("Right-hand side operator " + right.toString() + " is not an int");
        } else throw new NotAnIntException("Left-hand side operator " + left.toString() + " is not an int");
        return new IntValue();
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnvironment) {
        IType lhs_type = lhs.typeCheck(typeEnvironment),
                rhs_type = rhs.typeCheck(typeEnvironment);

        if (!lhs_type.equals(new IntType()))
            throw new RuntimeException("First operand is not an int");

        if (!rhs_type.equals(new IntType()))
            throw new RuntimeException("Second operand is not an int");

        return new IntType();
    }

    @Override
    public String toString() {
        return lhs.toString() + " " + operator + " " + rhs.toString();
    }
}
