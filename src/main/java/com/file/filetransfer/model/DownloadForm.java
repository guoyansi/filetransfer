package com.file.filetransfer.model;

public class DownloadForm {
    private String url;
    private String fileName;
    private String ftpCode;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFtpCode() {
        return ftpCode;
    }

    public void setFtpCode(String ftpCode) {
        this.ftpCode = ftpCode;
    }
}
