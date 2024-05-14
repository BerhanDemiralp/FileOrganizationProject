package Code;
import java.util.Scanner;

public class Main {
	
    public static void main(String[] args) {

    	
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello and welcome!");
        System.out.println("1- load passwords");
        System.out.println("2- search password");
        System.out.println("3- exit");

        Indexing index = new Indexing();
        Search search = new Search();

        while(true) {
            int input = scanner.nextInt();
            scanner.nextLine();
            if(input == 1) {
                index.getUnprocessedPasswords();
            }else if(input == 2) {
                System.out.println("Please enter the password you'd like to search for.");
                System.out.print("Password: ");
                String password = scanner.nextLine();
                long startTime = System.currentTimeMillis();
                search.search(password);
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                System.out.println("Search time: " + elapsedTime + " milliseconds");
            }
            else if(input== 3) {
                System.out.println("Program terminated.");
                break;
            }
            else {
                System.out.println("Invalid command");
            }
            System.out.println("*********************");
            System.out.println("1- load passwords");
            System.out.println("2- search password");
            System.out.println("3- exit");
        }
    }
}