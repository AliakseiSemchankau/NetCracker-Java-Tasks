package ru.ncedu.semchankau.ziparchiver;

import ru.ncedu.semchankau.archivecomparator.*;
import ru.ncedu.semchankau.archivecomparator.ZipException;
import ru.ncedu.semchankau.ziparchiver.exe.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aliaksei Semchankau on 05.08.2015.
 * main class
 */
public class ZipArchiver {

    /**
     * this map contains all needful classes for performing commands
     */
    private Map<String, ZipExe> zipCommmands = new HashMap<String, ZipExe>();

    /**
     * this constructor sets all needful commands
     */
    public ZipArchiver() {
        zipCommmands.put("pack", new ZipExePack());
        zipCommmands.put("unpack", new ZipExeUnPack());
        zipCommmands.put("add", new ZipExeAdd());
        zipCommmands.put("addcomment", new ZipExeAddComment());
        zipCommmands.put("getcomment", new ZipExeGetComment());
    }

    /**
     * this method executes arg line and checks, if this a correct command
     * @param argc string of arguments
     */
    public void execute(final String[] argc) {

        if (argc.length == 0) {
            System.out.println("there are no arguments");
            return;
        }

        String operation = argc[0];

        if (!zipCommmands.containsKey(operation)) {
            System.out.println("there is no such command as " + argc[0]);
            return;
        } else {
            try {
                zipCommmands.get(operation).exe(argc);
            } catch (IOException ioexc) {
                System.out.println("can't perform operation for " + Arrays.toString(argc));
            } catch (ru.ncedu.semchankau.archivecomparator.ZipException zExc) {
                System.out.println("some troubles with zip operations occured for " + Arrays.toString(argc));
            }
        }

    }

}
