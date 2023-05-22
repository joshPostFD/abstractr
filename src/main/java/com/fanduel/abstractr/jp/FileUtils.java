package com.fanduel.abstractr.jp;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {
    public static List<File> getAllFilesFromRootDirectory(File rootDirectory) throws IllegalArgumentException {
        if (!rootDirectory.isDirectory()) {
            throw new IllegalArgumentException("Supplied file is not a directory.");
        }
        LinkedList<File> files = new LinkedList<>(List.of(rootDirectory));
        List<File> allFiles = new LinkedList<>();
        while (!files.isEmpty()) {
            File root = files.pop();
            if (root.listFiles() == null) {
                continue;
            }
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    files.addLast(file);
                } else {
                    allFiles.add(file);
                }
            }
        }
        return allFiles;
    }
}
