package com.engagepoint.university.messaging.dto;

import com.engagepoint.university.messaging.dto.base.BaseDTO;

import java.util.Date;

public class EmailDTO extends BaseDTO {

    private String sender;
    private String subject;
    private String body;
    private Date sendDate;
    private Date deliveryDate;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "EmailDTO{" +
                "id=" + this.getId() +
                ", sender='" + sender + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", sendDate=" + sendDate +
                ", deliveryDate=" + deliveryDate +
                '}';
    }
}
