package Proje1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.util.*;

public class Main {
    public static class Indexing {
        private String suffix = "";
        private Map<Character, Integer> counts = new HashMap<>();
        private static final Map<Character, String> invalidCharMap = createInvalidCharMap();

        private static Map<Character, String> createInvalidCharMap() {
            Map<Character, String> map = new HashMap<>();
            map.put('\\', "backslash");
            map.put('/', "slash");
            map.put(':', "colon");
            map.put('*', "asterisk");
            map.put('?', "question mark");
            map.put('"', "quotation mark");
            map.put('<', "less than");
            map.put('>', "greater than");
            map.put('|', "pipe");
            map.put('.', "dot");
            return map;
        }

        public void getUnprocessedPasswords() {
            String sourceFolderPath = "Unprocessed-Passwords";
            File sourceFolder = new File(sourceFolderPath);

            if (sourceFolder.isDirectory()) {
                File[] files = sourceFolder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".txt")) {
                            processFile(file);
                        }
                    }
                }
            } else {
                System.out.println("Specified path is not a directory.");
            }
        }

        private void processFile(File file) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processPassword(line.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processPassword(String password) {
            String key = getKeyForPassword(password);
            String sourceFolderPath = "Index";
            File sourceFolder = new File(sourceFolderPath);

            if (sourceFolder.isDirectory()) {
                File[] folders = sourceFolder.listFiles();
                if (folders != null) {
                    for (File folder : folders) {
                        if (folder.isDirectory() && folder.getName().equals(key)) {
                            writePasswordToFile(folder, password, key);
                            return;
                        }
                    }
                }
                File newFolder = new File(sourceFolder, key);
                writePasswordToFile(newFolder, password, key);
            } else {
                File newFolder = new File(sourceFolder, key);
                writePasswordToFile(newFolder, password, key);
            }
        }

        private String getKeyForPassword(String password) {
            char firstChar = password.charAt(0);
            return invalidCharMap.getOrDefault(firstChar, String.valueOf(firstChar));
        }

        private void writePasswordToFile(File folder, String password, String key) {
            String sourceFolderPath = "Processed.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFolderPath, true))) {
                writer.write(generateContent(password) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            String folderPath = folder.getPath();
            int count = counts.getOrDefault(password.charAt(0), 0);
            counts.put(password.charAt(0), count + 1);
            suffix = String.valueOf(count / 10000);
            String filePath = folderPath + File.separator + key + "." + suffix + ".txt";
            folder.mkdirs();

            if (!passwordExistsInFile(filePath, password)) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                    writer.write(generateContent(password) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        private boolean passwordExistsInFile(String filePath, String password) {
            File txtFile = new File(filePath);
            if (txtFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.split("\\|")[0].trim().equals(password)) {
                            return true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        private String generateContent(String password) {
            return String.join("|", password, generateMD5Hash(password),generateSH128Hash(password), generateSHA256Hash(password), password.charAt(0) + "." + suffix);
        }

        private String generateMD5Hash(String text) {
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
        private String generateSH128Hash(String text) {
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

        private String generateSHA256Hash(String text) {
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

        public void show(String password,int suffix) {
            String content = generateContent(password);
            System.out.println("Searched password: " + content+ String.valueOf(suffix));
        }
    }

    public static class Search {
        private final Indexing index = new Indexing();

        public void search(String password) {
            String key = index.getKeyForPassword(password);
            String sourceFolderPath = "Index";
            File sourceFolder = new File(sourceFolderPath);

            if (sourceFolder.isDirectory()) {
                File[] folders = sourceFolder.listFiles();
                if (folders != null) {
                    for (File folder : folders) {
                        if (folder.isDirectory() && folder.getName().equals(key)) {
                        	int suffix = 0;
                        	for(File files : folder.listFiles()) {
                        		if (index.passwordExistsInFile(files.getAbsolutePath(), password)) {
                        			index.show(password,suffix);
                        			return;
                        		}
                        		suffix++;
                        	}
                        }
                    }
                }
                System.out.println("Password does not exist.");
                index.writePasswordToFile(new File(sourceFolder, key), password, key);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello and welcome!");
        System.out.println("1- Load passwords");
        System.out.println("2- Search password");
        System.out.println("3- Exit");

        Indexing index = new Indexing();
        Search search = new Search();

        while (true) {
            int input = scanner.nextInt();
            scanner.nextLine();
            if (input == 1) {
                index.getUnprocessedPasswords();
            } else if (input == 2) {
                System.out.println("Please enter the password you'd like to search for.");
                System.out.print("Password: ");
                String password = scanner.nextLine();
                long startTime = System.currentTimeMillis();
                search.search(password);
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                System.out.println("Search time: " + elapsedTime + " milliseconds");
            } else if (input == 3) {
                System.out.println("Program terminated.");
                break;
            } else {
                System.out.println("Invalid command");
            }
            System.out.println("*********************");
            System.out.println("1- Load passwords");
            System.out.println("2- Search password");
            System.out.println("3- Exit");
        }
    }
}
