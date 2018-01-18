package cj.software.camel.monitoring.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cj.software.camel.monitoring.data.MonitoredExchange;
import cj.software.camel.monitoring.monitor.Monitor;

public class LoggerMonitor
		implements
		Monitor
{
	// TODO the log level must somehow be configured. Possible solutions:
	// TODO 1. possible solution: as a parameter in the route
	// TODO 2. possible solution: when the JNDI-Context is set up
	private Logger logger = LogManager.getFormatterLogger();

	@Override
	public String monitor(MonitoredExchange pMonitoredEntity)
	{
		this.logger.info("Camel Context Name  = %s", pMonitoredEntity.getCamelContextName());
		this.logger.info("Endpoint-URI        = %s", pMonitoredEntity.getEndpointURI());
		this.logger.info("initial Route-ID    = %s", pMonitoredEntity.getInitialRouteId());
		this.logger.info("Current Route-ID    = %s", pMonitoredEntity.getCurrentRouteId());
		this.logger.info("Exchange created at = %s", pMonitoredEntity.getExchangeCreated());
		this.logger.info("Monitored at        = %s", pMonitoredEntity.getMonitored());
		return "log4j2";
	}

}
