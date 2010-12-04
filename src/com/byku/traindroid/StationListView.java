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

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public final class StationListView extends ListActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.textlist);
    }
	
    @Override
    public void onResume() {
        super.onResume();
        fillView();
    }
	
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) 
    {
    	Bundle bundle = new Bundle();
    	bundle.putString(getString(R.string.transfer_station_key), ((TextView)view).getText().toString());    	
        Intent intent = new Intent(StationListView.this, StationView.class);
        intent.putExtras(bundle);
        
        startActivity(intent);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.station_list_view_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.stationlistview_menu_add:
        		Intent stationView = new Intent(StationListView.this, StationView.class);               
                startActivity(stationView);
           
        		return true;

        	default:
        		return false;
        }
    }
	
	private void fillView()
	{
    	ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String,String>>();
    	   	
    	for (Station station : DataFacade.GetStations()) 
    	{
    		HashMap<String, String> item = new HashMap<String, String>();
    		item.put("key", station.getName());
    		items.add(item);
		}
    	
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.textlistitem, 
        		new String[]{"key"},
        		new int[]{R.id.textListItem});
        setListAdapter(adapter);
	}
}
