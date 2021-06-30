package com.basoft.eorder.interfaces.command.agent;

import com.basoft.eorder.application.framework.Command;

public class GenerateCagentQRCode implements Command {
    public static final String NAME = "generateCagentQRCode";

    private String agentId;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
