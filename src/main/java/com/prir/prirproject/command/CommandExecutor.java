package com.prir.prirproject.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandExecutor {

    private List<String> results = new ArrayList<>();
    private BufferedReader stdInput;
    private BufferedReader stdError;

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
        return Integer.valueOf(results.get(0));
    }

    public List<Integer> getPositions() {
        List<Integer> intPositions = new ArrayList<>();
        String positions = results.get(1);
        String positionsSubstring = positions.substring(1, positions.length()-1);

        for (String num : positionsSubstring.split(" ")) {
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
