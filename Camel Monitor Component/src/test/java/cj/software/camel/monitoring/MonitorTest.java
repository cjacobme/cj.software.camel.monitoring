package cj.software.camel.monitoring;

import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;

import cj.software.camel.monitoring.monitor.log4j2.LoggerMonitor;

public abstract class MonitorTest
		extends CamelTestSupport
{

	@Override
	protected JndiRegistry createRegistry() throws Exception
	{
		JndiRegistry lResult = super.createRegistry();
		lResult.bind("LoggerMonitor", new LoggerMonitor());
		return lResult;
	}

}
