package com.anchen.model;


public class Jrxy {

  private long studentId;
  private String psw;
  private String addressProvince;
  private String addressCity;
  private String addressRegion;
  private String address;
  private double lon;
  private double lat;
  private String pushChannel;
  private String serverChanKey;
  private String pushDeerKey;
  private long errorCount;


  public long getStudentId() {
    return studentId;
  }

  public void setStudentId(long studentId) {
    this.studentId = studentId;
  }


  public String getPsw() {
    return psw;
  }

  public void setPsw(String psw) {
    this.psw = psw;
  }


  public String getAddressProvince() {
    return addressProvince;
  }

  public void setAddressProvince(String addressProvince) {
    this.addressProvince = addressProvince;
  }


  public String getAddressCity() {
    return addressCity;
  }

  public void setAddressCity(String addressCity) {
    this.addressCity = addressCity;
  }


  public String getAddressRegion() {
    return addressRegion;
  }

  public void setAddressRegion(String addressRegion) {
    this.addressRegion = addressRegion;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }


  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }


  public String getPushChannel() {
    return pushChannel;
  }

  public void setPushChannel(String pushChannel) {
    this.pushChannel = pushChannel;
  }


  public String getServerChanKey() {
    return serverChanKey;
  }

  public void setServerChanKey(String serverChanKey) {
    this.serverChanKey = serverChanKey;
  }


  public String getPushDeerKey() {
    return pushDeerKey;
  }

  public void setPushDeerKey(String pushDeerKey) {
    this.pushDeerKey = pushDeerKey;
  }


  public long getErrorCount() {
    return errorCount;
  }

  public void setErrorCount(long errorCount) {
    this.errorCount = errorCount;
  }

}
