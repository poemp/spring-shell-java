package org.poem.core.enums;

import org.poem.tools.utils.string.StringUtils;

/**
 * @author poem
 */

public enum ActionEnums {

    /**
     * 帮助指令
     */
    HELP("help"),
    /**
     *  全部指令
     */
    DEFAULT_METHOD("all"),

    /**
     * 推出指令
     */
    EXIT("exit");

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
