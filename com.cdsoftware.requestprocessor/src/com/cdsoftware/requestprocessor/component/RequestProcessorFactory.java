package com.cdsoftware.requestprocessor.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.adempiere.server.IServerFactory;
import org.compiere.model.MRequestProcessor;
import org.compiere.server.RequestProcessor;

import com.cdsoftware.requestprocessor.server.CDSRequestProcessor;

public class RequestProcessorFactory implements IServerFactory<RequestProcessor, MRequestProcessor>{

	@Override
	public RequestProcessor[] create(Properties ctx) {
	    MRequestProcessor[] requestModels = MRequestProcessor.getActive(ctx);
	    List<RequestProcessor> list = new ArrayList<>();
	    for (MRequestProcessor model : requestModels) {
	        list.add(create(ctx, model));
	    }
	    return list.toArray(new RequestProcessor[0]);
	}

	@Override
	public RequestProcessor create(Properties ctx, MRequestProcessor serverModel) {
		return new CDSRequestProcessor(serverModel);
	}

	@Override
	public Class<MRequestProcessor> getProcessorClass() {
		return MRequestProcessor.class;
	}

}
