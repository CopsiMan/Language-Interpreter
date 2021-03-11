package GUI;

import Controller.Controller;
import Model.ADT.*;
import Model.Expression.ReadHeapExpression;
import Model.Expression.ValueExpression;
import Model.Expression.VariableExpression;
import Model.ProgramState;
import Model.Statement.*;
import Model.Types.IValue;
import Model.Types.IntType;
import Model.Types.IntValue;
import Model.Types.ReferenceType;
import Repo.Repository;
import View.Interpreter;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ProgramListController implements Initializable {

    @FXML
    private ListView<Controller> controllers;
    @FXML
    private Button select_program;
    List<Controller> controllers_list;
    static Repository repo1;
    static Controller controller1;

    public ProgramListController () {
        controllers_list = new ArrayList<>();
    }

    public ObservableList<IStatement> populate_list (List<IStatement> statements) {
        return FXCollections.observableArrayList(statements);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setup_list();
        ObservableList<Controller> cont = FXCollections.observableArrayList();
        cont.addAll(controllers_list);
        controllers.setItems(cont);
//        System.out.println(controllers_list);
        select_program.setOnAction(e -> {
            Stage run_program = new Stage();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RunProgramWindow.fxml"));
            RunProgramWindow run_window = new RunProgramWindow(controllers.getSelectionModel().getSelectedItem().copy());
            loader.setController(run_window);
//            Callback<Class<?>, Object> controllerFactory = type -> {
//                if (type == RunProgramWindow.class) {
//                    return new RunProgramWindow(myPrgList.);
//                } else {
//                    try {
//                        return type.newInstance() ;
//                    } catch (Exception exc) {
//                        System.err.println("Could not create controller for "+type.getName());
//                        throw new RuntimeException(exc);
//                    }
//                }
//            };
            try {
                root = loader.load();
                Scene scene = new Scene(root, 1200, 780);
                run_program.setTitle("Program state details");
                run_program.setScene(scene);
                run_program.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public void setup_list () {
        ArrayList<IStatement> statements = new ArrayList<IStatement>();
        statements.add(Interpreter.example1());
        statements.add(Interpreter.example2());
        statements.add(Interpreter.example3());
        statements.add(Interpreter.example4());
        statements.add(Interpreter.heap_allocation_1_4());
        statements.add(Interpreter.heap_reading_1_5());
        statements.add(Interpreter.heap_writing_1_6());
        statements.add(Interpreter.garbage_collector_2());
        statements.add(Interpreter.garbage_collector_fork());
        statements.add(Interpreter.while_statement_3());
        statements.add(Interpreter.fork_ex());
        statements.add(Interpreter.while_fork_statement());
//        System.out.println(statements);
        for (IStatement statement: statements) {
            var repo = new Repository("log_GUI.txt");
            var cont = new Controller(repo);
            try{
                cont.addToRepo(statement);
                controllers_list.add(cont);
            }
            catch (RuntimeException e){
//                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Current statement alert: " + statement.toString());
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                Button confirm = (Button) alert.getDialogPane().lookupButton( ButtonType.OK );
                confirm.setDefaultButton(false);
                confirm.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                alert.showAndWait();
            }
        }
//        System.out.println(controllers_list);

//        IStatement statement =
//                garbage_collector_fork();
//        //Interpreter.example4();
//        try {
//            statement.typeCheck(new Dictionary<>());
//        } catch (RuntimeException e) {
//            System.out.println(e.getMessage());
//        }

//        ProgramState program_state = new ProgramState(new Stack<IStatement>(), new Dictionary<String, IValue>(), new MyList<IValue>(), new FileTable<>(), new HeapTable<Integer, IValue>(), statement);
//        repo1 = new Repository("logGUI.txt");
//        controller1 = new Controller(repo1);
//        controller1.addToRepo(statement);

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

    private static IStatement assemble(IStatement[] statements) {
        if (statements.length == 1)
            return statements[0];
        return new CompoundStatement(statements[0], assemble(Arrays.copyOfRange(statements, 1, statements.length)));
    }
}
