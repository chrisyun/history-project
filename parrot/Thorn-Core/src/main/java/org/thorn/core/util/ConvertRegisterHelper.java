package org.thorn.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;

public class ConvertRegisterHelper {

	private ConvertRegisterHelper() {
	}

	public static void registerConverters() {
		ConvertUtils.register(new StringConverter(), String.class);
		// date
		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
		ConvertUtils.register(new SqlTimeConverter(null), Time.class);
		ConvertUtils.register(new SqlTimestampConverter(null), Timestamp.class);
		// number
		ConvertUtils.register(new BooleanConverter(null), Boolean.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new LongConverter(null), Long.class);
		ConvertUtils.register(new FloatConverter(null), Float.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
		ConvertUtils.register(new BigIntegerConverter(null), BigInteger.class);
	}
}
