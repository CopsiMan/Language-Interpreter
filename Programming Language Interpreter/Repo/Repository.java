package Repo;

import Model.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    List<ProgramState> programs;
    int current_program;
    String logFilePath;

    public List<ProgramState> getPrograms() {
        return programs;
    }

    @Override
    public void setPrograms(List<ProgramState> programs) {
        this.programs = programs;
    }


    public Repository(String logFilePath) {
        programs = new ArrayList<>();
        current_program = 0;
        this.logFilePath = logFilePath;
    }


    @Override
    public void add(ProgramState program_state) {
        this.programs.add(program_state);
        current_program = this.programs.size()-1;
    }


    public void setCurrent_program(int current_program) {
        this.current_program = current_program;
    }

    @Override
    public void logProgramState(ProgramState program_state) {
//        ProgramState current_state = programs.get(current_program);
        String log = program_state.toLogString();
        try {
            PrintWriter file = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath,true)));
            file.write(log + '\n');
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPath() {
        return logFilePath;
    }

//    @Override
//    public ProgramState getCurrentProgram() {
//        return programs.get(current_program);
//    }
//    @Override
//    public ProgramState getProgramAtIndex(int index) {
//        current_program = index;
//        return programs.get(index);
//    }
}
