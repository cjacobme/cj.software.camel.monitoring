package cj.software.camel.monitoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cj.software.camel.monitoring.data.MonitoredExchange;
import cj.software.camel.monitoring.monitor.Monitor;

public class MockMonitor
		implements
		Monitor
{
	private static int counter = 0;

	private List<MonitorEndpoint> endpoints = new ArrayList<>();

	private List<MonitoredExchange> monitoredExchanges = new ArrayList<>();

	@Override
	public String startNewRunningContext(String pRunningContext)
	{
		String lResult = String.format("Mock-Monitor #%d - %s", counter, pRunningContext);
		counter++;
		return lResult;
	}

	@Override
	public String monitor(MonitorEndpoint pEndpoint, MonitoredExchange pMonitoredExchange)
	{
		this.endpoints.add(pEndpoint);
		this.monitoredExchanges.add(pMonitoredExchange);
		return null;
	}

	public static int getCounter()
	{
		return counter;
	}

	public List<MonitorEndpoint> getEndpoints()
	{
		return Collections.unmodifiableList(this.endpoints);
	}

	public List<MonitoredExchange> getMonitoredExchanges()
	{
		return Collections.unmodifiableList(this.monitoredExchanges);
	}

	public int getNumEndpoints()
	{
		return this.endpoints.size();
	}

	public int getNumMonitoredExchanges()
	{
		return this.monitoredExchanges.size();
	}

	public MonitorEndpoint getEndpointAt(int pIndex)
	{
		return this.endpoints.get(pIndex);
	}

	public MonitoredExchange getMonitoredExchangeAt(int pIndex)
	{
		return this.monitoredExchanges.get(pIndex);
	}

	public void clear()
	{
		this.endpoints.clear();
		this.monitoredExchanges.clear();
		counter = 0;
	}

}
