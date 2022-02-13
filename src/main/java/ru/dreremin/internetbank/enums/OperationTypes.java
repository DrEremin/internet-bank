package ru.dreremin.internetbank.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OperationTypes {

    GET_BALANCE (1),
    PUT_MONEY (2),
    TAKE_MONEY (3),
    TRANSFER_MONEY (4);

    private final int value;

    /*OperationTypes(int value) { this.value = value; }*/

    /*public int getValue() { return value; }*/
}
