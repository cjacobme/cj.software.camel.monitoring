package cj.software.camel.monitoring;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import cj.software.camel.monitoring.data.MonitoredExchange;
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
		String lResult;
		if (lIndex >= 0)
		{
			lResult = lEndpointUri.substring(0, lIndex);
		}
		else
		{
			lResult = lEndpointUri;
		}
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
		case "entry":
			this.entry(pExchange);
			break;
		default:
			throw new UnsupportedOperationException("unknown URI-Part:" + lInitialUriPart);
		}

		pExchange.setOut(pExchange.getIn());
	}

	private void start(Exchange pExchange)
	{
		Monitor lMonitor = this.endpoint.getMonitor();
		pExchange.setProperty(MonitorComponent.MONITOR, lMonitor);
		String lRunId = lMonitor.startNewExchange(pExchange);
		pExchange.setProperty(MonitorComponent.MONITOR_RUN_ID, lRunId);
	}

	private void entry(Exchange pExchange)
	{
		MonitoredExchange lMonitoredExchange = Converter.toMonitoredExchange(pExchange);
		Monitor lMonitor = (Monitor) pExchange.getProperty(MonitorComponent.MONITOR);
		lMonitor.monitor(this.endpoint, lMonitoredExchange);
	}
}
