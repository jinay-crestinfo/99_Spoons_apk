package com.shj.device.cardreader;

import com.alibaba.fastjson.parser.JSONLexer;
import com.google.android.exoplayer.text.eia608.ClosedCaptionCtrl;
import com.serotonin.modbus4j.code.ExceptionCode;
import com.serotonin.modbus4j.code.FunctionCode;
import kotlin.jvm.internal.ByteCompanionObject;
import okio.Utf8;

/* loaded from: classes2.dex */
public class MdbFuncUtil {
    static byte[] crc16_tab_h = {0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64, 1, -64, Byte.MIN_VALUE, 65, 1, -64, Byte.MIN_VALUE, 65, 0, -63, -127, 64};
    static byte[] crc16_tab_l = {0, -64, -63, 1, -61, 3, 2, -62, -58, 6, 7, -57, 5, -59, -60, 4, -52, 12, 13, -51, FunctionCode.WRITE_COILS, -49, -50, 14, 10, -54, -53, ExceptionCode.GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND, -55, 9, 8, -56, -40, 24, ClosedCaptionCtrl.MID_ROW_CHAN_2, -39, 27, -37, -38, JSONLexer.EOI, 30, -34, -33, ClosedCaptionCtrl.TAB_OFFSET_CHAN_2, -35, 29, ClosedCaptionCtrl.MISC_CHAN_2, -36, ClosedCaptionCtrl.MISC_CHAN_1, -44, -43, 21, -41, ClosedCaptionCtrl.TAB_OFFSET_CHAN_1, FunctionCode.WRITE_MASK_REGISTER, -42, -46, 18, 19, -45, 17, -47, -48, 16, -16, 48, 49, -15, 51, -13, -14, 50, 54, -10, -9, 55, -11, 53, 52, -12, 60, -4, -3, 61, -1, Utf8.REPLACEMENT_BYTE, 62, -2, -6, 58, 59, -5, 57, -7, -8, 56, 40, -24, -23, ClosedCaptionCtrl.RESUME_DIRECT_CAPTIONING, -21, 43, 42, -22, -18, ClosedCaptionCtrl.ERASE_NON_DISPLAYED_MEMORY, ClosedCaptionCtrl.END_OF_CAPTION, -17, ClosedCaptionCtrl.CARRIAGE_RETURN, -19, -20, ClosedCaptionCtrl.ERASE_DISPLAYED_MEMORY, -28, 36, ClosedCaptionCtrl.ROLL_UP_CAPTIONS_2_ROWS, -27, ClosedCaptionCtrl.ROLL_UP_CAPTIONS_4_ROWS, -25, -26, ClosedCaptionCtrl.ROLL_UP_CAPTIONS_3_ROWS, 34, -30, -29, 35, -31, ClosedCaptionCtrl.BACKSPACE, 32, -32, -96, 96, 97, -95, 99, -93, -94, 98, 102, -90, -89, 103, -91, 101, 100, -92, 108, -84, -83, 109, -81, 111, 110, -82, -86, 106, 107, -85, 105, -87, -88, 104, 120, -72, -71, 121, -69, 123, 122, -70, -66, 126, ByteCompanionObject.MAX_VALUE, -65, 125, -67, -68, 124, -76, 116, 117, -75, 119, -73, -74, 118, 114, -78, -77, 115, -79, 113, 112, -80, 80, -112, -111, 81, -109, 83, 82, -110, -106, 86, 87, -105, 85, -107, -108, 84, -100, 92, 93, -99, 95, -97, -98, 94, 90, -102, -101, 91, -103, 89, 88, -104, -120, 72, 73, -119, 75, -117, -118, 74, 78, -114, -113, 79, -115, 77, 76, -116, 68, -124, -123, 69, -121, 71, 70, -122, -126, 66, 67, -125, 65, -127, Byte.MIN_VALUE, 64};

    public static int isOdd(int i) {
        return i & 1;
    }

    public static int HexToInt(String str) {
        return Integer.parseInt(str, 16);
    }

    public static byte HexToByte(String str) {
        return (byte) Integer.parseInt(str, 16);
    }

    public static String Byte2Hex(Byte b) {
        return String.format("%02x", b).toUpperCase();
    }

    public static String ByteArrToHex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            sb.append(Byte2Hex(Byte.valueOf(b)));
            sb.append("");
        }
        return sb.toString();
    }

    public static String ByteArrToHex(byte[] bArr, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        while (i < i2) {
            sb.append(Byte2Hex(Byte.valueOf(bArr[i])));
            i++;
        }
        return sb.toString();
    }

    public static byte[] HexToByteArr(String str) {
        byte[] bArr;
        int length = str.length();
        if (isOdd(length) == 1) {
            length++;
            bArr = new byte[length / 2];
            str = "0" + str;
        } else {
            bArr = new byte[length / 2];
        }
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = i + 2;
            bArr[i2] = HexToByte(str.substring(i, i3));
            i2++;
            i = i3;
        }
        return bArr;
    }

    public static int CRC_16(byte[] bArr, int i) {
        int i2 = 65535;
        for (int i3 = 0; i3 < i; i3++) {
            i2 ^= bArr[i3];
            for (int i4 = 0; i4 < 8; i4++) {
                i2 = (i2 & 1) == 1 ? (i2 >> 1) ^ 40961 : i2 >> 1;
            }
        }
        return i2;
    }

    public static int calcCrc16(byte[] bArr, int i, int i2) {
        return calcCrc16(bArr, i, i2, 65535);
    }

    public static int calcCrc16(byte[] bArr, int i, int i2, int i3) {
        int i4 = (65280 & i3) >> 8;
        int i5 = i3 & 255;
        int i6 = 0;
        while (i6 < i2) {
            int i7 = (i5 ^ bArr[i + i6]) & 255;
            int i8 = i4 ^ crc16_tab_h[i7];
            i6++;
            i4 = crc16_tab_l[i7];
            i5 = i8;
        }
        return ((i4 & 255) << 8) | (i5 & 255 & 65535);
    }

    public static byte[] ModbusSend(int i, int i2, int i3) {
        int calcCrc16 = calcCrc16(r1, 0, 6);
        byte[] bArr = {1, (byte) i, 0, (byte) i2, (byte) (i3 >> 8), (byte) i3, (byte) calcCrc16, (byte) (calcCrc16 >> 8)};
        return bArr;
    }

    public static byte[] ModbusWrite(int i, int i2, int i3) {
        int calcCrc16 = calcCrc16(r0, 0, 10);
        byte[] bArr = {1, 16, 0, (byte) i, 0, 2, (byte) (i2 >> 8), (byte) i2, (byte) (i3 >> 8), (byte) i3, (byte) calcCrc16, (byte) (calcCrc16 >> 8)};
        return bArr;
    }
}
