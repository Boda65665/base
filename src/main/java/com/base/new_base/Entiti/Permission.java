package com.base.new_base.Entiti;

public enum Permission {
    USER("user"),
    ADMIN("admin"),
    UNVERIFIED("unverified");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
