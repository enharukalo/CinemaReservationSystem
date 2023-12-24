package edu.estu;

import java.util.List;

public class Schedule {
    private String date;
    private List<Integer> reservedSeats;

    public Schedule(String date, List<Integer> reservedSeats) {
        this.date = date;
        this.reservedSeats = reservedSeats;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Integer> getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(List<Integer> reservedSeats) {
        this.reservedSeats = reservedSeats;
    }
}