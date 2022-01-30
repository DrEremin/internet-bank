package ru.dreremin.internetbank.models;

import javax.persistence.*;

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
}
