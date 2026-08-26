package com.cdsoftware.requestprocessor.server;

import org.compiere.model.MRequest;
import org.compiere.model.MRequestProcessor;
import org.compiere.server.RequestProcessor;
import org.compiere.util.CLogger;

public class CDSRequestProcessor extends RequestProcessor {

	private static final CLogger log = CLogger.getCLogger(CDSRequestProcessor.class);

	public CDSRequestProcessor(MRequestProcessor model) {
	    super(model);
	    log.warning("CDSRequestProcessor activo para " + model.getName());
	}

    @Override
    protected boolean sendEmail(MRequest request, String AD_Message) {
        return super.sendEmail(request, AD_Message);
    }
}
