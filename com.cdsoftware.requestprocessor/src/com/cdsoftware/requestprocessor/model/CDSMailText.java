package com.cdsoftware.requestprocessor.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.apache.commons.lang3.StringEscapeUtils;
import org.compiere.model.MMailText;
import org.compiere.model.PO;
import org.compiere.util.Util;

/**
 * Mail text parser with request-specific HTML handling.
 */
public class CDSMailText extends MMailText {

	private static final long serialVersionUID = 20260828L;

	private static final String COLUMNNAME_SUMMARY = "Summary";

	public CDSMailText(Properties ctx, int R_MailText_ID, String trxName) {
		super(ctx, R_MailText_ID, trxName);
	}

	public CDSMailText(Properties ctx, String R_MailText_UU, String trxName) {
		super(ctx, R_MailText_UU, trxName);
	}

	public CDSMailText(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	@Override
	protected String parseVariable(String variable, PO po, boolean keepEscapeSequence) {
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
}
