package com.example.photobook.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class FileZipper {
    public static ByteArrayOutputStream zip(List<String> files) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(out)) {
            for (String file : files) {
                File fileToZip = new File(file);
                zipOutputStream.putNextEntry(new ZipEntry(fileToZip.getName()));

                FileInputStream fis = new FileInputStream(fileToZip);
                IOUtils.copy(fis, zipOutputStream);
                fis.close();
                zipOutputStream.closeEntry();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return out;
    }
}
