package byku.traindroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class AsyncTimeTableUpdater extends AsyncTask<Object, Void, String>
{
    private ProgressDialog _dialog;
    private Activity _activity;

    public AsyncTimeTableUpdater(ProgressDialog dialog)
    {
        _dialog = dialog;
    }

    @Override
    protected String doInBackground(Object... objects)
    {
        _activity = (Activity) objects[7];
        String result = "";

        Station from = (Station)objects[0];
        Station to = (Station)objects[1];
        boolean updateYandex = (Boolean)objects[4];
        boolean updateTutu = (Boolean)objects[5];

        if (updateTutu && (isStringEmpty(from.getTutuId()) || isStringEmpty(to.getTutuId())))
        {
            result += "\nНе указан туту Id для одной из станций.";
        }
        if (updateYandex && (isStringEmpty(from.getYandexId()) || isStringEmpty(to.getYandexId())))
        {
            result += "\nНе указан яндекс Id для одной из станций.";
        }

        if (result.length() == 0)
        {
            result += DataFacade.UpdateTimeTable(from, to, (Integer)objects[2],
                    (Integer)objects[3], updateYandex, updateTutu);
            if ((Boolean)objects[6])
            {
                result += DataFacade.UpdateTimeTable(to, from, (Integer)objects[2],
                        (Integer)objects[3], updateYandex, updateTutu);
            }
        }

        return result;
    }

    @Override
    protected void onPreExecute()
    {
        _dialog.show();
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (result != null && !result.equals(""))
        {
            UIUtils.ShowToast(_activity, result);
        }

        _dialog.dismiss();
        _activity.finish();
    }

    private static boolean isStringEmpty(String string)
    {
        return string == null || string.length() == 0;
    }
}
