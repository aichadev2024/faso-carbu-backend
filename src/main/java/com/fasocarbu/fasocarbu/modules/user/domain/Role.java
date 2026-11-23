package com.fasocarbu.fasocarbu.modules.user.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    AGENT_STATION,
    GESTIONNAIRE,
    CHAUFFEUR,
    ADMIN_STATION,
    DEMANDEUR;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
