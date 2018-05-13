package org.poem.config;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.poem.core.bean.ShellMethodParameter;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.tools.utils.string.StringUtils;

import java.util.List;

public class ShellCommandParse   {

    private List<ShellMethodTarget> commands;

    private final Options searchOpts = new Options();

    public ShellCommandParse(List<ShellMethodTarget> commands) {
        this.commands = commands;
    }

    /**
     * 段参数
     *
     * @param name
     * @return
     */
    private String shortName(String name) {
        return StringUtils.isNotBlank(name) ? name.substring(0, 1) : "";
    }

    @SuppressWarnings({"deprecation", "static-access"})
    public ShellCommandParse createParse() {
        if (!this.commands.isEmpty()) {
            for (ShellMethodTarget shellMethodTarget : this.commands) {
                for (ShellMethodParameter shellMethodParameter : shellMethodTarget.getMethodParameterMap().values()) {
                    String shorName = this.shortName(shellMethodParameter.getName());
                    String detail = shellMethodParameter.getDetail();
                    Option option = OptionBuilder
                            .withDescription(detail)
                            .withArgName(shellMethodParameter.getName())
                            .hasArg()
                            .create(shorName);
                    searchOpts.addOption(option);
                }
            }
        }
        return  this;
    }

    public Options getSearchOpts() {
        return searchOpts;
    }
}
