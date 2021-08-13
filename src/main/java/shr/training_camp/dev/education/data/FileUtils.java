package shr.training_camp.dev.education.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {


    // ToDo As reminder

    public static void main(String[] args) {
        readListFromFile("src/main/resources/storage/check_method.txt");
        List<String> checkList = new ArrayList<>();
        checkList.add("A11\n");
        checkList.add("A12\n");
        FileUtils.writeToFile("src/main/resources/storage/check_method.txt", checkList);
    }

    public static void writeToFile(final String fileName, final String line) {
        try {
            StandardOpenOption standardOpenOption;
            if (!Files.exists(Paths.get(fileName))) {
                standardOpenOption = StandardOpenOption.CREATE;
            } else {
                standardOpenOption = StandardOpenOption.APPEND;
            }
            Files.write(
                    Paths.get(fileName),
                    line.getBytes(),
                    standardOpenOption);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(final String fileName, final List<String> lines) {
        try {
            StandardOpenOption standardOpenOption;
            if (!Files.exists(Paths.get(fileName))) {
                standardOpenOption = StandardOpenOption.CREATE;
            } else {
                standardOpenOption = StandardOpenOption.APPEND;
            }
            Files.write(
                    Paths.get(fileName),
                    lines,
                    standardOpenOption);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(final String fileName) {
        Path path = Paths.get(fileName);
        Stream<String> lines = null;
        try {
            lines = Files.lines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.collect(Collectors.joining("\n"));
    }

    public static List<String> readListFromFile(final String fileName) {
        Path path = Paths.get(fileName);
        Stream<String> lines = null;
        try {
            lines = Files.lines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.collect(Collectors.toList());
    }

}
