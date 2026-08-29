package com.cdsoftware.requestprocessor.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MRequest;
import org.compiere.model.Query;

/**
 * Request mail template configuration.
 */
public class MRequestMailTemplate extends X_CDS_RequestMailTemplate {

	private static final long serialVersionUID = 20260828L;

	public static final String EVENT_REQUEST_DUE = "RequestDue";
	public static final String EVENT_REQUEST_ALERT = "RequestAlert";
	public static final String EVENT_REQUEST_INACTIVE = "RequestInactive";
	public static final String EVENT_REQUEST_ESCALATE = "RequestEscalate";
	public static final String EVENT_REQUEST_UPDATED = "RequestUpdated";

	/**
	 * Standard constructor.
	 *
	 * @param ctx context
	 * @param CDS_RequestMailTemplate_ID record id
	 * @param trxName transaction
	 */
	public MRequestMailTemplate(Properties ctx, int CDS_RequestMailTemplate_ID, String trxName) {
		super(ctx, CDS_RequestMailTemplate_ID, trxName);
	}

	/**
	 * UUID constructor.
	 *
	 * @param ctx context
	 * @param CDS_RequestMailTemplate_UU record uuid
	 * @param trxName transaction
	 */
	public MRequestMailTemplate(Properties ctx, String CDS_RequestMailTemplate_UU, String trxName) {
		super(ctx, CDS_RequestMailTemplate_UU, trxName);
	}

	/**
	 * Load constructor.
	 *
	 * @param ctx context
	 * @param rs result set
	 * @param trxName transaction
	 */
	public MRequestMailTemplate(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * Find the best template configuration for a request processor event.
	 *
	 * @param request request being notified
	 * @param eventCode request processor event code, for example RequestAlert
	 * @return matching template configuration or null
	 */
	public static MRequestMailTemplate getFor(MRequest request, String eventCode) {
		if (request == null || !isSupportedEvent(eventCode))
			return null;

		int requestTypeId = request.getR_RequestType_ID();
		int clientId = request.getAD_Client_ID();
		int orgId = request.getAD_Org_ID();

		String whereClause = "IsActive='Y'"
			+ " AND CDS_EventCode=?"
			+ " AND R_MailText_ID IS NOT NULL"
			+ " AND AD_Client_ID IN (0,?)"
			+ " AND AD_Org_ID IN (0,?)"
			+ " AND (R_RequestType_ID=? OR R_RequestType_ID IS NULL)";
		String orderBy = "CASE WHEN R_RequestType_ID=" + requestTypeId + " THEN 0 ELSE 1 END,"
			+ " CASE WHEN AD_Org_ID=" + orgId + " THEN 0 ELSE 1 END,"
			+ " CASE WHEN AD_Client_ID=" + clientId + " THEN 0 ELSE 1 END,"
			+ " SeqNo, CDS_RequestMailTemplate_ID";

		return new Query(request.getCtx(), Table_Name, whereClause, request.get_TrxName())
			.setParameters(eventCode, clientId, orgId, requestTypeId)
			.setOrderBy(orderBy)
			.first();
	}

	private static boolean isSupportedEvent(String eventCode) {
		return EVENT_REQUEST_DUE.equals(eventCode)
			|| EVENT_REQUEST_ALERT.equals(eventCode)
			|| EVENT_REQUEST_INACTIVE.equals(eventCode)
			|| EVENT_REQUEST_ESCALATE.equals(eventCode)
			|| EVENT_REQUEST_UPDATED.equals(eventCode);
	}
}
