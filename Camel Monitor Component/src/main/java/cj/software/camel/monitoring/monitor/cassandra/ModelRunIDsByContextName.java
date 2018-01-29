package cj.software.camel.monitoring.monitor.cassandra;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "model_run_ids_by_context_name")
public class ModelRunIDsByContextName
		implements
		Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(name = "camel_context_name")
	@PartitionKey
	private String camelContextName;

	@Column(name = "model_run_id")
	@ClusteringColumn
	private UUID modelRunId;

	@Column(name = "camel_exchange_id")
	private String camelExchangeId;

	@Column(name = "start")
	private Instant start;

	@Column(name = "finish")
	private Instant finish;

	@Column(name = "running_state")
	private RunningState runningState = RunningState.RUNNING;

	private ModelRunIDsByContextName()
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

	public String getCamelExchangeId()
	{
		return this.camelExchangeId;
	}

	public Instant getStart()
	{
		return this.start;
	}

	public Instant getFinish()
	{
		return this.finish;
	}

	public RunningState getRunningState()
	{
		return this.runningState;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static class Builder
	{
		protected ModelRunIDsByContextName instance;

		protected Builder()
		{
			this.instance = new ModelRunIDsByContextName();
		}

		public Builder withCamelContextName(String pCamelContextName)
		{
			this.instance.camelContextName = pCamelContextName;
			return this;
		}

		public Builder withModelRunId(UUID pRunId)
		{
			this.instance.modelRunId = pRunId;
			return this;
		}

		public Builder withCamelExchangeId(String pCamelExchangeId)
		{
			this.instance.camelExchangeId = pCamelExchangeId;
			return this;
		}

		public Builder withStart(Instant pStart)
		{
			this.instance.start = pStart;
			return this;
		}

		public Builder withFinish(Instant pFinish)
		{
			this.instance.finish = pFinish;
			return this;
		}

		public ModelRunIDsByContextName build()
		{
			ModelRunIDsByContextName lResult = this.instance;
			this.instance = null;
			return lResult;
		}
	}
}
