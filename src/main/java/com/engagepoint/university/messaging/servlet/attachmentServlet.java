package com.engagepoint.university.messaging.servlet;

import com.engagepoint.university.messaging.dto.AttachmentDTO;
import com.engagepoint.university.messaging.service.repository.AttachmentService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AttachmentServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentServlet.class);

    @Inject
    private AttachmentService attachmentService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = 0;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (Exception e) {
            LOG.info("It's not a number!", e);
            return;
        }
        Base64 base64codec = new Base64();
        AttachmentDTO attachmentDTO = null;
        try {
            attachmentDTO = attachmentService.getById(id);
        } catch (NullPointerException e) {
            LOG.info("Error 404: Attachment Not Found!", e);
            return;
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Transfer-Encoding", "binary");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + attachmentDTO.getName() + "\"");
        IOUtils.copy(new ByteArrayInputStream(base64codec.decode(attachmentDTO.getContent())), resp.getOutputStream());
        resp.getOutputStream().flush();
        resp.getOutputStream().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
