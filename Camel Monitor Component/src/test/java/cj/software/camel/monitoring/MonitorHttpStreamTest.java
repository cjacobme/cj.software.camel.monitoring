package cj.software.camel.monitoring;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MonitorHttpStreamTest
		extends CamelTestSupport
{
	@EndpointInject(uri = "mock:downloaded")
	private MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	private ProducerTemplate downloader;

	@Override
	public RouteBuilder createRouteBuilder()
	{
		return new RouteBuilder()
		{
			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from ("direct:start")
					.routeId("download-nasa")
					.setBody(simple("{null}"))
					.setHeader(Exchange.HTTP_METHOD, constant("GET"))
					.to("https://www.nasa.gov")
					.wireTap("moni:?loggerName=nasa")
					.log("before sleep")
					.process(new Processor()
					{
						
						@Override
						public void process(Exchange pExchange) throws Exception
						{
							Thread.sleep(2000);
						}
					})
					.log("after sleep")
					.log("${body}")
					.to("mock:downloaded")
				;
				//@formatter:on
			}
		};
	}

	@Test
	public void downloadStream()
	{
		this.mockEndpoint.expectedMessageCount(1);
		this.downloader.sendBody("los gezz");
	}

}
