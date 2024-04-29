package com.xyshj.machine.facepay;

import android.content.Context;
import com.xyshj.database.setting.AppSetting;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class MerchantInfo {
    public static final String appId = "2019101668450153";
    public static final String appKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggzzzzzzAoIBAQCoyOvwHFmLAmEXMc24XVe88GTYwTkvAabSTjjuXvP9IJKJCihRJ9ROrhtpLq4KXVHmm5Aq/pQriWkbDI/uaSwWBswCbXQGsrWUSW+DIdQRhZcVYymPA+kCVTJMb9eszYPlB/DSR0M4KzFIy/kdZPEf7FNvYiL2NV48FDPwaIjQfaVKETjOqdNMATJCbvwe8Ko0tVOlu8LQ9cwPKzGzPbintrEqdo+IAw9/2P34qrDsDlJsHwNpQn6IDC5m7edl5RorXBhyHwBvoCiHkbEdoKR5pzxdJ42byxDQ6TOv6GWPIzph9wv4e9O9XbxCD7BteaGumYqUUN4QPNjj52AtFCedAgMBAAECggEAGlqtBGPY02y+rjl3jrb8cSL0DCg3fpB6dNID76xh2n9QghdU7j8S1mo3G3hPcozZsfFOVHhFuHqPL8sJWkePu6y6BBMFD9qtoYbJ46bPjyDV02AVaCBLLLMS1H6OJYw9cPCJeLvp0gZkfqJitmVBhSOIb5baMKlqmmTpKzuXST5kDwa4C55pognV2Zc99mHUQAUzE73kCe1krJIDBlhwa2oQxrGy0Caka44MX2HsDCk3gfu77OOLRyG/KGOGlmhMn66pTnEh+OC+sxQPzG/NAgyN5wpbRVpG9O0GWZi4SvQQHOF4E17BfZ0QOx3v/3VWrg+F1DkFuCn73mKvCg1GvQKBgQDfqjHCFetvKvasNwcM9H4qDI112GrdWBuBOaeQ+3fufiqO9kzsSLm4GA9hAL4OTtl4Y1qbc7NWlMAdciDJc/tNgPSHIX7TEKErr/L2Gv2HQpGpwwi0qxV1O9lNjOBfzNV9ZWW6VPmqXYoBUbI3kc10EkZmibnw9Po5Asqy52qe9wKBgQDBL6Bxon33Pkylh8npkkYBH7BOp9dYAUKZPRFcHK4UUQ/xps3cvYLqoEoaUYDTZTYpLu/wKWhs59a0Khh4FVC4E7gsSxX/Vt9gai0bHRCz8mxbsxspgKtK/0KaC1ahBfdNb86P1dJx6ihkWahro6A3brBB19VlNXxQKmX3YR+FCwKBgHH9srhhJYmV0vw4W16N6RMTLLGH6AcBVZtJwA4TK6+gG56bBt9RvV2G3aepkjOGmi1SLaaWlCsnSrZ3KSCi/MwmUfng51s42XuDhAdMADSEh+qGXRR/MUN7iieNCfmpUpFDMVKC5m+elqxPb84EgDqM6dZukRauNC0EMDx31w5HAoGASPNfNwoRX0gbUXbRfG5MGSiKP2mKHCV9rLgu0gXShsGFr885dd3qjgcxdHbKmwlhRnP/D0XTtSxnG2m6C76g2KI5riA8kkuP1qzBk7eKEym8WXRtEWMRTCTQVbNqZywFkyxhaFdqPLIbxqlEvEDxw3z8+iAy6SElbHBWSPBYIrkCgYEAvYmI3ogkQCgYSwZRNJQt4eHBYm9ZOHW+1LyMROqSh3i0aeJjKI4kUAoeIpMuI9smucD1CrBwi6x3SPVSvIbhY/qMqmoN4V6p8uHLkYJw9IkWvz5mNxcOF8FUbhLtHcGOPCxbp48ISRNP1KaCKI0NvnGgLX7az0JmRv1G7TIq8as=";
    public static final String partnerId = "2088511455976890";

    public static Map mockInfo(Context context) {
        HashMap hashMap = new HashMap();
        hashMap.put("partnerId", "2088431748526842");
        hashMap.put("merchantId", partnerId);
        hashMap.put("appId", appId);
        String machineId = AppSetting.getMachineId(context, null);
        if (machineId == null) {
            machineId = "0000000000";
        }
        hashMap.put("deviceNum", machineId);
        hashMap.put("brandCode", "xinyuan");
        hashMap.put("deviceMac", getMacAddress());
        return hashMap;
    }

    public static String getMacAddress() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            NetworkInterface byName = NetworkInterface.getByName("eth1");
            if (byName == null) {
                byName = NetworkInterface.getByName("wlan0");
            }
            if (byName == null) {
                return "02:00:00:00:00:02";
            }
            for (byte b : byName.getHardwareAddress()) {
                stringBuffer.append(String.format("%02X:", Byte.valueOf(b)));
            }
            if (stringBuffer.length() > 0) {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            }
            return stringBuffer.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
    }
}
