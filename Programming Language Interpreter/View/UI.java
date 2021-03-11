package View;

import Controller.Controller;
import Model.ProgramState;

import java.io.IOException;
import java.util.Scanner;

public class UI {
    Controller controller;
    public static boolean game_over = false;

    public UI(Controller controller) {
        this.controller = controller;
    }

    public void run() {
        while (!game_over) {
            printMenu();
            Scanner keyboard = new Scanner(System.in);
            int choice = keyboard.nextInt();
            ProgramState program_state = null;
//            if (choice > 0 && choice <= 3) {
//                try {
//                    allStep(controller.getProgramState(choice - 1));
//                } catch (IOException e){
//                    System.out.println(e.getMessage());
//                }
//            } else {
//                game_over = true;
//            }
        }
    }

//    private void allStep (ProgramState state) throws IOException {
//        while (!state.getStack().isEmpty()){
//            System.out.println(state.toLogString());
////            state = controller.oneStep(state);
//        }
//        System.out.println(state.toLogString());
//    }

    private static void printMenu() {
        System.out.println("0. Exit");
        System.out.println("1. int v; v=2;Print(v)");
        System.out.println("2. int a;int b; a=2+3*5;b=a+1;Print(b)");
        System.out.println("3. bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)");
        System.out.println("Choice > ");
    }
}
