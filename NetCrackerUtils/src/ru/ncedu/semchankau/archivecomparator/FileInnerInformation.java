package ru.ncedu.semchankau.archivecomparator;

/**
 * Created by Aliaksei Semchankau on 23.08.2015.
 * this class contains only hash and size of file
 */
public class FileInnerInformation implements Comparable{

    private long fileSize;
    private long fileHash;

    public FileInnerInformation(final FileInfo fileInfo) {
        fileSize = fileInfo.getFileSize();
        fileHash = fileInfo.getFileHash();
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getFileHash() {
        return fileHash;
    }

    @Override
    public int compareTo(final Object o) {
        FileInnerInformation anotherFile = (FileInnerInformation) o;
        if (fileSize < anotherFile.getFileSize()) {
            return -1;
        }
        if (fileSize > anotherFile.getFileSize()) {
            return 1;
        }
        if (fileHash < anotherFile.getFileHash()) {
            return -1;
        }
        if (fileHash > anotherFile.getFileHash()) {
            return 1;
        }
        return 0;
    }
}