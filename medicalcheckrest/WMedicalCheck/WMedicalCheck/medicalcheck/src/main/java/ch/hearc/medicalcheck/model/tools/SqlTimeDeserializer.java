package ch.hearc.medicalcheck.model.tools;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/*
* Project   : Medical Check Rest
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

public class SqlTimeDeserializer extends JsonDeserializer<Time> {

	/***
	 * Define time deserializer to match with app data
	 */
	@Override
	public Time deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		
		String s = p.getValueAsString();
		
		System.out.println(s);
		
		//07:00:00 PM
		String[] global = s.split(" ");
		String[] time = global[0].split(":");
		
		int hour = Integer.parseInt(time[0]);
		
		if(global[1].equals("PM"))
		{
			if(hour < 12)
				hour += 12;
		}
		else if (global[1].equals("AM"))
		{
			if(hour == 12)
				hour = 0;
		}
		
		String h = null;
		
		if(hour < 10)
			h = "0" + Integer.toString(hour);
		else
			h = Integer.toString(hour);
		
		String result = h + ":" + time[1] + ":" + time[2];
		
		return Time.valueOf(result);
	}
	
}
