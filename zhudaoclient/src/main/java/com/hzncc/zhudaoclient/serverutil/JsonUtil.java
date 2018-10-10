package com.hzncc.zhudaoclient.serverutil;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/7/26.
 */

public class JsonUtil {

    public static int getIntByObj(JSONObject obj, String key) {
        int result = -1;
        try {
            if (!obj.isNull(key)) {
                result = (int) obj.get(key);
            }
        } catch (JSONException e) {
            Log.e("JsonUtil", e.toString());
        }
        return result;
    }

    public static String getStringByObj(JSONObject obj, String key) {
        String result = "";
        try {
            if (!obj.isNull(key)) {
                result = (String) obj.get(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJsonObjByString(String str) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static String getRequestString(String request, HashMap<String, String> params) {
        JSONObject object = new JSONObject();
        try {
            object.put("request", request);
            Set<String> set = params.keySet();
            for (String key : set) {
                object.put(key, params.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String getRequestMulFileString(String request, List<String> files) {
        JSONObject object = new JSONObject();
        try {
            object.put("request", request);
            object.put("type", SocketManager.REPLY_NORMAL_MSG);
            JSONArray array = new JSONArray();
            for (String file : files) {
                array.put(file);
            }
            object.put("files", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String getReplyString(int rel, int type, String request, JSONObject msg) {
        JSONObject object = new JSONObject();
        try {
            object.put("rel", rel);
            object.put("type", type);
            object.put("request", request);
            object.put("msg", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String getAction(String result) {
        return getStringByObj(getJsonObjByString(result), "request");
    }


    public static String getOtherIp(String result) {
        return getStringByObj(getJsonObjByString(result), "from_ip");
    }

    public static boolean isSuccess(String result) {
        return getIntByObj(getJsonObjByString(result), "rel") == 0;
    }

    public static int getType(String result) {
        return getIntByObj(getJsonObjByString(result), "type");
    }

    public static String getRequestFilename(String result) {
        JSONObject object = getJsonObjByString(result);
        return getStringByObj(object, "file_name");
    }

    public static List<String> getRequestFilenames(String result) {
        List<String> list = new ArrayList<>();
        JSONObject object = getJsonObjByString(result);
        try {
            JSONArray array = object.getJSONArray("files");
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static JSONObject getRepylFileMessage(String filename) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("file_name", filename);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static String getFileName(String result) {
        String fileName = "";
        try {
            JSONObject object = new JSONObject(result);
            JSONObject msg = object.getJSONObject("msg");
            fileName = getStringByObj(msg, "file_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static JSONObject getObjectByFiles(File[] files) {
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (File file : files) {
                jsonArray.put(file.getAbsolutePath());
            }
            object.put("files", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static List<String> getFilesByObject(String result) {
        List<String> files = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(result).getJSONObject("msg");
            JSONArray jsonArray = object.getJSONArray("files");
            for (int i = 0; i < jsonArray.length(); i++) {
                files.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return files;
    }

}
