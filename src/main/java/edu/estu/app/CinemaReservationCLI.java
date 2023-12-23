package edu.estu.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CinemaReservationCLI {


    public static void main(String[] args) {
        AnsiConsole.systemInstall();

        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("╔═══════════════════════════════════════════════╗").reset());
        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("║        Welcome to the Cinema Reservation      ║").reset());
        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("║                     System                    ║").reset());
        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("╚═══════════════════════════════════════════════╝").reset());



        displayMainMenu();
        AnsiConsole.systemUninstall();
    }

    private static void showMessage(String message, Ansi.Color color) {
        System.out.println(Ansi.ansi().fg(color).a(message).reset());
    }

    private static void displayMainMenu() {
        showMessage("1. Register\n2. Login\n3. Exit", Ansi.Color.YELLOW);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
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

    private static void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.next();
        System.out.print("Enter your password: ");
        String password = scanner.next();

        User newUser = new User(username, password);
        try {
            writeUserToFile(newUser);
            showMessage("Registration successful.", Ansi.Color.GREEN);
        } catch (IOException e) {
            showMessage("An error occurred while registering. Please try again.", Ansi.Color.RED);
        }

        displayMainMenu();
    }

    private static void login() {
        Path usersFilePath = Paths.get("users.txt");
        if (!Files.exists(usersFilePath)) {
            showMessage("Users file not found. Please register first.", Ansi.Color.RED);
            displayMainMenu();
            return;
        }

        Scanner scanner = new Scanner(System.in);
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
                    displayReservationMenu();
                    loggedIn = true;
                    break;
                }
            }

            if (!loggedIn) {
                showMessage("Invalid username or password.", Ansi.Color.RED);
                displayMainMenu();
            }
        } catch (IOException e) {
            showMessage("An error occurred while logging in. Please try again. Error details: " + e.getMessage(), Ansi.Color.RED);
        }
    }

    private static void writeUserToFile(User user) throws IOException {
        try (FileWriter fileWriter = new FileWriter("users.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(user.getUsername() + ":" + user.getPassword());
        }
    }


    private static List<User> readUsersFromFile() throws IOException {
        List<User> users = new ArrayList<>();
        try (FileReader fileReader = new FileReader("users.txt");
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


    private static void exit() {
        showMessage("Goodbye!", Ansi.Color.GREEN);
        System.exit(0);
    }

    private static void displayReservationMenu() throws FileNotFoundException, IOException {
        showMessage("1. Make a reservation\n2. List reservations\n3. Cancel a reservation\n4. Exit", Ansi.Color.YELLOW);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                try {
                    makeReservation();
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

    private static List<Movie> readMovies() throws FileNotFoundException {
        Gson gson = new Gson();
        TypeToken<List<Movie>> token = new TypeToken<>() {};
        try (FileReader reader = new FileReader("movies.json")) {
            return gson.fromJson(reader, token.getType());
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    private static void writeMovies(List<Movie> movies) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(movies);
        Files.writeString(Path.of("movies.json"), json);
    }

    private static void makeReservation() throws FileNotFoundException, IOException {
        List<Movie> movies = readMovies();
        Movie selectedMovie = selectMovie(movies);
        showMessage("You have selected: " + selectedMovie.getTitle(), Ansi.Color.GREEN);

        int selectedDateIndex = selectDate(selectedMovie);
        List<Integer> reservedSeats = selectedMovie.getDates().get(selectedDateIndex).getReservedSeats();

        showMessage("Available seats (1-20) - Reserved seats: " + reservedSeats, Ansi.Color.YELLOW);

        int seatNumber = selectSeat();

        while (seatNumber < 1 || seatNumber > 20) {
            showMessage("Invalid seat number. Please choose again.", Ansi.Color.RED);
            seatNumber = selectSeat();
        }

        while (reservedSeats.contains(seatNumber)) {
            showMessage("Seat number " + seatNumber + " is already reserved. Please choose another seat.", Ansi.Color.RED);
            seatNumber = selectSeat();
        }

        reservedSeats.add(seatNumber);
        writeMovies(movies);

        showMessage("Reservation successful! Seat number " + seatNumber + " is reserved for you.", Ansi.Color.GREEN);
    }


    private static Movie selectMovie(List<Movie> movies) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < movies.size(); i++) {
            showMessage((i + 1) + ". " + movies.get(i).getTitle(), Ansi.Color.YELLOW);
        }

        int movieNumber = scanner.nextInt();
        return movies.get(movieNumber - 1);
    }

    private static int selectDate(Movie movie) {
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < movie.getDates().size(); i++) {
            showMessage((i + 1) + ". " + movie.getDates().get(i).getDate(), Ansi.Color.YELLOW);
        }

        int dateNumber = scanner.nextInt();
        return dateNumber - 1;
    }

    private static int selectSeat() {
        Scanner scanner = new Scanner(System.in);

        showMessage("Please select a seat number (1-20): ", Ansi.Color.CYAN);
        return scanner.nextInt();
    }
}