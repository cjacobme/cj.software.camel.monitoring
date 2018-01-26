package cj.software.camel.monitoring;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import cj.software.camel.monitoring.data.MonitoredExchange;
import cj.software.camel.monitoring.monitor.Monitor;

public class MonitorEntryTest
		extends MonitorTest
{
	private MockMonitor mockMonitor = new MockMonitor();

	@Produce(uri = "direct:start")
	private ProducerTemplate producerTemplate;

	@EndpointInject(uri = "mock:monitored")
	private MockEndpoint mockEndpoint;

	@Override
	protected Monitor getMonitor()
	{
		return this.mockMonitor;
	}

	@Override
	public RouteBuilder createRouteBuilder()
	{
		RouteBuilder lResult = new RouteBuilder()
		{

			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from("direct:start")
					.routeId("test-entry")
					.log("body is ${body}")
					.to("moni://start?runningContext=normal entry")
					.to("moni://entry")
					.to("mock:monitored")
				;
				//@formatter:on

			}
		};
		return lResult;
	}

	@Test
	public void entryFilled() throws InterruptedException
	{
		this.mockEndpoint.expectedMessageCount(1);
		this.producerTemplate.sendBody("start now!");
		this.mockEndpoint.assertIsSatisfied();

		List<MonitoredExchange> lMonitoredExchanges = this.mockMonitor.getMonitoredExchanges();
		Assertions.assertThat(lMonitoredExchanges).as("list of monitored exchanges").hasSize(1);
		MonitoredExchange lMonitored = lMonitoredExchanges.get(0);

		Assertions.assertThat(lMonitored.getCamelContextName()).as("camel context name").matches(
				"camel.*");
		Assertions.assertThat(lMonitored.getCurrentRouteId()).as("current route-id").isEqualTo(
				"test-entry");
		Assertions.assertThat(lMonitored.getEndpointURI()).as("endpoint uri").isEqualTo(
				"direct://start");
		Assertions.assertThat(lMonitored.getExchangeCreated()).as("Exchange created").isNotNull();
		Assertions.assertThat(lMonitored.getInitialRouteId()).as("initial route-id").isEqualTo(
				"test-entry");
		Assertions.assertThat(lMonitored.getMonitored()).as("monitored timestamp").isNotNull();
		Assertions.assertThat(lMonitored.getRunningContext()).as("running context").isEqualTo(
				"normal entry");
	}
}
