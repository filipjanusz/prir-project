package com.prir.prirproject.command;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandExecutorTest {

    CommandExecutor executor;

    @Test
    public void commandExecutorTest() throws Exception {

        List<String> command = Stream.of("python", ".\\tests\\kmp.py", ".\\tests\\test1.txt", "lol")
                .collect(Collectors.toList());

        executor = new CommandExecutor();
        executor.executeCommand(command);


        assertThat(executor.getPatternLength()).isEqualTo(3);
        assertThat(executor.getResult()).isEqualTo(30);
        assertThat(executor.getPositions().get(0)).isEqualTo(6);
        assertThat(executor.getPositions().get(1)).isEqualTo(557);
        assertThat(executor.getPositions().get(2)).isEqualTo(1362);
        assertThat(executor.getPositions().get(3)).isEqualTo(7);
        assertThat(executor.getPositions().get(4)).isEqualTo(22);
        assertThat(executor.getPositions().get(5)).isEqualTo(362);
    }
}
