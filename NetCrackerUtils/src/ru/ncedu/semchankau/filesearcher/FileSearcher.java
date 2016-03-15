package ru.ncedu.semchankau.filesearcher;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Aliaksei Semchankau on 04.08.2015.
 */
public class FileSearcher {

    private Path currentDirectory;
    private boolean exit = false;

    public boolean mustExit() {
        return exit;
    }

    /**
     * this constructor sets current directory and prints instruction for using utility
     */
    public FileSearcher() {
        System.out.println("you can use commands pwd, ls, cd for navigating in system and find to find files using regular expressions \n " +
                        "pwd  - prints working directory \n " +
                        "ls - lists all directories and files of current directory \n " +
                        "cd - change directory (you can also use ..) \n " +
                        "find - find all files in current directory (and subdirectories) which correspond to given regular expression\n" +
                        "you can use also keys --size(size of file) and --change (date of last change)\n" +
                        "for example: find --size --change G...\n" +
                        "notice: use keys BEFORE regex"
        );
        currentDirectory = Paths.get("").toAbsolutePath();
        //System.out.println("currentDirectory = " + currentDirectory.toString());

    }

    /**
     * this method reads consol arguments and transfers them to "doCommand"
     */
    public void readCommand() {
        Scanner scan = new Scanner(System.in);
        String argumentLine = scan.nextLine();
        String[] arguments = argumentLine.split(" ");
        doCommand(arguments);
    }

    /**
     * this method checks, which function should be called
     * @param arguments
     */
    private void doCommand(String[] arguments) {

        if (arguments[0].equals("exit")) {
            exit = true;
            return;
        }

        if (arguments[0].equals("pwd")) {
            doPWD(arguments);
            return;
        }

        if (arguments[0].equals("ls")) {
            doLS(arguments);
            return;
        }

        if (arguments[0].equals("cd")) {
            doCD(arguments);
            return;
        }

        if (arguments[0].equals("find")) {
            doFind(arguments);
            return;
        }

        throw new FileSearcherException("no such command as " + arguments[0]);

    }

    /**
     * print working directory
     * @param arguments
     */
    private void doPWD(String[] arguments) {

        if (arguments.length > 1) {
            throw new FileSearcherException("too many arguments for pwd");
        }

        try {
            System.out.println(currentDirectory.toRealPath().toString());
        } catch (IOException ioexc) {
            System.out.println("current directory cannot be presented to realPath");
        }
    }

    /**
     * lists all files in current directory
     */
    private void doLS(String[] arguments) {

        if (arguments.length > 1) {
            throw new FileSearcherException("to many arguments for ls");
        }
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentDirectory);
            for (Path file : directoryStream) {
                System.out.println(file.getFileName().toString());
            }
        } catch (IOException ioexc) {
            System.out.println("can't open directory " + currentDirectory);
        }

    }

    /**
     * it changes directory
     * @param arguments
     */
    private void doCD(String[] arguments) {

        if (arguments.length > 2 || arguments.length == 1) {
            throw new FileSearcherException("incorrect number of arguments for cd");
        }

        try {
            Path newDirectory = currentDirectory.resolve(arguments[1]).toRealPath();
            newDirectory.normalize();
            if (!Files.isDirectory(newDirectory)) {
                throw new FileSearcherException(arguments[1] + " isn't lead to directory");
            }

            currentDirectory = newDirectory;
        } catch (IOException ioexc) {
            System.out.println("can't get real path for " + arguments[1]);
        }

    }

    /**
     * this method parses argument line for finding
     * and send it in readable form in method findMatchers
     * @param arguments
     */
    private void doFind(String[] arguments) {

        boolean atributeSize = false;
        boolean atrubuteChangeDate = false;

        int argCounter = 1;

        for (String arg : arguments) {
            if (arg.equals("--size")) {
                atributeSize = true;
                ++argCounter;
            }
            if (arg.equals("--change")) {
                atrubuteChangeDate = true;
                ++argCounter;
            }
        }

        if (arguments.length != argCounter + 1) {
            throw new FileSearcherException("incorrect number of arguments for find");
        }

        String pattern = arguments[arguments.length - 1];

        findMatchers(currentDirectory, pattern, atributeSize, atrubuteChangeDate);

    }

    /**
     * this method finds matchers for pattern in given directory and its subdirectories
     * @param directory
     * @param pattern
     * @param atributeSize
     * @param atributeChangeDate
     */
    private void findMatchers(final Path directory, final String pattern, final boolean atributeSize, final boolean atributeChangeDate) {

        //System.out.println("pattern = " + pattern);

        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory);

            for (Path file : directoryStream) {
                String fileName = file.getFileName().toString();
                //System.out.println(fileName + " : " + fileName.matches(pattern));
                if (fileName.matches(pattern)) {
                    printInformation(file, atributeSize, atributeChangeDate);
                }
                if (Files.isDirectory(file)) {
                    findMatchers(file, pattern, atributeSize, atributeChangeDate);
                }
            }

        } catch (IOException ioexc) {
            System.out.println(directory + " can't be opened");
        }

    }

    /**
     * prints information about file in required format
     * @param file
     * @param atributeSize
     * @param atributeChangeDate
     * @throws IOException
     */
    public void printInformation(final Path file, boolean atributeSize, boolean atributeChangeDate) throws IOException {
        String fileOrDirectory;
        if (Files.isDirectory(file)) {
            fileOrDirectory = "directory | ";
        } else {
            fileOrDirectory = "file      | ";
        }
        String fullWay = file.toRealPath().toString();
        while (fullWay.length() < 80) {
            fullWay += " ";
        }
        fullWay += " |";
        String size = "";
        if (atributeSize) {
            long sz = file.toFile().length();
            size = convertLongToString(sz);
        }
        String lastChanged = "";
        if (atributeChangeDate) {

            Date date = new Date(file.toFile().lastModified());
            lastChanged = date.toString();
            while (lastChanged.length() < 20) {
                lastChanged += " ";
            }
            lastChanged += "| ";
        }
        String fileName = file.getFileName().toString();
        while (fileName.length() < 30) {
            fileName += " ";
        }
        fileName += "| ";
        String result = fileOrDirectory + size + lastChanged + fileName + fullWay;
        System.out.println(result);
    }


    /**
     * this method converts digit to char
     * @param a
     * @return
     */
    char charDigit(long a) {
        if (a == 0)
            return '0';
        if (a == 1)
            return '1';
        if (a == 2)
            return '2';
        if (a == 3)
            return '3';
        if (a == 4)
            return '4';
        if (a == 5)
            return '5';
        if (a == 6)
            return '6';
        if (a == 7)
            return '7';
        if (a == 8)
            return '8';

        return '9';
    }

    /**
     * this method converts long to string
     * @param number
     * @return
     */
    public String convertLongToString(long number) {
        String result = "";
        while (number > 0) {
            long digit = number % 10;
            result += charDigit(digit);
            number /= 10;
        }

        String tmp = "";
        for (int i = result.length() - 1; i >= 0; --i) {
            tmp += result.charAt(i);
        }

        result = tmp;

        while (result.length() < 15) {
            result += " ";
        }
        result += "| ";
        return result;
    }
}
