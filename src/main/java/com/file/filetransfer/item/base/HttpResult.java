package com.file.filetransfer.item.base;

public class HttpResult {
    private int status;
    private String msg;
    private Object data;

    public HttpResult() {
		super();
	}
    
    public HttpResult(int status) {
		super();
		this.status = status;
	}
    
    public HttpResult(int status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}
    
    public HttpResult(int status, String msg, Object data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public  static HttpResult success(){
        return new HttpResult(1);
    }

    public  static HttpResult success(String msg){
        return new HttpResult(1,msg);
    }
    public  static HttpResult success(String msg, Object data){
    	return new HttpResult(1,msg,data);
    }

    public  static HttpResult error(){
    	return new HttpResult(2);
    }

    public  static HttpResult error(String msg){
        return new HttpResult(2,msg);
    }
    public  static HttpResult error(String msg, Object data){
    	return new HttpResult(2,msg,data);
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    
    
}
