package co.com.bbva.app.notas.contables.util;

import co.com.bbva.app.notas.contables.carga.dto.Festivo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

	public DateUtils() {
	}

	public static Date getCleanDate(Date ad_d) {
		return getCleanDate(ad_d, 11);
	}

	public static Date getCleanDate(Date ad_d, int ai_field) {
		Calendar lc_c = getCalendar(ad_d);
		clearFrom(lc_c, ai_field);
		return lc_c.getTime();
	}

	public static java.sql.Date getCleanSQLDate(Date ad_d) {
		return getSQLDate(getCleanDate(ad_d));
	}

	public static java.sql.Date getCleanSQLDate(java.sql.Date ad_d, int ai_field) {
		return getSQLDate(getCleanDate(ad_d, ai_field));
	}

	public static Date getDate(String as_date, String as_format) {
		Date ld_d;
		if (StringUtils.isValidString(as_date) && StringUtils.isValidString(as_format)) {
			SimpleDateFormat lsdf_sdf = new SimpleDateFormat(as_format);
			lsdf_sdf.setLenient(false);
			try {
				ld_d = lsdf_sdf.parse(StringUtils.getString(as_date));
			} catch (Exception le_e) {
				ld_d = null;
			}
		} else {
			ld_d = null;
		}
		return ld_d;
	}

	public static Date getDate(Date ad_d, int ai_field, int ai_amount, boolean ab_before) {
		if (ad_d == null) {
			ad_d = new Date();
		}
		Calendar lc_c = getCalendar(ad_d);
		if (ab_before) {
			ai_amount *= -1;
		}
		lc_c.add(ai_field, ai_amount);
		return lc_c.getTime();
	}

	public static Date getDate(java.sql.Date ad_d) {
		return ad_d == null ? null : new Date(ad_d.getTime());
	}

	public static String getDay(int ai_day) {
		String ls_day = "";
		switch (ai_day) {
		case 1: // '\001'
			ls_day = "un";
			break;

		case 2: // '\002'
			ls_day = "dos";
			break;

		case 3: // '\003'
			ls_day = "tres";
			break;

		case 4: // '\004'
			ls_day = "cuatro";
			break;

		case 5: // '\005'
			ls_day = "cinco";
			break;

		case 6: // '\006'
			ls_day = "seis";
			break;

		case 7: // '\007'
			ls_day = "siete";
			break;

		case 8: // '\b'
			ls_day = "ocho";
			break;

		case 9: // '\t'
			ls_day = "nueve";
			break;

		case 10: // '\n'
			ls_day = "diez";
			break;

		case 11: // '\013'
			ls_day = "once";
			break;

		case 12: // '\f'
			ls_day = "doce";
			break;

		case 13: // '\r'
			ls_day = "trece";
			break;

		case 14: // '\016'
			ls_day = "catorce";
			break;

		case 15: // '\017'
			ls_day = "quince";
			break;

		case 16: // '\020'
			ls_day = "diez y s\351is";
			break;

		case 17: // '\021'
			ls_day = "diez y siete";
			break;

		case 18: // '\022'
			ls_day = "diez y ocho";
			break;

		case 19: // '\023'
			ls_day = "diez y nueve";
			break;

		case 20: // '\024'
			ls_day = "veinte";
			break;

		case 21: // '\025'
			ls_day = "veinte y un";
			break;

		case 22: // '\026'
			ls_day = "veinte y dos";
			break;

		case 23: // '\027'
			ls_day = "veinte y tres";
			break;

		case 24: // '\030'
			ls_day = "veinte y cuatro";
			break;

		case 25: // '\031'
			ls_day = "veinte y cinco";
			break;

		case 26: // '\032'
			ls_day = "veinte y seis";
			break;

		case 27: // '\033'
			ls_day = "veinte y siete";
			break;

		case 28: // '\034'
			ls_day = "veinte y ocho";
			break;

		case 29: // '\035'
			ls_day = "veinte y nueve";
			break;

		case 30: // '\036'
			ls_day = "treinta";
			break;

		case 31: // '\037'
			ls_day = "treinta y un";
			break;
		}
		return ls_day;
	}

	public static int getDayOfMonth(Date ad_d) {
		return getField(ad_d, 5);
	}

	public static int getDayOfWeek(int ai_i) {
		Calendar lc_c = getCalendar(null);
		int li_maxDayOfWeek = lc_c.getMaximum(7);
		int li_minDayOfWeek = lc_c.getMinimum(7);
		return li_minDayOfWeek > ai_i || ai_i > li_maxDayOfWeek ? li_maxDayOfWeek : ai_i;
	}

	public static int getDayOfWeek(Date ad_date) {
		Calendar lc_c = getCalendar(ad_date);

		// 1: Domingo
		// 7: Sbado
		return lc_c.get(Calendar.DAY_OF_WEEK);
	}

	public static String getDayNameOfWeek(Date ad_date) {
		Calendar lc_c = getCalendar(ad_date);
		int dayOfWeek = lc_c.get(Calendar.DAY_OF_WEEK);
		String dayName = "";

		// 1: Domingo
		// 7: Sbado
		switch (dayOfWeek) {
		case 1:
			dayName = "Domingo";
			break;
		case 2:
			dayName = "Lunes";
			break;
		case 3:
			dayName = "Martes";
			break;
		case 4:
			dayName = "Mircoles";
			break;
		case 5:
			dayName = "Jueves";
			break;
		case 6:
			dayName = "Viernes";
			break;
		case 7:
			dayName = "Sbado";
			break;
		}

		return dayName;
	}

	public static int getElapsed(Date ad_start, Date ad_end) {
		return (int) getElapsed(ad_start, ad_end, 1);
	}

	public static long getElapsed(Date ad_d1, Date ad_d2, int ai_field) {
		return getElapsed(getCalendar(ad_d1), getCalendar(ad_d2), ai_field);
	}

	public static long getElapsed(Calendar ac_c1, Calendar ac_c2, int ai_field) {
		long ll_elapsed = 0L;
		Calendar lc_before;
		Calendar lc_after;
		if (ac_c2.after(ac_c1)) {
			lc_after = (Calendar) ac_c2.clone();
			lc_before = (Calendar) ac_c1.clone();
		} else {
			lc_after = (Calendar) ac_c1.clone();
			lc_before = (Calendar) ac_c2.clone();
		}
		while (lc_before.before(lc_after)) {
			lc_before.add(ai_field, 1);
			if (validateYearForElapsedSum(lc_before, lc_after)) {
				ll_elapsed++;
			}
		}
		return ll_elapsed;
	}

	public static int getField(Date ad_d, int ai_field) {
		return getCalendar(ad_d).get(ai_field);
	}

	public static Date getFirstDateOfMonth(Date ad_d) {
		Calendar lc_c = getCalendar(ad_d);
		clearFrom(lc_c, 11);
		lc_c.set(5, lc_c.getActualMinimum(5));
		return lc_c.getTime();
	}

	public static Date getFirstDateOfMonth() {
		return getFirstDateOfMonth(new Date());
	}

	public static java.sql.Date getFirstSQLDateOfMonth(Date ad_d) {
		return getSQLDate(getFirstDateOfMonth(ad_d));
	}

	public static java.sql.Date getFirstSQLDateOfMonth() {
		return getFirstSQLDateOfMonth(getSQLDate());
	}

	public static Map<Integer, Long> getFullAge(Date ad_d1, Date ad_d2) {
		Map<Integer, Long> lm_age = new HashMap<Integer, Long>();
		Date ld_after;
		Date ld_before;
		if (ad_d2.after(ad_d1)) {
			ld_after = (Date) ad_d2.clone();
			ld_before = (Date) ad_d1.clone();
		} else {
			ld_after = (Date) ad_d1.clone();
			ld_before = (Date) ad_d2.clone();
		}
		Calendar lc_after = getCalendar(ld_after);
		Calendar lc_before = getCalendar(ld_before);
		calculateFieldForFullAge(lc_before, lc_after, lm_age, 1);
		calculateFieldForFullAge(lc_before, lc_after, lm_age, 2);
		calculateFieldForFullAge(lc_before, lc_after, lm_age, 5);
		calculateFieldForFullAge(lc_before, lc_after, lm_age, 11);
		calculateFieldForFullAge(lc_before, lc_after, lm_age, 12);
		calculateFieldForFullAge(lc_before, lc_after, lm_age, 13);
		calculateFieldForFullAge(lc_before, lc_after, lm_age, 14);
		return lm_age;
	}

	public static Date getLastDateOfMonth() {
		return getLastDateOfMonth(new Date());
	}

	public static Date getLastDateOfMonth(Date ad_d) {
		Calendar lc_c = getCalendar(ad_d);
		clearFrom(lc_c, 11);
		lc_c.set(5, getLastDayOfMonth(lc_c));
		return lc_c.getTime();
	}

	public static Date getLastDateOfYear(Date ad_d) {
		Calendar lc_c = getCalendar(ad_d);
		int li_year = lc_c.get(1);
		int li_month = lc_c.getActualMaximum(2);
		lc_c.set(2, li_month);
		int li_day = lc_c.getActualMaximum(5);
		lc_c.set(li_year, li_month, li_day);
		clearFrom(lc_c, 11);
		return lc_c.getTime();
	}

	public static int getLastDayOfMonth(int ai_month, int ai_year) {
		Calendar lc_c = getCalendar(null);
		lc_c.clear();
		lc_c.set(2, ai_month);
		lc_c.set(1, ai_year);
		lc_c.setLenient(false);
		return getLastDayOfMonth(lc_c);
	}

	public static int getLastDayOfMonth(Calendar ac_c) {
		return ac_c.getActualMaximum(5);
	}

	public static java.sql.Date getLastSQLDateOfMonth(Date ad_d) {
		return getSQLDate(getLastDateOfMonth(ad_d));
	}

	public static java.sql.Date getLastSQLDateOfMonth() {
		return getLastSQLDateOfMonth(getSQLDate());
	}

	public static boolean isLeapYear(int ai_year) {
		boolean lb_leapYear = true;
		if (ai_year % 4 == 0) {
			if (ai_year % 100 == 0) {
				if (ai_year % 400 == 0) {
					lb_leapYear = true;
				} else {
					lb_leapYear = false;
				}
			} else {
				lb_leapYear = true;
			}
		} else {
			lb_leapYear = false;
		}
		return lb_leapYear;
	}

	public static int getMonth(Date ad_d) {
		return getField(ad_d, 2) + 1;
	}

	public static String getMonthName(int ai_month) {
		String ls_month;
		switch (ai_month) {
		case 1: // '\001'
			ls_month = "Enero";
			break;

		case 2: // '\002'
			ls_month = "Febrero";
			break;

		case 3: // '\003'
			ls_month = "Marzo";
			break;

		case 4: // '\004'
			ls_month = "Abril";
			break;

		case 5: // '\005'
			ls_month = "Mayo";
			break;

		case 6: // '\006'
			ls_month = "Junio";
			break;

		case 7: // '\007'
			ls_month = "Julio";
			break;

		case 8: // '\b'
			ls_month = "Agosto";
			break;

		case 9: // '\t'
			ls_month = "Septiembre";
			break;

		case 10: // '\n'
			ls_month = "Octubre";
			break;

		case 11: // '\013'
			ls_month = "Noviembre";
			break;

		case 12: // '\f'
			ls_month = "Diciembre";
			break;

		default:
			ls_month = "";
			break;
		}
		return ls_month;
	}

	public static String getMonthName(Date ad_d) {
		return getMonthName(getMonth(ad_d));
	}

	public static java.sql.Date getSQLDate(String as_date, String as_format) {
		return getSQLDate(getDate(as_date, as_format));
	}

	public static java.sql.Date getSQLDate(java.sql.Date ad_d, int ai_field, int ai_amount, boolean ab_before) {
		return getSQLDate(getDate(ad_d, ai_field, ai_amount, ab_before));
	}

	public static java.sql.Date getSQLDate(Date ad_d) {
		return ad_d == null ? null : new java.sql.Date(ad_d.getTime());
	}

	public static java.sql.Date getSQLDate() {
		return getSQLDateNotNull(null);
	}

	public static java.sql.Date getSQLDateNotNull(Date ad_d) {
		if (ad_d == null) {
			ad_d = new Date();
		}
		return getSQLDate(ad_d);
	}

	public static Timestamp getTimestamp(Date ad_d) {
		return ad_d == null ? null : getTimestamp(ad_d.getTime());
	}

	public static Timestamp getTimestamp() {
		return getTimestampNotNull(null);
	}

	public static Timestamp getTimestamp(long al_long) {
		return new Timestamp(al_long);
	}

	public static Timestamp getTimestampNotNull(Date ad_d) {
		if (ad_d == null) {
			ad_d = new Date();
		}
		return getTimestamp(ad_d);
	}

	public static int getYear(Date ad_d) {
		return getField(ad_d, 1);
	}

	private static Calendar getCalendar(Date ad_d) {
		Calendar lc_c = new GregorianCalendar();
		if (ad_d != null) {
			lc_c.setTime(ad_d);
		}
		lc_c.setLenient(false);
		return lc_c;
	}

	private static void calculateFieldForFullAge(Calendar ac_before, Calendar ac_after, Map<Integer, Long> am_age, int ai_field) {
		long ll_elapsed = getElapsed(ac_before, ac_after, ai_field);
		Long ll_l = new Long(ll_elapsed);
		am_age.put(Integer.valueOf(ai_field), ll_l);
		ac_before.add(ai_field, ll_l.intValue());
	}

	private static void clearFrom(Calendar ac_c, int ai_field) {
		switch (ai_field) {
		case 5: // '\005'
			clearFromDayOfMonth(ac_c);
			break;

		case 10: // '\n'
		case 11: // '\013'
			clearFromHour(ac_c);
			break;

		case 14: // '\016'
			clearFromMillisecond(ac_c);
			break;

		case 12: // '\f'
			clearFromMinute(ac_c);
			break;

		case 2: // '\002'
			clearFromMonth(ac_c);
			break;

		case 13: // '\r'
			clearFromSecond(ac_c);
			break;
		}
	}

	private static void clearFromDayOfMonth(Calendar ac_c) {
		clearFromHour(ac_c);
		ac_c.clear(5);
	}

	private static void clearFromHour(Calendar ac_c) {
		clearFromMinute(ac_c);
		ac_c.clear(10);
		ac_c.clear(11);
	}

	private static void clearFromMillisecond(Calendar ac_c) {
		ac_c.clear(14);
	}

	private static void clearFromMinute(Calendar ac_c) {
		clearFromSecond(ac_c);
		ac_c.clear(12);
	}

	private static void clearFromMonth(Calendar ac_c) {
		clearFromDayOfMonth(ac_c);
		ac_c.clear(2);
	}

	private static void clearFromSecond(Calendar ac_c) {
		clearFromMillisecond(ac_c);
		ac_c.clear(13);
	}

	private static boolean validateDayOfMonthForElapsedSum(Calendar ac_before, Calendar ac_after) {
		int li_after = ac_after.get(5);
		int li_before = ac_before.get(5);
		return li_before < li_after || li_before == li_after && validateHourOfDayForElapsedSum(ac_before, ac_after);
	}

	private static boolean validateHourOfDayForElapsedSum(Calendar ac_before, Calendar ac_after) {
		int li_after = ac_after.get(11);
		int li_before = ac_before.get(11);
		return li_before < li_after || li_before == li_after && validateMinuteForElapsedSum(ac_before, ac_after);
	}

	private static boolean validateMillisecondForElapsedSum(Calendar ac_before, Calendar ac_after) {
		return ac_before.get(14) <= ac_after.get(14);
	}

	private static boolean validateMinuteForElapsedSum(Calendar ac_before, Calendar ac_after) {
		int li_after = ac_after.get(12);
		int li_before = ac_before.get(12);
		return li_before < li_after || li_before == li_after && validateSecondForElapsedSum(ac_before, ac_after);
	}

	private static boolean validateMonthForElapsedSum(Calendar ac_before, Calendar ac_after) {
		int li_after = ac_after.get(2);
		int li_before = ac_before.get(2);
		return li_before < li_after || li_before == li_after && validateDayOfMonthForElapsedSum(ac_before, ac_after);
	}

	private static boolean validateSecondForElapsedSum(Calendar ac_before, Calendar ac_after) {
		int li_after = ac_after.get(13);
		int li_before = ac_before.get(13);
		return li_before < li_after || li_before == li_after && validateMillisecondForElapsedSum(ac_before, ac_after);
	}

	private static boolean validateYearForElapsedSum(Calendar ac_before, Calendar ac_after) {
		int li_after = ac_after.get(1);
		int li_before = ac_before.get(1);
		return li_before < li_after || li_before == li_after && validateMonthForElapsedSum(ac_before, ac_after);
	}

	public static int getMaxDaysMonth(int month, int year) {
		Calendar lc_c = new GregorianCalendar(year, month - 1, 1);
		return lc_c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int getDaysBetweenLastDayOfMonthAndToday(int month, int year, ArrayList<Date> holidays) {
		int actualDay = 0;
		int actualMonth = 0;
		int actualYear = 0;
		int posDay = 0;
		int posMonth = 0;
		int posYear = 0;
		// int dayOfWeek = 0;
		int countWorkDays = 0;
		boolean indicator = false;

		String actualDate = StringUtils.getString(new Date(), "ddMMyyyy");
		Date auxDate;

		actualDay = Integer.parseInt(actualDate.substring(0, 2));
		actualMonth = Integer.parseInt(actualDate.substring(2, 4));
		actualYear = Integer.parseInt(actualDate.substring(4, 8));

		posDay = 1;
		if (month < 12) {
			posMonth = month + 1;
			posYear = year;
		} else {
			posMonth = 1;
			posYear = year + 1;
		}

		while (!indicator) {
			auxDate = getDate(String.valueOf(posDay) + "-" + String.valueOf(posMonth) + "-" + String.valueOf(posYear), "d-M-yyyy");
			if (!holidays.contains(auxDate)) {
				countWorkDays++;
			}

			if (posDay < getMaxDaysMonth(posMonth, posYear)) {
				posDay++;
			} else {
				posDay = 1;
				if (posMonth < 12) {
					posMonth++;
				} else {
					posMonth = 1;
					posYear++;
				}
			}
			// si es el dia actual..
			if (posDay >= actualDay && posMonth >= actualMonth && posYear >= actualYear) {
				return countWorkDays;
			}
		}
		return countWorkDays;
	}

	public static Date getDateTodayBeforeDays(int days, ArrayList<Date> holidays) {
		int posDay = 0;
		int posMonth = 0;
		int posYear = 0;
		// int dayOfWeek = 0;
		int countWorkDays = days;
		boolean indicator = false;

		Date auxDate = null;

		posDay = Integer.parseInt(StringUtils.getString(DateUtils.getTimestamp(), "dd"));
		posMonth = Integer.parseInt(StringUtils.getString(DateUtils.getTimestamp(), "MM"));
		posYear = Integer.parseInt(StringUtils.getString(DateUtils.getTimestamp(), "yyyy"));

		if (posDay > 1) {
			posDay--;
		} else {
			if (posMonth > 1) {
				posMonth--;
			} else {
				posMonth = 12;
				posYear--;
			}
			posDay = getMaxDaysMonth(posMonth, posYear);
		}

		while (!indicator) {
			auxDate = getDate(String.valueOf(posDay) + "-" + String.valueOf(posMonth) + "-" + String.valueOf(posYear), "d-M-yyyy");
			// dayOfWeek = getDayOfWeek(auxDate);
			// if ((dayOfWeek != 1) && (dayOfWeek != 7)) {
			if (holidays.indexOf(auxDate) < 0) {
				countWorkDays--;
			}
			// }

			if (posDay > 1) {
				posDay--;
			} else {
				if (posMonth > 1) {
					posMonth--;
				} else {
					posMonth = 12;
					posYear--;
				}
				posDay = getMaxDaysMonth(posMonth, posYear);
			}

			if (countWorkDays == 0) {
				indicator = true;
			}
		}

		return auxDate;
	}

	public static Date getDatePlusDays(Date date, int days) {
		int day = 0;
		int month = 0;
		int year = 0;
		int count = 0;

		day = Integer.parseInt(StringUtils.getString(date, "dd"));
		month = Integer.parseInt(StringUtils.getString(date, "MM"));
		year = Integer.parseInt(StringUtils.getString(date, "yyyy"));

		while (count < days) {
			if (day < DateUtils.getMaxDaysMonth(month, year)) {
				day++;
			} else {
				day = 1;
				if (month < 12) {
					month++;
				} else {
					month = 1;
					year++;
				}
			}
			count++;
		}

		return DateUtils.getDate(String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year), "d-M-yyyy");
	}

	@SuppressWarnings("deprecation")
	public static int getFestivosEntre(Date from, Date to, List<Festivo> festivos) {
		int ret = 0;
		Date date = from;
		while (date.before(to)) {
			for (Festivo f : festivos) {
				if (date.getYear() == f.getFecha().getYear() && date.getMonth() == f.getFecha().getMonth() && date.getDate() == f.getFecha().getDate()) {
					ret++;
				}
			}
			date.setDate(date.getDate() + 1);
		}

		return ret;
	}

	public static Date getNextWorkDay(ArrayList<Date> holidays) {
		int posDay = Integer.parseInt(StringUtils.getString(DateUtils.getTimestamp(), "dd"));
		int posMonth = Integer.parseInt(StringUtils.getString(DateUtils.getTimestamp(), "MM"));
		int posYear = Integer.parseInt(StringUtils.getString(DateUtils.getTimestamp(), "yyyy"));

		Date auxDate = getDate(String.valueOf(posDay) + "-" + String.valueOf(posMonth) + "-" + String.valueOf(posYear), "d-M-yyyy");

		if (holidays.size() != 0) {
			while (true) {
				if (holidays.indexOf(auxDate) < 0) {
					break;
				}
				auxDate = getDatePlusDays(auxDate, 1);
			}
		}

		return auxDate;
	}
}