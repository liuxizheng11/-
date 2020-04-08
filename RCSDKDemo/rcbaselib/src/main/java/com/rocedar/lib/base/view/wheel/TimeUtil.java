package com.rocedar.lib.base.view.wheel;



public class TimeUtil {

	/**
	 * 求出某年某月某日的这一天是星期几
	 * 
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 */
	public static String day(int y, int m, int d) {
		if (m == 1 || m == 2) {
			m += 12;
			y--;
		}
		int week = d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400
				+ 1;
		week = week % 7;
		String w = "日一二三四五六".substring(week, week + 1);
		return w;
	}
	
	/**
	 * 求星期组的下标
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 */
	public static int day1(int y, int m, int d) {
		if (m == 1 || m == 2) {
			m += 12;
			y--;
		}
		int week = d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400
				+ 1;
		week = week % 7;
		return week;
	}

	/**
	 * 计算指定年份的某个月有多少天
	 * 
	 * @param y
	 * @param m
	 * @return
	 */
	public static int month(int y, int m) {
		int days = 0;
		String leap = year(y);
		if (m > 7) {
			if (m % 2 == 0) {
				days = 31;
			} else {
				days = 30;
			}
		} else {
			if (m % 2 == 1) {
				days = 31;
			} else {
				days = 30;
			}
			if (m == 2 && leap.equals("闰年")) {
				days = 29;
			} else if (m == 2 && leap.equals("平年")) {
				days = 28;
			}
		}
		return days;
	}

	/**
	 *  判断该年是不是闰年
	 * @param y
	 * @return
	 */
	public static String year(int y) {
		String leap = (y % 400 == 0 || y % 4 == 0 && y % 100 != 0) ? "闰年" : "平年";
		return leap;
	}
	
	/**
	 *  判断该年是不是闰年
	 * @param y
	 * @return
	 */
	public static boolean isLeapYear(int y) {
		boolean leap = (y % 400 == 0 || y % 4 == 0 && y % 100 != 0) ? true : false;
		return leap;
	}

}
