package cj.software.camel.monitoring.monitor.cassandra;

import java.io.Serializable;
import java.util.Map;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;

@UDT(name = "message")
public class Message
		implements
		Serializable
{
	private static final long serialVersionUID = 1L;

	@Field(name = "message_id")
	private String messageId;

	@Field(name = "body_class")
	private String bodyClass;

	@Field(name = "body")
	private String body;

	@Field(name = "headers")
	private Map<String, String> headers;

	private Message()
	{
	}

	public String getMessageId()
	{
		return this.messageId;
	}

	public String getBodyClass()
	{
		return this.bodyClass;
	}

	public String getBody()
	{
		return this.body;
	}

	public Map<String, String> getHeaders()
	{
		return this.headers;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static class Builder
	{
		protected Message instance;

		protected Builder()
		{
			this.instance = new Message();
		}

		public Builder withMessageId(String pMessageId)
		{
			this.instance.messageId = pMessageId;
			return this;
		}

		public Builder withBodyClass(Class<?> pBodyClass)
		{
			this.instance.bodyClass = (pBodyClass != null ? pBodyClass.getName() : null);
			return this;
		}

		public Builder withBody(String pBody)
		{
			this.instance.body = pBody;
			return this;
		}

		public Builder withHeaders(Map<String, String> pHeaders)
		{
			this.instance.headers = pHeaders;
			return this;
		}

		public Message build()
		{
			Message lResult = this.instance;
			this.instance = null;
			return lResult;
		}
	}
}
