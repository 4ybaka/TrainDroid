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

package byku.traindroid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * Class of station.
 */
public class Station 
{
	String _name;
	String _tutuId;
	String _yandexId;
	
	public Station(String name, String tutuId, String yandexId)
	{
		_name = name;
		_tutuId = tutuId;
		_yandexId = yandexId;
	}
	
	public Station(FileInputStream stream)
	{
		Deserialize(stream);
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getTutuId()
	{
		return _tutuId;
	}
	
	public String getYandexId()
	{
		return _yandexId;
	}
	
	public void setName(String value)
	{
		_name = value;
	}
	
	public void setTutuId(String value)
	{
		_tutuId = value;
	}
	
	public void setYandexId(String value)
	{
		_yandexId = value;
	}	
	
	public void Serialize(FileOutputStream stream)
	{
		try 
		{
			ByteUtils.WriteString(stream, _name);
			ByteUtils.WriteString(stream, _tutuId);
			ByteUtils.WriteString(stream, _yandexId);
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
			_name = ByteUtils.ReadString(stream);
			_tutuId = ByteUtils.ReadString(stream);
			_yandexId = ByteUtils.ReadString(stream);
		}
		catch (IOException e) 
		{
			//TODO : Throw error and handle it.
		}
	}
}
