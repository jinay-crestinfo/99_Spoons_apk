package com.shj.setting.mainSettingItem;

import android.content.Context;
import com.shj.setting.R;
import com.xyshj.database.setting.SettingType;

/* loaded from: classes2.dex */
public class SettingTypeName {
    public static int getParentId(int i) {
        switch (i) {
            case 102:
            case 103:
            case 104:
                return 0;
            default:
                return -1;
        }
    }

    public static String getSettingName(Context context, int i) {
        switch (i) {
            case 102:
                return context.getResources().getString(R.string.lab_machineid);
            case 103:
                return context.getResources().getString(R.string.lab_devicetype);
            case 104:
                return context.getResources().getString(R.string.lab_machinetype);
            case 105:
                return context.getResources().getString(R.string.lab_paytype);
            case 106:
                return context.getResources().getString(R.string.activity);
            case 107:
                return context.getResources().getString(R.string.website);
            case 108:
                return context.getResources().getString(R.string.activity_picture);
            case 109:
                return context.getResources().getString(R.string.shopping_interface_style);
            case 110:
                return context.getResources().getString(R.string.quantity_of_merchandise_per_page);
            case 111:
                return context.getResources().getString(R.string.style);
            case 112:
                return context.getResources().getString(R.string.lab_volume);
            case 113:
                return context.getResources().getString(R.string.lab_volume_advertisement);
            case 114:
                return context.getResources().getString(R.string.lab_volume_broadcast);
            case 115:
                return context.getResources().getString(R.string.lab_enable_pricecheck);
            case 116:
                return context.getResources().getString(R.string.lab_tips);
            case 117:
                return context.getResources().getString(R.string.swallowing_money_time);
            case 118:
                return context.getResources().getString(R.string.disposal_of_surplus_amount);
            case 119:
                return context.getResources().getString(R.string.setting_coin_system);
            case 120:
                return context.getResources().getString(R.string.coins_finve_cents);
            case SettingType.COINS_ONE_YUAN /* 121 */:
                return context.getResources().getString(R.string.coins_one_yuan);
            case 122:
                return context.getResources().getString(R.string.price_setting);
            case 123:
            case SettingType.INVENTORY_SETTING_SINGLE /* 131 */:
            case SettingType.CARGO_CAPACITY_SETTING /* 136 */:
            case SettingType.CARGO_CODE_SETTING /* 140 */:
            case 146:
            case 150:
            case SettingType.ELECTROMAGNETIC_LOCK_ON_TIME_SIGLE /* 274 */:
            case SettingType.NEW_LINKAGE_SYNCHRONIZATION_SIGLE /* 329 */:
                return context.getResources().getString(R.string.single);
            case SettingType.PRICE_SETTING_WHOLE_LAYER /* 124 */:
            case 132:
            case SettingType.CARGO_CAPACITY_WHOLE_LAYER /* 137 */:
            case 143:
            case 147:
            case 151:
            case 154:
            case SettingType.CARGO_CODE_WHOLE_LAYER /* 268 */:
            case SettingType.ELECTROMAGNETIC_LOCK_ON_TIME_WHOLE_LAYER /* 275 */:
            case SettingType.NEW_LINKAGE_SYNCHRONIZATION_LAYER /* 330 */:
                return context.getResources().getString(R.string.whole_layer);
            case 125:
            case SettingType.INVENTORY_WHOLE_MACHINE /* 133 */:
            case SettingType.CARGO_CAPACITY_WHOLE_MACHINE /* 138 */:
            case SettingType.CARGO_CODE_WHOLE_MACHINE /* 141 */:
            case 144:
            case 148:
            case 152:
            case 155:
            case SettingType.ELECTROMAGNETIC_LOCK_ON_TIME_WHOLE_MACHINE /* 276 */:
            case 331:
                return context.getResources().getString(R.string.whole_machine);
            case 126:
                return context.getResources().getString(R.string.synchronous_backstage);
            case 127:
                return context.getResources().getString(R.string.lab_downloadgoodsimage);
            case 128:
                return context.getResources().getString(R.string.lab_downloadgoodsinfo);
            case SettingType.COMMODITY_ONE_BUTTON_SETUP /* 129 */:
                return context.getResources().getString(R.string.lab_onekeyset);
            case SettingType.INVENTORY /* 130 */:
                return context.getResources().getString(R.string.setting_inventory);
            case SettingType.FULL_DELIVERY /* 134 */:
                return context.getResources().getString(R.string.full_confirm);
            case SettingType.CARGO_CAPACITY /* 135 */:
                return context.getResources().getString(R.string.cargo_capacity);
            case SettingType.CARGO_CODE /* 139 */:
                return context.getResources().getString(R.string.cargo_code);
            case SettingType.DROP_INSPECTION /* 142 */:
                return context.getResources().getString(R.string.drop_inspection);
            case 145:
                return context.getResources().getString(R.string.belt_setting);
            case 149:
                return context.getResources().getString(R.string.circle_setting);
            case 153:
                return context.getResources().getString(R.string.cargo_card_setup);
            case 156:
                return context.getResources().getString(R.string.shipment_test);
            case 157:
                return context.getResources().getString(R.string.license_authentication);
            case 158:
                return context.getResources().getString(R.string.banknote_diagnosis);
            case 159:
                return context.getResources().getString(R.string.coin_device_diagnostics);
            case 160:
                return context.getResources().getString(R.string.card_cargo_clearance);
            case 161:
                return context.getResources().getString(R.string.gear_box_fault_clearing);
            case 162:
                return context.getResources().getString(R.string.lift_fault_clearance);
            case 163:
                return context.getResources().getString(R.string.card_cargo_turn_to_4_circle_clearance);
            case 164:
                return context.getResources().getString(R.string.network_state);
            case 165:
                return context.getResources().getString(R.string.open_the_web_page);
            case 166:
                return context.getResources().getString(R.string.send_and_receive_normal);
            case 167:
                return context.getResources().getString(R.string.querying_quantity_of_goods);
            case 168:
                return context.getResources().getString(R.string.track_test);
            case 169:
                return context.getResources().getString(R.string.cargo_loop_test);
            case SettingType.RESTORE_FACTORY_SETTINGS /* 170 */:
                return context.getResources().getString(R.string.restore_factory_settings);
            case SettingType.RESET_LOGIN_PASSWORD /* 171 */:
                return context.getResources().getString(R.string.reset_login_password);
            case 172:
                return context.getResources().getString(R.string.file_management);
            case SettingType.PRINTER /* 173 */:
                return context.getResources().getString(R.string.printer);
            case SettingType.CARD_READER /* 174 */:
                return context.getResources().getString(R.string.card_reader);
            case SettingType.SCAVENGING_WHARF /* 175 */:
                return context.getResources().getString(R.string.scavenging_wharf);
            case 176:
                return context.getResources().getString(R.string.human_body_equipment);
            case 177:
                return context.getResources().getString(R.string.microwave_oven);
            case 178:
                return context.getResources().getString(R.string.temperature_controller_setting);
            case 179:
                return context.getResources().getString(R.string.compressor_settings);
            case 180:
                return context.getResources().getString(R.string.main_control_equipment);
            case 181:
            case SettingType.LIGHTING_CONTROL_TR /* 353 */:
                return context.getResources().getString(R.string.lighting_control);
            case 182:
                return context.getResources().getString(R.string.banknote);
            case 183:
                return context.getResources().getString(R.string.banknote_give_change);
            case 184:
                return context.getResources().getString(R.string.banknote_receive_face_value);
            case 185:
                return context.getResources().getString(R.string.banknote_collecting_mode);
            case SettingType.BANKNOTE_CHANGE_OF_MONEY /* 186 */:
                return context.getResources().getString(R.string.banknote_change_of_money);
            case 187:
                return context.getResources().getString(R.string.coin_changer);
            case SettingType.COIN_QUERY /* 188 */:
                return context.getResources().getString(R.string.coin_query);
            case 189:
                return context.getResources().getString(R.string.coin_detector);
            case SettingType.DAILY_SALES /* 190 */:
                return context.getResources().getString(R.string.daily_sales);
            case SettingType.MONTHLY_SALES /* 191 */:
                return context.getResources().getString(R.string.monthly_sales);
            case 192:
                return context.getResources().getString(R.string.annual_sales);
            case 193:
                return context.getResources().getString(R.string.total_transaction_volume);
            case SettingType.GOODS_SALES_INFORMATION /* 194 */:
                return context.getResources().getString(R.string.goods_sales_information);
            case SettingType.CLEAR_LOCAL_SALES_RECORDS /* 195 */:
                return context.getResources().getString(R.string.clear_local_sales_records);
            case SettingType.TRANSACTION_FLOW_QUERY /* 196 */:
                return context.getResources().getString(R.string.transaction_flow_query);
            case SettingType.APPTYPE /* 197 */:
            case 198:
            case 200:
            case 201:
            case 202:
            case SettingType.QUICK_PASS_AREA /* 203 */:
            case SettingType.QUICK_PASS_SERIAL_PORT /* 204 */:
            case 205:
            case 215:
            case SettingType.REMINDER /* 217 */:
            case SettingType.CIVILIAN_SERVICE /* 218 */:
            case SettingType.ACTIVITY_BULLETIN /* 219 */:
            case 221:
            case SettingType.HIDE_HELP_QRCODE /* 270 */:
            case SettingType.ENABLE_SHOPPING_CART_FACE /* 291 */:
            case SettingType.PAYMENT_METHOD_SCAN /* 316 */:
            case SettingType.DRUG_BOX_MENU_NAME_LEFT /* 342 */:
            case SettingType.DRUG_BOX_MENU_NAME_RIGHT /* 343 */:
            case SettingType.MERCHANT_NUMBER /* 344 */:
            default:
                return null;
            case 199:
                return context.getResources().getString(R.string.auto_upgrade);
            case SettingType.PAYMENT_METHOD_CASH /* 206 */:
                return context.getResources().getString(R.string.lab_paytype_cash);
            case SettingType.PAYMENT_METHOD_WEIXIN /* 207 */:
                return context.getResources().getString(R.string.lab_paytype_weixin);
            case SettingType.PAYMENT_METHOD_ZFB /* 208 */:
                return context.getResources().getString(R.string.lab_paytype_zfb);
            case SettingType.PAYMENT_METHOD_YL /* 209 */:
                return context.getResources().getString(R.string.lab_paytype_ylsm);
            case SettingType.PAYMENT_METHOD_YLX /* 210 */:
                return context.getResources().getString(R.string.lab_paytype_ylsmx);
            case 211:
                return context.getResources().getString(R.string.lab_paytype_ylsf);
            case 212:
                return context.getResources().getString(R.string.lab_paytype_iccard);
            case 213:
                return context.getResources().getString(R.string.lab_paytype_jdzf);
            case 214:
                return context.getResources().getString(R.string.promotion_model);
            case SettingType.MONETARY_SYMBOL /* 216 */:
                return context.getResources().getString(R.string.monetary_symbol);
            case 220:
                return context.getResources().getString(R.string.auto_to_activity);
            case 222:
                return context.getResources().getString(R.string.lab_enable_shopcar);
            case 223:
                return context.getResources().getString(R.string.enable_face_pay);
            case 224:
                return context.getResources().getString(R.string.system_data_export);
            case 225:
                return context.getResources().getString(R.string.system_data_import);
            case 226:
                return context.getResources().getString(R.string.decimal_point_digit);
            case 227:
                return context.getResources().getString(R.string.closing_time_of_pick_up_port);
            case SettingType.CONNECTING_ELEVATOR /* 228 */:
                return context.getResources().getString(R.string.connecting_elevator);
            case 229:
                return context.getResources().getString(R.string.frequency_adjustment_shipment_testing);
            case 230:
                return context.getResources().getString(R.string.delivery_detection_sensitivity);
            case 231:
                return context.getResources().getString(R.string.lift_layer_location);
            case SettingType.MICROWAVE_OVEN_POSITIONING /* 232 */:
                return context.getResources().getString(R.string.microwave_oven_positioning);
            case SettingType.MANUAL_POSITIONING_OF_BOXES /* 233 */:
                return context.getResources().getString(R.string.manual_positioning_of_boxes);
            case 234:
                return context.getResources().getString(R.string.sensor_status_box);
            case 235:
                return context.getResources().getString(R.string.heating_time_of_box);
            case 236:
                return context.getResources().getString(R.string.parameter_setting_temperature_controller);
            case 237:
                return context.getResources().getString(R.string.cargo_way_mode);
            case 238:
                return context.getResources().getString(R.string.motor_stop_threshold);
            case 239:
                return context.getResources().getString(R.string.short_circuit_threshold_motor);
            case 240:
                return context.getResources().getString(R.string.linkage_synchronization_time);
            case 241:
                return context.getResources().getString(R.string.cargo_linkage);
            case 242:
                return context.getResources().getString(R.string.lift_speed_setting);
            case 243:
                return context.getResources().getString(R.string.lift_test);
            case 244:
                return context.getResources().getString(R.string.lift_sensitivity_settings);
            case 245:
                return context.getResources().getString(R.string.closing_time_anti_theft_board);
            case 246:
                return context.getResources().getString(R.string.box_spacing_lunch_box);
            case 247:
                return context.getResources().getString(R.string.institutional_function_testing);
            case 248:
                return context.getResources().getString(R.string.android_setting);
            case 249:
                return context.getResources().getString(R.string.backups_shj_app);
            case 250:
                return context.getResources().getString(R.string.full_screen_advertising);
            case 251:
                return context.getResources().getString(R.string.half_screen_advertising);
            case 252:
                return context.getResources().getString(R.string.delete_full_screen_advertising);
            case 253:
                return context.getResources().getString(R.string.delete_half_screen_advertising);
            case 254:
                return context.getResources().getString(R.string.testing_lunch_box);
            case 255:
                return context.getResources().getString(R.string.copy_log_u_disk);
            case 256:
                return context.getResources().getString(R.string.box_speed_settings);
            case 257:
                return context.getResources().getString(R.string.box_sensitivity_settings);
            case 258:
                return context.getResources().getString(R.string.testing_box_lunch_machine);
            case 259:
                return context.getResources().getString(R.string.lab_tem_dis);
            case SettingType.SHOW_BALANCE /* 260 */:
                return context.getResources().getString(R.string.lab_change_dis);
            case SettingType.AD_PLAYER /* 261 */:
                return context.getResources().getString(R.string.ad_player);
            case SettingType.SHOW_CODE_ZERO /* 262 */:
                return context.getResources().getString(R.string.show_code_zero);
            case SettingType.CALL_PHONE /* 263 */:
                return context.getResources().getString(R.string.call_phone);
            case SettingType.DOWNLOAD_MATERIAL_FILE /* 264 */:
                return context.getResources().getString(R.string.download_material_file);
            case SettingType.PAYMENT_METHOD_AGGREGATE_CODE /* 265 */:
                return context.getResources().getString(R.string.payment_method_aggregate_code);
            case SettingType.NETWORKING_TIMEOUT /* 266 */:
                return context.getResources().getString(R.string.networking_timeout);
            case SettingType.GOODWAY_SELECTION /* 267 */:
                return context.getResources().getString(R.string.cargo_routing);
            case SettingType.KEFU_PHONE /* 269 */:
                return context.getResources().getString(R.string.kefu_phone);
            case SettingType.SETTING_UP_HUMIDIFIER /* 271 */:
                return context.getResources().getString(R.string.setting_up_humidifier);
            case SettingType.FAULT_TEMPERATURE_PROBE /* 272 */:
                return context.getResources().getString(R.string.fault_temperature_probe);
            case SettingType.ELECTROMAGNETIC_LOCK_ON_TIME /* 273 */:
                return context.getResources().getString(R.string.electromagnetic_lock_on_time);
            case SettingType.SETTING_SYSTEM_TIME /* 277 */:
                return context.getResources().getString(R.string.setting_system_time);
            case SettingType.REPLENISHMENT_OTHER /* 278 */:
                return context.getResources().getString(R.string.replenishment_other);
            case SettingType.CARGO_MANAGEMENT_OTHER /* 279 */:
                return context.getResources().getString(R.string.cargo_management_other);
            case SettingType.GOODS_INFO_LIST /* 280 */:
                return context.getResources().getString(R.string.goods_info_list);
            case 281:
                return context.getResources().getString(R.string.yyt_ad);
            case SettingType.LANCHER_SETTINGS /* 282 */:
                return context.getResources().getString(R.string.lancher_settings);
            case SettingType.NUMBER_CABINETS /* 283 */:
                return context.getResources().getString(R.string.number_cabinets);
            case SettingType.SOUND_SETTING_ADVERTISEMENT_TIME1 /* 284 */:
                return context.getResources().getString(R.string.sound_setting_advertisement_time1);
            case SettingType.SOUND_SETTING_ADVERTISEMENT_TIME2 /* 285 */:
                return context.getResources().getString(R.string.sound_setting_advertisement_time2);
            case SettingType.SOUND_SETTING_VOICE_TIME1 /* 286 */:
                return context.getResources().getString(R.string.sound_setting_voice_time1);
            case SettingType.SOUND_SETTING_VOICE_TIME2 /* 287 */:
                return context.getResources().getString(R.string.sound_setting_voice_time2);
            case SettingType.DOWNLOAD_INSTRUCTIONS_PICTURES /* 288 */:
                return context.getResources().getString(R.string.download_instructions_pictures);
            case SettingType.MAIN_BOARD_SEQUENCE_NUMBER /* 289 */:
                return context.getResources().getString(R.string.main_board_sequence_number);
            case SettingType.LOG_SETTING /* 290 */:
                return context.getResources().getString(R.string.log_look);
            case SettingType.BOX_RICE_MACHINE_CABINET_SETTING /* 292 */:
                return context.getResources().getString(R.string.box_rice_machine_cabinet_setting);
            case SettingType.SHOW_MARKETING /* 293 */:
                return context.getResources().getString(R.string.show_marketing);
            case SettingType.SHOW_SHOPPING_BUTTON /* 294 */:
                return context.getResources().getString(R.string.show_shopping_button);
            case SettingType.SOFT_MANAGE /* 295 */:
                return context.getResources().getString(R.string.soft_manage);
            case SettingType.PAY_QRCODE_LEVEL /* 296 */:
                return context.getResources().getString(R.string.pay_qrcode_level);
            case SettingType.HEART_DIALOG /* 297 */:
                return context.getResources().getString(R.string.heart_dialog);
            case SettingType.CAMERA_TEST /* 298 */:
                return context.getResources().getString(R.string.is_it_enabled);
            case SettingType.CAMERA_AUTO_TAKE /* 299 */:
                return context.getResources().getString(R.string.camera_takeinterval);
            case 300:
                return context.getResources().getString(R.string.camera_test);
            case SettingType.DOWNLOAD_MERCHANDISE_DETAILPICTURES /* 301 */:
                return context.getResources().getString(R.string.download_merchandise_detailpictures);
            case SettingType.DEVICE_FACE_SN /* 302 */:
                return context.getResources().getString(R.string.device_face_sn);
            case SettingType.DEVICE_SCAN_PORT_ADDERS_YR /* 303 */:
                return context.getResources().getString(R.string.device_scan_yr);
            case SettingType.ALWAYS_HEATING /* 304 */:
                return context.getResources().getString(R.string.always_heating);
            case SettingType.OPEN_GRID_MACHINE_GRID /* 305 */:
                return context.getResources().getString(R.string.open_grid_machine_grid);
            case SettingType.OPEN_GRID_MACHINE_GRID_ALL /* 306 */:
                return context.getResources().getString(R.string.open_grid_machine_grid_all);
            case 307:
                return context.getResources().getString(R.string.open_grid_machine_grid_cabinet);
            case 308:
                return context.getResources().getString(R.string.open_grid_machine_grid_layer);
            case SettingType.LIGHT_INSPECTION_STATUS_QUERY /* 309 */:
                return context.getResources().getString(R.string.light_inspection_status_query);
            case SettingType.CALCULATED_INVENTORY /* 310 */:
                return context.getResources().getString(R.string.calculated_inventory);
            case SettingType.MAINCONTOL_PROGRAM_UPDATE /* 311 */:
                return context.getResources().getString(R.string.maincontol_program_update);
            case SettingType.SHELF_DRIVING_PROGRAM_UPDATE /* 312 */:
                return context.getResources().getString(R.string.shelf_driving_program_update);
            case SettingType.SHELF_DRIVING_VERSION_QUERY /* 313 */:
                return context.getResources().getString(R.string.shelf_driving_version_query);
            case SettingType.SCAN_BARCODE_REPLENISHMENT /* 314 */:
                return context.getResources().getString(R.string.scan_barcode_replenishment);
            case SettingType.PAYMENT_METHOD_JF /* 315 */:
                return context.getResources().getString(R.string.payment_method_jf);
            case SettingType.DOWNLOAD_SIGNAL_GOODS_PIC /* 317 */:
                return context.getResources().getString(R.string.download_signal_goods_pic);
            case SettingType.TOP_LIGHT_CONTROL /* 318 */:
                return context.getResources().getString(R.string.top_light_control);
            case SettingType.ID_CARD_READER_TEST /* 319 */:
                return context.getResources().getString(R.string.id_card_reader_test);
            case SettingType.LIGHT_BOX_ROLLING_INTERVAL /* 320 */:
                return context.getResources().getString(R.string.light_box_rolling_interval);
            case SettingType.MICROPHONE_TEST /* 321 */:
                return context.getResources().getString(R.string.microphone_test);
            case SettingType.REBOOT_ANDROID_SYSTEM /* 322 */:
                return context.getResources().getString(R.string.reboot_android_system);
            case SettingType.LIGHTBOX_CANVAS /* 323 */:
                return context.getResources().getString(R.string.lightbox_canvas);
            case SettingType.HIGH_TIME_METER_TEST /* 324 */:
                return context.getResources().getString(R.string.high_time_meter_test);
            case SettingType.FACEPAY_TYPE /* 325 */:
                return context.getResources().getString(R.string.facepay_type);
            case SettingType.SURVEILLANCE_CAMERA_PID_VID /* 326 */:
                return context.getResources().getString(R.string.surveillance_camera_pid_vid);
            case SettingType.HIGH_TIME_METER_PID_VID /* 327 */:
                return context.getResources().getString(R.string.high_time_meter_pid_vid);
            case SettingType.NEW_LINKAGE_SYNCHRONIZATION_TIME /* 328 */:
                return context.getResources().getString(R.string.linkage_synchronization_time);
            case 332:
                return context.getResources().getString(R.string.coin_return);
            case SettingType.DEVICE_DEPLOYMENT_LOCATION /* 333 */:
                return context.getResources().getString(R.string.device_deployment_location);
            case 334:
                return context.getResources().getString(R.string.pick_up_port_count);
            case 335:
                return context.getResources().getString(R.string.data_save_location);
            case SettingType.WORK_MODE /* 336 */:
                return context.getResources().getString(R.string.work_mode);
            case SettingType.FIND_PEOPER_TEST /* 337 */:
                return context.getResources().getString(R.string.find_peoper_test);
            case SettingType.FIND_PEOPER_SENSITIVITY /* 338 */:
                return context.getResources().getString(R.string.find_peoper_sensitivity);
            case SettingType.FIND_PEOPER_DISTANCE /* 339 */:
                return context.getResources().getString(R.string.find_peoper_distance);
            case 340:
                return context.getResources().getString(R.string.control_temperature_set);
            case SettingType.DRUG_BOX_MENU_NAME /* 341 */:
                return context.getResources().getString(R.string.drug_box_menu_name);
            case SettingType.PRACTICE_MODE /* 345 */:
                return context.getResources().getString(R.string.practice_mode);
            case SettingType.QRCODE_FLOAT_VIEW /* 346 */:
                return context.getResources().getString(R.string.qrcode_float_view);
            case SettingType.QRCODE_FLOAT_VIEW_ENABLE /* 347 */:
                return context.getResources().getString(R.string.is_it_enabled);
            case SettingType.QRCODE_FLOAT_VIEW_IMAGE /* 348 */:
                return context.getResources().getString(R.string.qrcode_float_image);
            case SettingType.BANK_MACHINE /* 349 */:
                return context.getResources().getString(R.string.bank_machine);
            case 350:
                return context.getResources().getString(R.string.disinfectant_tank_control);
            case SettingType.WATER_INLET_SOLENOID_VALVE_CONTROL /* 351 */:
                return context.getResources().getString(R.string.water_inlet_solenoid_valve_control);
            case SettingType.MIXING_TANK_CONTROL /* 352 */:
                return context.getResources().getString(R.string.mixing_tank_control);
            case 354:
                return context.getResources().getString(R.string.open_grid_machine_grid_single);
            case SettingType.GRID_MACHINE_FULL_LOAD /* 355 */:
                return context.getResources().getString(R.string.grid_machine_full_load);
        }
    }

    public static String getSettingKey(int i) {
        return String.valueOf(i);
    }
}
