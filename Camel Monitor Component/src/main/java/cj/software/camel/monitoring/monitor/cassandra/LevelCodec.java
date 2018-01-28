package cj.software.camel.monitoring.monitor.cassandra;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.Level;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;

public class LevelCodec
		extends TypeCodec<Level>
{
	public LevelCodec()
	{
		super(DataType.text(), Level.class);
	}

	@Override
	public ByteBuffer serialize(Level pValue, ProtocolVersion pProtocolVersion)
			throws InvalidTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Level deserialize(ByteBuffer pBytes, ProtocolVersion pProtocolVersion)
			throws InvalidTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Level parse(String pValue) throws InvalidTypeException
	{
		Level lResult = Level.valueOf(pValue);
		return lResult;
	}

	@Override
	public String format(Level pValue) throws InvalidTypeException
	{
		return pValue.toString();
	}

}
