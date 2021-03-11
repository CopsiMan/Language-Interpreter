package Repo;

import Model.ProgramState;

import java.util.ArrayList;
import java.util.List;

public interface IRepository {
    List<ProgramState> getPrograms();
    void setPrograms(List<ProgramState> programs);
    void add(ProgramState program_state);
    void logProgramState(ProgramState program_state);

    String getPath();
//    ProgramState getProgramAtIndex(int index);
//    ProgramState getCurrentProgram();
}
