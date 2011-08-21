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

import java.io.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public final class StationView extends Activity
{
	private Station _station = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.station_layout);
        
        Button button = (Button)findViewById(R.id.StationViewDeleteButton);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				deleteStation();
			}
		});
        
        button = (Button)findViewById(R.id.StationViewSaveButton);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				saveStation();
			}
		});

        button = (Button)findViewById(R.id.StationViewHelpButton);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				showHelpDialog();
			}
		});
        
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
        	String name = bundle.getString(getString(R.string.transfer_station_key));
        	
        	if (name == null || name.equals(""))
        	{
        		UIUtils.ShowToast(getApplicationContext(), getString(R.string.no_data));
        	}
        	else
        	{
        		_station = DataFacade.FindStation(name);
        		fillView();
        	}
        }
    }	

	private void deleteStation()
	{
		DataFacade.GetStations().remove(_station);
		
		try 
		{
			FileOutputStream stream = openFileOutput(getString(R.string.data_file_name), Context.MODE_PRIVATE);
			DataFacade.Serialize(stream);
			stream.close();
		}
		catch (Exception e) 
		{
			UIUtils.ShowToast(this, "Ошибка: " + e.getMessage());
		}
		
		finish();
	}
	
	private void saveStation()
	{
		String name = ((EditText)findViewById(R.id.StationNameEditText)).getText().toString();
		String tutuId = ((EditText)findViewById(R.id.StationTutuIDEditText)).getText().toString();
		String yandexId = ((EditText)findViewById(R.id.StationYandexIDEditText)).getText().toString();
		
		if (_station == null)
		{
			_station = new Station(name, tutuId, yandexId);
			DataFacade.GetStations().add(_station);
		}
		else
		{
			_station.setName(name);
			_station.setTutuId(tutuId);
			_station.setYandexId(yandexId);
		}
		
		try 
		{
			FileOutputStream stream = openFileOutput(getString(R.string.data_file_name), Context.MODE_PRIVATE);
			DataFacade.Serialize(stream);
			stream.close();
		}
		catch (Exception e) 
		{
			UIUtils.ShowToast(this, "Ошибка: " + e.getMessage());
		}
		
		finish();
	}

    /**
     * Shows help dialog.
     */
    private void showHelpDialog()
    {
        WebView webView = new WebView(this);
        webView.setBackgroundColor(0);
        webView.loadDataWithBaseURL(null, getHelpMessage(), "text/html", "utf-8", null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.help_dialog_title))
                .setView(webView)
                .setCancelable(false)
                .setPositiveButton(
                        getResources().getString(R.string.changelog_ok_button),
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        });
        builder.create().show();
    }

    /**
     * Gets help message from resources.
     */
    private String getHelpMessage()
    {
        StringBuilder message = new StringBuilder();
        try
        {
            InputStream stream = getResources().openRawResource(R.raw.help_stations);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                message.append(line);
            }
        }
        catch(IOException exception)
        {
            Log.e(Utils.TAG, "Cannot read file with help.", exception);
            message.append(getString(R.string.help_file_not_found));
        }
        return message.toString();
    }
	
	private void fillView()
	{
		EditText control = (EditText)findViewById(R.id.StationNameEditText);
		control.setText(_station.getName());
		
		control = (EditText)findViewById(R.id.StationTutuIDEditText);
		control.setText(_station.getTutuId());
		
		control = (EditText)findViewById(R.id.StationYandexIDEditText);
		control.setText(_station.getYandexId());
		
		Button button = (Button)findViewById(R.id.StationViewDeleteButton);
		button.setEnabled(true);
	}
}
