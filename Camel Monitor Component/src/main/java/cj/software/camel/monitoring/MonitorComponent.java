package cj.software.camel.monitoring;

import java.util.Map;
import java.util.Set;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.CamelContextHelper;

import cj.software.camel.monitoring.monitor.Monitor;

/**
 * Represents the component that manages {@link MonitorEndpoint}.
 */
public class MonitorComponent
		extends DefaultComponent
{
	public static final String MONITOR_RUN_ID = "CamelMonitorRunId";

	public static final String MONITOR = "CamelMonitor";

	@Override
	protected Endpoint createEndpoint(
			String pURI,
			String pRemaining,
			Map<String, Object> pParameters) throws Exception
	{
		MonitorEndpoint lEndpoint = new MonitorEndpoint(pURI, this);
		setProperties(lEndpoint, pParameters);

		CamelContext lCtx = getCamelContext();

		switch (pRemaining)
		{
		case "start":
			this.startMonitoring(lCtx, pParameters, lEndpoint);
			break;
		case "entry":
			break;
		default:
			throw new IllegalArgumentException("unknown remaining URI part: " + pRemaining);
		}

		return lEndpoint;
	}

	private void startMonitoring(
			CamelContext pCtx,
			Map<String, Object> pParameters,
			MonitorEndpoint pEndpoint)
	{
		Monitor lMonitor = this.lookupMonitor(pCtx, pEndpoint);
		pEndpoint.setMonitor(lMonitor);
	}

	private Monitor lookupMonitor(CamelContext pCtx, MonitorEndpoint pEndpoint)
	{
		Monitor lResult = null;

		/*
		 * try to lookup by its reference
		 */
		String lMonitorRef = pEndpoint.getMonitorRef();
		if (lMonitorRef != null)
		{
			lResult = CamelContextHelper.mandatoryLookup(pCtx, lMonitorRef, Monitor.class);
		}

		/*
		 * if the registry contains just one Monitor implementation, use this
		 */
		if (lResult == null)
		{
			Set<Monitor> lMonitors = pCtx.getRegistry().findByType(Monitor.class);
			int lNumMonitors = lMonitors.size();
			if (lNumMonitors > 1)
			{
				throw new IllegalArgumentException(
						String.format(
								"%d Monitors were found in the registry "
										+ "and no explicit configuration provided",
								lNumMonitors));
			}
			else if (lNumMonitors == 1)
			{
				lResult = lMonitors.iterator().next();
			}
		}

		if (lResult == null)
		{
			throw new IllegalArgumentException("monitor must be configured");
		}
		return lResult;
	}
}
