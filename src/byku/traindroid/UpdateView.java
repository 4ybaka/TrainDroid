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

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;

public class UpdateView extends Activity {
	
	private int _dayFrom;
	private int _dayTo;
	private int _monthFrom;
	private int _monthTo;
	private int _yearFrom;
	private int _yearTo;
	
	private static final int DATE_PICKER_DIALOG_ID_DATE_FROM = 0;
	private static final int DATE_PICKER_DIALOG_ID_DATE_TO = 1;

    private DatePickerDialog _datePickerDialogFrom;
    private DatePickerDialog _datePickerDialogTo;
    private AsyncTimeTableUpdater _asyncUpdater;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.update_view_layout);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_timetable));

        _asyncUpdater = new AsyncTimeTableUpdater(progressDialog);
        
		Calendar calendar = Calendar.getInstance();
		_dayFrom = calendar.get(Calendar.DAY_OF_MONTH);
		_dayTo = calendar.get(Calendar.DAY_OF_MONTH);
		_monthFrom = calendar.get(Calendar.MONTH);
		_monthTo = calendar.get(Calendar.MONTH);
		_yearFrom = calendar.get(Calendar.YEAR);
		_yearTo = calendar.get(Calendar.YEAR);
		updateDates(true);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list_item, DataFacade.GetStationNames());
        
        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.AutoCompleteTextStationFrom);
        textView.setAdapter(adapter);
        textView = (AutoCompleteTextView)findViewById(R.id.AutoCompleteTextStationTo);
        textView.setAdapter(adapter);
        
        Button button = (Button)findViewById(R.id.UpdateButton);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Do();
			}
		});
        
        button = (Button)findViewById(R.id.DateFromButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_PICKER_DIALOG_ID_DATE_FROM);
            }
        });
        
        button = (Button)findViewById(R.id.DateToButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_PICKER_DIALOG_ID_DATE_TO);
            }
        });
    }
    
    public void Do()
    {
		try
		{
			AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.AutoCompleteTextStationFrom);
			String stationFromText = textView.getText().toString();
			Station stationFrom = DataFacade.FindStation(stationFromText);
			
			textView = (AutoCompleteTextView)findViewById(R.id.AutoCompleteTextStationTo);
			String stationToText = textView.getText().toString();
			Station stationTo = DataFacade.FindStation(stationToText);
			
			if (stationFrom == null || stationTo == null)
			{
				String message = String.format("Станция \"%s\" не найдена.", 
						stationFrom == null ? stationFromText : stationToText);
				UIUtils.ShowToast(this, message);
				
				finish();
				return;
			}
			
			Date dateFrom = new Date(_yearFrom, _monthFrom, _dayFrom);
			Date dateTo = new Date(_yearTo, _monthTo, _dayTo);
			Date today = Utils.Today();
			
			int daysFrom = (int)Utils.DatesDifferenceInDays(today, dateFrom);
			int daysCount = (int)Utils.DatesDifferenceInDays(dateFrom, dateTo) + 1;
			
			CheckBox checkBox = (CheckBox)findViewById(R.id.CheckBoxYandex);
			Boolean updateYandex = checkBox.isChecked();
			checkBox = (CheckBox)findViewById(R.id.CheckBoxTutu);
			Boolean updateTutu = checkBox.isChecked();

			checkBox = (CheckBox)findViewById(R.id.CheckBoxBothSide);
			Boolean bothSide = checkBox.isChecked();

            _asyncUpdater.execute(stationFrom, stationTo, daysFrom, daysCount, updateYandex, updateTutu, bothSide, this);
		}
		catch(Exception e)
		{
			UIUtils.ShowToast(this, "Error: " + ((e.getMessage() == null) ? e.getCause().getMessage() : e.getMessage()));
		}
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_PICKER_DIALOG_ID_DATE_FROM:
            _datePickerDialogFrom = new DatePickerDialog(this, DateFromListener, _yearFrom, _monthFrom, _dayFrom);
            return _datePickerDialogFrom;
        case DATE_PICKER_DIALOG_ID_DATE_TO:
            _datePickerDialogTo = new DatePickerDialog(this, DateToListener, _yearTo, _monthTo, _dayTo);
            return _datePickerDialogTo;
        }
        return null;
    }
    
    private DatePickerDialog.OnDateSetListener DateFromListener =
        new DatePickerDialog.OnDateSetListener()
    	{
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                _yearFrom = year;
                _monthFrom = monthOfYear;
                _dayFrom = dayOfMonth;
                updateDates(true);
            }
        };

    private DatePickerDialog.OnDateSetListener DateToListener =
        new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                _yearTo = year;
                _monthTo = monthOfYear;
                _dayTo = dayOfMonth;
                updateDates(false);
            }
        };
        
    private void updateDates(boolean dateFromSet)
    {
        syncDates(dateFromSet);
    	String date = Utils.DateToString(_dayFrom, _monthFrom + 1, _yearFrom);
    	((Button)findViewById(R.id.DateFromButton)).setText(date);
    	
    	date = Utils.DateToString(_dayTo, _monthTo + 1, _yearTo);
    	((Button)findViewById(R.id.DateToButton)).setText(date);
    }

    /**
     * Sync date from and date to.
     */
    private void syncDates(boolean dateFromSet)
    {
        Date dateFrom = new Date(_yearFrom, _monthFrom, _dayFrom);
        Date dateTo = new Date(_yearTo, _monthTo, _dayTo);

        if (dateFrom.after(dateTo))
        {
            if(dateFromSet)
            {
                _yearTo = _yearFrom;
                _monthTo = _monthFrom;
                _dayTo = _dayFrom;
            }
            else
            {
                _yearFrom = _yearTo;
                _monthFrom = _monthTo;
                _dayFrom = _dayTo;
            }
        }

        if (_datePickerDialogFrom != null)
        {
            _datePickerDialogFrom.updateDate(_yearFrom, _monthFrom, _dayFrom);
        }
        if (_datePickerDialogTo != null)
        {
            _datePickerDialogTo.updateDate(_yearFrom, _monthFrom, _dayFrom);
        }
    }
}
