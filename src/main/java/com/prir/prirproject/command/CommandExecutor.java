package com.prir.prirproject.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {

    private List<String> results = new ArrayList<>();
    private BufferedReader stdInput;
//    private BufferedReader stdError;

    public void executeCommand(List<String> command) {

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();

            stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = stdInput.readLine()) != null) {
                results.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getResult() {
        int result = 0;
        for (int i=0; i<results.size(); i=i+2) {
            result += Integer.valueOf(results.get(i));
        }
        return result;
    }

    public List<Integer> getPositions() {
        List<Integer> intPositions = new ArrayList<>();
        StringBuilder positions = new StringBuilder();
        for (int i=1; i<results.size(); i=i+2) {
            positions.append(results.get(i));
            positions.append(" ");
        }

        for (String num : positions.toString().split(" ")) {
            intPositions.add(Integer.valueOf(num));
        }

        return intPositions;
    }

    public List<String> getStdOutput() {
        return results;
    }

//    public String getStdError() {
//        return stdError.lines().collect(Collectors.joining());
//    }
}
