package com.engagepoint.acceptancetest.steps.sms;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.commons.util.windowing.WindowFuture;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.*;
import com.engagepoint.university.messaging.smpp.SMSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SmsController {
    public static Logger log = LoggerFactory.getLogger(SMSClient.class);

    //public static void main(String[] args) throws SmppInvalidArgumentException {
    public void sendSMS(String sender, String receiver, String body) {
        DefaultSmppClient client = new DefaultSmppClient();

        SmppSessionConfiguration sessionConfig = new SmppSessionConfiguration();

        sessionConfig.setWindowSize(1);
        sessionConfig.setName("Tester.Session.0");
        sessionConfig.setType(SmppBindType.TRANSCEIVER);
        sessionConfig.setHost("127.0.0.1");
        sessionConfig.setPort(2776);
        sessionConfig.setConnectTimeout(10000);
        sessionConfig.setSystemId("smppclient1");
        sessionConfig.setPassword("password");
        sessionConfig.getLoggingOptions().setLogBytes(true);
        // to enable monitoring (request expiration)
        sessionConfig.setRequestExpiryTimeout(10000);
        sessionConfig.setWindowMonitorInterval(10000);
        sessionConfig.setCountersEnabled(true);

        try {
            SmppSession session = client.bind(sessionConfig, new SmsSessionHandler());

            SubmitSm sm2 = createSubmitSm(sender, receiver, body, "UCS-2");
            sm2.setReferenceObject("Hello2" + sm2 + "//***//");

            WindowFuture<Integer, PduRequest, PduResponse> future2 = session.sendRequestPdu(sm2, TimeUnit.SECONDS.toMillis(10), false);
            while (!future2.isDone()) {
                log.debug("Not done");
                log.debug("Not done Succes is {}", future2.isSuccess());
            }
            session.close();
            session.destroy();
            client.destroy();
        } catch (SmppTimeoutException | SmppChannelException | InterruptedException | UnrecoverablePduException | RecoverablePduException ex) {
            log.error("{}", ex);
        }
    }

    public static SubmitSm createSubmitSm(String src, String dst, String text, String charset) throws SmppInvalidArgumentException {
        SubmitSm sm = new SubmitSm();
        sm.setSourceAddress(new Address((byte) 5, (byte) 0, src));
        sm.setDestAddress(new Address((byte) 1, (byte) 1, dst));
        sm.setDataCoding((byte) 8);
        sm.setShortMessage(CharsetUtil.encode(text, charset));
        sm.setRegisteredDelivery((byte) 1);
        return sm;
    }

}
