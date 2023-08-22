package com.example.highspots.enums;

import androidx.annotation.NonNull;

public enum Pot {

    // Pots here
    not_a_pot(null, null);

    final private String pot;

    final private PotType potType;

    Pot(String pot, PotType potType) {
        this.pot = pot;
        this.potType = potType;
    }

    @NonNull
    @Override
    public String toString() {
        return this.pot;
    }
}
