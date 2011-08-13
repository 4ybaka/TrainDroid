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

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public final class TimeTableView extends ListActivity {
	
	private final String STATION_FROM_KEY = "FromKey";
	private final String STATION_TO_KEY = "ToKey";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) 
		{
			String date = bundle.getString(getString(R.string.transfer_date_key));
			String stationFromText = bundle.getString(getString(R.string.transfer_from_key));
			String stationToText = bundle.getString(getString(R.string.transfer_to_key));
			
			Station stationFrom = DataFacade.FindStation(stationFromText);
			Station stationTo = DataFacade.FindStation(stationToText);
			
			if (date == null || date.equals("")
				|| stationFrom == null
				|| stationTo == null) 
			{
				UIUtils.ShowToast(getApplicationContext(), getString(R.string.no_data));
			}
			else
			{
				fillView(date, stationFrom, stationTo);
			}
		} 
		else
		{
			UIUtils.ShowToast(getApplicationContext(),
					getString(R.string.incorrect_input_data));
		}

		setContentView(R.layout.textlist);
	}

	private void fillView(String date, Station from, Station to) {
		ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		ArrayList<TimeTable> timeTables = DataFacade.GetTimeTables(date, from, to);

		ArrayList<Pair<String, String>> first = timeTables.get(0).getTime();
		ArrayList<Pair<String, String>> second = (timeTables.size() > 1) 
			? timeTables.get(1).getTime()
			: new ArrayList<Pair<String,String>>();
			
		int maxTrainCount = Math.max(first.size(), second.size());
		
		String firstSourceName = timeTables.get(0).getSource();
		String secondSourceName = (timeTables.size() > 1) 
				? timeTables.get(1).getSource()
				: "No source";
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(STATION_FROM_KEY, firstSourceName);
		item.put(STATION_TO_KEY, secondSourceName);
		items.add(item);
		
		for (int i = 0; i < maxTrainCount; ++i) 
		{
			String firstText = (i < first.size()) 
				? (first.get(i).getFirst() + " - " + first.get(i).getSecond())
				: "";
			String secondText = (i < second.size()) 
				? (second.get(i).getFirst() + " - " + second.get(i).getSecond())
				: "";
				
			item = new HashMap<String, String>();
			item.put(STATION_FROM_KEY, firstText);
			item.put(STATION_TO_KEY, secondText);
			items.add(item);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.two_column_list_item, new String[] { STATION_FROM_KEY, STATION_TO_KEY },
				new int[] { R.id.leftTextListItem, R.id.rightTextListItem });
		setListAdapter(adapter);
	}
}
