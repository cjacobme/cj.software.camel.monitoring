package cj.software.camel.monitoring.monitor.cassandra;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "exchange_details")
public class ExchangeDetails
{
	@Column(name = "camel_context_name")
	@PartitionKey(0)
	private String camelContextName;

	@Column(name = "model_run_id")
	@PartitionKey(1)
	private UUID modelRunId;

	@Column(name = "detail_id")
	@ClusteringColumn
	private UUID detailId;

	@Column(name = "level")
	private Level level;

	@Column(name = "camel_exchange_id")
	private String camelExchangeId;

	@Column(name = "endpoint_uri")
	private String endpointUri;

	@Column(name = "initial_route_id")
	private String initialRouteId;

	@Column(name = "current_route_id")
	private String currentRouteId;

	@Column(name = "exchange_created")
	private Instant exchangeCreated;

	@Column(name = "monitored")
	private Instant monitored;

	@Column(name = "in_message")
	private Message inMessage;

	@Column(name = "out_message")
	private Message outMessage;

	@Column(name = "properties")
	private Map<String, String> properties;

	private ExchangeDetails()
	{
	}

	public String getCamelContextName()
	{
		return this.camelContextName;
	}

	public UUID getModelRunId()
	{
		return this.modelRunId;
	}

	public UUID getDetailId()
	{
		return this.detailId;
	}

	public Level getLevel()
	{
		return this.level;
	}

	public String getCamelExchangeId()
	{
		return this.camelExchangeId;
	}

	public String getEndpointUri()
	{
		return this.endpointUri;
	}

	public String getInitialRouteId()
	{
		return this.initialRouteId;
	}

	public String getCurrentRouteId()
	{
		return this.currentRouteId;
	}

	public Instant getExchangeCreated()
	{
		return this.exchangeCreated;
	}

	public Instant getMonitored()
	{
		return this.monitored;
	}

	public Message getInMessage()
	{
		return this.inMessage;
	}

	public Message getOutMessage()
	{
		return this.outMessage;
	}

	public Map<String, String> getProperties()
	{
		return this.properties;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static class Builder
	{
		protected ExchangeDetails instance;

		protected Builder()
		{
			this.instance = new ExchangeDetails();
		}

		public Builder withCamelContextName(String pCamelContextName)
		{
			this.instance.camelContextName = pCamelContextName;
			return this;
		}

		public Builder withModelRunId(UUID pModelRunId)
		{
			this.instance.modelRunId = pModelRunId;
			return this;
		}

		public Builder withDetailId(UUID pDetailId)
		{
			this.instance.detailId = pDetailId;
			return this;
		}

		public Builder withLevel(Level pLevel)
		{
			this.instance.level = pLevel;
			return this;
		}

		public Builder withCamelExchangeId(String pExchangeId)
		{
			this.instance.camelExchangeId = pExchangeId;
			return this;
		}

		public Builder withEndpointUri(String pEndpointUri)
		{
			this.instance.endpointUri = pEndpointUri;
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

		public Builder withExchangeCreated(Instant pExchangeCreated)
		{
			this.instance.exchangeCreated = pExchangeCreated;
			return this;
		}

		public Builder withMonitored(Instant pMonitored)
		{
			this.instance.monitored = pMonitored;
			return this;
		}

		public Builder withInMessage(Message pInMessage)
		{
			this.instance.inMessage = pInMessage;
			return this;
		}

		public Builder withOutMessage(Message pOutMessage)
		{
			this.instance.outMessage = pOutMessage;
			return this;
		}

		public Builder withProperties(Map<String, String> pProperties)
		{
			this.instance.properties = pProperties;
			return this;
		}

		public ExchangeDetails build()
		{
			ExchangeDetails lResult = this.instance;
			this.instance = null;
			return lResult;
		}
	}
}
