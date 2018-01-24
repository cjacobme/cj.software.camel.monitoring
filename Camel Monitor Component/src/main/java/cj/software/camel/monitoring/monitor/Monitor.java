package cj.software.camel.monitoring.monitor;

import cj.software.camel.monitoring.MonitorEndpoint;
import cj.software.camel.monitoring.data.MonitoredExchange;

/**
 * Interface that all monitors have to implement.
 */
public interface Monitor
{
	/**
	 * starts the monitoring of a new run for a new running context.
	 * 
	 * @param pRunningContext
	 *            the running context in question
	 * @return a unique id for the new run.
	 */
	public String startNewRunningContext(String pRunningContext);

	/**
	 * monitors a monitored Exchange
	 * 
	 * @param pEndpoint
	 *            the endpoint which holds configuration entries
	 * @param pMonitoredExchange
	 *            the Exchange to be monitored
	 * @return a unique id for the monitored exchange if the implementation has stored it somehow.
	 */
	public String monitor(MonitorEndpoint pEndpoint, MonitoredExchange pMonitoredExchange);
}
