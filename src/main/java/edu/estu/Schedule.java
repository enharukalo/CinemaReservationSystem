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

    public List<Integer> getReservedSeats() {
        return reservedSeats;
    }

}