package cj.software.camel.monitoring;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

public class MonitorTimerTest
		extends MonitorTest
{
	@EndpointInject(uri = "mock:timer-started")
	private MockEndpoint timerStarted;

	@Override
	public RouteBuilder createRouteBuilder()
	{
		RouteBuilder lResult = new RouteBuilder()
		{

			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from ("timer:moni-test?delay=1000&repeatCount=15&period=1000")
					.routeId("my-timer")
					.log("next cycle started ${header.CamelTimerCounter} in exchange ${exchangeId}")
					.setBody(header(Exchange.TIMER_COUNTER))
					.wireTap("moni://?loggerName=my-timer-log")
					.to("mock:timer-started")
				;
				//@formatter:on
			}
		};
		return lResult;
	}

	@Test
	public void timedRoute() throws InterruptedException
	{
		this.timerStarted.expectedMessageCount(5);
		this.timerStarted.assertIsSatisfied(180000);
	}
}
