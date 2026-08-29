package com.cdsoftware.requestprocessor.event;

import static org.compiere.model.SystemIDs.MESSAGE_REQUESTUPDATE;

import org.adempiere.base.annotation.EventTopicDelegate;
import org.adempiere.base.event.RequestSendEMailEventData;
import org.adempiere.base.event.annotations.RequestSendEmailEventDelegate;
import org.apache.commons.lang3.StringEscapeUtils;
import org.compiere.model.MNote;
import org.compiere.model.MMessage;
import org.compiere.model.MRequest;
import org.compiere.model.X_R_Request;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.osgi.service.event.Event;

import com.cdsoftware.requestprocessor.model.CDSMailText;
import com.cdsoftware.requestprocessor.model.MRequestMailTemplate;

@EventTopicDelegate
public class RequestUpdateMailTemplateEvent extends RequestSendEmailEventDelegate {

	private static final CLogger log = CLogger.getCLogger(RequestUpdateMailTemplateEvent.class);

	private static final String VARIABLE_ORIGINAL_SUBJECT = "OriginalSubject";
	private static final String VARIABLE_REQUEST_UPDATE_MESSAGE = "RequestUpdateMessage";
	private static final String VARIABLE_REQUEST_UPDATE_MESSAGE_HTML = "RequestUpdateMessageHtml";
	private static final String VARIABLE_RECIPIENT_NAME = "RecipientName";
	private static final String VARIABLE_SENDER_NAME = "SenderName";

	public RequestUpdateMailTemplateEvent(Event event) {
		super(event);
	}

	@Override
	protected void onRequestSendEmail(RequestSendEMailEventData eventData) {
		if (eventData == null || eventData.getClient() == null || eventData.getTo() == null)
			return;

		MRequest request = new MRequest(Env.getCtx(), eventData.getRequestID(), null);
		CDSMailText mailText = getMailText(request);
		if (mailText == null) {
			sendEmail(eventData, eventData.getSubject(), eventData.getMessage(), eventData.isHtml());
			return;
		}

		mailText.setPO(request, true);
		mailText.setUser(eventData.getTo().getAD_User_ID());
		mailText.setLanguage(eventData.getClient().getAD_Language());
		setVariables(mailText, eventData);

		String subject = mailText.getMailHeader();
		if (Util.isEmpty(subject, true))
			subject = eventData.getSubject();

		sendEmail(eventData, subject, mailText.getMailText(true), mailText.isHtml());
	}

	private CDSMailText getMailText(MRequest request) {
		if (request == null || request.is_new())
			return null;

		MRequestMailTemplate templateConfig = MRequestMailTemplate.getFor(
			request,
			MRequestMailTemplate.EVENT_REQUEST_UPDATED
		);
		if (templateConfig == null || templateConfig.getR_MailText_ID() <= 0)
			return null;

		CDSMailText mailText = new CDSMailText(Env.getCtx(), templateConfig.getR_MailText_ID(), null);
		if (mailText.is_new())
			return null;

		return mailText;
	}

	private void setVariables(CDSMailText mailText, RequestSendEMailEventData eventData) {
		mailText.setCustomVariable(VARIABLE_ORIGINAL_SUBJECT, eventData.getSubject());
		mailText.setCustomVariable(VARIABLE_REQUEST_UPDATE_MESSAGE, eventData.getMessage());
		mailText.setCustomVariable(VARIABLE_REQUEST_UPDATE_MESSAGE_HTML, toHtml(eventData.getMessage()));
		mailText.setCustomVariable(VARIABLE_RECIPIENT_NAME, eventData.getTo().getName());
		mailText.setCustomVariable(VARIABLE_SENDER_NAME,
			eventData.getFrom() != null ? eventData.getFrom().getName() : "");
	}

	private String toHtml(String text) {
		if (Util.isEmpty(text))
			return "";

		int summaryStart = text.indexOf(MRequest.SEPARATOR);
		if (summaryStart < 0)
			return plainTextToHtml(text);

		summaryStart += MRequest.SEPARATOR.length();
		int resultStart = text.indexOf("\n----------\n", summaryStart);
		int trailerStart = text.indexOf(MRequest.SEPARATOR, summaryStart);
		int summaryEnd = text.length();
		if (resultStart >= 0)
			summaryEnd = resultStart;
		if (trailerStart >= 0 && trailerStart < summaryEnd)
			summaryEnd = trailerStart;

		String beforeSummary = text.substring(0, summaryStart - MRequest.SEPARATOR.length());
		String summary = text.substring(summaryStart, summaryEnd);
		String afterSummary = text.substring(summaryEnd);

		return plainTextToHtml(beforeSummary)
			+ "<hr/>"
			+ StringEscapeUtils.unescapeHtml4(summary)
			+ plainTextToHtml(afterSummary);
	}

	private String plainTextToHtml(String text) {
		return StringEscapeUtils.escapeHtml4(text).replace("\r\n", "\n").replace("\n", "<br/>");
	}

	private void sendEmail(RequestSendEMailEventData eventData, String subject, String message, boolean isHtml) {
		boolean sent = eventData.getClient().sendEMail(
			eventData.getFrom(),
			eventData.getTo(),
			subject,
			message,
			eventData.getAttachment(),
			isHtml
		);

		if (!sent) {
			MRequest request = new MRequest(Env.getCtx(), eventData.getRequestID(), null);
			int clientId = !request.is_new() ? request.getAD_Client_ID() : eventData.getClient().getAD_Client_ID();
			int orgId = !request.is_new() ? request.getAD_Org_ID() : 0;
			String messageValue = MMessage.get(Env.getCtx(), MESSAGE_REQUESTUPDATE).getValue();
			MNote note = new MNote(Env.getCtx(), messageValue, eventData.getTo().getAD_User_ID(), clientId, orgId, null);
			note.setRecord(X_R_Request.Table_ID, eventData.getRequestID());
			note.setReference(subject);
			note.setTextMsg(message);
			note.saveEx();
			log.warning("Could not send request update email, notice created for " + eventData.getTo());
		}
	}
}
