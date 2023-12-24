package edu.estu;

public class Reservation {
    private String movieTitle;
    private String date;
    private int seatNumber;

    public Reservation(String movieTitle, String date, int seatNumber) {
        this.movieTitle = movieTitle;
        this.date = date;
        this.seatNumber = seatNumber;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
