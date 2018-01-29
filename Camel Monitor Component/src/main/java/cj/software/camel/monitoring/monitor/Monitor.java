package cj.software.camel.monitoring.monitor;

import org.apache.camel.Exchange;

import cj.software.camel.monitoring.MonitorEndpoint;
import cj.software.camel.monitoring.data.MonitoredExchange;

/**
 * Interface that all monitors have to implement.
 */
public interface Monitor
{
	/**
	 * starts the monitoring for a new Exchange.
	 * 
	 * @param pRunningContext
	 *            the running context in question
	 * @return a unique id for the new run. This id will be input parameter for the invocations of
	 *         {@link #startNewExchange(Exchange)} and {@link #finishExchange(Exchange)}
	 */
	public String startNewExchange(Exchange pExchange);

	/**
	 * monitors a monitored Exchange
	 * 
	 * @param pEndpoint
	 *            the endpoint which holds configuration entries
	 * @param pMonitoredExchange
	 *            the Exchange to be monitored. The Monitoring-Run-ID can be found at
	 *            {@link MonitoredExchange#getRunId()}.
	 * @return a unique id for the monitored exchange if the implementation has stored it somehow.
	 */
	public String monitor(MonitorEndpoint pEndpoint, MonitoredExchange pMonitoredExchange);

	/**
	 * informs the monitor that this exchange is finished now. Because of possible parallel
	 * processing, more calls to {@link #monitor(String, MonitorEndpoint, MonitoredExchange)} can
	 * happen after that, so don't close resources.
	 * 
	 * @param pMonitoringId
	 *            The Id that was returned by {@link #startNewExchange(Exchange)}
	 * @param pExchange
	 *            The Camel Exchange that is finished now.
	 */
	public void finishExchange(String pMonitoringId, Exchange pExchange);
}
