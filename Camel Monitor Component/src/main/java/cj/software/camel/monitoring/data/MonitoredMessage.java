package cj.software.camel.monitoring.data;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MonitoredMessage
		implements
		Serializable
{
	private static final long serialVersionUID = 1L;

	private String messageId;

	private Class<?> bodyClass;

	private Object body;

	private Map<String, Object> headers = new HashMap<>();

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

	public Map<String, Object> getHeaders()
	{
		return Collections.unmodifiableMap(this.headers);
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

		public Builder withHeaders(Map<String, Object> pHeaders)
		{
			this.instance.headers.clear();
			return this.addHeaders(pHeaders);
		}

		public Builder addHeaders(Map<String, Object> pHeaders)
		{
			this.instance.headers.putAll(pHeaders);
			return this;
		}

		public Builder addHeader(String pKey, Object pValue)
		{
			this.instance.headers.put(pKey, pValue);
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
