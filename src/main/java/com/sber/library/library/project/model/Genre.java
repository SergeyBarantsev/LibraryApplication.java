package com.sber.library.library.project.model;

public enum Genre {
    FANTASY("Фантастика"),
    SCIENCE_FICTION("Научная фантастика"),
    DRAMA("Драма"),
    NOVEL("Роман");

    private final String genreText;

    Genre(String genreText) {
        this.genreText = genreText;
    }

    public String getGenreText() {
        return this.genreText;
    }
}

