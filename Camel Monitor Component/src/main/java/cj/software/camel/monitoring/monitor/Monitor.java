package cj.software.camel.monitoring.monitor;

import cj.software.camel.monitoring.MonitorEndpoint;
import cj.software.camel.monitoring.data.MonitoredExchange;

/**
 * Interface that all monitors have to implement.
 */
public interface Monitor
{
	/**
	 * monitors a monitored Exchange
	 * 
	 * @param pEndpoint
	 *            the endpoint which holds configuration entries
	 * @param pMonitoredEntity
	 *            the Exchange to be monitored
	 * @return a unique id for the monitored exchange if the implementation has stored it somehow.
	 */
	public String monitor(MonitorEndpoint pEndpoint, MonitoredExchange pMonitoredEntity);
}
