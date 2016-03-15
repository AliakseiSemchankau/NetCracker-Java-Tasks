package ru.ncedu.semchankau.archivecomparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aliaksei Semchankau on 18.08.2015.
 */

/**
 * This table contains all differences in readable form.
 * All information is listed.
 */
public class ChangeTable {

    private List<FileInfo> deletedFiles = new ArrayList<FileInfo>();
    private List<FileInfo> updatedFiles = new ArrayList<FileInfo>();
    private List<FileInfo> newFiles = new ArrayList<FileInfo>();
    private List<FileInfoPair> renamedFiles = new ArrayList<ChangeTable.FileInfoPair>();

    public ChangeTable() {}

    public void addDeleted(final FileInfo fileInfo) {
        deletedFiles.add(fileInfo);
    }

    public void addUpdated(final FileInfo fileInfo) {
        updatedFiles.add(fileInfo);
    }

    public void addNew(final FileInfo fileInfo) {
        newFiles.add(fileInfo);
    }

    public void addRenamed(final FileInfo fileInfo1, final FileInfo fileInfo2) {
        renamedFiles.add(new FileInfoPair(fileInfo1, fileInfo2));
    }


    public List<FileInfo> getDeletedFiles() {
        return deletedFiles;
    }

    public List<FileInfo> getUpdatedFiles() {
        return updatedFiles;
    }

    public List<FileInfo> getNewFiles() {
        return newFiles;
    }

    public List<FileInfoPair> getRenamedFiles() {
        return renamedFiles;
    }

    public class FileInfoPair {

        private FileInfo str1;
        private FileInfo str2;

        public FileInfoPair(final FileInfo str1, final FileInfo str2) {
            this.str1 = str1;
            this.str2 = str2;
        }

        public FileInfo getStr1() {
            return str1;
        }

        public FileInfo getStr2() {
            return str2;
        }

    }

}
