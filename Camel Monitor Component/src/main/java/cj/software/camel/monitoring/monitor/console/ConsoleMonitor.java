package cj.software.camel.monitoring.monitor.console;

import cj.software.camel.monitoring.data.MonitoredExchange;
import cj.software.camel.monitoring.monitor.Monitor;

public class ConsoleMonitor
		implements
		Monitor
{

	@Override
	public String monitor(MonitoredExchange pMonitoredEntity)
	{
		synchronized (System.out)
		{
			this.print("camel context name", pMonitoredEntity.getCamelContextName());
			this.print("endpoint URI", pMonitoredEntity.getEndpointURI());
			this.print("initial Route ID", pMonitoredEntity.getInitialRouteId());
			this.print("current Route ID", pMonitoredEntity.getCurrentRouteId());
			this.print("created at", pMonitoredEntity.getExchangeCreated());
			this.print("monitored", pMonitoredEntity.getMonitored());
			System.out.println();
			System.out.println();
			System.out.println();
		}
		return "System.out";
	}

	private void print(String pResource, Object pEntry)
	{
		System.out.println(String.format("%-20s = %s", pResource, pEntry));
	}
}
