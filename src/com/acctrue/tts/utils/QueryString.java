package com.acctrue.tts.utils;

import java.net.URLEncoder;
import java.util.HashMap;

import android.text.TextUtils;

public class QueryString {

	private StringBuffer query = new StringBuffer();

	public QueryString() {

	}

	public void add(String name, String value) {
		if (TextUtils.isEmpty(query)) {
			query.append("?");
		} else {
			query.append("&");
		}
		encode(name, value);
	}

	public void add(String name, int value) {
		add(name, String.valueOf(value));
	}

	private void encode(String name, String value) {
		try {
			if (TextUtils.isEmpty(name) || TextUtils.isEmpty(value)) {
				return;
			}
			query.append(URLEncoder.encode(name, "UTF-8"));
			query.append("=");
			query.append(URLEncoder.encode(value, "UTF-8"));
		} catch (Exception ex) {
		}
	}

	public String getQuery() {
		return query.toString();
	}

	public String toString() {
		return getQuery();
	}

	public static HashMap<String, String> fromQueryString(String qs) {
		HashMap<String, String> map = new HashMap<String, String>();

		if (TextUtils.isEmpty(qs)) {
			return map;
		}
		qs = qs.substring(1);
		String[] array = qs.split("&");
		for (String s : array) {
			if (TextUtils.isEmpty(s)) {
				continue;
			}
			String[] kv = s.split("=");
			if (kv == null || kv.length < 2) {
				continue;
			}
			map.put(kv[0], kv[1]);
		}

		return map;
	}
}