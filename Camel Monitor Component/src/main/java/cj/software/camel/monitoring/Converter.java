package cj.software.camel.monitoring;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.spi.UnitOfWork;

import cj.software.camel.monitoring.data.MonitoredExchange;
import cj.software.camel.monitoring.data.MonitoredMessage;

class Converter
{
	static MonitoredExchange toMonitoredExchange(Exchange pExchange)
	{
		UnitOfWork lUnitOfWork = pExchange.getUnitOfWork();

		MonitoredMessage lInMessage = toMonitoredMessage(pExchange.getIn());
		MonitoredMessage lOutMessage = toMonitoredMessage(pExchange.getOut());

		MonitoredExchange lResult = MonitoredExchange
				.builder()
				.withExchangeId(pExchange.getExchangeId())
				.withCamelContextName(pExchange.getContext().getName())
				.withEndpointURI(lUnitOfWork.getRouteContext().getEndpoint().getEndpointKey())
				.withInitialRouteId(pExchange.getFromRouteId())
				.withCurrentRouteId(lUnitOfWork.getRouteContext().getRoute().getId())
				.withExchangeCreated(Converter.toOffsetDateTime(pExchange.getCreated()))
				.withMonitored(OffsetDateTime.now(ZoneId.of("UTC")))
				.withInMessage(lInMessage)
				.withOutMessage(lOutMessage)
				.build();
		return lResult;
	}

	static MonitoredMessage toMonitoredMessage(Message pMessage)
	{
		MonitoredMessage lResult;
		if (pMessage != null)
		{
			lResult = MonitoredMessage
					.builder()
					.withMessageId(pMessage.getMessageId())
					.withBody(pMessage.getBody())
					.build();
		}
		else
		{
			lResult = null;
		}
		return lResult;
	}

	static OffsetDateTime toOffsetDateTime(Date pDate)
	{
		OffsetDateTime lResult;
		if (pDate != null)
		{
			Instant lInstant = pDate.toInstant();
			// TODO is the following line correct? Or should the zone id be configured?
			ZonedDateTime lZDT = ZonedDateTime
					.ofInstant(lInstant, ZoneId.systemDefault())
					.withZoneSameInstant(ZoneId.of("UTC"));
			lResult = lZDT.toOffsetDateTime();
		}
		else
		{
			lResult = null;
		}
		return lResult;
	}
}
