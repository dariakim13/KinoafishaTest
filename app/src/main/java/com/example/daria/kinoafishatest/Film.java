package com.example.daria.kinoafishatest;

/**
 * Created by User on 06.09.2016.
 */
public class Film {
    private String trailer;
    private String actors;
    private String production;
    private String director;
    private String description;
    private String period;
    private String title;
    private String poster;
    private String genre;

    Film(String trailer, String actors, String production, String director, String description, String period,
                String title, String poster, String genre) {
        this.trailer = trailer;
        this.actors = actors;
        this.production = production;
        this.director = director;
        this.description = description;
        this.period = period;
        this.title = title;
        this.poster = poster;
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public String getDescription() {
        return description;
    }

    public String getDirector() {
        return director;
    }

    public String getGenre() {
        return genre;
    }

    public String getPeriod() {
        return period;
    }

    public String getPoster() {
        return poster;
    }

    public String getProduction() {
        return production;
    }

    public String getTitle() {
        return title;
    }

    public String getTrailer() {
        return trailer;
    }
}
