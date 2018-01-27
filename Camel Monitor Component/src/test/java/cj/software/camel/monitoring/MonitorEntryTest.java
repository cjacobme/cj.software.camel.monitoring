package cj.software.camel.monitoring;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import cj.software.camel.monitoring.data.MonitoredExchange;
import cj.software.camel.monitoring.data.MonitoredMessage;
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

		List<Exchange> lExchanges = this.mockEndpoint.getExchanges();
		Assertions.assertThat(lExchanges).as("list of Exchanges").hasSize(1);
		Exchange lExchange = lExchanges.get(0);
		String lExchangeId = lExchange.getExchangeId();
		String lCamelContextName = lExchange.getContext().getName();

		List<MonitoredExchange> lMonitoredExchanges = this.mockMonitor.getMonitoredExchanges();
		Assertions.assertThat(lMonitoredExchanges).as("list of monitored exchanges").hasSize(1);
		MonitoredExchange lMonitored = lMonitoredExchanges.get(0);

		Assertions.assertThat(lMonitored.getCamelContextName()).as("camel context name").isEqualTo(
				lCamelContextName);
		Assertions.assertThat(lMonitored.getCurrentRouteId()).as("current route-id").isEqualTo(
				"test-entry");
		Assertions.assertThat(lMonitored.getEndpointURI()).as("endpoint uri").isEqualTo(
				"direct://start");
		Assertions.assertThat(lMonitored.getExchangeCreated()).as("Exchange created").isNotNull();
		Assertions.assertThat(lMonitored.getExchangeId()).as("exchange-id").isEqualTo(lExchangeId);
		Assertions.assertThat(lMonitored.getInitialRouteId()).as("initial route-id").isEqualTo(
				"test-entry");
		Assertions.assertThat(lMonitored.getMonitored()).as("monitored timestamp").isNotNull();
		Assertions.assertThat(lMonitored.getRunningContext()).as("running context").isEqualTo(
				"normal entry");
		Assertions.assertThat(lMonitored.getRunId()).as("run-id").isEqualTo(
				"Mock-Monitor #0 - normal entry");

		Map<String, Object> lProperties = lMonitored.getProperties();
		this.assertExchangeProperties(lProperties);

		MonitoredMessage lInMessage = lMonitored.getInMessage();
		this.assertInMessage(lInMessage);
		MonitoredMessage lOutMessage = lMonitored.getOutMessage();
		this.assertOutMessage(lOutMessage);
	}

	private void assertExchangeProperties(Map<String, Object> pProperties)
	{
		Assertions.assertThat(pProperties).as("properties").isNotNull().isNotEmpty();
		Set<String> lKeys = pProperties.keySet();
		Assertions.assertThat(lKeys).as("Keys").containsExactlyInAnyOrder(
				"CamelCreatedTimestamp",
				"CamelExternalRedelivered",
				"CamelMessageHistory",
				"CamelMonitor",
				"CamelMonitorRunId",
				"CamelMonitorRunningContext",
				"CamelToEndpoint");
		Assertions
				.assertThat(pProperties.get("CamelCreatedTimestamp"))
				.as("created timestamp")
				.isInstanceOf(Date.class);
		Assertions
				.assertThat(pProperties.get("CamelExternalRedelivered"))
				.as("externally redelivered")
				.isEqualTo(Boolean.FALSE);
		Assertions
				.assertThat(pProperties.get("CamelMessageHistory"))
				.as("Message history")
				.isInstanceOf(LinkedList.class);
		Assertions.assertThat(pProperties.get("CamelMonitor")).isInstanceOf(Monitor.class);
		Assertions.assertThat(pProperties.get("CamelMonitorRunId")).as("Run-ID").isEqualTo(
				"Mock-Monitor #0 - normal entry");
		Assertions
				.assertThat(pProperties.get("CamelMonitorRunningContext"))
				.as("Running Context")
				.isEqualTo("normal entry");
		Assertions.assertThat(pProperties.get("CamelToEndpoint")).as("to-endpoint").isEqualTo(
				"moni://entry");
	}

	private void assertInMessage(MonitoredMessage pMessage)
	{
		Assertions.assertThat(pMessage).as("IN message").isNotNull();
		Assertions.assertThat(pMessage.getMessageId()).as("message id").isNotEmpty();
		Assertions.assertThat(pMessage.getBodyClass()).as("body class").isEqualTo(String.class);
		Assertions.assertThat(pMessage.getBody()).as("body").isEqualTo("start now!");

		Map<String, Object> lHeaders = pMessage.getHeaders();
		Assertions.assertThat(lHeaders).as("IN headers").isNotNull();
		Set<String> lKeys = lHeaders.keySet();
		Assertions.assertThat(lKeys).as("IN header keys").containsExactlyInAnyOrder("breadcrumbId");
		String lBreadCrumbId = (String) lHeaders.get("breadcrumbId");
		Assertions.assertThat(lBreadCrumbId).as("breadcrumbid").isNotEmpty();
	}

	private void assertOutMessage(MonitoredMessage pMessage)
	{
		Assertions.assertThat(pMessage).as("OUT message").isNotNull();
		Assertions.assertThat(pMessage.getMessageId()).as("message id").isNotEmpty();
		Assertions.assertThat(pMessage.getBodyClass()).as("body class").isNull();
		Assertions.assertThat(pMessage.getBody()).as("body").isNull();
		Map<String, Object> lHeaders = pMessage.getHeaders();

		Assertions.assertThat(lHeaders).as("OUT headers").isNotNull();
		Set<String> lKeys = lHeaders.keySet();
		Assertions.assertThat(lKeys).as("OUT header keys").isEmpty();
	}
}
