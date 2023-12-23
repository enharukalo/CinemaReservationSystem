package edu.estu.app;

import java.util.List;

public class Movie {
    private String title;
    private List<MovieDate> dates;

    public Movie(String title, List<MovieDate> dates) {
        this.title = title;
        this.dates = dates;
    }

    public String getTitle() {
        return title;
    }

    public List<MovieDate> getDates() {
        return dates;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDates(List<MovieDate> dates) {
        this.dates = dates;
    }
}
