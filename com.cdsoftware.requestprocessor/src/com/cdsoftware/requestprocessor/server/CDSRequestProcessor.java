package com.cdsoftware.requestprocessor.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MMailText;
import org.compiere.model.MRequest;
import org.compiere.model.MRequestProcessor;
import org.compiere.server.RequestProcessor;
import org.compiere.util.CLogger;
import org.compiere.util.Msg;
import org.compiere.util.Util;

import com.cdsoftware.requestprocessor.model.MRequestMailTemplate;

public class CDSRequestProcessor extends RequestProcessor {

	private static final CLogger log = CLogger.getCLogger(CDSRequestProcessor.class);

	public CDSRequestProcessor(MRequestProcessor model) {
	    super(model);
	    log.warning("CDSRequestProcessor activo para " + model.getName());
	}

    @Override
    protected boolean sendEmail(MRequest request, String AD_Message) {
        MRequestMailTemplate templateConfig = MRequestMailTemplate.getFor(request, AD_Message);
        if (templateConfig == null)
            return super.sendEmail(request, AD_Message);

        int mailTextId = templateConfig.getR_MailText_ID();

        if (mailTextId <= 0)
            return super.sendEmail(request, AD_Message);

        MMailText mailText = new MMailText(getCtx(), mailTextId, null);
        if (mailText.is_new())
            return super.sendEmail(request, AD_Message);

        mailText.setPO(request, true);
        mailText.setUser(request.getSalesRep_ID());
        mailText.setLanguage(m_client.getAD_Language());

        String subject = mailText.getMailHeader();
        String message = mailText.getMailText(true);

        if (Util.isEmpty(subject, true)) {
            subject = Msg.getMsg(m_client.getAD_Language(), AD_Message,
                new String[] { request.getDocumentNo() });
        }

        List<File> attachments = new ArrayList<>();
        File pdf = request.createPDF();
        if (pdf != null)
            attachments.add(pdf);

        return m_client.sendEMailAttachments(
            request.getSalesRep_ID(),
            subject,
            message,
            attachments,
            mailText.isHtml()
        );
    }
}
