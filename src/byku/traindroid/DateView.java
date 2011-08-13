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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public final class DateView extends ListActivity {
	
	private final String STATION_KEY = "StationKey";
	private final String STATION_SEPARATOR = " - ";
	
	private String _date;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle bundle = getIntent().getExtras();
        
        if (bundle != null){
        	_date = bundle.getString(getString(R.string.transfer_date_key));
        	if (_date == null || _date.equals(""))
        	{
        		UIUtils.ShowToast(getApplicationContext(), getString(R.string.no_data));
        	}
        	else
        	{
        		fillView();
        	}
        }
        else
        {
			UIUtils.ShowToast(getApplicationContext(), getString(R.string.incorrect_input_data));
        }
        
        setContentView(R.layout.textlist);
    }
    
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) 
    {
    	//TODO: [Code] Save items in separate collection in fillView and get item by 'position'.
    	String[] stations = ((TextView)view).getText().toString().split(STATION_SEPARATOR);

    	Bundle bundle = new Bundle();
    	bundle.putString(getString(R.string.transfer_date_key), _date);

    	Station stationFrom = DataFacade.FindStation(stations[0]);
    	Station stationTo = DataFacade.FindStation(stations[1]);
    	
    	bundle.putString(getString(R.string.transfer_from_key), stationFrom.getName());
    	bundle.putString(getString(R.string.transfer_to_key), stationTo.getName());
    	
        Intent intent = new Intent(DateView.this, TimeTableView.class);
        intent.putExtras(bundle);
        
        startActivity(intent);
    }
    
    private void fillView()
    {
    	ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String,String>>();
    	ArrayList<TimeTable> timeTables = DataFacade.GetTimeTables(_date);
    	ArrayList<String> existsItems = new ArrayList<String>();
    	
    	for (TimeTable timeTable : timeTables) 
    	{
    		String text = timeTable.getStationFrom().getName() + STATION_SEPARATOR + timeTable.getStationTo().getName();
    		if (!existsItems.contains(text))
    		{
				HashMap<String, String> item = new HashMap<String, String>();
				item.put(STATION_KEY, text);
				items.add(item);
				
				existsItems.add(text);
    		}
		}
    	
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.textlistitem, 
        		new String[]{STATION_KEY},
        		new int[]{R.id.textListItem});
        setListAdapter(adapter);
    }
}
