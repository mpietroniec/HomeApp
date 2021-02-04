package com.p.homeapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expenditure {
    private long expenditureID;
    private String expenditureName;
    private String expenditureDate;
    private float expenditureAmount;

    @Override
    public String toString() {
        return "Expenditure{" +
                "expenditureID=" + expenditureID +
                ", expenditureName='" + expenditureName + '\'' +
                ", expenditureDate='" + expenditureDate + '\'' +
                ", expenditureAmount=" + expenditureAmount +
                '}';
    }
}
