package com.cdsoftware.requestprocessor.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.model.MTable;
import org.compiere.util.KeyNamePair;

/** Generated Interface for CDS_RequestMailTemplate
 *  @author iDempiere (generated)
 *  @version Release 12
 */
public interface I_CDS_RequestMailTemplate
{
	/** TableName=CDS_RequestMailTemplate */
	public static final String Table_Name = "CDS_RequestMailTemplate";

	/** AD_Table_ID */
	public static final int Table_ID = MTable.getTable_ID(Table_Name);

	KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

	/** AccessLevel = 3 - Client - Org */
	BigDecimal accessLevel = BigDecimal.valueOf(3);

	/** Column name AD_Client_ID */
	public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Tenant.
	  * Tenant for this installation.
	  */
	public int getAD_Client_ID();

	/** Column name AD_Org_ID */
	public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within tenant
	  */
	public void setAD_Org_ID(int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within tenant
	  */
	public int getAD_Org_ID();

	/** Column name CDS_EventCode */
	public static final String COLUMNNAME_CDS_EventCode = "CDS_EventCode";

	/** Set Event Code */
	public void setCDS_EventCode(String CDS_EventCode);

	/** Get Event Code */
	public String getCDS_EventCode();

	/** Column name CDS_RequestMailTemplate_ID */
	public static final String COLUMNNAME_CDS_RequestMailTemplate_ID = "CDS_RequestMailTemplate_ID";

	/** Set Request Mail Template */
	public void setCDS_RequestMailTemplate_ID(int CDS_RequestMailTemplate_ID);

	/** Get Request Mail Template */
	public int getCDS_RequestMailTemplate_ID();

	/** Column name CDS_RequestMailTemplate_UU */
	public static final String COLUMNNAME_CDS_RequestMailTemplate_UU = "CDS_RequestMailTemplate_UU";

	/** Set CDS_RequestMailTemplate_UU */
	public void setCDS_RequestMailTemplate_UU(String CDS_RequestMailTemplate_UU);

	/** Get CDS_RequestMailTemplate_UU */
	public String getCDS_RequestMailTemplate_UU();

	/** Column name Created */
	public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

	/** Column name CreatedBy */
	public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

	/** Column name IsActive */
	public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive(boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

	/** Column name R_MailText_ID */
	public static final String COLUMNNAME_R_MailText_ID = "R_MailText_ID";

	/** Set Mail Template.
	  * Text templates for mailings
	  */
	public void setR_MailText_ID(int R_MailText_ID);

	/** Get Mail Template.
	  * Text templates for mailings
	  */
	public int getR_MailText_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_R_MailText getR_MailText() throws RuntimeException;

	/** Column name R_RequestType_ID */
	public static final String COLUMNNAME_R_RequestType_ID = "R_RequestType_ID";

	/** Set Request Type.
	  * Type of request
	  */
	public void setR_RequestType_ID(int R_RequestType_ID);

	/** Get Request Type.
	  * Type of request
	  */
	public int getR_RequestType_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_R_RequestType getR_RequestType() throws RuntimeException;

	/** Column name SeqNo */
	public static final String COLUMNNAME_SeqNo = "SeqNo";

	/** Set Sequence */
	public void setSeqNo(int SeqNo);

	/** Get Sequence */
	public int getSeqNo();

	/** Column name Updated */
	public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

	/** Column name UpdatedBy */
	public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

	/** Column name Value */
	public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue(String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
