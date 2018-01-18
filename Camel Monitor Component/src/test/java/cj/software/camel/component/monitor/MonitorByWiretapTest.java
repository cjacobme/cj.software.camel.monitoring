package cj.software.camel.component.monitor;

import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MonitorByWiretapTest
		extends CamelTestSupport
{

	@Test
	public void testMonitor() throws Exception
	{
		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedMinimumMessageCount(1);
		this.template.sendBody("direct:start", "los jetzt");

		assertMockEndpointsSatisfied(4, TimeUnit.MINUTES);
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
						.routeId("wiretap-start")
						.wireTap("moni://bar?option=4711")
						.log("${body}")
						.to("direct:step2");
				from("direct:step2")
						.routeId("wiretap-step #2")
						.log("${body}")
						.wireTap("moni://?name=MyNameIsNobody")
						.setBody(constant("hello world"))
						.log("${body}")
						.wireTap("moni://xyz")
						.process(new Processor()
						{

							@Override
							public void process(Exchange pExchange) throws Exception
							{
								Thread.sleep(10000);
							}
						})
						.to("mock:result");
			}
		};
	}

}
