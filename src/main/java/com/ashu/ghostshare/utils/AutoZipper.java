package com.ashu.ghostshare.utils;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class AutoZipper {

    /**
     * Folder ko zip mein convert karta hai aur temporary file return karta hai.
     */
    public static File zipFolder(File folder) throws IOException {
        // Temporary zip file ka naam aur location
        String zipFileName = folder.getName() + "_GhostArchive.zip";
        File tempZip = new File(System.getProperty("java.io.tmpdir"), zipFileName);
        
        System.out.println("[ARCHIVE] Creating temporary archive at: " + tempZip.getAbsolutePath());

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempZip))) {
            Path sourcePath = folder.toPath();
            
            // Poore folder tree ko walk karna
            Files.walk(sourcePath)
                .filter(path -> !Files.isDirectory(path)) // Sirf files ko pakdo
                .forEach(path -> {
                    ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                    try {
                        zos.putNextEntry(zipEntry);
                        Files.copy(path, zos);
                        zos.closeEntry();
                    } catch (IOException e) {
                        System.err.println("Error zipping file: " + path);
                    }
                });
        }
        
        return tempZip;
    }
}