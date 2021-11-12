package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

public interface HttpResponseHandler {
    void onStringRequestSuccess(String response);
    void onJsonObjectRequestSuccess(JSONObject response);
    void onJsonArrayRequestSuccess(JSONArray response);

    void onFail(String response);
}
