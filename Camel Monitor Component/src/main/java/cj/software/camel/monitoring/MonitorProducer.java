package cj.software.camel.monitoring;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import cj.software.camel.monitoring.data.MonitoredExchange;

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

	@Override
	public void process(Exchange pExchange) throws Exception
	{
		MonitoredExchange lMonitoredExchange = Converter.toMonitoredExchange(pExchange);
		this.endpoint.getMonitor().monitor(this.endpoint, lMonitoredExchange);
	}
}
