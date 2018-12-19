package com.file.filetransfer.model;

public class FileBean {
    private String code;
    private String tmp_linux_address;
    private String fact_linux_address;
    private String file_name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getTmp_linux_address() {
        return tmp_linux_address;
    }

    public void setTmp_linux_address(String tmp_linux_address) {
        this.tmp_linux_address = tmp_linux_address;
    }

    public String getFact_linux_address() {
        return fact_linux_address;
    }

    public void setFact_linux_address(String fact_linux_address) {
        this.fact_linux_address = fact_linux_address;
    }
}
