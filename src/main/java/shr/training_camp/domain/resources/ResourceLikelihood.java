package shr.training_camp.domain.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceLikelihood {

    private Long idResource;
    // weight - this is the comparative value, when the hero searches there are the few resources he can find and likelihood is counted depend of summary weights
    private int weight;

}
