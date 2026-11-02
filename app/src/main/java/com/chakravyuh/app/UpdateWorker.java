package com.chakravyuh.app;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONObject;

/** એપ બંધ હોય ત્યારે પણ દર 15 મિનિટે નવું વર્ઝન ચેક કરી નોટિફિકેશન મોકલે; નવી APK હોય તો પહેલેથી ડાઉનલોડ કરી રાખે */
public class UpdateWorker extends Worker {
    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context c = getApplicationContext();
        SharedPreferences p = Updater.prefs(c);
        String url = p.getString("url", "");
        if (url == null || url.isEmpty()) return Result.success();
        try {
            JSONObject v = new JSONObject(new String(Updater.get(url + "version.json?t=" + System.currentTimeMillis()), "UTF-8"));
            int nv = v.optInt("version", 0);
            int bundled = Updater.bundledVersion(c);
            int cur = Math.max(p.getInt("curVersion", 0), Math.max(bundled, p.getInt("updVersion", 0)));
            int last = p.getInt("lastNotified", 0);
            boolean apk = v.optBoolean("apk", false);
            String apkUrl = v.optString("apkUrl", "");
            boolean needApk = apk && nv > bundled;
            if ((nv > cur || needApk) && nv > last) {
                if (needApk && apkUrl.startsWith("https://") && p.getInt("apkReady", 0) < nv) {
                    try {
                        Updater.downloadApk(c, apkUrl, nv);
                    } catch (Exception ignored) {
                        // ડાઉનલોડ ન થાય તો પણ સામાન્ય નોટિફિકેશન મોકલો
                    }
                }
                boolean ready = needApk && p.getInt("apkReady", 0) >= nv;
                String note = "";
                if (v.optJSONArray("notes") != null && v.optJSONArray("notes").length() > 0) note = " — " + v.optJSONArray("notes").optString(0);
                Store.notify(c, Store.CH_UPD, "🔔 ચક્રવ્યુહનું નવું અપડેટ આવ્યું (v" + nv + ")",
                        (ready ? "ઇન્સ્ટોલ કરવા અહીં દબાવો" : "એપ ખોલીને અપડેટ કરો") + note, true);
                p.edit().putInt("lastNotified", nv).apply();
            }
        } catch (Exception e) {
            return Result.retry();
        }
        return Result.success();
    }
}
