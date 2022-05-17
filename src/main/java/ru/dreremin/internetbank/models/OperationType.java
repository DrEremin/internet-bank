package ru.dreremin.internetbank.models;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.persistence.*;

@AllArgsConstructor
@Getter
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
