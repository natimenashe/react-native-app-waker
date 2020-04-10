package com.idom.appWaker.permissions;

/**
 * User: nmenashe
 * Date: 10-04-2020
 * Time: 21:55
 */
public class PermissionRequiredResult {

    private boolean permissionRequired;

    private String manufacturer;


    public boolean isPermissionRequired() {
        return permissionRequired;
    }

    public void setPermissionRequired(boolean permissionRequired) {
        this.permissionRequired = permissionRequired;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
