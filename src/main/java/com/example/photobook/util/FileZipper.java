package com.example.photobook.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class FileZipper {
    public static void zip(List<String> files, ServletOutputStream sos) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(sos)) {
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
    }
}
