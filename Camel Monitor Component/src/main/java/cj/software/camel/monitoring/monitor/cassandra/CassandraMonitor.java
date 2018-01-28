package cj.software.camel.monitoring.monitor.cassandra;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import cj.software.camel.monitoring.MonitorEndpoint;
import cj.software.camel.monitoring.data.MonitoredExchange;
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
			cluster.getConfiguration().getCodecRegistry().register(InstantCodec.instance);
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
		Instant lStart = OffsetDateTime.now().toInstant();
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
		// TODO Auto-generated method stub
		return null;
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

}
