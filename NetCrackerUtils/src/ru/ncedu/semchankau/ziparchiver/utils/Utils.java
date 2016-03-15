package ru.ncedu.semchankau.ziparchiver.utils;

import ru.ncedu.semchankau.ziparchiver.ZipException;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Aliaksei Semchankau on 25.08.2015.
 */
public class Utils {

    public static void writeFromFisToZos(final FileInputStream fis, final ZipOutputStream zos) throws IOException {

        byte[] buf = new byte[8000];
        int length;
        while (true) {
            length = fis.read(buf);
            if (length < 0) {
                break;
            }
            zos.write(buf, 0, length);
        }
    }

    public static void zipFile(final String reset, final ZipOutputStream zos, final String fileName) {

        try {
            ZipEntry zipEntry = new ZipEntry(fileName);
            FileInputStream fis = new FileInputStream(reset + fileName);
            try {
                zos.putNextEntry(zipEntry);
            } catch (IOException ioexc) {
                throw new ZipException("can't put next entry for zos " + fileName);
            }
            try {
                writeFromFisToZos(fis, zos);
            } catch (IOException ioexc) {
                throw new ZipException("can't write from fis to zos : " + fileName);
            }
            try {
                zos.closeEntry();
            } catch (IOException ioexc) {
                throw new ZipException("can't close zos entry :" + fileName);
            }
            try {
                fis.close();
            } catch (IOException ioexc) {
                throw new ZipException("can't close fis or zos " + fileName);
            }
        } catch (FileNotFoundException fnfexc) {
            throw new ZipException("can't open file " + reset + fileName);
        }
    }

    public static void unZip(final ZipInputStream zis, final String fileName, final File dest) {
        //try {

        String fullName = dest + "\\" + fileName;
        File destFile = new File(fullName);

        String dirName;

       // System.out.println("fullName " + fullName);

        try {
            int maxLen = Math.max(fullName.lastIndexOf("\\") + 1, fullName.lastIndexOf("/") + 1);
            dirName = fullName.substring(0, maxLen);
        } catch (Exception exc) {
            dirName = fullName;
        }

        //System.out.println("dirName " + dirName);

        File currentFile = new File(fileName);
        if (currentFile.isDirectory()) {
           dirName = fullName;
        }

        File directories = new File(dirName);
        directories.mkdirs();

        try {
            destFile.createNewFile();
        } catch (IOException ioExc) {
            System.out.println("can't create " + destFile.getAbsolutePath());
        }

       // File currentFile = new File(fileName);
        if (currentFile.isDirectory()) {
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(destFile);
            writeFromZisToFos(zis, fos);
        } catch (FileNotFoundException fnfExc) {
            System.out.println("can't find " + destFile.getAbsolutePath());
        } catch (IOException ioExc) {
            System.out.println("can't perform writing from zis to fos: " + destFile.getAbsolutePath());
        }
        // } catch (IOException ioExc) {
        //     System.out.println("troubles with " + fileName);
        //     System.out.println(ioExc.toString());
        //  }
    }

    public static void writeFromZisToFos(final ZipInputStream zis, final FileOutputStream fos) throws IOException {
        byte[] buf = new byte[8000];
        int length;
        while (true) {
            length = zis.read(buf);
            if (length < 0) {
                break;
            }
            fos.write(buf, 0, length);
        }
    }

    public static void deleteFile(final String fileName) {

        File file = new File(fileName);
        if (file.isDirectory()) {
            for (File innerFile : file.listFiles()) {
                deleteFile(innerFile.getAbsolutePath());
            }
        }
        file.delete();
    }

}
