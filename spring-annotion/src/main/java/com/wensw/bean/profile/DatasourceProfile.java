package com.wensw.bean.profile;

public class DatasourceProfile {

    private String url;
    private String user;
    private String driverClass;
    private String password;
    private String timeZone;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
        return "DatasourceProfile{" +
                "url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", driverClass='" + driverClass + '\'' +
                ", password='" + password + '\'' +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }
}
