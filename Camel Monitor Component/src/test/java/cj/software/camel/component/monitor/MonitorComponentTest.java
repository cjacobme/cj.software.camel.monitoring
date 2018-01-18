package cj.software.camel.component.monitor;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MonitorComponentTest
		extends CamelTestSupport
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
						.to("moni://bar?option=4711")
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