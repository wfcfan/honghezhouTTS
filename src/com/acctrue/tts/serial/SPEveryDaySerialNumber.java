package com.acctrue.tts.serial;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.acctrue.tts.utils.SharedPreferencesUtils;

public class SPEveryDaySerialNumber extends EveryDaySerialNumber {

	private final static String FIELD_SEPARATOR = ",";

	public SPEveryDaySerialNumber(int width) {
		super(width);

	}

	@Override
	protected int getOrUpdateNumber(Date current, int start) {
		String date = format(current);
		int num = start;

		String str = SharedPreferencesUtils.getSerialNumber();
		if (!str.equals("")) {
			List<String> list = this.readList(str);
			if(list.get(0).equals(date)){
				num = Integer.parseInt(list.get(1));
			}

		}
		SharedPreferencesUtils.setSerialNumber(date + FIELD_SEPARATOR + (num + 1));
		return num;
	}

	private List<String> readList(String str) {
		List<String> list = new ArrayList<String>();
		String[] arr = str.split(FIELD_SEPARATOR);
		for (String s : arr)
			list.add(s);

		return list;
	}

}
