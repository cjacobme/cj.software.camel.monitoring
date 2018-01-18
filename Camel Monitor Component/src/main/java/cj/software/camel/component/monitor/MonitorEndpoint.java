package cj.software.camel.component.monitor;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

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
}
