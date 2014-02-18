package com.engagepoint.university.messaging.services;

import com.engagepoint.university.messaging.dao.specific.EmailDAO;
import com.engagepoint.university.messaging.dto.EmailDTO;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class EmailService implements Serializable{

    @Inject
    private EmailDAO emailDAO;

    @Inject
    private InitService initService;

    private List<EmailDTO> emailListSortedByBackend;

    private List<EmailDTO> emailListSortedByFrontend;

    public void setEmailListSortedByBackend(List<EmailDTO> emails) {

        this.emailListSortedByBackend = emails;
    }

    public List<EmailDTO> getEmailListSortedByBackend(){

        return emailDAO.getEmailsSortByDeliverDate();
    }

    public void setEmailListSortedByFrontend(List<EmailDTO> emails) {

        this.emailListSortedByBackend = emails;
    }

    public List<EmailDTO> getEmailListSortedByFrontend(){

        return initService.getEmailDTOList();
    }



}
