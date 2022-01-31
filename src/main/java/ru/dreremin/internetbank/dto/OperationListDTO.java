package ru.dreremin.internetbank.dto;

import java.io.Serializable;
import java.util.List;
import ru.dreremin.internetbank.models.OperationDescription;

public class OperationListDTO implements Serializable {

    private final List<OperationDescription> operationList;

    public OperationListDTO(List<OperationDescription> operationList) {
        this.operationList = operationList;
        for(OperationDescription operationDescription : operationList) {
            operationDescription.removingNullFromRecipientAccountId();
        }
    }

    public List<OperationDescription> getOperationList() {
        return List.copyOf(this.operationList);
    }
}
