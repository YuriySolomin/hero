package shr.training_camp.games.search;

import org.springframework.stereotype.Component;
import shr.training_camp.util.RandomUtils;

import java.util.HashMap;
import java.util.Map;

public class SearchTemp {

    private double likelihood;
    private Long idResource;
    private int searchCount;

    public SearchTemp(double likelihood, Long idResource, int searchCount) {
        this.likelihood = likelihood;
        this.idResource = idResource;
        this.searchCount = searchCount;
    }

    public Map<Long, Integer> search() {
        Map<Long, Integer> result = new HashMap<>();
        Integer value = 1;
        for (int i=1; i<= searchCount; i++) {
            if (RandomUtils.isEventHappens(likelihood)) {
                if (result.containsKey(idResource)) {
                    value = result.get(idResource) + 1;
                }
                result.put(idResource, value);
            }
        }
        return result;
    }
}
