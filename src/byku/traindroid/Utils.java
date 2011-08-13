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

import java.util.Calendar;
import java.util.Date;

/*
 * Difference utilities.
 */
public final class Utils 
{
	/*
	 * Converts date to string.
	 */
	public static String DateToString(int day, int month, int year)
	{
		String dayString = (day < 10) ? "0" + Integer.toString(day) : Integer.toString(day);
		String monthString = (month < 10) ? "0" + Integer.toString(month) : Integer.toString(month);
		
		return dayString + "." + monthString + "." + Integer.toString(year);
	}
	
	public static String DateToString(Date date)
	{
		return Integer.toString(date.getDate()) + "." + Integer.toString(date.getMonth() + 1) + "." + Integer.toString(date.getYear());
	}
	
	/*
	 * Creates date based on parsed string. 
	 */
	public static Date StringToDate(String date, Boolean needToCorrect)
	{
		String[] text = date.split("\\.");
		int day = Integer.parseInt(text[0]);
		int month = Integer.parseInt(text[1]) - ((needToCorrect) ? 1 : 0);
		String yearString = (text[2].substring(text[2].length()-1, text[2].length()).equals("*"))
				? text[2].substring(0, text[2].length()-1)
				: text[2];
		int year = Integer.parseInt(yearString);
		
		return new Date(year, month, day);
	}
	
	/*
	 * Gets difference between dates in days. 
	 */
	public static long DatesDifferenceInDays(Date dateFrom, Date dateTo)
	{
		Calendar calendar = Calendar.getInstance();
		int days = 0;
		
		// TODO: Leap year is not take account.
		days += (dateTo.getYear() - dateFrom.getYear()) * 365;
		for (int i = dateFrom.getMonth(); i < dateTo.getMonth() - 1; ++i)
		{
			//TODO: Not tested.
			calendar.set(Calendar.MONTH, i);			
			days += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		
		if (dateTo.getMonth() == dateFrom.getMonth())
		{
			days += (dateTo.getDate() - dateFrom.getDate());
		}
		else
		{
			calendar.set(Calendar.MONTH, dateFrom.getMonth());			
			days += calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - dateFrom.getDate();
			days += dateTo.getDate();
		}
		
		return days;
	}

	public static Date Today()
	{
		Calendar calendar = Calendar.getInstance();
		return new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
	}
}
