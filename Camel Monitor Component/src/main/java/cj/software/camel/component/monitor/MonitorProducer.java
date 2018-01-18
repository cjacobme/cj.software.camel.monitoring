package cj.software.camel.component.monitor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Monitor producer.
 */
public class MonitorProducer
		extends DefaultProducer
{
	private static final Logger LOG = LoggerFactory.getLogger(MonitorProducer.class);
	private MonitorEndpoint endpoint;

	public MonitorProducer(MonitorEndpoint endpoint)
	{
		super(endpoint);
		this.endpoint = endpoint;
	}

	@Override
	public void process(Exchange pExchange) throws Exception
	{
		synchronized (System.out)
		{
			System.out.println("Endpoint Key                = " + this.endpoint.getEndpointKey());
			System.out.println("Endpoint URI                = " + this.endpoint.getEndpointUri());
			System.out.println("Endpoint ID                 = " + this.endpoint.getId());
			System.out.println("Endpoint Name               = " + this.endpoint.getName());
			System.out.println("Endpoint Option             = " + this.endpoint.getOption());
			System.out.println("Camel-Context name          = " + pExchange.getContext().getName());
			System.out.println(
					"initiated route-id          = "
							+ pExchange.getFromRouteId()
							+ " (from-Endpoint="
							+ pExchange.getFromEndpoint()
							+ ")");
			System.out.println(
					"Route-ID                    = "
							+ pExchange.getUnitOfWork().getRouteContext().getRoute().getId());
			System.out.println("created timestamp           = " + pExchange.getCreated());
			System.out.println("Exchange-ID                 = " + pExchange.getExchangeId());
			this.printMessage("in", pExchange.getIn());
			this.printMessage("out", pExchange.getOut());
			System.out.println();

		}
	}

	private void printMessage(String pRole, Message pMessage)
	{
		if (pMessage != null)
		{
			System.out.println(String.format("   %3s Properties ============", pRole));
			System.out.println(
					String.format(
							"   %-3s %-20s = %s",
							pRole,
							"Message-ID",
							pMessage.getMessageId()));
			Object lBody = pMessage.getBody();
			if (lBody != null)
			{
				System.out.println(
						String.format(
								"   %-3s %-20s = %s",
								pRole,
								"Body Classname",
								lBody.getClass().getName()));
				System.out.println(
						String.format(
								"   %-3s %-20s = %s",
								pRole,
								"Body toString",
								lBody.toString()));
			}
			else
			{
				System.out.println(String.format("   %3s has no body", pRole));
			}
		}
		else
		{
			System.out.println(String.format("   %3s no message", pRole));
		}
	}
}
