package com.fasocarbu.fasocarbu.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    AGENT_STATION,
    GESTIONNAIRE,
    CHAUFFEUR;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name(); 
    }
}
