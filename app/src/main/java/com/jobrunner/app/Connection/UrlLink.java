package com.jobrunner.app.Connection;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

public class UrlLink {
    private JSONParser2 jsonParser;

    public UrlLink() {
        jsonParser = new JSONParser2();
    }

    public static String url_path = "http://192.168.0.226/jobrunnerv2/api/employee/";

    public static String getVersion_URL = url_path + "getAppVersion.php";


    public JSONObject getVersion(String version) {
        HashMap<String, String> params = new HashMap<>();
        params.put("version",version);
        return jsonParser.makeHttpRequest(getVersion_URL, "POST", params);
    }
}
