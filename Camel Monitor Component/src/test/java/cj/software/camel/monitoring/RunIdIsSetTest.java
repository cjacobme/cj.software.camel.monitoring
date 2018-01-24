package cj.software.camel.monitoring;

import java.util.Hashtable;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.spi.Registry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import cj.software.camel.monitoring.data.MonitoredExchange;
import cj.software.camel.monitoring.monitor.Monitor;

public class RunIdIsSetTest
		extends CamelTestSupport
{

	private long clock = System.currentTimeMillis();

	@Produce(uri = "direct:start")
	private ProducerTemplate producerTemplate;

	@EndpointInject(uri = "mock:monitorStarted")
	private MockEndpoint mockStarted;

	@Override
	protected JndiRegistry createRegistry() throws NamingException
	{
		Hashtable<Object, Object> lTableWithFactory = new Hashtable<>();
		lTableWithFactory.put(
				"java.naming.factory.initial",
				"org.apache.camel.util.jndi.CamelInitialContextFactory");
		JndiRegistry lResult = new JndiRegistry(new InitialContext(lTableWithFactory));
		lResult.bind("MonitorMock", new MonitorMock());
		return lResult;
	}

	private class MonitorMock
			implements
			Monitor
	{
		private boolean called = false;

		@Override
		public String startNewRunningContext(String pRunningContext)
		{
			String lResult = String.format("%d", RunIdIsSetTest.this.clock);
			this.called = true;
			return lResult;
		}

		boolean isCalled()
		{
			return this.called;
		}

		@Override
		public String monitor(MonitorEndpoint pEndpoint, MonitoredExchange pMonitoredExchange)
		{
			return null;
		}
	}

	@Override
	protected RouteBuilder createRouteBuilder()
	{
		RouteBuilder lResult = new RouteBuilder()
		{
			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from ("direct:start")
					.routeId("checkRunIdIsSet")
					.to("moni://start?runningContext=checkRunIdIsSet")
					.to("mock:monitorStarted")
				;
				//@formatter:on
			}
		};
		return lResult;
	}

	@Test
	public void idIsSet() throws Exception
	{
		this.mockStarted.expectedMessageCount(1);
		this.producerTemplate.sendBody("los jetzt");
		this.mockStarted.assertIsSatisfied();

		CamelContext lCtx = super.context();
		Registry lRegistry = lCtx.getRegistry();
		MonitorMock lMonitorMock = lRegistry.lookupByNameAndType("MonitorMock", MonitorMock.class);
		boolean lIsCalled = lMonitorMock.isCalled();
		Assertions.assertThat(lIsCalled).as("is called").isTrue();

		List<Exchange> lExchanges = this.mockStarted.getExchanges();
		Assertions.assertThat(lExchanges).as("list of Exchanges").hasSize(1);
		Exchange lExchange = lExchanges.get(0);
		String lRunId = (String) lExchange.getProperty(MonitorComponent.MONITOR_RUN_ID);
		Assertions.assertThat(lRunId).isEqualTo(String.valueOf(this.clock));
	}
}
