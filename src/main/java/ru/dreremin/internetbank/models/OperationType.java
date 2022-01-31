package ru.dreremin.internetbank.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "operation_type")
public class OperationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "operation_name")
    private String operationName;

    public OperationType() {}

    public OperationType(long id, String operationName) {

        this.id = id;
        this.operationName = operationName;
    }

    public long getId() { return id; }

    public String getOperationName() { return operationName; }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof OperationType)) return false;
        OperationType that = (OperationType) o;
        return getId() == that.getId()
                && getOperationName().equals(that.getOperationName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getOperationName());
    }
}
