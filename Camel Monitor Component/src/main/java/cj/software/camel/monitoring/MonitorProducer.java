package cj.software.camel.monitoring;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

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
		System.out.println("endpoint-URI is \"" + lEndpointUri + "\"");
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
		return lResult;
	}

	@Override
	public void process(Exchange pExchange) throws Exception
	{
		// TODO
		// String lEndpointUri = this.getInitialUriPart();
		// switch (lEndpointUri)
		// {
		// case "moni://start":
		// this.start(pExchange);
		// break;
		// case "moni://entry":
		// MonitoredExchange lMonitoredExchange = Converter.toMonitoredExchange(pExchange);
		// this.endpoint.getMonitor().monitor(this.endpoint, lMonitoredExchange);
		// break;
		// case "moni://error":
		// break;
		// case "moni://finished":
		// break;
		// default:
		// throw new UnsupportedOperationException("unsupported uri: " + lEndpointUri);
		// }

		pExchange.setOut(pExchange.getIn());
	}

	private void start(Exchange pExchange)
	{
		String lRunningContext = this.endpoint.getRunningContext();
		if (lRunningContext == null)
		{
			throw new IllegalArgumentException("running context required for moni://start");
		}
		lRunningContext = lRunningContext.trim();
		if (lRunningContext.isEmpty())
		{
			throw new IllegalArgumentException("empty running context string");
		}
	}
}
