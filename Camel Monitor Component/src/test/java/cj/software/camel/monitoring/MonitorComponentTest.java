package cj.software.camel.monitoring;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

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
						.to("moni://bar?option=4711&loggerName=start")
						.log("${body}")
						.to("mock:result")
						.to("direct:step2");
				from("direct:step2")
						.routeId("step #2")
						.log("${body}")
						.to("moni://?name=MyNameIsNobody")
						.setBody(constant("hello world"))
						.log("${body}")
						.to("moni://xyz");
			}
		};
	}
}
