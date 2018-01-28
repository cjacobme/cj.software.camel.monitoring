package cj.software.camel.monitoring.monitor.cassandra;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
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
		return lResult;
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
					.to("mock:finished")
				;
				//@formatter:on
			}
		};
	}

	@Test
	public void simple() throws InterruptedException
	{
		this.mockFinished.expectedMessageCount(1);
		this.directStart.sendBody("los jetzt");
		this.mockFinished.assertIsSatisfied(120000);
	}
}
