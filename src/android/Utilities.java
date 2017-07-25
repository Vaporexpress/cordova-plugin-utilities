package com.vaporexpress.utilities;

import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class Utilities extends CordovaPlugin {
    protected static final String LOG_TAG = "Utilities";
    private Context ctx;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        ctx = this.cordova.getActivity().getApplicationContext();

        if (action.equals("getHtmlSource")) {
            
            JSONObject r = new JSONObject();
            r.put("result", this.htmlSource());
            callbackContext.success(r);
        if (action.equals("openURL")) {
            JSONObject r = new JSONObject();
            r.put("result", "Test OK");
            callbackContext.success(r);            
        } else {
            return false;
        }
        return true;
    }

    private String getHtmlSource() {
        return "<div>Test</div>";
    }

}
