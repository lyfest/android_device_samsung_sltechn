/*
 * Copyright (C) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cyanogenmod.radioworkaround;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.lang.Thread;
import java.lang.reflect.Method;

public class RadioWorkaround extends Activity {

    private static final String LOG_TAG = "RadioWorkaround";

    /**
     * Get the value for the given key.
     *
     * @return if the key isn't found, return def if it isn't null,
     * or an empty string otherwise
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    private static String getSystemProperties(String key, String def)
        throws IllegalArgumentException
    {
        String ret = def;
        try {
            Class<?> SystemProperties = Class.forName("android.os.SystemProperties");
            //Parameters Types
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = { String.class, String.class };
            Method get = SystemProperties.getMethod("get", paramTypes);
            //Parameters
            Object[] params = { key, def };
            ret = (String) get.invoke(SystemProperties, params);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
        }
        return ret;
    }

    private void setPhoneRadioPower(boolean power) {
        /*
        import com.android.internal.telephony.PhoneFactory;
        import com.android.internal.telephony.Phone;
        Phone phone = PhoneFactory.getDefaultPhone();
        phone.setRadioPower(power);
        */
        try {
            Class<?> PhoneFactory = Class.forName("com.android.internal.telephony.PhoneFactory");
            Method getDefaultPhone = PhoneFactory.getMethod("getDefaultPhone");
            Object phone = getDefaultPhone.invoke(null);

            Method setRadioPower = phone.getClass().getMethod("setRadioPower", new Class[]{ boolean.class });
            setRadioPower.invoke(phone, new Object[]{ new Boolean(power) });

        } catch (Exception e) {
            Log.d(LOG_TAG, "setPhoneRadioPower", e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            final String prop = getSystemProperties("persist.radio.workaround",
                                                    "true");

            Log.d(LOG_TAG, "try to off-on Radio (" + prop + ")");

            if ("true".equals(prop) || "on".equals(prop)) {
                setPhoneRadioPower(false);
                Thread.sleep(500);
                setPhoneRadioPower(true);
            }

            finishRadioWorkaround();
        } catch (Exception e) {
            Log.e(LOG_TAG, "failed to off-on Radio", e);
            finishRadioWorkaround();
        }
    }


    private void finishRadioWorkaround() {
        finish();
    }
}
