package ru.ncedu.semchankau.archivecomparator;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Aliaksei Semchankau on 18.08.2015.
 */
public class ArchiveComparator {

    ArchiveComparator() {
    }

    ;

    /**
     * This method takes two archives (if argument line is empty it provides you an archive chooser);
     * After that method prints difference between archives;
     *
     * @param args
     */
    public void work(final String... args) {
        String firstArchive = "";
        String secondArchive = "";

        if (args.length != 2) {
            // System.out.println("len = " + args.length);
            //for (String arg : args)
            //    System.out.println(arg);
            firstArchive = choose("choose first archive");
            secondArchive = choose("choose second archive");
        } else {
            firstArchive = args[0];
            secondArchive = args[1];
        }

        try {
            List<FileInfo> firstArchiveList = listFiles(firstArchive);
            List<FileInfo> secondArchiveList = listFiles(secondArchive);
            ChangeTable changeTable = compare(firstArchiveList, secondArchiveList);
            String dest = System.getProperty("user.dir");
            dest += "/";
            /*
            dest += "\"";
            dest += firstArchive;
            dest += "\"";
            dest += " compared with ";
            dest += "\"";
            dest += secondArchive;
            dest += "\"";
            dest += ".txt";
            */
            dest += "comparing result.txt";
            print(changeTable, firstArchive, secondArchive, dest);
        } catch (ZipException zExc) {
            System.out.println("some problems while listing archive files occured: " + zExc.getMessage());
        }
    }

    /**
     * It creates JFileChooser for choosing archive names with message as a title;
     *
     * @param message
     * @return
     */
    private static String choose(final String message) {
        JFileChooser jFileChooser = new JFileChooser();
        while (true) {
            int result = jFileChooser.showDialog(null, message);
            if (result == JFileChooser.APPROVE_OPTION) {
                return jFileChooser.getSelectedFile().toString();
            }
        }
    }

