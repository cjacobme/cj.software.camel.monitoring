package cj.software.camel.monitoring;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.camel.CamelContext;
import org.apache.camel.FailedToCreateRouteException;
import org.apache.camel.ResolveEndpointFailedException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.junit.Assert;
import org.junit.Test;

import cj.software.camel.monitoring.monitor.log4j2.LoggerMonitor;

public class MonitorStartTest
{
	protected JndiRegistry createRegistry() throws Exception
	{
		JndiRegistry lResult = new JndiRegistry(this.createJndiContext());
		lResult.bind("LoggerMonitor", new LoggerMonitor());
		return lResult;
	}

	protected Context createJndiContext() throws Exception
	{
		Properties properties = new Properties();

		// jndi.properties is optional
		InputStream in = getClass().getClassLoader().getResourceAsStream("jndi.properties");
		if (in != null)
		{
			properties.load(in);
		}
		else
		{
			properties.put(
					"java.naming.factory.initial",
					"org.apache.camel.util.jndi.CamelInitialContextFactory");
		}
		return new InitialContext(new Hashtable<Object, Object>(properties));
	}

	@Test
	public void runningContextSet() throws Exception
	{
		JndiRegistry lRegistry = this.createRegistry();
		CamelContext lCtx = new DefaultCamelContext(lRegistry);
		lCtx.addRoutes(new RouteBuilder()
		{

			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from ("direct:start")
					.routeId("test-running-context")
					.to("moni://start?loggerName=xyz&runningContext=MeineSchnittstelle")
				;
				//@formatter:on
			}
		});
		lCtx.start();
		try
		{
			Thread.sleep(1000l);
		}
		finally
		{
			lCtx.stop();
		}
	}

	@Test
	public void nullRunningContextFails() throws Exception
	{
		JndiRegistry lRegistry = this.createRegistry();
		CamelContext lCtx = new DefaultCamelContext(lRegistry);
		lCtx.addRoutes(new RouteBuilder()
		{

			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from ("direct:start")
					.routeId("test-null-running-context")
					.to("moni://start?loggerName=xyz")
				;
				//@formatter:on
			}
		});
		try
		{
			lCtx.start();
			try
			{
				Thread.sleep(1000l);
			}
			finally
			{
				lCtx.stop();
			}
			Assert.fail("expected exception not thrown");
		}
		catch (FailedToCreateRouteException pFCRE)
		{
			ResolveEndpointFailedException lREFE = (ResolveEndpointFailedException) pFCRE
					.getCause();
			IllegalArgumentException lIAE = (IllegalArgumentException) lREFE.getCause();
			String lMessage = lIAE.getMessage();
			assertThat(lMessage).as("Message in Exception").isEqualTo(
					"running context required for moni://start");
		}
	}

	@Test
	public void emptyRunningContextFails() throws Exception
	{
		JndiRegistry lRegistry = this.createRegistry();
		CamelContext lCtx = new DefaultCamelContext(lRegistry);
		lCtx.addRoutes(new RouteBuilder()
		{

			@Override
			public void configure() throws Exception
			{
				//@formatter:off
				from ("direct:start")
					.routeId("test-empty-running-context")
					.to("moni://start?runningContext=&loggerName=xyz")
				;
				//@formatter:on
			}
		});
		try
		{
			lCtx.start();
			try
			{
				Thread.sleep(1000l);
			}
			finally
			{
				lCtx.stop();
			}
			Assert.fail("expected exception not thrown");
		}
		catch (FailedToCreateRouteException pFCRE)
		{
			ResolveEndpointFailedException lREFE = (ResolveEndpointFailedException) pFCRE
					.getCause();
			IllegalArgumentException lIAE = (IllegalArgumentException) lREFE.getCause();
			String lMessage = lIAE.getMessage();
			assertThat(lMessage).as("Message in Exception").isEqualTo(
					"empty running context string");
		}
	}
}
