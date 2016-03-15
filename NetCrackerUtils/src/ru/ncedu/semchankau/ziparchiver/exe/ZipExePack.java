package ru.ncedu.semchankau.ziparchiver.exe;

import ru.ncedu.semchankau.archivecomparator.*;
import ru.ncedu.semchankau.ziparchiver.*;
import ru.ncedu.semchankau.ziparchiver.ZipException;
import ru.ncedu.semchankau.ziparchiver.utils.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Aliaksei Semchankau on 05.08.2015.
 * This method packs list of files into archive
 * format: pack, archiveName, String... files
 */
public class ZipExePack extends ZipExe {

    /**
     * List of files of some directory and its subdirectories
     */
    private List<String> listFiles = new ArrayList<String>();

    @Override
    public void exe(final String[] argc) {

        System.out.println(Arrays.toString(argc));

        if (argc.length < 3) {
            throw new ZipException("not enough arguments to pack");
        }

        boolean commentAdd = false;
        String comment = "";
        int start = 2;

        if (argc[1].equals("--comment")) {

            commentAdd = true;
            comment = argc[2];
            start = 4;
            if (argc.length < 5) {
                throw new ZipException("not enough arguments to pack with comment");
            }
        }

        String archiveAddress = argc[start  - 1];

        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(archiveAddress));

            for (int i = start; i < argc.length; ++i) {

                File curFile = new File(argc[i]);
                String path = curFile.getAbsolutePath();
                String reset = path.substring(0, path.lastIndexOf("\\") + 1);

                //System.out.println("path " + path);
               // System.out.println("reset " + reset);

                listFiles.clear();
                addToFileList(reset, curFile);       // lists all files in directory
                for (String fileName : listFiles) {       // zips all listed files

                    //System.out.println("fileName " + fileName);

                    try {
                        Utils.zipFile(reset, zos, fileName);
                    } catch (Exception exc) {
                        try {
                            System.out.println(exc.getMessage());
                            zos.close();
                        } catch (IOException ioExc) {
                            System.out.println("can't close zipOutputStream");
                        }
                    }
                }
            }

            if (commentAdd) {
                zos.setComment(comment);
            }

            try {
                zos.close();
            } catch (IOException ioexc) {
                throw new ZipException("can't close zos");
            }
        } catch (FileNotFoundException fnfexc) {
            throw new ZipException("can't find " + archiveAddress);
        }
    }

    private void addToFileList(final String reset, final File fileArg) {

        //System.out.println("fileArg = " + fileArg);

        if (!fileArg.isDirectory()) {

            String path = fileArg.getAbsolutePath();
            String shortName = path.substring(reset.length(), path.length());
            listFiles.add(shortName);
            return;
        }
        for (File innerFile : fileArg.listFiles()) {
            addToFileList(reset, innerFile);
        }
    }



}
