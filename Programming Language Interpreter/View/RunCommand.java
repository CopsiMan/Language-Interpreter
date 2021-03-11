package View;

import Controller.Controller;

import java.io.IOException;

public class RunCommand  extends  Command{
    private final Controller controller;

    public  RunCommand(String key, String desc, Controller controller){
        super(key,desc);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.addProgram(Integer.parseInt(super.getKey()) - 1);
            controller.allStep();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
