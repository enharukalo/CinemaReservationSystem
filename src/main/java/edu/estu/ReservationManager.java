package edu.estu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.fusesource.jansi.Ansi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static edu.estu.CinemaReservationCLI.showMessage;

public class ReservationManager {
    private static final String MOVIES_JSON = "movies.json";
    private static final Gson gson = new Gson();

    private static List<Movie> readMovies() throws FileNotFoundException {
        TypeToken<List<Movie>> token = new TypeToken<>() {
        };
        try (FileReader reader = new FileReader(MOVIES_JSON)) {
            return gson.fromJson(reader, token.getType());
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    private static void writeMovies(List<Movie> movies) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(movies);
        Files.writeString(Path.of(MOVIES_JSON), json);
    }

    static void makeReservation() throws IOException {
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

        // Insert the seat number in the reserved seats list in sorted order
        int insertIndex = Collections.binarySearch(reservedSeats, seatNumber);
        insertIndex = (insertIndex < 0) ? -insertIndex - 1 : insertIndex;
        reservedSeats.add(insertIndex, seatNumber);
        writeMovies(movies);

        String movieTitle = selectedMovie.getTitle();
        String date = selectedMovie.getDates().get(selectedDateIndex).getDate();
        Reservation reservation = new Reservation(movieTitle, date, seatNumber);
        CinemaReservationCLI.currentUser.getReservations().add(reservation);

        try {
            List<User> users = UserManager.readUsersFromJSON();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUsername().equals(CinemaReservationCLI.currentUser.getUsername())) {
                    users.set(i, CinemaReservationCLI.currentUser);
                    break;
                }
            }
            UserManager.writeUsersToJSON(users);
        } catch (IOException e) {
            showMessage("An error occurred while trying to update user data. Error details: " + e.getMessage(), Ansi.Color.RED);
        }

        showMessage("Reservation successful! Seat number " + seatNumber + " is reserved for you.", Ansi.Color.GREEN);
    }


    private static Movie selectMovie(List<Movie> movies) {
        for (int i = 0; i < movies.size(); i++) {
            showMessage((i + 1) + ". " + movies.get(i).getTitle(), Ansi.Color.YELLOW);
        }

        int movieNumber = CinemaReservationCLI.getIntInput();
        return movies.get(movieNumber - 1);
    }

    private static int selectDate(Movie movie) {
        for (int i = 0; i < movie.getDates().size(); i++) {
            showMessage((i + 1) + ". " + movie.getDates().get(i).getDate(), Ansi.Color.YELLOW);
        }

        int dateNumber = CinemaReservationCLI.getIntInput();
        if (dateNumber < 1 || dateNumber > movie.getDates().size()) {
            showMessage("Invalid date number. Please choose again.", Ansi.Color.RED);
            dateNumber = CinemaReservationCLI.getIntInput();
        }
        return dateNumber - 1;
    }

    private static int selectSeat() {
        showMessage("Please select a seat number (1-20): ", Ansi.Color.CYAN);
        return CinemaReservationCLI.getIntInput();
    }
}
