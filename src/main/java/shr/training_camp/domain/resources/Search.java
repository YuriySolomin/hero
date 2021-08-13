package shr.training_camp.domain.resources;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Search {
    // class for creation the likelihoods of searchin
    private int duration; // minutes
    private int numberOfTries; // depend on level and licenses
    private List<ResourceLikelihood> resourceLikelihoods;
    //private Map<Long, Integer> resourceLikelihoods;
    // Map contains the result of search, where Key - the ID of resource, Value - count
    private Map<Long, Integer> searchResults;
    private int experience; // Sum random value is generated after every trying
    private double expBonus; // Percent. It value depends on player and can increase or decrease his experience';
}
