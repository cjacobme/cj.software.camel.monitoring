package cj.software.camel.monitoring.monitor.cassandra;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import cj.software.camel.monitoring.MonitorEndpoint;
import cj.software.camel.monitoring.data.MonitoredExchange;
import cj.software.camel.monitoring.data.MonitoredMessage;
import cj.software.camel.monitoring.monitor.Monitor;

public class CassandraMonitor
		implements
		Monitor,
		AutoCloseable
{
	private static Cluster cluster;

	private static Session session;

	private static MappingManager mappingManager;

	private Logger logger = LogManager.getFormatterLogger();

	private CassandraMonitor()
	{
	}

	public CassandraMonitor(String pHostname, String pKeyspaceName)
	{
		this();
		if (cluster == null)
		{
			cluster = Cluster.builder().addContactPoint(pHostname).build();
			this.logger.info("created cluster \"%s\"", pHostname);
			CodecRegistry lRegister = cluster.getConfiguration().getCodecRegistry();
			lRegister.register(InstantCodec.instance);
			lRegister.register(new LevelCodec());
			lRegister.register(new EnumNameCodec<RunningState>(RunningState.class));
			session = cluster.connect(pKeyspaceName);
			this.logger.info("opened session on keyspace \"%s\"", pKeyspaceName);
			mappingManager = new MappingManager(session);
		}
	}

	@Override
	public String startNewExchange(Exchange pExchange)
	{
		UUID lTimebasedUUID = UUIDs.timeBased();
		String lResult = lTimebasedUUID.toString();
		String lCamelContextName = pExchange.getContext().getName();
		String lCamelExchangeId = pExchange.getExchangeId();
		Date lExchStart = pExchange.getCreated();
		Instant lStart = lExchStart.toInstant();
		ModelRunIDsByContextName lModelRunIDsByContextName = ModelRunIDsByContextName
				.builder()
				.withCamelContextName(lCamelContextName)
				.withModelRunId(lTimebasedUUID)
				.withCamelExchangeId(lCamelExchangeId)
				.withStart(lStart)
				.build();
		ModelRunStartsByContextName lModelRunStartsByContextName = ModelRunStartsByContextName
				.builder()
				.withCamelContextName(lCamelContextName)
				.withCamelExchangeId(lCamelExchangeId)
				.withStart(lStart)
				.withModelRunId(lTimebasedUUID)
				.build();
		BatchStatement lBatch = new BatchStatement();
		Mapper<ModelRunIDsByContextName> lMapper1 = mappingManager.mapper(
				ModelRunIDsByContextName.class);
		lBatch.add(lMapper1.saveQuery(lModelRunIDsByContextName));
		Mapper<ModelRunStartsByContextName> lMapper2 = mappingManager.mapper(
				ModelRunStartsByContextName.class);
		lBatch.add(lMapper2.saveQuery(lModelRunStartsByContextName));
		session.execute(lBatch);
		return lResult;
	}

	@Override
	public String monitor(MonitorEndpoint pEndpoint, MonitoredExchange pMonitoredExchange)
	{
		UUID lDetailId = UUIDs.timeBased();
		Level lLevel = pEndpoint.getLogLevel();
		String lLoggerName = pEndpoint.getLoggerName();
		Instant lExchangeCreated = this.toInstant(pMonitoredExchange.getExchangeCreated());
		Instant lMonitored = this.toInstant(pMonitoredExchange.getMonitored());
		Message lInMessage = this.toMessage(pMonitoredExchange.getInMessage());
		Message lOutMessage = this.toMessage(pMonitoredExchange.getOutMessage());
		Map<String, String> lProperties = this.toStringStringMap(
				pMonitoredExchange.getProperties());
		ExchangeDetails lExchangeDetails = ExchangeDetails
				.builder()
				.withCamelContextName(pMonitoredExchange.getCamelContextName())
				.withModelRunId(UUID.fromString(pMonitoredExchange.getRunId()))
				.withDetailId(lDetailId)
				.withLevel(lLevel)
				.withLoggerName(lLoggerName)
				.withCamelExchangeId(pMonitoredExchange.getExchangeId())
				.withEndpointUri(pMonitoredExchange.getEndpointURI())
				.withInitialRouteId(pMonitoredExchange.getInitialRouteId())
				.withCurrentRouteId(pMonitoredExchange.getCurrentRouteId())
				.withExchangeCreated(lExchangeCreated)
				.withMonitored(lMonitored)
				.withInMessage(lInMessage)
				.withOutMessage(lOutMessage)
				.withProperties(lProperties)
				.build();

		Mapper<ExchangeDetails> lMapper = CassandraMonitor.mappingManager.mapper(
				ExchangeDetails.class);
		lMapper.save(lExchangeDetails);

		return lDetailId.toString();
	}

	private Message toMessage(MonitoredMessage pMonitoredMessage)
	{
		Object lBody = pMonitoredMessage.getBody();
		// TODO conversion of body with several options: toString, constant, # of elements in array
		// or collection...
		String lBodyString = (lBody != null ? lBody.toString() : null);
		Map<String, String> lHeaders = this.toStringStringMap(pMonitoredMessage.getHeaders());
		Message lResult = Message
				.builder()
				.withMessageId(pMonitoredMessage.getMessageId())
				.withBodyClass(pMonitoredMessage.getBodyClass())
				.withBody(lBodyString)
				.withHeaders(lHeaders)
				.build();
		return lResult;
	}

	private Map<String, String> toStringStringMap(Map<String, Object> pSource)
	{
		Map<String, String> lResult;
		if (pSource != null)
		{
			int lSize = pSource.size();
			lResult = new HashMap<>(lSize);
			Set<String> lKeys = pSource.keySet();
			for (String bKey : lKeys)
			{
				Object lObject = pSource.get(bKey);
				String lAsString = lObject.toString();
				// TODO conversion of body with several options: toString, constant, # of elements
				// in array
				// or collection...
				lResult.put(bKey, lAsString);
			}
		}
		else
		{
			lResult = null;
		}
		return lResult;
	}

	private Instant toInstant(OffsetDateTime pODT)
	{
		Instant lResult = (pODT != null ? pODT.toInstant() : null);
		return lResult;
	}

	@Override
	public void close() throws Exception
	{
		if (mappingManager != null)
		{
			mappingManager = null;
		}
		if (session != null)
		{
			session.close();
			this.logger.info("closed session");
		}
		if (cluster != null)
		{
			cluster.close();
			this.logger.info("closed cluster");
		}

	}

	@Override
	public void finishExchange(String pMonitoringId, Exchange pExchange)
	{
		UUID lMonitoringUUID = UUID.fromString(pMonitoringId);
		CamelContext lCtx = pExchange.getContext();
		String lContextName = lCtx.getName();
		Date lExchStart = pExchange.getCreated();
		Instant lStart = lExchStart.toInstant();

		BatchStatement lBatch = new BatchStatement();

		Instant lNow = Instant.now();

		PreparedStatement lPrep1 = session.prepare(
				"UPDATE model_run_ids_by_context_name "
						+ "SET running_state = ?, finish=? "
						+ "WHERE camel_context_name = ? "
						+ "AND model_run_id = ?");
		BoundStatement lBound1 = lPrep1.bind(
				RunningState.FINISHED_SUCCESS,
				lNow,
				lContextName,
				lMonitoringUUID);
		lBatch.add(lBound1);

		PreparedStatement lPrep2 = session.prepare(
				"UPDATE model_run_starts_by_context_name "
						+ "SET running_state = ?, finish = ? "
						+ "WHERE camel_context_name = ? "
						+ "AND start = ?");
		BoundStatement lBound2 = lPrep2.bind(
				RunningState.FINISHED_SUCCESS,
				lNow,
				lContextName,
				lStart);
		lBatch.add(lBound2);

		session.execute(lBatch);
		// TODO 2. Tabelle auch noch und beide im Batch
	}

}
