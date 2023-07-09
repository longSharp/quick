package com.quick.mongodb.common.enums;

public enum ConditionType {
    /**
     * =
     */
    IS("is"),
    /**
     *  !=
     */
    NE("ne"),
    /**
     * <=
     */
    LTE("lte"),
    /**
     * <
     */
    LT("lt"),
    /**
     * >=
     */
    GTE("gte"),
    /**
     * >
     */
    GT("gt"),
    /**
     *  not
     */
    NOT("not"),
    /**
     * in
     */
    IN("in"),
    /**
     * not in
     */
    NIN("nin");

    private String type;

    ConditionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
