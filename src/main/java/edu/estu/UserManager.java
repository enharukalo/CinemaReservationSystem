package edu.estu;

import org.fusesource.jansi.Ansi;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static edu.estu.CinemaReservationCLI.showMessage;

public class UserManager {

    private static final String USERS_TXT = "users.txt";
    private static final Scanner scanner = new Scanner(System.in);

    static void register() {
        System.out.print("Enter your username: ");
        String username = scanner.next();
        System.out.print("Enter your password: ");
        String password = scanner.next();

        User newUser = new User(username, password);

        // Check if user already exists
        // If user exists, display error message and return to main menu
        // If user does not exist, write user to file and display success message
        // and return to main menu

        try {
            List<User> users = readUsersFromFile();
            boolean userExists = false;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    showMessage("User already exists. Please choose another username.", Ansi.Color.RED);
                    userExists = true;
                    break;
                }
            }

            if (!userExists) {
                writeUserToFile(newUser);
                showMessage("User registered successfully.", Ansi.Color.GREEN);
            }
        } catch (IOException e) {
            showMessage("An error occurred while registering. Please try again. Error details: " + e.getMessage(), Ansi.Color.RED);
        }

        CinemaReservationCLI.displayMainMenu();
    }

    static void login() {
        Path usersFilePath = Paths.get(USERS_TXT);
        if (!Files.exists(usersFilePath)) {
            showMessage("Users file not found. Please register first.", Ansi.Color.RED);
            CinemaReservationCLI.displayMainMenu();
            return;
        }

        System.out.print("Enter your username: ");
        String username = scanner.next();
        System.out.print("Enter your password: ");
        String password = scanner.next();

        try {
            List<User> users = readUsersFromFile();
            boolean loggedIn = false;
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    showMessage("Login successful.", Ansi.Color.GREEN);
                    CinemaReservationCLI.displayReservationMenu();
                    loggedIn = true;
                    break;
                }
            }

            if (!loggedIn) {
                showMessage("Invalid username or password.", Ansi.Color.RED);
                CinemaReservationCLI.displayMainMenu();
            }
        } catch (IOException e) {
            showMessage("An error occurred while logging in. Please try again. Error details: " + e.getMessage(), Ansi.Color.RED);
        }
    }

    private static void writeUserToFile(User user) throws IOException {
        try (FileWriter fileWriter = new FileWriter(USERS_TXT, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(user.getUsername() + ":" + user.getPassword());
        }
    }


    private static List<User> readUsersFromFile() throws IOException {
        List<User> users = new ArrayList<>();
        try (FileReader fileReader = new FileReader(USERS_TXT);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(":");
                if (tokens.length == 2) {
                    String username = tokens[0];
                    String password = tokens[1];
                    User user = new User(username, password);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading users file: " + e.getMessage(), e);
        }

        return users;
    }
}
