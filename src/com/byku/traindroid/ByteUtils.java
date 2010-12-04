/*******************************************************************************
 * Copyright (C) 2010 Dmitriy Nesterov
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
 * Utilities to store data.
 */
public final class ByteUtils 
{
	/*
	 * Writes string to a specified stream.
	 */
	public static void WriteString(FileOutputStream stream, String text) throws IOException
	{
		byte[] bytes = text.getBytes();
		WriteInt(stream, bytes.length);
		stream.write(bytes);
	}
	
	/*
	 * Reads string from a specified stream. 
	 */
	public static String ReadString(FileInputStream stream) throws IOException
	{
		int length = ByteUtils.ReadInt(stream);
		byte[] buffer = new byte[length];
		if (stream.read(buffer) != length)
		{
			//TODO : Throw error and handle it.
		}
		
		return new String(buffer);
	}
	
	/*
	 * Writes array of pairs to a specified stream.
	 */
	public static void WriteArrayOfStringPairs(FileOutputStream stream, ArrayList<Pair<String, String>> pairs) 
		throws IOException
	{
		WriteInt(stream, pairs.size());
		for (Pair<String, String> pair : pairs) {
			WriteString(stream, pair.getFirst());
			WriteString(stream, pair.getSecond());
		}
	}
	
	/*
	 * Reads array of pairs from a specified stream.
	 */
	public static ArrayList<Pair<String, String>> ReadArrayOfStringPairs(FileInputStream stream) 
		throws IOException
	{
		int size = ReadInt(stream);
		ArrayList<Pair<String, String>> result = new ArrayList<Pair<String,String>>(size);
		
		for (int i = 0; i < size; ++i)
		{
			String first = ReadString(stream);
			String second = ReadString(stream);
			Pair<String, String> pair = new Pair<String, String>(first, second);
			
			result.add(pair);
		}
		
		return result;
	}
	
	/*
	 * Writes integer to a specified stream.
	 */
	public static void WriteInt(FileOutputStream stream, int value) throws IOException
	{
		stream.write(IntToBytes(value));
	}
	
	/*
	 * Reads integer from a specified stream.
	 */
	public static int ReadInt(FileInputStream stream) throws IOException
	{
		byte[] bytes = new byte[4];
		if (stream.read(bytes) != 4)
		{
			//TODO : Throw error and handle it.
		}

		return BytesToInt(bytes);
	}
	
	/*
	 * Converts integer to byte array.
	 */
	public static byte[] IntToBytes(int value)
	{
		byte[] result = new byte[4];
		result[0] =(byte)( value >> 24 );
		result[1] =(byte)( (value << 8) >> 24 );
		result[2] =(byte)( (value << 16) >> 24 );
		result[3] =(byte)( (value << 24) >> 24 );
		
		return result;
	}
	
	/*
	 * Converts byte array to integer.
	 */
	public static int BytesToInt(byte[] value)
	{
		int result = 0;
		
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            result += (value[i] & 0x000000FF) << shift;
        }
		
		return result;
	}
}
