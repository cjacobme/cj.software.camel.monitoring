package cj.software.camel.monitoring;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.spi.UnitOfWork;

import cj.software.camel.monitoring.data.MonitoredExchange;

class Converter
{
	static MonitoredExchange toMonitoredExchange(Exchange pExchange)
	{
		UnitOfWork lUnitOfWork = pExchange.getUnitOfWork();
		MonitoredExchange lResult = MonitoredExchange
				.builder()
				.withCamelContextName(pExchange.getContext().getName())
				.withEndpointURI(lUnitOfWork.getRouteContext().getEndpoint().getEndpointKey())
				.withInitialRouteId(pExchange.getFromRouteId())
				.withCurrentRouteId(lUnitOfWork.getRouteContext().getRoute().getId())
				.withExchangeCreated(Converter.toOffsetDateTime(pExchange.getCreated()))
				.withMonitored(OffsetDateTime.now(ZoneId.of("UTC")))
				.build();
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
