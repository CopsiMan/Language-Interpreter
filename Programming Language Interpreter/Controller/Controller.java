package Controller;

import Model.ADT.*;
import Model.ADT.Dictionary;
import Model.ADT.Stack;
import Model.ProgramState;
import Model.Statement.CompoundStatement;
import Model.Statement.IStatement;
import Model.Types.IType;
import Model.Types.IValue;
import Model.Types.ReferenceValue;
import Repo.IRepository;
import Repo.Repository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    IRepository repo;
    ArrayList<ProgramState> programs;
    ExecutorService executor;

    public Controller(IRepository repo) {
        this.repo = repo;
        this.programs = new ArrayList<>();
    }

    List<ProgramState> removeCompletedPrograms(List<ProgramState> programs) {
        return programs.stream()
                .filter(p -> !p.isCompleted())
                .collect(Collectors.toList());
    }

    public void oneStepForAllPrograms(List<ProgramState> program_states) {
//        program_states.forEach(p -> {
////            if (!(p.getStack().top() instanceof CompoundStatement))
//            repo.logProgramState(p);
//        });
        List<Callable<ProgramState>> callable_list = program_states.stream()
                .map(p -> (Callable<ProgramState>) p::oneStep)
                .collect(Collectors.toList());

        List<ProgramState> newProgramList = null;
        try {
            newProgramList = executor.invokeAll(callable_list).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e.toString());
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            throw new RuntimeException(e.toString());
        }
        program_states.forEach(p -> {
            if (!(p.getStack().top() instanceof CompoundStatement))
            repo.logProgramState(p);
        });
        program_states.addAll(newProgramList);
        repo.setPrograms(program_states);
    }

    public void addProgram(int index) {
        repo.add(programs.get(index));
    }

    public void allStep() {
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrograms(repo.getPrograms());
        programStates.forEach(p -> repo.logProgramState(p));
        while (programStates.size() > 0) {
            programStates.get(0).getHeapTable().setContent(conservativeGarbageCollector(programStates,programStates.get(0).getHeapTable().getContent()));
            oneStepForAllPrograms(programStates);
            programStates = removeCompletedPrograms(repo.getPrograms());
        }
        executor.shutdownNow();
        repo.setPrograms(programStates);
    }

//    public void allStep(int index) throws IOException {
//        ProgramState program_state = programs.get(index);
//        repo.add(program_state);
////        repo.logProgramState();
////        int len = 0;
//        repo.logProgramState(program_state);
//        while (!program_state.isCompleted()) {
//            program_state.oneStep();
////            repo.logProgramState();
//            program_state.getHeapTable().setContent(unsafeGarbageCollector(
//                    getAddressesFromSymTable(program_state.getSymbolsTable().getContent().values(), program_state.getHeapTable().getContent().values()),
//                    program_state.getHeapTable().getContent()));
//            if (!(program_state.getStack().top() instanceof CompoundStatement))
//                repo.logProgramState(program_state);
////            if (len % 2 == 0)
////            len++;
//        }
//    }


    @Override
    public String toString() {
        return repo.getPrograms().get(0).getStatement().toString();
    }

    public void add(IStatement statement) {
        statement.typeCheck(new Dictionary<String, IType>());
        ProgramState program_state = new ProgramState(new Stack<IStatement>(), new Dictionary<String, IValue>(), new MyList<IValue>(), new FileTable<>(), new HeapTable<Integer, IValue>(), statement);
        programs.add(program_state);
//        repo.add(program_state);
    }


    //    public ProgramState oneStep(ProgramState state) throws IOException {
//        IStack<IStatement> stack = state.getStack();
//        IStatement current_statement = stack.pop();
//        return current_statement.execute(state);
////        try {
////            state = current_statement.execute(state);
////            repo.logProgramState();
////            return state;
////        } catch (RuntimeException e) {
////            System.out.println(e.getMessage());
////            return state;
////        }
//    }
    Map<Integer, IValue> conservativeGarbageCollector(List<ProgramState> programStates, Map<Integer, IValue> heap) {
        var all_symbol_tables_values = programStates.get(0).getSymbolsTable().getContent().values();
//        System.out.println(programStates);
        var addr = programStates.stream()
                .map(p -> p.getSymbolsTable().getContent().values())
                .collect(Collectors.toList())
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
//        System.out.println(programStates);
        var heap_values = heap.values();
        var addresses = getAddressesFromSymTable(addr,heap_values);
        return heap.entrySet().stream()
                .filter(e -> addresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddresses, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAddressesFromSymTable(Collection<IValue> symTableValues, Collection<IValue> heap) {
        return Stream.concat(
                heap.stream()
                        .filter(v -> v instanceof ReferenceValue)
                        .map(v -> {
                            ReferenceValue v1 = (ReferenceValue) v;
                            return v1.getAddress();
                        }),
                symTableValues.stream()
                        .filter(v -> v instanceof ReferenceValue)
                        .map(v -> {
                            ReferenceValue v1 = (ReferenceValue) v;
                            return v1.getAddress();
                        })
        )
                .collect(Collectors.toList());

    }

    public void addToRepo(IStatement statement) {
        statement.typeCheck(new Dictionary<>());
        ProgramState program_state = new ProgramState(new Stack<IStatement>(), new Dictionary<String, IValue>(), new MyList<IValue>(), new FileTable<>(), new HeapTable<Integer, IValue>(), statement);
        repo.add(program_state);
    }

    public List<ProgramState> getProgramStates() {
        return repo.getPrograms();
    }

    public void oneStepGUI() {
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrograms(repo.getPrograms());
        programStates.forEach(p -> repo.logProgramState(p));
        if (programStates.size() > 0) {
            programStates.get(0).getHeapTable().setContent(conservativeGarbageCollector(programStates, programStates.get(0).getHeapTable().getContent()));
            oneStepForAllPrograms(programStates);
//            programStates = removeCompletedPrograms(repo.getPrograms());
        } else {
            executor.shutdownNow();
            repo.setPrograms(programStates);
            throw new RuntimeException("Program Finished!");
        }
        executor.shutdownNow();
        repo.setPrograms(programStates);
    }

    public Controller copy() {
        Repository repo_copy = new Repository(this.repo.getPath());
        IStatement statement = this.repo.getPrograms().get(0).getStatement();
        Controller controller_new = new Controller(repo_copy);
        controller_new.addToRepo(statement);
        return controller_new;
    }

//    public ProgramState getProgramState(int index) {
//        return repo.getProgramAtIndex(index);
//    }

}
