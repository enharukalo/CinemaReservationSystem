package edu.estu;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.util.Scanner;

public class CinemaReservationCLI {

    private static final Scanner scanner = new Scanner(System.in);
    public static User currentUser = null;

    public static void showMessage(String message, Ansi.Color color) {
        System.out.println(Ansi.ansi().fg(color).a(message).reset());
    }

    public static void main(String[] args) {
        AnsiConsole.systemInstall();

        showMessage("╔═══════════════════════════════════════════════╗", Ansi.Color.YELLOW);
        showMessage("║        Welcome to the Cinema Reservation      ║", Ansi.Color.YELLOW);
        showMessage("║                     System                    ║", Ansi.Color.YELLOW);
        showMessage("╚═══════════════════════════════════════════════╝", Ansi.Color.YELLOW);

        displayMainMenu();
        AnsiConsole.systemUninstall();
    }

    static void displayMainMenu() {
        showMessage("1. Register\n2. Login\n3. Exit", Ansi.Color.YELLOW);
        int choice = getIntInput();
        switch (choice) {
            case 1:
                UserManager.register();
                break;
            case 2:
                UserManager.login();
                break;
            case 3:
                exit();
                break;
            default:
                showMessage("Invalid option. Please choose again.", Ansi.Color.RED);
                displayMainMenu();
                break;
        }
    }

    static void displayReservationMenu() throws IOException {
        showMessage("1. Make a reservation\n2. List reservations\n3. Cancel a reservation\n4. Exit", Ansi.Color.YELLOW);
        int choice = getIntInput();
        switch (choice) {
            case 1:
                try {
                    ReservationManager.makeReservation();
                } catch (IOException e) {
                    showMessage("An error occurred while trying to make the reservation. Error details: " + e.getMessage(), Ansi.Color.RED);
                }
                break;
            case 2:
                // listReservations();
                break;
            case 3:
                // cancelReservation();
                break;
            case 4:
                exit();
                break;
            default:
                showMessage("Invalid option. Please choose again.", Ansi.Color.RED);
                displayReservationMenu();
                break;
        }
    }

    static void exit() {
        showMessage("Goodbye!", Ansi.Color.GREEN);
        System.exit(0);
    }

    public static int getIntInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            showMessage("Invalid input. Please enter a valid number.", Ansi.Color.RED);
            scanner.nextLine();
            return getIntInput();
        }
    }

}