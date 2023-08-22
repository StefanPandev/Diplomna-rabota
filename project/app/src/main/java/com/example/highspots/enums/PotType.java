package com.example.highspots.enums;

import androidx.annotation.NonNull;

public enum PotType {

    SATIVA("Sativa"),
    INDICA("Indica"),
    KUSH("Kush");

    final String potType;

    PotType(String potType) {
        this.potType = potType;
    }

    @NonNull
    @Override
    public String toString() {
        return this.potType;
    }
}
