package cj.software.camel.monitoring;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

import cj.software.camel.monitoring.monitor.Monitor;
import cj.software.camel.monitoring.monitor.log4j2.LoggerMonitor;

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
	@UriPath
	@Metadata(required = "true")
	private String name;
	@UriParam(defaultValue = "10")
	private int option = 10;

	@UriPath(name = "loggerName",
			description = "Name of the logger if the LoggerMonitor is used",
			defaultValue = "LoggerMonitor",
			javaType = "java.lang.String")
	private String loggerName = "LoggerMonitor";

	// private Monitor monitor = new ConsoleMonitor(); // TODO read from registry
	private Monitor monitor = new LoggerMonitor(); // TODO read from registry

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

	/**
	 * Some description of this option, and what it does
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	/**
	 * Some description of this option, and what it does
	 */
	public void setOption(int option)
	{
		this.option = option;
	}

	public int getOption()
	{
		return this.option;
	}

	public String getLoggerName()
	{
		return this.loggerName;
	}

	public void setLoggerName(String pLoggerName)
	{
		this.loggerName = pLoggerName;
	}
}
