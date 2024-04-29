package com.xyshj.machine.facepay;

import android.util.Xml;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes2.dex */
public class ReturnXMLParser {
    public static String parseGetAuthInfoXML(InputStream inputStream) throws Exception {
        XmlPullParser newPullParser = Xml.newPullParser();
        newPullParser.setInput(inputStream, "UTF-8");
        String str = null;
        for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
            if (eventType == 2 && newPullParser.getName().equals("authinfo")) {
                newPullParser.next();
                str = newPullParser.getText();
            }
        }
        return str;
    }
}
