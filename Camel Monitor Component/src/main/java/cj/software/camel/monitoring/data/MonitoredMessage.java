package cj.software.camel.monitoring.data;

import java.io.InputStream;
import java.io.OutputStream;
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
			if (pBody != null)
			{
				this.instance.bodyClass = pBody.getClass();
				if (pBody instanceof OutputStream || pBody instanceof InputStream)
				{
					this.instance.body = pBody.getClass().getName() + " will not be converted";
				}
				else
				{
					this.instance.body = pBody;
				}
			}
			else
			{
				this.instance.body = pBody;
				this.instance.bodyClass = null;
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
