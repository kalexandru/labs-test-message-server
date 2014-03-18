package com.engagepoint.university.messaging.entity;

import com.engagepoint.university.messaging.entity.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "sms")
@NamedQueries({
        @NamedQuery(name = Sms.GET_ALL_SMS, query = "SELECT sm FROM Sms sm"),
        @NamedQuery(name = Sms.GET_ALL_BY_SMS_ID, query = "SELECT sm FROM Sms sm WHERE sm.id = :idSms"),
        @NamedQuery(name = Sms.GET_ALL_BY_SENDER, query = "SELECT sm FROM Sms sm WHERE sm.sender LIKE :sender"),
        @NamedQuery(name = Sms.GET_ALL_BY_SEND_DATE, query = "SELECT sm FROM Sms sm WHERE sm.sendDate = :sendDate"),
        @NamedQuery(name = Sms.GET_ALL_BY_DELIVERY_DATE, query = "SELECT sm FROM Sms sm WHERE sm.deliveryDate = :deliveryDate"),
        @NamedQuery(name = Sms.DELETE_SMS_LIST, query = "DELETE FROM Sms sm WHERE sm.id IN :idList"),
        @NamedQuery(name = Sms.GET_SMS_QUICK_SEARCH, query = "SELECT DISTINCT sm FROM Sms sm" +
                " LEFT JOIN sm.userCollection userCollection" +
                " WHERE userCollection.name IN (SELECT us.name FROM User us WHERE us.name LIKE :userName) OR" +
                " sm.sender LIKE :sender OR sm.recipient LIKE :recipient OR sm.body LIKE :body OR cast(sm.deliveryDate as string) LIKE :deliveryDate"),
})

public class Sms implements Serializable, BaseEntity {

    private static final long serialVersionUID = 6745638798781234739L;

    public static final String GET_ALL_SMS = "Sms.findAll";
    public static final String GET_ALL_BY_SMS_ID = "Sms.findByIdSms";
    public static final String GET_ALL_BY_SENDER = "Sms.findBySender";
    public static final String GET_ALL_BY_SEND_DATE = "Sms.findBySendDate";
    public static final String GET_ALL_BY_DELIVERY_DATE = "Sms.findByDeliveryDate";
    public static final String DELETE_SMS_LIST = "Sms.deleteSmsList";
    public static final String GET_SMS_QUICK_SEARCH = "Sms.smsQuickSearch";

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
    private Date sendDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delivery_date")
    private Date deliveryDate;

    @ManyToMany(mappedBy = "smsCollection", fetch = FetchType.EAGER)
    private Collection<User> userCollection;

    @Column(name = "recipient")
    private String recipient;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

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
