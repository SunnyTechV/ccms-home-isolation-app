package com.tecgvg.ccms.hi.domain.enumeration;

/**
 * The QuestionType enumeration.
 */
public enum QuestionType {
    FREETEXT("Text"),
    MULTISELECT("MultiSelection"),
    SINGLE("Single");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
