package com.cdsoftware.requestprocessor.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringEscapeUtils;
import org.compiere.model.MMailText;
import org.compiere.model.MRequest;
import org.compiere.model.MSysConfig;
import org.compiere.model.PO;
import org.compiere.util.Util;

/**
 * Mail text parser with request-specific HTML handling.
 */
public class CDSMailText extends MMailText {

	private static final long serialVersionUID = 20260828L;

	private static final String COLUMNNAME_SUMMARY = "Summary";
	private static final String APPLICATION_URL_HARDCODED = "USE_HARDCODED";
	private static final String VARIABLE_REQUEST_URL = "RequestURL";
	private static final String VARIABLE_REQUEST_LINK = "RequestLink";

	private final Map<String, String> customVariables = new HashMap<>();

	public CDSMailText(Properties ctx, int R_MailText_ID, String trxName) {
		super(ctx, R_MailText_ID, trxName);
	}

	public CDSMailText(Properties ctx, String R_MailText_UU, String trxName) {
		super(ctx, R_MailText_UU, trxName);
	}

	public CDSMailText(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public void setCustomVariable(String name, String value) {
		if (!Util.isEmpty(name, true))
			customVariables.put(name, value != null ? value : "");
	}

	public void setRequestVariables(MRequest request) {
		String requestUrl = getRequestURL(request);
		setCustomVariable(VARIABLE_REQUEST_URL, requestUrl);
		setCustomVariable(VARIABLE_REQUEST_LINK, getRequestLink(requestUrl));
	}

	@Override
	protected String parseVariable(String variable, PO po, boolean keepEscapeSequence) {
		String customValue = customVariables.get(variable);
		if (customValue != null)
			return customValue;

		if (isHtml() && COLUMNNAME_SUMMARY.equalsIgnoreCase(variable)) {
			return parseSummaryAsHtml(po);
		}
		return super.parseVariable(variable, po, keepEscapeSequence);
	}

	private String parseSummaryAsHtml(PO po) {
		if (po == null || po.get_ColumnIndex(COLUMNNAME_SUMMARY) < 0)
			return "@" + COLUMNNAME_SUMMARY + "@";

		String summary = po.get_ValueAsString(COLUMNNAME_SUMMARY);
		if (Util.isEmpty(summary))
			return "";

		return StringEscapeUtils.unescapeHtml4(summary);
	}

	private String getRequestURL(MRequest request) {
		if (request == null || request.is_new())
			return "";

		String applicationUrl = MSysConfig.getValue(
			MSysConfig.APPLICATION_URL,
			"",
			request.getAD_Client_ID(),
			request.getAD_Org_ID()
		);
		if (Util.isEmpty(applicationUrl, true) || APPLICATION_URL_HARDCODED.equals(applicationUrl))
			return "";

		return normalizeApplicationUrl(applicationUrl)
			+ "?Action=Zoom&TableName=R_Request&Record_ID=" + request.getR_Request_ID();
	}

	private String normalizeApplicationUrl(String applicationUrl) {
		String normalized = applicationUrl.trim();
		while (normalized.endsWith("/"))
			normalized = normalized.substring(0, normalized.length() - 1);

		if (normalized.endsWith("/webui"))
			return normalized + "/index.zul";
		if (!normalized.endsWith("index.zul"))
			return normalized + "/webui/index.zul";

		return normalized;
	}

	private String getRequestLink(String requestUrl) {
		if (Util.isEmpty(requestUrl, true))
			return "";

		String escapedUrl = StringEscapeUtils.escapeHtml4(requestUrl);
		return "<a href=\"" + escapedUrl + "\" style=\"color:#1f5d8f; font-weight:bold;\">Abrir solicitud</a>";
	}
}
