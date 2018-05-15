package org.poem.core.enums;

import org.poem.tools.utils.string.StringUtils;

public enum ActionEnums {

    HELP("help"),
    DEFAULTMETHOD("defaultMethod");

    ActionEnums(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public boolean equals(String other) {
        return StringUtils.isNotBlank(other) ? action.equals(other.toLowerCase()) : false;
    }

    private final String action;
}
