package cj.software.camel.monitoring.monitor.cassandra;

import java.time.Instant;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "model_run_starts_by_context_name")
public class ModelRunStartsByContextName
{
	@Column(name = "camel_context_name")
	@PartitionKey
	private String camelContextName;

	@Column(name = "start")
	@ClusteringColumn
	private Instant start;

	@Column(name = "camel_exchange_id")
	private String camelExchangeId;

	@Column(name = "finish")
	private Instant finish;

	@Column(name = "model_run_id")
	private UUID modelRunId;

	@Column(name = "successful")
	private boolean successful = true; // TODO 3 states.

	private ModelRunStartsByContextName()
	{
	}

	public String getCamelContextName()
	{
		return this.camelContextName;
	}

	public Instant getStart()
	{
		return this.start;
	}

	public String getCamelExchangeId()
	{
		return this.camelExchangeId;
	}

	public Instant getFinish()
	{
		return this.finish;
	}

	public UUID getModelRunId()
	{
		return this.modelRunId;
	}

	public boolean isSuccessful()
	{
		return this.successful;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static class Builder
	{
		protected ModelRunStartsByContextName instance;

		protected Builder()
		{
			this.instance = new ModelRunStartsByContextName();
		}

		public Builder withCamelContextName(String pCamelContextName)
		{
			this.instance.camelContextName = pCamelContextName;
			return this;
		}

		public Builder withStart(Instant pStart)
		{
			this.instance.start = pStart;
			return this;
		}

		public Builder withCamelExchangeId(String pCamelExchangeId)
		{
			this.instance.camelExchangeId = pCamelExchangeId;
			return this;
		}

		public Builder withModelRunId(UUID pModelRunId)
		{
			this.instance.modelRunId = pModelRunId;
			return this;
		}

		public Builder withFinish(Instant pFinish)
		{
			this.instance.finish = pFinish;
			return this;
		}

		public Builder withSuccessful(boolean pSuccessful)
		{
			this.instance.successful = pSuccessful;
			return this;
		}

		public ModelRunStartsByContextName build()
		{
			ModelRunStartsByContextName lResult = this.instance;
			this.instance = null;
			return lResult;
		}
	}
}
