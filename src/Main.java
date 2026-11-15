
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Create scenario objects
        CampusRoute campus = new CampusRoute(input);
        SecureSteps secure = new SecureSteps(input);

        int choice;

        do {
            System.out.println("\n====================");
            System.out.println("      CLARITYAI");
            System.out.println("====================");

            System.out.println("1. CampusRoute");
            System.out.println("2. SecureSteps");
            System.out.println("0. Exit");

            System.out.print("Choose a scenario: ");
            choice = input.nextInt();

            switch (choice) {

                case 1:
                    campus.run();
                    break;

                case 2:
                    secure.run();
                    break;

                case 0:
                    System.out.println("Thank you for using ClarityAI!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);

        input.close();
    }
}
