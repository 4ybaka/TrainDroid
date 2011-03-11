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

import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public final class RegexpView extends Activity
{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.regexp_view_layout);
        fillView();
        
        Button button = (Button)findViewById(R.id.RegexpViewSaveButton);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				save();
			}
		});
	}
	
	private void save()
	{
		try 
		{
			String tutu = ((EditText)findViewById(R.id.RegexpTutuEditText)).getText().toString();
			String yandex = ((EditText)findViewById(R.id.RegexpYandexEditText)).getText().toString();
			DataFacade.TUTU_REGEXP = tutu;
			DataFacade.YANDEX_REGEXP = yandex;
			
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
	
	private void fillView()
	{
		EditText control = (EditText)findViewById(R.id.RegexpTutuEditText);
		control.setText(DataFacade.TUTU_REGEXP);
		
		control = (EditText)findViewById(R.id.RegexpYandexEditText);
		control.setText(DataFacade.YANDEX_REGEXP);
	}
}
