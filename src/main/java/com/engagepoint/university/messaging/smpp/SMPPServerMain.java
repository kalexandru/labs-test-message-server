package com.engagepoint.university.messaging.smpp;

import com.cloudhopper.smpp.*;
import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.*;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppProcessingException;
import com.engagepoint.university.messaging.dto.SmsDTO;
import com.engagepoint.university.messaging.service.repository.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class SMPPServerMain {
    private static final Logger LOG = LoggerFactory.getLogger(SMPPServerMain.class);

    private ThreadPoolExecutor executor;
    private ScheduledThreadPoolExecutor monitorExecutor;
    private SmppServerConfiguration configuration;
    private DefaultSmppServer smppServer;
    private static final  int PORT = 2776;
    private static final  String HOST = "127.0.0.1";

    @Inject
    private SmsService smsService;

    public SMPPServerMain() {
        setExecutor();
        setConfiguration();
        setMonitorExecutor();
    }

    public void startSmppServer() {
        smppServer = new DefaultSmppServer(this.configuration, new DefaultSmppServerHandler(),
                this.executor, this.monitorExecutor);
        try {
            smppServer.start();
        } catch (SmppChannelException e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    public void stopSmppServer() {
        smppServer.stop();
        smppServer.destroy();
    }

    public void setExecutor() {
        this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    public void setMonitorExecutor() {
        this.monitorExecutor = (ScheduledThreadPoolExecutor)
                Executors.newScheduledThreadPool(1, new ThreadFactory() {
                    private AtomicInteger sequence = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName("SmppServerSessionWindowMonitorPool-" + sequence.getAndIncrement());
                        return t;
                    }
                });
    }

    public void setConfiguration() {
        this.configuration = new SmppServerConfiguration();
        configuration.setPort(PORT);
        configuration.setMaxConnectionSize(10);
        configuration.setNonBlockingSocketsEnabled(true);
        configuration.setDefaultRequestExpiryTimeout(30000);
        configuration.setDefaultWindowMonitorInterval(15000);
        configuration.setDefaultWindowSize(5);
        configuration.setDefaultWindowWaitTimeout(configuration.getDefaultRequestExpiryTimeout());
        configuration.setDefaultSessionCountersEnabled(true);
        configuration.setJmxEnabled(true);
        configuration.setReuseAddress(true);
    }

    public int getPort () {
        return PORT;
    }

    public String getHost () {
        return HOST;
    }

    public class DefaultSmppServerHandler implements SmppServerHandler {

        @Override
        public void sessionBindRequested(Long sessionId, SmppSessionConfiguration sessionConfiguration, final BaseBind bindRequest) throws SmppProcessingException {
            sessionConfiguration.setName("Application.SMPP." + sessionConfiguration.getSystemId());
        }

        @Override
        public void sessionCreated(Long sessionId, SmppServerSession session, BaseBindResp preparedBindResponse) throws SmppProcessingException {
            LOG.info("Session created: {}", session);
            // need to do something it now (flag we're ready)
            session.serverReady(new TestSmppSessionHandler());
        }

        @Override
        public void sessionDestroyed(Long sessionId, SmppServerSession session) {
            LOG.info("Session destroyed: {}", session);
            if (session.hasCounters()) {
                LOG.info(" final session rx-submitSM: {}", session.getCounters().getRxSubmitSM());
            }
            session.destroy();
        }
    }

    public class TestSmppSessionHandler extends DefaultSmppSessionHandler {

        @Override
        public PduResponse firePduRequestReceived(PduRequest pduRequest) {
            SubmitSm req = (SubmitSm) pduRequest;
            String str = null;

            try {
                str = new String(req.getShortMessage(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOG.info(e.getMessage(), e);
            }
            SmsDTO smsDTO = new SmsDTO();
            smsDTO.setBody(str);
            smsDTO.setSender(req.getSourceAddress().getAddress());
            smsDTO.setDeliveryDate(new Date());
            smsDTO.setSendDate(new Date());
            smsDTO.setRecipient(req.getDestAddress().getAddress());

            smsService.save(smsDTO);

            return pduRequest.createResponse();
        }
    }

}
