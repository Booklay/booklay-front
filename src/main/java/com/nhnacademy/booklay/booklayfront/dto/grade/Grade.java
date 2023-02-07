package com.nhnacademy.booklay.booklayfront.dto.grade;

import lombok.Getter;

@Getter
public enum Grade {
    WHITE(1, "화이트"),
    SILVER(2, "실버"),
    GOLD(3, "골드"),
    PLATINUM(4, "플래티넘");

    private final int value;
    private final String korGrade;

    Grade(int value, String korGrade) {
        this.value = value;
        this.korGrade = korGrade;
    }
}
