package shr.training_camp.games.search;

import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.Resources;
import shr.training_camp.domain.resources.ResourceLikelihood;
import shr.training_camp.domain.resources.Search;
import shr.training_camp.util.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchItems {
    // ToDo Depend on level and location, player can find the different resources
    // It's necessary to implement the search algorithm, random
    // There are the following tasks:
    // 1. Prepare the object for search. With likelihood, resourceList, time, the count of tries
    // 2. There are the following entities: location,
    private Player player;
    private SearchLocations searchLocations;

    public static void main(String[] args) {
        SearchItems searchItems = new SearchItems();
        Search search = searchItems.prepareSearch();
        Map<Long, Integer> convert = new HashMap<>();
        for (ResourceLikelihood resourceLikelihood: search.getResourceLikelihoods()) {
            convert.put(resourceLikelihood.getIdResource(), resourceLikelihood.getWeight());
        }
        for (int i=0; i<= 100; i++) {
            Long id = RandomUtils.getValueFromMap(convert);
            System.out.println(id);
        }

    }

    public void search() {


    }

    private Search prepareSearch() {
        List<ResourceLikelihood> hardcodedList = new ArrayList<>();
        hardcodedList.add(new ResourceLikelihood(6L, 20000));
        hardcodedList.add(new ResourceLikelihood(7L, 2000));
        hardcodedList.add(new ResourceLikelihood(8L, 20));
        Search result = new Search();
        result.setDuration(60);
        result.setNumberOfTries(10);
        result.setResourceLikelihoods(hardcodedList);
        return result;

    }

}
