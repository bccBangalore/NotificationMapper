package org.rssb.notification.model;

public class SewadarAttendance {
    private String sewadarCode;
    private String sewadarName;
    private String attendanceDays;
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

    public String getAttendanceDays() {
        return attendanceDays;
    }

    public void setAttendanceDays(String attendanceDays) {
        this.attendanceDays = attendanceDays;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return "SewadarAttendance{" +
                "sewadarCode='" + sewadarCode + '\'' +
                ", sewadarName='" + sewadarName + '\'' +
                ", attendanceDays='" + attendanceDays + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}
