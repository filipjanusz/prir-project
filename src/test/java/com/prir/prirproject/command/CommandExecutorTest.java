package com.prir.prirproject.command;

import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandExecutorTest {

    CommandExecutor executor;

    @Test
    public void commandExecutorTest() throws Exception {

        List<String> command = Stream.of("python", ".\\tests\\kmp.py", ".\\upload-dir\\test1.txt", "lol")
                .collect(Collectors.toList());

        executor = new CommandExecutor();
        executor.executeCommand(command);

        assertThat(executor.getResult()).isEqualTo(13);
        assertThat(executor.getPositions().get(0)).isEqualTo(14);
        assertThat(executor.getPositions().get(1)).isEqualTo(12);
        assertThat(executor.getPositions().get(2)).isEqualTo(15);
    }
}
