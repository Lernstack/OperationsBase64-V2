import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class OperationsInBase64 {

    public static void main(String[] args) {
        String messageData = JOptionPane.showInputDialog(null, "Ingresa el valor a convertir");
        System.out.println(operationResponse(messageData));
    }

    public static String encode(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String message) {
        byte[] messageDecode = Base64.getDecoder().decode(message);
        return new String(messageDecode, StandardCharsets.UTF_8);
    }

    public static void dirUnzip(String location) {
        File dir = new File(location);
        if (!dir.exists()) {
            final boolean mkdirs = dir.mkdirs();
            //System.out.println(mkdirs);
        }
    }

    public static void dirZip(String dirOutput, byte[] decodedBytes) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dirOutput), 1024);
            out.write(decodedBytes);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static byte[] decodeToBytes(String dataToDecode) {
        return Base64.getDecoder().decode(dataToDecode);
    }

    public static List<String> unzip(String zipFilePath, String destDir) {
        // List for fileNames
        List<String> fileNames = new ArrayList<String>();

        dirUnzip(destDir);
        // create output directory if it doesn't exist

        FileInputStream fis;
        // buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);

                // Add name files
                fileNames.add(newFile.getAbsolutePath());

                // System.out.println("Unzipping to " + newFile.getAbsolutePath());
                // create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while (( len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                // close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            // close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    public static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.ISO_8859_1)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static String operationResponse(String message) {

        if (message.length() < 50) {
            return "Base64 incorrecto";
        } else {
            String location = "C:\\UNZIP\\";
            String fileName = "EDDI05.zip";
            String fileType = ".PDF";
            String nameUnzipFile = "";
            String dirRoot = System.getProperty("user.home");
            String path = dirRoot + File.separator + fileName;


            decodeToBytes(message);
            dirZip(path, decodeToBytes(message));
            List<String> nameFiles = unzip(path, location);

            for (String name : nameFiles) {
                //System.out.println(name);
                if (name.contains(fileType)) {
                    nameUnzipFile = name;
                }
            }
            return readFile(nameUnzipFile) + "FILENAME: " + nameUnzipFile;
        }
    }
}
