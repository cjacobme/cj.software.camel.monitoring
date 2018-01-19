package cj.software.camel.monitoring.monitor.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cj.software.camel.monitoring.MonitorEndpoint;
import cj.software.camel.monitoring.data.MonitoredExchange;
import cj.software.camel.monitoring.data.MonitoredMessage;
import cj.software.camel.monitoring.monitor.Monitor;

public class LoggerMonitor
		implements
		Monitor
{

	@Override
	public String monitor(MonitorEndpoint pEndpoint, MonitoredExchange pMonitoredEntity)
	{
		// TODO the log level must somehow be configured. Possible solutions:
		// TODO 1. possible solution: as a parameter in the route
		// TODO 2. possible solution: when the JNDI-Context is set up
		String lLoggerName = pEndpoint.getLoggerName();
		Logger lLogger = LogManager.getFormatterLogger(lLoggerName);

		lLogger.info(
				"##################                   %s                   ##################",
				pMonitoredEntity.getExchangeId());

		lLogger.info("Camel Context Name  = %s", pMonitoredEntity.getCamelContextName());
		lLogger.info("Endpoint-URI        = %s", pMonitoredEntity.getEndpointURI());
		lLogger.info("initial Route-ID    = %s", pMonitoredEntity.getInitialRouteId());
		lLogger.info("Current Route-ID    = %s", pMonitoredEntity.getCurrentRouteId());
		lLogger.info("Exchange created at = %s", pMonitoredEntity.getExchangeCreated());
		lLogger.info("Monitored at        = %s", pMonitoredEntity.getMonitored());
		lLogger.info("");

		lLogger.info("=========        IN MESSAGE        =========");
		this.logMessage(pMonitoredEntity.getInMessage(), lLogger);
		lLogger.info("");

		lLogger.info("=========       OUT MESSAGE        =========");
		this.logMessage(pMonitoredEntity.getOutMessage(), lLogger);
		lLogger.info("");

		return "log4j2 (" + lLoggerName + ")";
	}

	private void logMessage(MonitoredMessage pMessage, Logger pLogger)
	{
		if (pMessage != null)
		{
			pLogger.info("   Message-Id            = %s", pMessage.getMessageId());

			Class<?> lBodyClass = pMessage.getBodyClass();
			if (lBodyClass != null)
			{
				pLogger.info("   Body's class name     = %s", lBodyClass.getName());
			}
			else
			{
				pLogger.info("   Body's class name does not exist");
			}

			Object lBody = pMessage.getBody();
			if (lBody != null)
			{
				pLogger.info("   Body                  = %s", lBody.toString());
			}
			else
			{
				pLogger.info("   Body does not exist");
			}
		}
		else
		{
			pLogger.info("   this message does not exist");
		}
	}
}
