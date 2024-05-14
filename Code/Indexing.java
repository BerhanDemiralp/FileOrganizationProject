package Code;

import javax.print.DocFlavor;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Indexing {
    String suffix = "";
    Map<Character, Integer> counts = new HashMap<>();
    public void process(){

    }

    public void getUnprocessedPasswords() {
        // Klasör yolunu belirtin
        String sourceFolderPath = "src/Unprocessed-Passwords";

        // Klasörü temsil eden File nesnesini oluşturun
        File sourceFolder = new File(sourceFolderPath);

        // Klasördeki dosyaları kontrol edin
        if (sourceFolder.isDirectory()) {
            // Klasördeki her dosya için işlem yapın
            File[] files = sourceFolder.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            getFileWithPassword(line.trim());
                        }
                        System.out.println();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Belirtilen yol bir klasörü temsil etmiyor.");
        }
    }

    public void getFileWithPassword(String password){
        Map<Character, String> invalidCharMap = new HashMap<>();
        invalidCharMap.put('\\', "backslash");
        invalidCharMap.put('/', "slash");
        invalidCharMap.put(':', "colon");
        invalidCharMap.put('*', "asterisk");
        invalidCharMap.put('?', "question mark");
        invalidCharMap.put('"', "quotation mark");
        invalidCharMap.put('<', "less than");
        invalidCharMap.put('>', "greater than");
        invalidCharMap.put('|', "pipe");
        invalidCharMap.put('.', "dot");

        String sourceFolderPath = "src/Index";

        File sourceFolder = new File(sourceFolderPath);
        String key;
        char _key = password.charAt(0);
        if(invalidCharMap.containsKey(_key)){
            key = invalidCharMap.get(_key);
        }
        else {
            key = String.valueOf(_key);
        }

        if(sourceFolder.isDirectory()){
            File[] folders = sourceFolder.listFiles();
            for (File folder : folders) {
                String foldername = folder.getName() ;

                Boolean folderbool = folder.getName().trim().equals(key);
                if (folder.getName().trim().equals(key) && folder.isFile()) {
                    writePasswordToFile(folder,password,key);
                }
            }
            String newFolderPath = sourceFolder + File.separator + key;
            File _folder =  new File(newFolderPath);
            writePasswordToFile(_folder,password,key);
        }
        else {
            String newFolderPath = sourceFolder + File.separator + key;
            File folder =  new File(newFolderPath);
          //  String newFilePath = folder.getPath() + File.separator + key + ".txt";
          //  File txtFile = new File(newFilePath);
            writePasswordToFile(folder,password,key);
        }
    }
    public void writePasswordToFile(File file, String password,String key) {
    	String folderpath = file.getPath();
        String filepath = folderpath + File.separator + key + suffix + ".txt";
        new File(folderpath).mkdirs();
        String content; // Password|MD5Hash|Sha128|Sha256|source_file_name
        content = generateContent(password);
        File txtFile = new File(filepath);
        boolean passwordExists = false;
        if(txtFile.exists()) {
        	try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Şifre dosyadas zaten varsa işlemi sonlandır
                    if (line.equals(generateContent(password))) {
                        passwordExists = true;
                        break;
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!passwordExists) {
        	
        	int count = counts.getOrDefault(password.charAt(0), 0);
            counts.put(password.charAt(0), counts.getOrDefault(password.charAt(0), 0) + 1);
        	System.out.println(count);
        	suffix = String.valueOf(count/1000);
        	System.out.println(suffix);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
                writer.write(content + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String generateContent(String password){
        String content = password;
        content = content + "|" +generateMD5Hash(password);
        content = content + "|" +generateSHA1Hash(password);
        content = content + "|" +generateSHA256Hash(password);
        content = content + "|" +password.charAt(0);
        return content;
    }
    public String generateMD5Hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(text.getBytes());

            byte[] hashBytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String generateSHA1Hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            md.update(text.getBytes());

            byte[] hashBytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String generateSHA256Hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(text.getBytes());

            byte[] hashBytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

