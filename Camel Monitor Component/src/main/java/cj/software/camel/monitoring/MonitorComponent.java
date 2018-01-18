package cj.software.camel.monitoring;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link MonitorEndpoint}.
 */
public class MonitorComponent
		extends DefaultComponent
{

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters)
			throws Exception
	{
		Endpoint endpoint = new MonitorEndpoint(uri, this);
		setProperties(endpoint, parameters);
		return endpoint;
	}
}
