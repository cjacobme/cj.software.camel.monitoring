package cj.software.camel.monitoring.monitor.cassandra;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.apache.camel.support.LifecycleStrategySupport;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SimpleInsertTest
		extends CamelTestSupport
{
	@Produce(uri = "direct:start")
	private ProducerTemplate directStart;

	@EndpointInject(uri = "mock:finished")
	private MockEndpoint mockFinished;

	@Override
	protected Context createJndiContext() throws Exception
	{
		InitialContext lResult = (InitialContext) super.createJndiContext();
		lResult.bind("Cassandra-Monitor", new CassandraMonitor("localhost", "camel_monitoring"));
		return lResult;
	}

	@Override
	protected CamelContext createCamelContext() throws Exception
	{
		DefaultCamelContext lResult = (DefaultCamelContext) super.createCamelContext();
		lResult.setName("Simple-Insert-Test");
		lResult.addLifecycleStrategy(new LifecycleStrategySupport()
		{
			@Override
			public void onContextStop(CamelContext pContext)
			{
				tryToStop(pContext);
			}
		});
		return lResult;
	}

	private void tryToStop(CamelContext pContext)
	{
		// TODO how can we do this better?
		Registry lRegistry = pContext.getRegistry();
		CassandraMonitor lMonitor = lRegistry.lookupByNameAndType(
				"Cassandra-Monitor",
				CassandraMonitor.class);
		if (lMonitor != null)
		{
			try
			{
				lMonitor.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	protected RouteBuilder createRouteBuilder()
	{
		return new RouteBuilder()
		{

			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from ("direct:start")
					.routeId("moni-cassandra")
					.to("moni://start")
					.to("moni:entry?loggerName=hugo")
					.process(new ToUpper())
					.to("moni:entry?loggerName=karl&logLevel=WARN")
					.to("moni:finished")
					.to("mock:finished")
				;
				//@formatter:on
			}
		};
	}

	private class ToUpper
			implements
			Processor
	{

		@Override
		public void process(Exchange pExchange) throws Exception
		{
			Message lIn = pExchange.getIn();
			String lBody = lIn.getBody(String.class);
			lBody = lBody.toUpperCase();
			lIn.setBody(lBody);
		}

	}

	@Test
	public void simple() throws InterruptedException
	{
		this.mockFinished.expectedMessageCount(1);
		this.directStart.sendBody("los jetzt");
		this.mockFinished.assertIsSatisfied(120000);
	}
}
