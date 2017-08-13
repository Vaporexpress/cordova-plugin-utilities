package com.vaporexpress.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vaporexpress.catastro.R;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class echoes a string called from JavaScript.
 */
public class Utilities extends CordovaPlugin {
    protected static final String LOG_TAG = "Utilities";
    private Context webviewctx;
    private WebSettings settings;
    private static AlertDialog alertDialog;
    private CallbackContext callbackContext;
    private String WRONG_PARAMS = "Wrong parameters.";
    private String UNKNOWN_ERROR = "Unknown error.";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        final CordovaWebView cwv = webView;
        try{
            cwv.getEngine().getView().post(new Runnable() {
                @Override
                public void run() {
                    settings = ((WebView) cwv.getEngine().getView()).getSettings();
                }
            });
            //settings = ((WebView) webView.getEngine().getView()).getSettings();
        }catch (Exception error){
            settings = null;
        }
    }
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        webviewctx = webView.getContext();
        final CallbackContext callback = callbackContext;
        JSONObject r;

        if (action.equals("getHtmlSource")) {
            r = new JSONObject();
            r.put("result", this.getHtmlSource());
            callback.success(r);
        } else if (action.equals("getMimeType")) {
            String url = args.getString(0);
            r = new JSONObject();
            r.put("result", this.getMimeType(url));
            callback.success(r);
        } else if (action.equals("dialog")) {
            String title = args.getString(0);
            String message = args.getString(1);
            this.dialog(title, message);
        } else if (action.equals("getContentURI")) {
            final JSONObject queryArgs = args.getJSONObject(0);
            if (queryArgs == null) {
                callback.error(WRONG_PARAMS);
                return false;
            }
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    runQuery(queryArgs, callback);
                }
            });
            return true;
        } else {
            return false;
        }
        return true;

    }

    private String getHtmlSource() {
        return "<div>Test</div>";
    }

    private String getMimeType(String url) {
        String content = "";
        try {
            //String ua=new WebView(this.webView.getContext()).getSettings().getUserAgentString();
            String ua=settings.getUserAgentString();
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("HEAD");
            conn.setRequestProperty("User-Agent", ua);
            int status = conn.getResponseCode();
            //if ((status == HttpURLConnection.HTTP_OK ) || (status == HttpURLConnection.HTTP_PARTIAL)) {
                content = conn.getHeaderField("Content-Type");
                conn.disconnect();
            //}
        } catch (IOException e) {
            LOG.w(LOG_TAG, "No mimetype found");
            return "";
        }
        return content;
        //return "";
    }
    private void dialog(String title, String message) {
        if (alertDialog != null && alertDialog.isShowing()) {
            // A dialog is already open, wait for it to be dismissed, do nothing
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(webviewctx, R.style.AlertDialogCustom);
            final EditText edittext = new EditText(webviewctx);
            edittext.setSingleLine();
            FrameLayout container = new FrameLayout(webviewctx);
            FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = webviewctx.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = webviewctx.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            edittext.setLayoutParams(params);
            container.addView(edittext);
            edittext.requestFocus();

            // Custom Title
            LayoutInflater inflater = (LayoutInflater)webviewctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //View customTitle = inflater.inflate(R.layout.custom_title, null);
            View customTitle = inflater.inflate(R.layout.custom_title, null);
            TextView titlecus = (TextView) customTitle.findViewById(R.id.title);
            titlecus.setText(title);
            alert.setCustomTitle(customTitle);

            //alert.setTitle(title);
            alert.setView(container);
            alert.setMessage(message);

            alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    //String YouEditTextValue = edittext.getText().toString();
                    callbackContext.success(edittext.getText().toString());
                }
            });

            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                    String nada = null;
                    callbackContext.error("cancelled");
                }
            });

            alertDialog = alert.create();
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alertDialog.show();
        }
    }
    private void runQuery(JSONObject queryArgs, CallbackContext callback) {
        Uri contentUri = null;
        String[] projection = null;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        JSONArray resultJSONArray;

        try {
            if (!queryArgs.isNull("contentUri")) {
                contentUri = Uri.parse(queryArgs.getString("contentUri"));
            } else {
                callback.error(WRONG_PARAMS);
                return;
            }
        } catch (JSONException e) {
            callback.error(WRONG_PARAMS);
            return;
        }
        if (contentUri == null) {
            callback.error(WRONG_PARAMS);
            return;
        }

        try {
            if (!queryArgs.isNull("projection")) {
                JSONArray projectionJsonArray = queryArgs.getJSONArray("projection");
                projection = new String[projectionJsonArray.length()];
                for (int i = 0; i < projectionJsonArray.length(); i++) {
                    projection[i] = projectionJsonArray.getString(i);
                }
            }
        } catch (JSONException e1) {
            projection = null;
        }

        try {
            if (!queryArgs.isNull("selection")) {
                selection = queryArgs.getString("selection");
            }
        } catch (JSONException e1) {
            selection = null;
        }

        try {
            if (!queryArgs.isNull("selectionArgs")) {
                JSONArray selectionArgsJsonArray = queryArgs.getJSONArray("selectionArgs");
                selectionArgs = new String[selectionArgsJsonArray.length()];
                for (int i = 0; i < selectionArgsJsonArray.length(); i++) {
                    selectionArgs[i] = selectionArgsJsonArray.getString(i);
                }
            }
        } catch (JSONException e1) {
            selectionArgs = null;
        }

        try {
            if (!queryArgs.isNull("sortOrder")) {
                sortOrder = queryArgs.getString("sortOrder");
            }
        } catch (JSONException e1) {
            sortOrder = null;
        }

        // Permisions



        // run query
        Cursor result=null;
        try {
            result = cordova.getActivity().getContentResolver().query(contentUri, projection, selection, selectionArgs, sortOrder);
        }
        catch (Exception e) {
            //LOG.d(LOG_TAG, e.getStackTrace().toString());
            LOG.e(LOG_TAG, "exception", e);
        }
        resultJSONArray = new JSONArray();

        // Some providers return null if an error occurs, others throw an exception
        if(result == null) {
            callback.error(UNKNOWN_ERROR);
        } else {
            try {
                while (result != null && result.moveToNext()) {
                    JSONObject resultRow = new JSONObject();
                    int colCount = result.getColumnCount();
                    for (int i = 0; i < colCount; i++) {
                        try {
                            switch (result.getType(i)) {
                                case 0: // Null
                                    resultRow.put(result.getColumnName(i), null);
                                    break;
                                case 1: // Integer
                                    resultRow.put(result.getColumnName(i), result.getInt(i));
                                    break;
                                case 2: // Float
                                    resultRow.put(result.getColumnName(i), result.getFloat(i));
                                    break;
                                case 3: // String
                                    resultRow.put(result.getColumnName(i), result.getString(i));
                                    break;
                                case 4: // Blob
                                    resultRow.put(result.getColumnName(i), result.getBlob(i).toString());
                                    break;
                            }
                        } catch (JSONException e) {
                            resultRow = null;
                        }
                    }
                    resultJSONArray.put(resultRow);
                }
            } finally {
                if(result != null) result.close();
            }
            callback.success(resultJSONArray);
        }
    }
}
