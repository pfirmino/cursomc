package com.pfirmino.cursomc.resources.exceptions;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StandardError implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer status;
    private String msg;

    @JsonFormat( pattern = "dd/MM/yyyy HH:mm" )
    private Date timeStamp;

    public StandardError(Integer status, String msg, Date date) {
        super();
        this.status = status;
        this.msg = msg;
        this.timeStamp = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    
}
