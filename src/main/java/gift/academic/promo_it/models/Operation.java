package gift.academic.promo_it.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
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
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @Column("operation_name")
    String operationName;

    public Operation() {}

    public Operation(String operation_name) {
        this.operationName = operation_name;
    }

    public static Operation fromModel(Operation operation) {
        if (operation == null) return null;

        Operation newOperation = new Operation();
        newOperation.setId(operation.getId());
        newOperation.setOperationName(operation.getOperationName());
        return newOperation;
    }
}
