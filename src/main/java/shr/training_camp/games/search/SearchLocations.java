package shr.training_camp.games.search;

import lombok.Getter;

public enum SearchLocations {

    TRASH("Trash"),
    WOMAN_FEET("woman feet"),
    SPECIAL_ZONE_1("special 1"),
    WOMAN_BODY("woman body"),
    DRESS("dress");

    @Getter
    private final String key;

    SearchLocations(String key) {
        this.key = key;
    }

    }
