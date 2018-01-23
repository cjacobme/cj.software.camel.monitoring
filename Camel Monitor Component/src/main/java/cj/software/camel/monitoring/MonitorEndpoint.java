package cj.software.camel.monitoring;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;
import org.apache.logging.log4j.Level;

import cj.software.camel.monitoring.monitor.Monitor;

/**
 * Represents a Monitor endpoint.
 */
@UriEndpoint(firstVersion = "0.0.1-SNAPSHOT",
		scheme = "moni",
		title = "Monitor",
		syntax = "moni:name",
		label = "custom")
public class MonitorEndpoint
		extends DefaultEndpoint
{

	@UriPath(name = "loggerName",
			description = "Name of the logger if the LoggerMonitor is used",
			defaultValue = "LoggerMonitor",
			javaType = "java.lang.String")
	private String loggerName = "LoggerMonitor";

	@UriPath(name = "monitorRef",
			description = "Reference to the monitor implementation",
			javaType = "java.lang.String")
	private String monitorRef;

	@UriPath(name = "logLevel",
			description = "Log-Level",
			defaultValue = "INFO",
			javaType = "org.apache.logging.log4jLevel")
	private Level logLevel = Level.INFO;

	private Monitor monitor;

	public MonitorEndpoint()
	{
	}

	public MonitorEndpoint(String uri, MonitorComponent component)
	{
		super(uri, component);
	}

	@SuppressWarnings("deprecation")
	public MonitorEndpoint(String endpointUri)
	{
		super(endpointUri);
	}

	@Override
	public Producer createProducer() throws Exception
	{
		return new MonitorProducer(this);
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception
	{
		throw new UnsupportedOperationException(
				"Consumer is not possible for " + this.getEndpointUri());
	}

	@Override
	public boolean isSingleton()
	{
		return true;
	}

	public Monitor getMonitor()
	{
		return this.monitor;
	}

	public String getLoggerName()
	{
		return this.loggerName;
	}

	public void setLoggerName(String pLoggerName)
	{
		this.loggerName = pLoggerName;
	}

	public String getMonitorRef()
	{
		return this.monitorRef;
	}

	public void setMonitorRef(String pMonitorRef)
	{
		this.monitorRef = pMonitorRef;
	}

	public void setMonitor(Monitor pMonitor)
	{
		this.monitor = pMonitor;
	}

	public Level getLogLevel()
	{
		return this.logLevel;
	}

	public void setLogLevel(Level pLogLevel)
	{
		this.logLevel = pLogLevel;
	}

	public void setLogLevel(String pLogLevel)
	{
		this.setLogLevel(Level.valueOf(pLogLevel));
	}
}
