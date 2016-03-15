package ru.ncedu.semchankau.ziparchiver.exe;

import ru.ncedu.semchankau.archivecomparator.ZipException;
import ru.ncedu.semchankau.ziparchiver.ZipArchiver;
import ru.ncedu.semchankau.ziparchiver.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by Aliaksei Semchankau on 30.08.2015.
 * adds comment to existing archive
 * format: addcomment, archiveName, comment
 */
public class ZipExeAddComment extends ZipExe {
    @Override
    public void exe(String[] argc) throws IOException, ZipException {

        if (argc.length < 3) {
            throw new ZipException("not enough arguments for add");
        }

        String archiveAddress = argc[1];
        String comment = argc[2];
        ZipArchiver zipArchiver =  new ZipArchiver();

        String[] unPackArgc = new String[3];
        unPackArgc[0] = "unpack";
        unPackArgc[1] = archiveAddress;
        unPackArgc[2] = "tmpFile";

        zipArchiver.execute(unPackArgc);

        Utils.deleteFile(archiveAddress);

        File temporaryFile = new File("tmpFile");

        String[] files = new String[temporaryFile.list().length];
        int index = 0;
        for (File file : temporaryFile.listFiles()) {
            files[index] = file.getAbsolutePath();
            ++index;
        }

        String[] packArgc = new String[temporaryFile.listFiles().length + 4];
        packArgc[0] = "pack";
        packArgc[1] = "--comment";
        packArgc[2] = comment;
        packArgc[3] = archiveAddress;
        //packArgc[2] = "tmpFile";
        for (int i = 0; i < files.length; ++i) {
            packArgc[i + 4] = files[i];
        }

        zipArchiver.execute(packArgc);

        Utils.deleteFile("tmpFile");

        //ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(archiveAddress));

    }
}
