package cj.software.camel.monitoring;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Ignore;
import org.junit.Test;

import cj.software.camel.monitoring.monitor.Monitor;
import cj.software.camel.monitoring.monitor.log4j2.LoggerMonitor;

@Ignore
public class MonitorComponentTest
		extends MonitorTest
{

	@Test
	public void testMonitor() throws Exception
	{
		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedMinimumMessageCount(1);
		this.template.sendBody("direct:start", "los jetzt");

		assertMockEndpointsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception
	{
		return new RouteBuilder()
		{
			@Override
			public void configure()
			{
				from("direct:start")
						.routeId("try")
						.to("moni://bar?loggerName=start")
						.log("${body}")
						.to("mock:result")
						.to("direct:step2");
				from("direct:step2")
						.routeId("step #2")
						.log("${body}")
						.to("moni://bar")
						.setBody(constant("hello world"))
						.log("${body}")
						.to("moni://xyz");
			}
		};
	}

	@Override
	protected Monitor getMonitor()
	{
		return new LoggerMonitor();
	}
}
