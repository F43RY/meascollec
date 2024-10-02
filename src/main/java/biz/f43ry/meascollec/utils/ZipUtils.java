package biz.f43ry.meascollec.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    // Decomprime lo zip e restituisce il contenuto dell'XML come stringa
    public static String unzipFile(byte[] zipData) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(zipData);
             ZipInputStream zis = new ZipInputStream(bais)) {
            
            ZipEntry zipEntry = zis.getNextEntry();
            if (zipEntry != null && !zipEntry.isDirectory()) {
                // Legge il file XML
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }
                return baos.toString("UTF-8"); // Ritorna l'XML come stringa
            }
        }
        throw new IOException("No file found in the zip");
    }
}
