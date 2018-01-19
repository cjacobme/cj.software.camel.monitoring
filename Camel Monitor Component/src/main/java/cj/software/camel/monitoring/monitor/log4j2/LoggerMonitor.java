package cj.software.camel.monitoring.monitor.log4j2;

import org.apache.logging.log4j.Level;
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
		Level lLevel = pEndpoint.getLogLevel();

		// TODO the log level must somehow be configured. Possible solutions:
		// TODO 1. possible solution: as a parameter in the route
		// TODO 2. possible solution: when the JNDI-Context is set up
		String lLoggerName = pEndpoint.getLoggerName();
		Logger lLogger = LogManager.getFormatterLogger(lLoggerName);

		synchronized (lLogger)
		{
			lLogger.log(
					lLevel,
					"##################                   %s                   ##################",
					pMonitoredEntity.getExchangeId());

			lLogger.log(lLevel, "Camel Context Name  = %s", pMonitoredEntity.getCamelContextName());
			lLogger.log(lLevel, "Endpoint-URI        = %s", pMonitoredEntity.getEndpointURI());
			lLogger.log(lLevel, "initial Route-ID    = %s", pMonitoredEntity.getInitialRouteId());
			lLogger.log(lLevel, "Current Route-ID    = %s", pMonitoredEntity.getCurrentRouteId());
			lLogger.log(lLevel, "Exchange created at = %s", pMonitoredEntity.getExchangeCreated());
			lLogger.log(lLevel, "Monitored at        = %s", pMonitoredEntity.getMonitored());
			lLogger.log(lLevel, "");

			lLogger.log(lLevel, "=========        IN MESSAGE        =========");
			this.logMessage(pMonitoredEntity.getInMessage(), lLogger, lLevel);
			lLogger.log(lLevel, "");

			lLogger.log(lLevel, "=========       OUT MESSAGE        =========");
			this.logMessage(pMonitoredEntity.getOutMessage(), lLogger, lLevel);
			lLogger.log(lLevel, "");
		}

		return "log4j2 (" + lLoggerName + ")";
	}

	private void logMessage(MonitoredMessage pMessage, Logger pLogger, Level pLevel)
	{
		if (pMessage != null)
		{
			pLogger.log(pLevel, "   Message-Id            = %s", pMessage.getMessageId());

			Class<?> lBodyClass = pMessage.getBodyClass();
			if (lBodyClass != null)
			{
				pLogger.log(pLevel, "   Body's class name     = %s", lBodyClass.getName());
			}
			else
			{
				pLogger.log(pLevel, "   Body's class name does not exist");
			}

			Object lBody = pMessage.getBody();
			if (lBody != null)
			{
				pLogger.log(pLevel, "   Body                  = %s", lBody.toString());
			}
			else
			{
				pLogger.log(pLevel, "   Body does not exist");
			}
		}
		else
		{
			pLogger.log(pLevel, "   this message does not exist");
		}
	}
}
