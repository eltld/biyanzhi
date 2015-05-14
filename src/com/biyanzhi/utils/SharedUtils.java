package com.biyanzhi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.biyanzhi.applation.MyApplation;

/**
 * * SharedPreferences 的公具类
 * 
 * @author teeker_bin
 * 
 */
public class SharedUtils {
	private static final String SP_NAME = "tf";
	private static SharedPreferences sharedPreferences = MyApplation
			.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	private static Editor editor = sharedPreferences.edit();
	public static final String SP_UID = "uid";;

	public static String getString(String key, String defaultValue) {
		return sharedPreferences.getString(key, defaultValue);
	}

	public static int getInt(String key, int defaultValue) {
		return sharedPreferences.getInt(key, defaultValue);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	public static void setString(String key, String value) {
		editor.putString(key, value);
		editor.commit();

	}

	public static long getLong(String key, long defaultValue) {
		return sharedPreferences.getLong(key, defaultValue);

	}

	public static void setLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public static void setInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public static void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void setUid(String uid) {
		setString(SP_UID, uid);
	}

	public static String getUid() {
		return getString(SP_UID, "0");
	}

	public static int getIntUid() {
		String uid = getUid();
		if (uid.length() > 0) {
			return Integer.parseInt(uid);
		}
		return 0;
	}

	public static void settingNewVersion(boolean isNewVersion) {
		editor.putBoolean("new_version", isNewVersion);
		editor.commit();
	}

	public static boolean getNewVersion() {
		return sharedPreferences.getBoolean("new_version", false);

	}

	public static void setUserName(String value) {
		editor.putString("username", value);
		editor.commit();
	}

	public String getUserName() {
		return sharedPreferences.getString("username", "");

	}

	public static String getAPPUserName() {
		return sharedPreferences.getString("app_user_name", "");

	}

	public static void setAPPUserName(String value) {
		editor.putString("app_user_name", value);
		editor.commit();
	}

	public static String getAPPUserAvatar() {
		return sharedPreferences.getString("app_user_avatar", "");

	}

	public static void setAPPUserAvatar(String value) {
		editor.putString("app_user_avatar", value);
		editor.commit();
	}

	public static String getAPPUserGender() {
		return sharedPreferences.getString("app_user_gender", "");

	}

	public static void setAPPUserGender(String value) {
		editor.putString("app_user_gender", value);
		editor.commit();
	}

	public static String getAPPUserBirthday() {
		return sharedPreferences.getString("app_user_birthday", "");

	}

	public static void setAPPUserBirthday(String value) {
		editor.putString("app_user_birthday", value);
		editor.commit();
	}

	public static String getAPPUserRegisterTime() {
		return sharedPreferences.getString("app_user_rigistertime", "");

	}

	public static void setAPPUserRegisterTime(String value) {
		editor.putString("app_user_rigistertime", value);
		editor.commit();
	}

	public static String getAPPUserDeclaration() {
		return sharedPreferences.getString("app_user_declaration", "");

	}

	public static void setAPPUserDeclaration(String value) {
		editor.putString("app_user_declaration", value);
		editor.commit();
	}

	public static String getAPPUserDescription() {
		return sharedPreferences.getString("app_user_description", "");

	}

	public static void setAPPUserAddress(String value) {
		editor.putString("app_user_address", value);
		editor.commit();
	}

	public static String getAPPUserAddress() {
		return sharedPreferences.getString("app_user_address", "");

	}

	public static void setAPPUserProvince(String value) {
		editor.putString("app_user_province", value);
		editor.commit();
	}

	public static String getAPPUserProvince() {
		return sharedPreferences.getString("app_user_province", "");

	}

	public static void setAPPUserProvinceKey(String value) {
		editor.putString("app_user_province_key", value);
		editor.commit();
	}

	public static String getAPPUserProvinceKey() {
		return sharedPreferences.getString("app_user_province_key", "");

	}

	public static void setAPPUserChatID(String value) {
		editor.putString("app_user_chat_id", value);
		editor.commit();
	}

	public static String getAPPUserChatID() {
		return sharedPreferences.getString("app_user_chat_id", "");

	}

	public static void setAPPUserSortKey(String value) {
		editor.putString("app_user_sortkey", value);
		editor.commit();
	}

	public static String getAPPUserSortKey() {
		return sharedPreferences.getString("app_user_sortkey", "");

	}

	public static void setAPPUserDescription(String value) {
		editor.putString("app_user_description", value);
		editor.commit();
	}

	public static void setCircleMemberLastReqTime(int cid, long lastReqTime) {
		editor.putLong("circle_member_last_req_time" + cid, lastReqTime);
		editor.commit();
	}

	public static long getCircleMemberLastReqTime(int cid) {
		return sharedPreferences.getLong("circle_member_last_req_time" + cid,
				0l);

	}

	public static void setCircleMemberLocalLastReqTime(int cid, long lastReqTime) {
		editor.putLong("circle_member_local_last_req_time" + cid, lastReqTime);
		editor.commit();
	}

	public static long getCircleMemberLocalLastReqTime(int cid) {
		return sharedPreferences.getLong("circle_member_local_last_req_time"
				+ cid, 0l);

	}

	public static void setCircleLastRequestTime(long lastReqTime) {
		editor.putLong("circle_last_request_time", lastReqTime);
		editor.commit();
	}

	public static long getCircleLastRequestTime() {
		return sharedPreferences.getLong("circle_last_request_time", 0l);

	}

	public static void setGrowthLastRequestTime(String lastReqTime) {
		editor.putString("growth_last_request_time", lastReqTime);
		editor.commit();
	}

	public static String getGrowthLastRequestTime() {
		return sharedPreferences.getString("growth_last_request_time", "0");

	}

	public static void clearData() {
		editor.clear();
		editor.commit();
	}
}
