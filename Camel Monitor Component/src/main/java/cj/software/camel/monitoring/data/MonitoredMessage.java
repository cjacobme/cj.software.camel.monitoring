package cj.software.camel.monitoring.data;

import java.io.Serializable;

public class MonitoredMessage
		implements
		Serializable
{
	private static final long serialVersionUID = 1L;

	private String messageId;

	private Class<?> bodyClass;

	private Object body;

	private MonitoredMessage()
	{
	}

	public String getMessageId()
	{
		return this.messageId;
	}

	public Class<?> getBodyClass()
	{
		return this.bodyClass;
	}

	public Object getBody()
	{
		return this.body;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static class Builder
	{
		protected MonitoredMessage instance;

		protected Builder()
		{
			this.instance = new MonitoredMessage();
		}

		public Builder withMessageId(String pMessageId)
		{
			this.instance.messageId = pMessageId;
			return this;
		}

		public Builder withBody(Object pBody)
		{
			this.instance.body = pBody;
			if (pBody != null)
			{
				this.instance.bodyClass = pBody.getClass();
			}
			else
			{
				this.instance.body = null;
			}
			return this;
		}

		public MonitoredMessage build()
		{
			MonitoredMessage lResult = this.instance;
			this.instance = null;
			return lResult;
		}
	}
}