    /**
     * this method lists all files of given archive and returns list with FileInfo
     *
     * @param archive
     * @return
     * @throws ZipException
     */
    private static List<FileInfo> listFiles(final String archive) throws ZipException {
        List<FileInfo> infoList = new ArrayList<FileInfo>();
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(archive));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName();
                long fileSize = entry.getSize();
                long fileHash = entry.getCrc();
                infoList.add(new FileInfo(fileName, fileSize, fileHash));
            }
        } catch (FileNotFoundException fnfexc) {
            throw new ZipException("can't find archive " + archive);
        } catch (IOException ioexc) {
            throw new ZipException("some of inner files are incorrect");
        }

        return infoList;
    }

    /**
     * this method compares two archives using lists of files of that archive
     *
     * @param firstArchiveList
     * @param secondArchiveList
     * @return
     */
    private static ChangeTable compare(final List<FileInfo> firstArchiveList, final List<FileInfo> secondArchiveList) {

        ChangeTable changeTable = new ChangeTable();

        Map<String, FileInfo> firstArchiveMap = makeArchiveMap(firstArchiveList);
        Map<String, FileInfo> secondArchiveMap = makeArchiveMap(secondArchiveList);

        /**
         * this cycle checks if files of first archive were deleted (or renamed) comparing with second archive
         */

        for (Map.Entry<String, FileInfo> entry : firstArchiveMap.entrySet()) {
            String fileName = entry.getKey();
            FileInfo updatedFileInfo = secondArchiveMap.get(fileName);
            if (updatedFileInfo == null) {
                changeTable.addDeleted(entry.getValue());
            } else {
                if (!updatedFileInfo.equals(entry.getValue())) {
                    changeTable.addUpdated(updatedFileInfo);
                }
            }
        }

        /**
         * This cycle checks if files of second archive were added comparing with first archive
         */
        for (Map.Entry<String, FileInfo> entry : secondArchiveMap.entrySet()) {
            String fileName = entry.getKey();
            FileInfo updatedFileInfo = firstArchiveMap.get(fileName);
            if (updatedFileInfo == null) {
                changeTable.addNew(entry.getValue());
            }
        }

        Map<FileInnerInformation, List<FileInfo>> firstArchiveSenseMap = makeArchiveSenseMap(firstArchiveList);
        Map<FileInnerInformation, List<FileInfo>> secondArchiveSenseMap = makeArchiveSenseMap(secondArchiveList);

        /**
         * Here we are checking that files with different names have the same size and hash
         */
        for (Map.Entry<FileInnerInformation, List<FileInfo>> entry : secondArchiveSenseMap.entrySet()) {

            FileInnerInformation innerInformation = entry.getKey();
            if (firstArchiveSenseMap.get(innerInformation) == null) {
                continue;
            }

            List<FileInfo> firstList = firstArchiveSenseMap.get(innerInformation);
            List<FileInfo> secondList = secondArchiveSenseMap.get(innerInformation);

            for (FileInfo oldFile : firstList) {
                for (FileInfo newFile : secondList) {
                    if (!oldFile.getFileName().equals(newFile.getFileName())) {
                        changeTable.addRenamed(oldFile, newFile);
                    }
                }
            }

        }

        return changeTable;
    }


    /**
     * here we constructing map with name of file to its informination
     *
     * @param archiveList
     * @return
     */
    private static Map<String, FileInfo> makeArchiveMap(final List<FileInfo> archiveList) {
        Map<String, FileInfo> archiveMap = new TreeMap<String, FileInfo>();

        for (FileInfo fileInfo : archiveList) {
            //System.out.println(fileInfo.getFileName() + " " + fileInfo.getFileSize() + " " + fileInfo.getFileHash());
            archiveMap.put(fileInfo.getFileName(), fileInfo);
        }

        return archiveMap;
    }

    /**
     * here we building map with inner information of file to list with names if files with such information
     *
     * @param archiveList
     * @return
     */
    private static Map<FileInnerInformation, List<FileInfo>> makeArchiveSenseMap(final List<FileInfo> archiveList) {
        Map<FileInnerInformation, List<FileInfo>> archiveSenseMap = new TreeMap<FileInnerInformation, List<FileInfo>>();

        for (FileInfo fileInfo : archiveList) {
            FileInnerInformation innerInformation = new FileInnerInformation(fileInfo);
            if (archiveSenseMap.get(innerInformation) == null) {
                archiveSenseMap.put(innerInformation, new ArrayList<FileInfo>());
            }
            archiveSenseMap.get(innerInformation).add(fileInfo);
        }

        return archiveSenseMap;
    }

    /**
     * this method prints differences given by changeTable
     * in form:
     * " - file" - file was removed
     * " * file" - file was changed
     * " + file" - file was added
     * @param changeTable
     * @param firstArchiveName
     * @param secondArchiveName
     * @param dest
     */
    private static void print(final ChangeTable changeTable, final String firstArchiveName, final String secondArchiveName, final String dest) {

        File destFile = new File(dest);

        //System.out.println("dest = " + dest);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(destFile));

            List<FileInfo> deletedFiles = changeTable.getDeletedFiles();
            bw.write("-(" + deletedFiles.size() + "):");
            bw.newLine();
            for (FileInfo fileInfo : deletedFiles) {
                bw.write(fileInfo.getFileName());
                bw.newLine();
            }
            bw.newLine();

            List<FileInfo> updatedFiles = changeTable.getUpdatedFiles();
            bw.write("*(" + updatedFiles.size() + "):");
            bw.newLine();
            for (FileInfo fileInfo : updatedFiles) {
                bw.write(fileInfo.getFileName());
                bw.newLine();
            }
            bw.newLine();

            List<FileInfo> newFiles = changeTable.getNewFiles();
            bw.write("+(" + newFiles.size() + "):");
            bw.newLine();
            for (FileInfo fileInfo : newFiles) {
                bw.write(fileInfo.getFileName());
                bw.newLine();
            }
            bw.newLine();

            List<ChangeTable.FileInfoPair> renamedFiles = changeTable.getRenamedFiles();
            bw.write("renamed (" + renamedFiles.size() + "):");
            bw.newLine();
            for (ChangeTable.FileInfoPair fileInfoPair : renamedFiles) {
                bw.write(fileInfoPair.getStr1().getFileName() + " -> " + fileInfoPair.getStr2().getFileName());
                bw.newLine();
            }
            bw.newLine();

            bw.close();

        } catch (FileNotFoundException fnfExc) {
            System.out.println("can't create destination file " + dest);
        } catch (IOException ioExc) {
            System.out.println("can't perform writing into file " + dest);
        }
    }

}
