package gift.academic.promo_it.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("operation")
public class Operation {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    private Long id;

    public String getOperationName() {
        return operation_name;
    }

    public void setOperationName(String operationName) {
        this.operation_name = operationName;
    }

    String operation_name;

    public Operation() {}

    public Operation(String operation_name) {
        this.operation_name = operation_name;
    }
}
