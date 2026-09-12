package com.cdsoftware.requestprocessor.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MBPartner;
import org.compiere.model.MRequest;
import org.compiere.model.MRequestProcessor;
import org.compiere.model.MUser;
import org.compiere.server.RequestProcessor;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;

import com.cdsoftware.requestprocessor.model.CDSMailText;
import com.cdsoftware.requestprocessor.model.MRequestMailTemplate;

public class CDSRequestProcessor extends RequestProcessor {

	private static final CLogger log = CLogger.getCLogger(CDSRequestProcessor.class);

	public CDSRequestProcessor(MRequestProcessor model) {
	    super(model);
	    log.warning("CDSRequestProcessor activo para " + model.getName());
	}

    @Override
    protected boolean sendEmail(MRequest request, String AD_Message) {
        String subject = Msg.getMsg(m_client.getAD_Language(), AD_Message,
            new String[] { request.getDocumentNo() });
        Boolean sent = sendTemplateEmail(request, AD_Message, request.getSalesRep_ID(), subject);
        if (sent != null)
            return sent;

        return super.sendEmail(request, AD_Message);
    }

    @Override
    protected boolean escalate(MRequest request) {
        MUser supervisor = request.getSalesRep();
        int supervisor_ID = request.getSalesRep().getSupervisor_ID();
        if (supervisor_ID == 0 && m_model.getSupervisor_ID() != 0)
            supervisor_ID = m_model.getSupervisor_ID();
        if (supervisor_ID != 0 && supervisor_ID != request.getSalesRep_ID())
            supervisor = MUser.get(getCtx(), supervisor_ID);

        String subject = Msg.getMsg(m_client.getAD_Language(), MRequestMailTemplate.EVENT_REQUEST_ESCALATE,
            new String[] { request.getDocumentNo(), supervisor.getName() });

        String to = request.getSalesRep().getEMail();
        if (to == null || to.length() == 0)
            log.warning("SalesRep has no EMail - " + request.getSalesRep());
        else {
            Boolean sent = sendTemplateEmail(request, MRequestMailTemplate.EVENT_REQUEST_ESCALATE,
                request.getSalesRep_ID(), subject);
            if (sent == null)
                m_client.sendEMail(request.getSalesRep_ID(), subject, request.getSummary(), request.createPDF());
        }

        if (request.getSalesRep_ID() != supervisor.getAD_User_ID()) {
            to = supervisor.getEMail();
            if (to == null || to.length() == 0)
                log.warning("Supervisor has no EMail - " + supervisor);
            else {
                Boolean sent = sendTemplateEmail(request, MRequestMailTemplate.EVENT_REQUEST_ESCALATE,
                    supervisor.getAD_User_ID(), subject);
                if (sent == null)
                    m_client.sendEMail(supervisor.getAD_User_ID(), subject, request.getSummary(), request.createPDF());
            }
        }

        request.setDueType();
        request.setIsEscalated(true);
        request.setResult(subject);
        return request.save();
    }

    private Boolean sendTemplateEmail(MRequest request, String eventCode, int recipientId, String defaultSubject) {
        MRequestMailTemplate templateConfig = MRequestMailTemplate.getFor(request, eventCode);
        if (templateConfig == null)
            return null;

        int mailTextId = templateConfig.getR_MailText_ID();

        if (mailTextId <= 0)
            return null;

        CDSMailText mailText = new CDSMailText(getCtx(), mailTextId, null);
        if (mailText.is_new())
            return null;

        mailText.setPO(request, false);
        mailText.setUser(recipientId);
        mailText.setLanguage(getMailTextLanguage(recipientId));
        mailText.setRequestVariables(request);

        String subject = mailText.getMailHeader();
        String message = mailText.getMailText(true);

        if (Util.isEmpty(subject, true))
            subject = defaultSubject;

        List<File> attachments = new ArrayList<>();
        File pdf = request.createPDF();
        if (pdf != null)
            attachments.add(pdf);

        return m_client.sendEMailAttachments(
            recipientId,
            subject,
            message,
            attachments,
            mailText.isHtml()
        );
    }

    private String getMailTextLanguage(int recipientId) {
        String language = getRecipientBPartnerLanguage(recipientId);
        if (!Util.isEmpty(language, true))
            return language;

        language = Env.getContext(getCtx(), Env.LANGUAGE);
        if (!Util.isEmpty(language, true))
            return language;

        return m_client.getAD_Language();
    }

    private String getRecipientBPartnerLanguage(int recipientId) {
        MUser recipient = MUser.get(getCtx(), recipientId);
        if (recipient == null || recipient.getC_BPartner_ID() <= 0)
            return null;

        MBPartner bpartner = new MBPartner(getCtx(), recipient.getC_BPartner_ID(), null);
        return bpartner.getAD_Language();
    }
}
