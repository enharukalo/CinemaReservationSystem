package edu.estu;

import java.util.List;

public class Movie {
    private String title;
    private List<Schedule> dates;

    public Movie(String title, List<Schedule> dates) {
        this.title = title;
        this.dates = dates;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Schedule> getDates() {
        return dates;
    }

    public void setDates(List<Schedule> dates) {
        this.dates = dates;
    }
}
