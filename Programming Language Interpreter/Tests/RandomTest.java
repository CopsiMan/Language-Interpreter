package Tests;

import Controller.Controller;
import Model.Expression.*;
import Model.Statement.*;
import Model.Types.*;
import Repo.IRepository;
import Repo.Repository;
import View.Interpreter;

import java.io.IOException;
import java.util.Arrays;

//import org.junit.jupiter.api.Test;

public class RandomTest {
    //    @Test
    public static void main(String[] args) {
        IRepository repo = new Repository("log_test.txt");
        Controller controller = new Controller(repo);
//        controller.add(test_exp_computation());
        var program = Interpreter.bad_typeCheck();
//        System.out.println(program);
        try {
            controller.add(program);
            controller.addProgram(0);
            controller.allStep();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
//        var state = controller.getProgramState(0);
//        while (!state.getStack().isEmpty()) {
//            System.out.println(state.toLogString());
//            state = controller.oneStep(state);
//        }
//        System.out.println(state.toLogString());
    }

    private static IStatement reference_test_1() {
        IStatement[] statements = {new VariableDeclarationStatement("a", new IntType()),
                new VariableDeclarationStatement("b", new IntType())};
        return assemble(statements);
    }

    private static IStatement reference_test_2() {
        IStatement[] statements = {
                new VariableDeclarationStatement("a", new ReferenceType(new IntType())),
                new VariableDeclarationStatement("b", new ReferenceType((new ReferenceType(new StringType()))))};
        return assemble(statements);
    }

    private static IStatement heap_allocation_1_4() {
        IStatement[] statements = {
                new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new NewStatement("v", new ValueExpression(new IntValue(20))),
                new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))),
                new NewStatement("a", new VariableExpression("v")),
                new PrintStatement(new VariableExpression("v")),
                new PrintStatement(new VariableExpression("a"))
        };
        return assemble(statements);
    }

    private static IStatement garbage_collector_2() {
        IStatement[] statements = {
                new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new NewStatement("v", new ValueExpression(new IntValue(20))),
                new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))),
                new NewStatement("a", new VariableExpression("v")),
                new NewStatement("v", new ValueExpression(new IntValue(30))),
                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))))
        };
        return assemble(statements);
    }

    private static IStatement assemble(IStatement[] statements) {
        if (statements.length == 1)
            return statements[0];
        return new CompoundStatement(statements[0], assemble(Arrays.copyOfRange(statements, 1, statements.length)));
    }

    private static IStatement while_statement_3() {
        IStatement[] statements = {
                new VariableDeclarationStatement("v", new IntType()),
                new AssignmentStatement("v", new ValueExpression(new IntValue(4))),
                new WhileStatement(new LogicExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                new AssignmentStatement("v", new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1))))))
        };
        return assemble(statements);
    }

    private static IStatement heap_reading_1_5() {
        IStatement[] statements = {
                new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new NewStatement("v", new ValueExpression(new IntValue(20))),
                new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))),
                new NewStatement("a", new VariableExpression("v")),
                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                new PrintStatement(new ArithmeticExpression('+', new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))), new ValueExpression(new IntValue(5))))
        };
        return assemble(statements);
    }

    private static IStatement heap_writing_1_6() {
        IStatement[] statements = {
                new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new NewStatement("v", new ValueExpression(new IntValue(20))),
                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                new WriteHeapStatement(new VariableExpression("v"), new ValueExpression(new IntValue(30))),
                new PrintStatement(new ArithmeticExpression('+', new ReadHeapExpression (new VariableExpression("v")), new ValueExpression(new IntValue(5))))
        };
        return assemble(statements);
    }


    private static IStatement test_exp_computation() {
        return new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
                        new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                                new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new IntValue(4))),
                                        new CompoundStatement(new AssignmentStatement("b", new ValueExpression(new IntValue(4))),
                                                new CompoundStatement(new IfStatement(new LogicExpression(new VariableExpression("a"), new VariableExpression("b"), ">="),
                                                        new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                                                        new AssignmentStatement("v", new ValueExpression(new IntValue(3)))),
                                                        new PrintStatement(new VariableExpression("v"))))))));
    }

    private static IStatement test_IO() {
        return new CompoundStatement(new VariableDeclarationStatement("file_name", new StringType()),
                new CompoundStatement(new AssignmentStatement("file_name", new ValueExpression(new StringValue("src/Model/Files/test.in"))),
                        new CompoundStatement(new OpenReadFileStatement(new VariableExpression("file_name")),
                                new CompoundStatement(new VariableDeclarationStatement("var", new IntType()),
                                        new CompoundStatement(new ReadFromFileStatement(new VariableExpression("file_name"), "var"),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("var")),
                                                        new CompoundStatement(new ReadFromFileStatement(new VariableExpression("file_name"), "var"),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("var")),
                                                                        new CloseReadFile(new VariableExpression("file_name"))))))))));
    }

    private static IStatement example3() {
        return new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"), new AssignmentStatement("v", new ValueExpression(new IntValue(2))), new AssignmentStatement("v", new ValueExpression(new IntValue(3)))), new PrintStatement(new VariableExpression("v"))))));
    }
}
