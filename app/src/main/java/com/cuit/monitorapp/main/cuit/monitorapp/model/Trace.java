package com.cuit.monitorapp.main.cuit.monitorapp.model;

public class Trace {
    private String acceptTime;
    private String acceptContent;
    private String acceptPic;
    // private int acceptPic;
    private String acceptVideo;

    public Trace() {
    }

    public Trace(String acceptTime, String acceptContent) {
        this.acceptTime = acceptTime;
        this.acceptContent = acceptContent;
    }

    public Trace(String acceptTime, String acceptContent, String acceptPic) {
        this.acceptTime = acceptTime;
        this.acceptContent = acceptContent;
        this.acceptPic = acceptPic;
    }

    public Trace(String acceptTime, String acceptContent, String acceptPic, String acceptVideo) {
        this.acceptTime = acceptTime;
        this.acceptContent = acceptContent;
        this.acceptPic = acceptPic;
        this.acceptVideo = acceptVideo;
    }

    // public Trace(String acceptTime, String acceptContent, int acceptPic) {
    //     this.acceptTime = acceptTime;
    //     this.acceptContent = acceptContent;
    //     this.acceptPic = acceptPic;
    // }
    //
    // public Trace(String acceptTime, String acceptContent, int acceptPic, String acceptVideo) {
    //     this.acceptTime = acceptTime;
    //     this.acceptContent = acceptContent;
    //     this.acceptPic = acceptPic;
    //     this.acceptVideo = acceptVideo;
    // }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptContent() {
        return acceptContent;
    }

    public void setAcceptContent(String acceptContent) {
        this.acceptContent = acceptContent;
    }

    public String getAcceptPic() {
        return acceptPic;
    }

    public void setAcceptPic(String acceptPic) {
        this.acceptPic = acceptPic;
    }
    // public int getAcceptPic() {
    //     return acceptPic;
    // }
    //
    // public void setAcceptPic(int acceptPic) {
    //     this.acceptPic = acceptPic;
    // }

    public String getAcceptVideo() {
        return acceptVideo;
    }

    public void setAcceptVideo(String acceptVideo) {
        this.acceptVideo = acceptVideo;
    }
}