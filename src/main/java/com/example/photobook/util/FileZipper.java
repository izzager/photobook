package com.example.photobook.util;

import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class FileZipper {
    public static void zip(List<String> files, ServletOutputStream sos) {
        try(ZipOutputStream zipOut = new ZipOutputStream(sos)) {
            for (String file : files) {
                File fileToZip = new File(file);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
