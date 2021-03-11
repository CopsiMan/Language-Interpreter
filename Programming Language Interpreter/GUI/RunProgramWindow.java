package GUI;

import Controller.Controller;
import Model.ProgramState;
import Model.Statement.IStatement;
import Model.Types.IValue;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.net.URL;
import java.util.*;

public class RunProgramWindow implements Initializable {

    Controller controller;

    @FXML
    TextField number_of_programs;
    @FXML
    Button run_button;
    @FXML
    TableView<HashMap.Entry<Integer, IValue>> heapTable;
    @FXML
    TableColumn<HashMap.Entry<Integer,IValue>, String> heap_table_address;
    @FXML
    TableColumn<HashMap.Entry<Integer,IValue>, String> heap_table_value;
    @FXML
    ListView<String> outputList;
    @FXML
    ListView<String> program_states_list;
    @FXML
    ListView<String> fileTable;
    @FXML
    TableView<HashMap.Entry<String, IValue>> symbol_table;
    @FXML
    TableColumn<HashMap.Entry<String, IValue>, String> symbol_table_variable;
    @FXML
    TableColumn<HashMap.Entry<String, IValue>, String> symbol_table_value;
    @FXML
    ListView<String> execution_stack;

    public RunProgramWindow (Controller controller) {
        this.controller = controller;
//        System.out.println("this was called");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstTime();
        program_states_list.setOnMouseClicked(e -> setSymbolTableAndExecutionStack());
        run_button.setOnAction(event -> {
            try {
                controller.oneStepGUI();
            } catch (RuntimeException e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Current program alert");
                alert.setHeaderText(null);
//                alert.setContentText("Program successfully finished!");
                alert.setContentText(e.getMessage());
                Button confirm = (Button) alert.getDialogPane().lookupButton( ButtonType.OK );
                confirm.setDefaultButton(false);
                confirm.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                alert.showAndWait();
                Stage stage = (Stage) heapTable.getScene().getWindow();
//                stage.close();
            }
//            System.out.println("update");
            updateComponents();
        });
    }

    private void updateComponents() {
        if (controller.getProgramStates().size() != 0) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Current program had a problem");
//            alert.setHeaderText(null);
//            alert.setContentText("Program successfully finished!");
//            Button confirm = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
//            confirm.setDefaultButton(false);
//            confirm.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
//            alert.showAndWait();
////            Stage stage = (Stage) heapTable.getScene().getWindow();
////            stage.close();
//        } else {
//            System.out.println(controller.getProgramStates());
            setOutputList();
            setHeapTable();
            setFileTable();
            setSymbolTableAndExecutionStack();
        }
        setNumberTextField();
        setProgramStatesList();
        if (program_states_list.getSelectionModel().getSelectedItems() == null)
            program_states_list.getSelectionModel().selectFirst();

    }

    private void setProgramStatesList() {
        ObservableList<String> programs = FXCollections.observableArrayList();
        for (ProgramState state: controller.getProgramStates()) {
           programs.add("" + state.getId());
        }
        program_states_list.setItems(programs);
    }

    public void firstTime() {
        setNumberTextField();
        setHeapTable();
        setOutputList();
        setFileTable();
        setProgramStatesList();
        program_states_list.getSelectionModel().selectFirst();
        setSymbolTableAndExecutionStack();
    }

    private void setFileTable() {
        ObservableList<String> files = FXCollections.observableArrayList();
        for (String path: controller.getProgramStates().get(0).getFileTable().getContent().keySet()) {
            files.add(path.toString());
        }
        fileTable.setItems(files);

    }

    private void setHeapTable() {
        ObservableList<HashMap.Entry<Integer, IValue>> heapTableList = FXCollections.observableArrayList();
        heap_table_address.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Integer.toString(cellData.getValue().getKey())));
        heap_table_value.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));
        heapTableList.addAll(controller.getProgramStates().get(0).getHeapTable().getContent().entrySet());
        heapTable.setItems(heapTableList);
    }

    private void setSymbolTableAndExecutionStack() {
        symbol_table.refresh();
        ObservableList<HashMap.Entry<String, IValue>> symTableList = FXCollections.observableArrayList();
        ObservableList<String> exeStackItemsList = FXCollections.observableArrayList();
        symbol_table_variable.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));
        symbol_table_value.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().toString()));
//        System.out.println(controller.getProgramStates().get(0).getSymbolsTable());
        List<ProgramState> programs = controller.getProgramStates();
        ProgramState program = null;
        for (ProgramState state : programs) {
            if (state.getId() == Integer.parseInt(program_states_list.getSelectionModel().getSelectedItem())) {
                program = state;
                break;
            }
        }
        if (program != null){
            symTableList.addAll(program.getSymbolsTable().getContent().entrySet());
//            System.out.println(program.getSymbolsTable().getContent().entrySet());
            for (IStatement statement: program.getStack().getStack()){
                exeStackItemsList.add(statement.toString());
            }
            symbol_table.setItems(symTableList);
            execution_stack.setItems(exeStackItemsList);
        }
    }

    private void setOutputList() {
        ObservableList<String> output = FXCollections.observableArrayList();
        for (IValue value: controller.getProgramStates().get(0).getOutput().getList()) {
            output.add(value.toString());
        }
        outputList.setItems(output);
    }

    public void setNumberTextField() {
        number_of_programs.setText("" + controller.getProgramStates().size());
//        System.out.println(controller.getProgramStates().size());
    }

}
