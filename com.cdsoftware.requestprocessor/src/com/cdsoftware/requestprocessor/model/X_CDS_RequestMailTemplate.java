/** Generated Model - DO NOT CHANGE */
package com.cdsoftware.requestprocessor.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.I_Persistent;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.POInfo;
import org.compiere.util.KeyNamePair;

/** Generated Model for CDS_RequestMailTemplate
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$
 */
@org.adempiere.base.Model(table="CDS_RequestMailTemplate")
public class X_CDS_RequestMailTemplate extends PO implements I_CDS_RequestMailTemplate, I_Persistent
{
	/**
	 *
	 */
	private static final long serialVersionUID = 20260828L;

	/** Standard Constructor */
	public X_CDS_RequestMailTemplate(Properties ctx, int CDS_RequestMailTemplate_ID, String trxName)
	{
		super(ctx, CDS_RequestMailTemplate_ID, trxName);
		/** if (CDS_RequestMailTemplate_ID == 0)
		{
			setAD_Org_ID(0);
			setCDS_RequestMailTemplate_ID(0);
			setIsActive(true);
		} */
	}

	/** Standard Constructor */
	public X_CDS_RequestMailTemplate(Properties ctx, int CDS_RequestMailTemplate_ID, String trxName, String ... virtualColumns)
	{
		super(ctx, CDS_RequestMailTemplate_ID, trxName, virtualColumns);
		/** if (CDS_RequestMailTemplate_ID == 0)
		{
			setAD_Org_ID(0);
			setCDS_RequestMailTemplate_ID(0);
			setIsActive(true);
		} */
	}

	/** Standard Constructor */
	public X_CDS_RequestMailTemplate(Properties ctx, String CDS_RequestMailTemplate_UU, String trxName)
	{
		super(ctx, CDS_RequestMailTemplate_UU, trxName);
		/** if (CDS_RequestMailTemplate_UU == null)
		{
			setAD_Org_ID(0);
			setCDS_RequestMailTemplate_ID(0);
			setIsActive(true);
		} */
	}

	/** Standard Constructor */
	public X_CDS_RequestMailTemplate(Properties ctx, String CDS_RequestMailTemplate_UU, String trxName, String ... virtualColumns)
	{
		super(ctx, CDS_RequestMailTemplate_UU, trxName, virtualColumns);
		/** if (CDS_RequestMailTemplate_UU == null)
		{
			setAD_Org_ID(0);
			setCDS_RequestMailTemplate_ID(0);
			setIsActive(true);
		} */
	}

	/** Load Constructor */
	public X_CDS_RequestMailTemplate(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}

	/** AccessLevel
	  * @return 3 - Client - Org
	  */
	protected int get_AccessLevel()
	{
		return accessLevel.intValue();
	}

	/** Load Meta Data */
	protected POInfo initPO(Properties ctx)
	{
		POInfo poi = POInfo.getPOInfo(ctx, Table_ID, get_TrxName());
		return poi;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder("X_CDS_RequestMailTemplate[")
			.append(get_ID()).append(",Value=").append(getValue()).append("]");
		return sb.toString();
	}

	/** Set Event Code.
		@param CDS_EventCode Event Code
	*/
	public void setCDS_EventCode(String CDS_EventCode)
	{
		set_Value(COLUMNNAME_CDS_EventCode, CDS_EventCode);
	}

	/** Get Event Code.
		@return Event Code
	  */
	public String getCDS_EventCode()
	{
		return (String)get_Value(COLUMNNAME_CDS_EventCode);
	}

	/** Set Request Mail Template.
		@param CDS_RequestMailTemplate_ID Request Mail Template
	*/
	public void setCDS_RequestMailTemplate_ID(int CDS_RequestMailTemplate_ID)
	{
		if (CDS_RequestMailTemplate_ID < 1)
			set_ValueNoCheck(COLUMNNAME_CDS_RequestMailTemplate_ID, null);
		else
			set_ValueNoCheck(COLUMNNAME_CDS_RequestMailTemplate_ID, Integer.valueOf(CDS_RequestMailTemplate_ID));
	}

	/** Get Request Mail Template.
		@return Request Mail Template
	  */
	public int getCDS_RequestMailTemplate_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CDS_RequestMailTemplate_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set CDS_RequestMailTemplate_UU.
		@param CDS_RequestMailTemplate_UU CDS_RequestMailTemplate_UU
	*/
	public void setCDS_RequestMailTemplate_UU(String CDS_RequestMailTemplate_UU)
	{
		set_Value(COLUMNNAME_CDS_RequestMailTemplate_UU, CDS_RequestMailTemplate_UU);
	}

	/** Get CDS_RequestMailTemplate_UU.
		@return CDS_RequestMailTemplate_UU
	  */
	public String getCDS_RequestMailTemplate_UU()
	{
		return (String)get_Value(COLUMNNAME_CDS_RequestMailTemplate_UU);
	}

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_R_MailText getR_MailText() throws RuntimeException
	{
		return (org.compiere.model.I_R_MailText)MTable.get(getCtx(), org.compiere.model.I_R_MailText.Table_ID)
			.getPO(getR_MailText_ID(), get_TrxName());
	}

	/** Set Mail Template.
		@param R_MailText_ID Text templates for mailings
	*/
	public void setR_MailText_ID(int R_MailText_ID)
	{
		if (R_MailText_ID < 1)
			set_Value(COLUMNNAME_R_MailText_ID, null);
		else
			set_Value(COLUMNNAME_R_MailText_ID, Integer.valueOf(R_MailText_ID));
	}

	/** Get Mail Template.
		@return Text templates for mailings
	  */
	public int getR_MailText_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_MailText_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_R_RequestType getR_RequestType() throws RuntimeException
	{
		return (org.compiere.model.I_R_RequestType)MTable.get(getCtx(), org.compiere.model.I_R_RequestType.Table_ID)
			.getPO(getR_RequestType_ID(), get_TrxName());
	}

	/** Set Request Type.
		@param R_RequestType_ID Type of request
	*/
	public void setR_RequestType_ID(int R_RequestType_ID)
	{
		if (R_RequestType_ID < 1)
			set_Value(COLUMNNAME_R_RequestType_ID, null);
		else
			set_Value(COLUMNNAME_R_RequestType_ID, Integer.valueOf(R_RequestType_ID));
	}

	/** Get Request Type.
		@return Type of request
	  */
	public int getR_RequestType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_RequestType_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Sequence.
		@param SeqNo Sequence
	*/
	public void setSeqNo(int SeqNo)
	{
		set_Value(COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Sequence
	  */
	public int getSeqNo()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue(String Value)
	{
		set_Value(COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

	/** Get Record ID/ColumnName
		@return ID/ColumnName pair
	  */
	public KeyNamePair getKeyNamePair()
	{
		return new KeyNamePair(get_ID(), getValue());
	}
}
