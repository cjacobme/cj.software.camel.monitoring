package cj.software.camel.monitoring.data;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Entity that contains all informations to be monitored
 */
public class MonitoredExchange
		implements
		Serializable
{
	private static final long serialVersionUID = 1L;

	private String exchangeId;

	private String camelContextName;

	private String endpointURI;

	private String initialRouteId;

	private String currentRouteId;

	private OffsetDateTime exchangeCreated;

	private OffsetDateTime monitored;

	private MonitoredMessage inMessage;

	private MonitoredMessage outMessage;

	// TODO: Properties etc

	private MonitoredExchange()
	{
	}

	public String getExchangeId()
	{
		return this.exchangeId;
	}

	public String getCamelContextName()
	{
		return this.camelContextName;
	}

	public String getEndpointURI()
	{
		return this.endpointURI;
	}

	public String getInitialRouteId()
	{
		return this.initialRouteId;
	}

	public String getCurrentRouteId()
	{
		return this.currentRouteId;
	}

	public OffsetDateTime getExchangeCreated()
	{
		return this.exchangeCreated;
	}

	public OffsetDateTime getMonitored()
	{
		return this.monitored;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public MonitoredMessage getInMessage()
	{
		return this.inMessage;
	}

	public MonitoredMessage getOutMessage()
	{
		return this.outMessage;
	}

	public static class Builder
	{
		protected MonitoredExchange instance;

		protected Builder()
		{
			this.instance = new MonitoredExchange();
		}

		public Builder withExchangeId(String pExchangeId)
		{
			this.instance.exchangeId = pExchangeId;
			return this;
		}

		public Builder withCamelContextName(String pCamelContextName)
		{
			this.instance.camelContextName = pCamelContextName;
			return this;
		}

		public Builder withEndpointURI(String pEndpointURI)
		{
			this.instance.endpointURI = pEndpointURI;
			return this;
		}

		public Builder withInitialRouteId(String pInitialRouteId)
		{
			this.instance.initialRouteId = pInitialRouteId;
			return this;
		}

		public Builder withCurrentRouteId(String pCurrentRouteId)
		{
			this.instance.currentRouteId = pCurrentRouteId;
			return this;
		}

		public Builder withExchangeCreated(OffsetDateTime pExchangeCreated)
		{
			this.instance.exchangeCreated = pExchangeCreated;
			return this;
		}

		public Builder withMonitored(OffsetDateTime pMonitored)
		{
			this.instance.monitored = pMonitored;
			return this;
		}

		public Builder withInMessage(MonitoredMessage pInMessage)
		{
			this.instance.inMessage = pInMessage;
			return this;
		}

		public Builder withOutMessage(MonitoredMessage pOutMessage)
		{
			this.instance.outMessage = pOutMessage;
			return this;
		}

		public MonitoredExchange build()
		{
			MonitoredExchange lResult = this.instance;
			this.instance = null;
			return lResult;
		}
	}
}
