/*******************************************************************************
 * Copyright (C) 2010-2011 Dmitriy Nesterov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.byku.traindroid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Class of timetable.
 */
public class TimeTable {
	
	private Station _stationFrom;
	private Station _stationTo;
	private String _date;
	private String _source;
	private ArrayList<Pair<String, String>> _time;
	
	public TimeTable(Station from, Station to, String date, String source, ArrayList<Pair<String, String>> time)
	{
		_stationFrom = from;
		_stationTo = to;
		_date = date;
		_source = source;
		_time = time;
	}
	
	public TimeTable(FileInputStream stream)
	{
		Deserialize(stream);
	}

	public Station getStationFrom() {
		return _stationFrom;
	}

	public Station getStationTo() {
		return _stationTo;
	}

	public String getDate() {
		return _date;
	}

	public String getSource() {
		return _source;
	}
	
	public ArrayList<Pair<String, String>> getTime() {
		return _time;
	}
	
	public void Serialize(FileOutputStream stream)
	{
		try 
		{
			ByteUtils.WriteString(stream, _stationFrom.getName());
			ByteUtils.WriteString(stream, _stationTo.getName());
			ByteUtils.WriteString(stream, _date);
			ByteUtils.WriteString(stream, _source);
			ByteUtils.WriteArrayOfStringPairs(stream, _time);
		}
		catch (IOException e) 
		{
			//TODO : Throw error and handle it.
		}
	}
	
	public void Deserialize(FileInputStream stream)
	{
		try 
		{
			String stationFrom = ByteUtils.ReadString(stream);
			_stationFrom = DataFacade.FindStation(stationFrom);
			String stationTo = ByteUtils.ReadString(stream);
			_stationTo = DataFacade.FindStation(stationTo);
			
			_date = ByteUtils.ReadString(stream);
			_source = ByteUtils.ReadString(stream);
			_time = ByteUtils.ReadArrayOfStringPairs(stream);
		}
		catch (IOException e) 
		{
			//TODO : Throw error and handle it.
		}
	}
}
