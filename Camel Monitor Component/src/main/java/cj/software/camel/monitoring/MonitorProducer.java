package cj.software.camel.monitoring;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import cj.software.camel.monitoring.monitor.Monitor;

/**
 * The Monitor producer.
 */
public class MonitorProducer
		extends DefaultProducer
{
	private MonitorEndpoint endpoint;

	public MonitorProducer(MonitorEndpoint endpoint)
	{
		super(endpoint);
		this.endpoint = endpoint;
	}

	private String getInitialUriPart()
	{
		String lEndpointUri = this.endpoint.getEndpointUri();
		int lIndex = lEndpointUri.indexOf("?");
		String lResult = lEndpointUri.substring(0, lIndex);
		lResult = lResult.substring("moni://".length());
		return lResult;
	}

	@Override
	public void process(Exchange pExchange) throws Exception
	{
		String lInitialUriPart = this.getInitialUriPart();
		switch (lInitialUriPart)
		{
		case "start":
			this.start(pExchange);
			break;
		default:
			throw new UnsupportedOperationException("unknown URI-Part:" + lInitialUriPart);
		}

		pExchange.setOut(pExchange.getIn());
	}

	private void start(Exchange pExchange)
	{
		Monitor lMonitor = this.endpoint.getMonitor();
		String lRunningContext = this.endpoint.getRunningContext();
		String lRunId = lMonitor.startNewRunningContext(lRunningContext);
		pExchange.setProperty(MonitorComponent.MONITOR_RUN_ID, lRunId);
	}
}
