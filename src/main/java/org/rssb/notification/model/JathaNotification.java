package org.rssb.notification.model;

public class JathaNotification {

    private String sewadarCode;
    private String sewadarName;
    private String jathaVenue;
    private String sewaDepartment;
    private String sewaStartDate;
    private String sewaEndDate;
    private String mobileNo;

    public String getSewadarCode() {
        return sewadarCode;
    }

    public void setSewadarCode(String sewadarCode) {
        this.sewadarCode = sewadarCode;
    }

    public String getSewadarName() {
        return sewadarName;
    }

    public void setSewadarName(String sewadarName) {
        this.sewadarName = sewadarName;
    }

    public String getJathaVenue() {
        return jathaVenue;
    }

    public void setJathaVenue(String jathaVenue) {
        this.jathaVenue = jathaVenue;
    }

    public String getSewaDepartment() {
        return sewaDepartment;
    }

    public void setSewaDepartment(String sewaDepartment) {
        this.sewaDepartment = sewaDepartment;
    }

    public String getSewaStartDate() {
        return sewaStartDate;
    }

    public void setSewaStartDate(String sewaStartDate) {
        this.sewaStartDate = sewaStartDate;
    }

    public String getSewaEndDate() {
        return sewaEndDate;
    }

    public void setSewaEndDate(String sewaEndDate) {
        this.sewaEndDate = sewaEndDate;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return "JathaNotification{" +
                "sewadarCode='" + sewadarCode + '\'' +
                ", sewadarName='" + sewadarName + '\'' +
                ", jathaVenue='" + jathaVenue + '\'' +
                ", sewaDepartment='" + sewaDepartment + '\'' +
                ", sewaStartDate='" + sewaStartDate + '\'' +
                ", sewaEndDate='" + sewaEndDate + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}
