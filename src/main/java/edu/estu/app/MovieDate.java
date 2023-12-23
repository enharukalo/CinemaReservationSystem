package edu.estu.app;

import java.util.List;

public class MovieDate {
    private String date;
    private List<Integer> reservedSeats;

    public MovieDate(String date, List<Integer> reservedSeats) {
        this.date = date;
        this.reservedSeats = reservedSeats;
    }

    public String getDate() {
        return date;
    }

    public List<Integer> getReservedSeats() {
        return reservedSeats;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setReservedSeats(List<Integer> reservedSeats) {
        this.reservedSeats = reservedSeats;
    }
}
