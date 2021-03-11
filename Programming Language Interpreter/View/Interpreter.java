package View;

import Controller.Controller;
import Model.Expression.*;
import Model.Statement.*;
import Model.Types.*;
import Repo.IRepository;
import Repo.Repository;

import java.util.Arrays;

public class Interpreter {
    public static void main(String[] args) {

        IRepository repo = new Repository("log.txt");
        Controller controller = new Controller(repo);

        var ex1 = example1();
        var ex2 = example2();
        var ex3 = example3();
        var ex4 = example4();
        var heap_alloc = heap_allocation_1_4();
        var heap_read = heap_reading_1_5();
        var heap_write = heap_writing_1_6();
        var garbage = garbage_collector_2();
        var while_ex = while_statement_3();
        var fork_ex = fork_ex();

        try {
            controller.add(ex1);
            controller.add(ex2);
            controller.add(ex3);
            controller.add(ex4);
            controller.add(heap_alloc);
            controller.add(heap_read);
            controller.add(heap_write);
            controller.add(garbage);
            controller.add(while_ex);
            controller.add(fork_ex);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunCommand("1", ex1.toString(), controller));
        menu.addCommand(new RunCommand("2", ex2.toString(), controller));
        menu.addCommand(new RunCommand("3", ex3.toString(), controller));
        menu.addCommand(new RunCommand("4", ex4.toString(), controller));
        menu.addCommand(new RunCommand("5", heap_alloc.toString(), controller));
        menu.addCommand(new RunCommand("6", heap_read.toString(), controller));
        menu.addCommand(new RunCommand("7", heap_write.toString(), controller));
        menu.addCommand(new RunCommand("8", garbage.toString(), controller));
        menu.addCommand(new RunCommand("9", while_ex.toString(), controller));
        menu.addCommand(new RunCommand("10", fork_ex.toString(), controller));
        menu.show();
    }

    public static IStatement reference_test_1() {
        IStatement[] statements = {new VariableDeclarationStatement("a", new IntType()),
                new VariableDeclarationStatement("b", new IntType())};
        return assemble(statements);
    }

    public static IStatement reference_test_2() {
        IStatement[] statements = {
                new VariableDeclarationStatement("a", new ReferenceType(new IntType())),
                new VariableDeclarationStatement("b", new ReferenceType((new ReferenceType(new StringType()))))};
        return assemble(statements);
    }

    public static IStatement heap_allocation_1_4() {
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

    public static IStatement garbage_collector_2() {
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

    public static IStatement garbage_collector_fork() {
        IStatement[] fork_statement = {
                new VariableDeclarationStatement("b", new IntType()),
                new NewStatement("a", new VariableExpression("v")),
                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))),
        };
        IStatement[] statements = {
                new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new NewStatement("v", new ValueExpression(new IntValue(20))),
                new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))),
                new ForkStatement(assemble(fork_statement)),
                new VariableDeclarationStatement("b", new IntType()),
                new PrintStatement(new VariableExpression("v")),
                new NewStatement("v", new ValueExpression(new IntValue(30))),
        };
        return assemble(statements);
    }

    public static IStatement assemble(IStatement[] statements) {
        if (statements.length == 1)
            return statements[0];
        return new CompoundStatement(statements[0], assemble(Arrays.copyOfRange(statements, 1, statements.length)));
    }

    public static IStatement while_fork_statement() {
        IStatement[] statements = {
                new VariableDeclarationStatement("v", new IntType()),
                new AssignmentStatement("v", new ValueExpression(new IntValue(4))),
                new WhileStatement(new LogicExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                new CompoundStatement(new AssignmentStatement("v", new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))),
                                        new ForkStatement(example2())))),
                new PrintStatement(new VariableExpression("v"))
        };
        return assemble(statements);
    }

    public static IStatement while_statement_3() {
        IStatement[] statements = {
                new VariableDeclarationStatement("v", new IntType()),
                new AssignmentStatement("v", new ValueExpression(new IntValue(4))),
                new WhileStatement(new LogicExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                new AssignmentStatement("v", new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                new PrintStatement(new VariableExpression("v"))
        };
        return assemble(statements);
    }

    public static IStatement heap_reading_1_5() {
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

    public static IStatement heap_writing_1_6() {
        IStatement[] statements = {
                new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new NewStatement("v", new ValueExpression(new IntValue(20))),
                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                new WriteHeapStatement(new VariableExpression("v"), new ValueExpression(new IntValue(30))),
                new PrintStatement(new ArithmeticExpression('+', new ReadHeapExpression (new VariableExpression("v")), new ValueExpression(new IntValue(5))))
        };
        return assemble(statements);
    }


    public static IStatement example1() {
        return new CompoundStatement(new VariableDeclarationStatement("v", new BoolType()),
                new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
    }

    public static IStatement example2() {
        return new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
                        new CompoundStatement(new AssignmentStatement("a", new ArithmeticExpression('+', new ValueExpression(new IntValue(2)), new ArithmeticExpression('*', new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
                                new CompoundStatement(new AssignmentStatement("b", new ArithmeticExpression('+', new VariableExpression("a"), new ValueExpression(new IntValue(1)))), new PrintStatement(new VariableExpression("b"))))));
    }

    public static IStatement example3() {
        return new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"), new AssignmentStatement("v", new ValueExpression(new IntValue(2))), new AssignmentStatement("v", new ValueExpression(new IntValue(3)))), new PrintStatement(new VariableExpression("v"))))));
    }

    public static IStatement bad_typeCheck() {
        return new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"), new AssignmentStatement("v", new ValueExpression(new IntValue(2))), new AssignmentStatement("v", new ValueExpression(new IntValue(3)))), new PrintStatement(new VariableExpression("v"))))));
    }

    public static IStatement example4() {
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

    public static IStatement fork_ex() {
        IStatement[] statements_fork = {
                new WriteHeapStatement(new VariableExpression("a"),new ValueExpression(new IntValue(30))),
                new AssignmentStatement("v", new ValueExpression(new IntValue(32))),
                new PrintStatement(new VariableExpression("v")),
                new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
        };
        IStatement[] statements = {
                new VariableDeclarationStatement("v", new IntType()),
                new VariableDeclarationStatement("a", new ReferenceType(new IntType())),
                new AssignmentStatement("v", new ValueExpression(new IntValue(10))),
                new NewStatement("a", new ValueExpression(new IntValue(22))),
                new ForkStatement(assemble(statements_fork)),
                new PrintStatement(new VariableExpression("v")),
                new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
        };
        return assemble(statements);
    }

}
