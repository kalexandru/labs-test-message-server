package com.engagepoint.university.messaging.entities;

import com.engagepoint.university.messaging.entities.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "sms")
@NamedQueries({
        @NamedQuery(name = Sms.GET_SEARCHING_SMS, query = "SELECT sm FROM Sms sm WHERE" +
                " sm.sender LIKE :sender OR" +
                " sm.body LIKE :body"),

        @NamedQuery(name = Sms.GET_ALL_SMS, query = "SELECT sm FROM Sms sm"),
        @NamedQuery(name = Sms.GET_ALL_BY_SMS_ID, query = "SELECT sm FROM Sms sm WHERE sm.id = :idSms"),
        @NamedQuery(name = Sms.GET_ALL_BY_SENDER, query = "SELECT sm FROM Sms sm WHERE sm.sender = :sender"),
        @NamedQuery(name = Sms.GET_ALL_BY_SEND_DATE, query = "SELECT sm FROM Sms sm WHERE sm.sendDate = :sendDate"),
        @NamedQuery(name = Sms.GET_ALL_BY_DELIVERY_DATE, query = "SELECT sm FROM Sms sm WHERE sm.deliveryDate = :deliveryDate"),
        @NamedQuery(name = Sms.DELETE_SMS_LIST, query = "DELETE FROM Sms sm WHERE sm.id IN :idList")})

public class Sms implements Serializable, BaseEntity {

    private static final long serialVersionUID = 6745638798781234739L;
    public static final String GET_SEARCHING_SMS = "Sms.findSearchingSms";

    public static final String GET_ALL_SMS = "Sms.findAll";
    public static final String GET_ALL_BY_SMS_ID = "Sms.findByIdSms";
    public static final String GET_ALL_BY_SENDER = "Sms.findBySender";
    public static final String GET_ALL_BY_SEND_DATE = "Sms.findBySendDate";
    public static final String GET_ALL_BY_DELIVERY_DATE = "Sms.findByDeliveryDate";
    public static final String DELETE_SMS_LIST = "Sms.deleteSmsList";
    public static final String PARAM_IDS_LIST = "idList";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sender")
    private String sender;

    @Lob
    @Size(max = 65535)
    @Column(name = "body")
    private String body;

    @Column(name = "send_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "delivery_date")
    private Date deliveryDate;

    @ManyToMany(mappedBy = "smsCollection")
    private Collection<User> userCollection;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }
}
