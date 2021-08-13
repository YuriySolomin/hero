package shr.training_camp.core.model.database.specification;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
