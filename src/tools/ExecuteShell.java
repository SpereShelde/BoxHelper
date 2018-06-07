package tools;

import java.io.IOException;

/**
 * Created by wzf on 2018/6/1.
 */
public class ExecuteShell implements Runnable{

    private String[] command;

    public ExecuteShell(String[] command) {
        this.command = command;
    }

    @Override
    public void run() {
        try {
            new ProcessBuilder(this.command).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
