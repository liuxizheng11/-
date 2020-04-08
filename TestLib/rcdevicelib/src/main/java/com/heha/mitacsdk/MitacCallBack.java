package com.heha.mitacsdk;

public abstract class MitacCallBack<T> {
    public final static int STATUS_UNSTART = 1;
    public final static int STATUS_PEDING = 2;   
    public final static int STATUS_FINISH = 3;
    private int status = STATUS_UNSTART;
    
    private String errorMsg = null;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public abstract void callback(int errorCode, T t) ;
}
