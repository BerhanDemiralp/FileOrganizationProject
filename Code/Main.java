package Code;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        Indexing index = new Indexing();
        Search search = new Search();

        //index.getUnprocessedPasswords();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the password you'd like to search for.");
        System.out.print("Password: ");
        String password = scanner.nextLine();
        search.search(password);


    }
}