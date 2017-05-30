package com.kpisoft.user.impl.openam.commands;

public class OpenAmConfiguration
{
    private boolean realmEnabled;
    private String baseUrl;
    private String redirectUrl;
    private String adminLogin;
    private String adminPassword;
    private String userCommandPrefix;
    private String createUserCommand;
    private String updateUserCommand;
    private String deleteUserCommand;
    private String sessionCommandPrefix;
    private String loginCommand;
    private String logoutCommand;
    private String sessionValidateCommand;
    private String sessionProfileCommand;
    private String userIdentityAttribute;
    private String createRealmCommand;
    private String getRealmCommand;
    
    public OpenAmConfiguration() {
        this.realmEnabled = false;
    }
    
    public boolean isRealmEnabled() {
        return this.realmEnabled;
    }
    
    public void setRealmEnabled(final boolean realmEnabled) {
        this.realmEnabled = realmEnabled;
    }
    
    public String getBaseUrl() {
        return this.baseUrl;
    }
    
    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getRedirectUrl() {
        return this.redirectUrl;
    }
    
    public void setRedirectUrl(final String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    
    public String getAdminLogin() {
        return this.adminLogin;
    }
    
    public void setAdminLogin(final String adminLogin) {
        this.adminLogin = adminLogin;
    }
    
    public String getAdminPassword() {
        return this.adminPassword;
    }
    
    public void setAdminPassword(final String adminPassword) {
        this.adminPassword = adminPassword;
    }
    
    public String getUserCommandPrefix() {
        return this.userCommandPrefix;
    }
    
    public void setUserCommandPrefix(final String userPrefix) {
        this.userCommandPrefix = userPrefix;
    }
    
    public String getCreateUserCommand() {
        return this.createUserCommand;
    }
    
    public void setCreateUserCommand(final String createUserCommand) {
        this.createUserCommand = createUserCommand;
    }
    
    public String getUpdateUserCommand() {
        return this.updateUserCommand;
    }
    
    public void setUpdateUserCommand(final String updateUserCommand) {
        this.updateUserCommand = updateUserCommand;
    }
    
    public String getDeleteUserCommand() {
        return this.deleteUserCommand;
    }
    
    public void setDeleteUserCommand(final String deleteUserCommand) {
        this.deleteUserCommand = deleteUserCommand;
    }
    
    public String getLoginCommand() {
        return this.loginCommand;
    }
    
    public void setLoginCommand(final String loginCommand) {
        this.loginCommand = loginCommand;
    }
    
    public String getLogoutCommand() {
        return this.logoutCommand;
    }
    
    public void setLogoutCommand(final String logoutCommand) {
        this.logoutCommand = logoutCommand;
    }
    
    public String getSessionValidateCommand() {
        return this.sessionValidateCommand;
    }
    
    public void setSessionValidateCommand(final String sessionValidateCommand) {
        this.sessionValidateCommand = sessionValidateCommand;
    }
    
    public String getSessionProfileCommand() {
        return this.sessionProfileCommand;
    }
    
    public void setSessionProfileCommand(final String sessionProfileCommand) {
        this.sessionProfileCommand = sessionProfileCommand;
    }
    
    public String getCreateRealmCommand() {
        return this.createRealmCommand;
    }
    
    public void setCreateRealmCommand(final String createRealmCommand) {
        this.createRealmCommand = createRealmCommand;
    }
    
    public String getGetRealmCommand() {
        return this.getRealmCommand;
    }
    
    public void setGetRealmCommand(final String getRealmCommand) {
        this.getRealmCommand = getRealmCommand;
    }
    
    public String getSessionCommandPrefix() {
        return this.sessionCommandPrefix;
    }
    
    public void setSessionCommandPrefix(final String sessionCommandPrefix) {
        this.sessionCommandPrefix = sessionCommandPrefix;
    }
    
    public String getUserIdentityAttribute() {
        return this.userIdentityAttribute;
    }
    
    public void setUserIdentityAttribute(final String userIdentityAttribute) {
        this.userIdentityAttribute = userIdentityAttribute;
    }
}
