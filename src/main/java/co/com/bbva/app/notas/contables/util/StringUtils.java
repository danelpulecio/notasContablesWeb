package co.com.bbva.app.notas.contables.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

	public StringUtils() {
	}

	public static int getAppearance(String as_s, char ac_c) {
		int li_count = 0;
		if (isValidString(as_s)) {
			int li_i = 0;
			for (int li_lenght = as_s.length(); li_i < li_lenght; li_i++)
				if (as_s.charAt(li_i) == ac_c)
					li_count++;

		}
		return li_count;
	}

	public static String getString(Date ad_d, String as_format) {
		String ls_date;
		if (ad_d != null && isValidString(as_format)) {
			SimpleDateFormat lsdf_sdf = new SimpleDateFormat(as_format);
			lsdf_sdf.setLenient(false);
			try {
				StringBuffer lsb_date = new StringBuffer();
				lsb_date = lsdf_sdf.format(ad_d, lsb_date, new FieldPosition(1));
				ls_date = lsb_date.toString();
			} catch (Exception le_e) {
				ls_date = null;
			}
		} else {
			ls_date = null;
		}
		return ls_date;
	}

	public static String getString(double ad_d) {
		DecimalFormat ldf_f = new DecimalFormat();
		ldf_f.setDecimalSeparatorAlwaysShown(false);
		ldf_f.setGroupingUsed(false);
		return ldf_f.format(ad_d);
	}

	public static String getString(String as_s) {
		return as_s != null ? as_s.trim() : null;
	}

	public static String getString(String as_s, int ai_maxLength) {
		String ls_s = null;
		if (as_s != null) {
			as_s = as_s.trim();
			ls_s = as_s.substring(0, Math.min(ai_maxLength, as_s.length()));
		}
		return ls_s;
	}

	public static String getStringLeftPadding(StringBuffer asb_sb, int ai_lenght, char ac_padd) {
		return getStringPadding(asb_sb, ai_lenght, ac_padd, true, false);
	}

	public static String getStringLeftPadding(StringBuffer asb_sb, int ai_lenght) {
		return getStringPadding(asb_sb, ai_lenght, ' ', true, false);
	}

	public static String getStringLeftPadding(String as_s, int ai_lenght, char ac_padd) {
		StringBuffer lsb_sb = new StringBuffer();
		if (as_s != null)
			lsb_sb.append(as_s);
		return getStringPadding(lsb_sb, ai_lenght, ac_padd, true, false);
	}

	public static String getStringLeftPadding(String as_s, int ai_lenght) {
		return getStringLeftPadding(as_s, ai_lenght, ' ');
	}

	public static String getStringLeftPaddingFixed(StringBuffer asb_sb, int ai_lenght, char ac_padd) {
		return getStringPadding(asb_sb, ai_lenght, ac_padd, true, true);
	}

	public static String getStringLeftPaddingFixed(StringBuffer asb_sb, int ai_lenght) {
		return getStringPadding(asb_sb, ai_lenght, ' ', true, true);
	}

	public static String getStringLeftPaddingFixed(String as_s, int ai_lenght, char ac_padd) {
		StringBuffer lsb_sb = new StringBuffer();
		if (as_s != null)
			lsb_sb.append(as_s);
		return getStringPadding(lsb_sb, ai_lenght, ac_padd, true, true);
	}

	public static String getStringLowerCase(String as_s) {
		return as_s != null ? as_s.trim().toLowerCase() : null;
	}

	public static String getStringNormalized(String as_s) {
		String ls_s;
		if ((ls_s = getStringTrim(as_s)) != null) {
			ls_s = ls_s.toUpperCase();
			/*
			ls_s = ls_s.replace('\300', 'A');
			ls_s = ls_s.replace('\301', 'A');
			ls_s = ls_s.replace('\302', 'A');
			ls_s = ls_s.replace('\303', 'A');
			ls_s = ls_s.replace('\304', 'A');
			ls_s = ls_s.replace('\305', 'A');
			ls_s = ls_s.replace('\310', 'E');
			ls_s = ls_s.replace('\311', 'E');
			ls_s = ls_s.replace('\312', 'E');
			ls_s = ls_s.replace('\313', 'E');
			ls_s = ls_s.replace('\314', 'I');
			ls_s = ls_s.replace('\315', 'I');
			ls_s = ls_s.replace('\316', 'I');
			ls_s = ls_s.replace('\317', 'I');
			ls_s = ls_s.replace('\322', 'O');
			ls_s = ls_s.replace('\323', 'O');
			ls_s = ls_s.replace('\324', 'O');
			ls_s = ls_s.replace('\325', 'O');
			ls_s = ls_s.replace('\326', 'O');
			ls_s = ls_s.replace('\331', 'U');
			ls_s = ls_s.replace('\332', 'U');
			ls_s = ls_s.replace('\333', 'U');
			ls_s = ls_s.replace('\334', 'U');
			*/
		}
		return ls_s;
	}

	public static String getStringNotBlank(String as_s) {
		String ls_s = null;
		if (as_s != null) {
			StringBuffer lsb_sb = new StringBuffer(getString(as_s));
			for (int li_i = 0; li_i < lsb_sb.length();) {
				char lc_c = lsb_sb.charAt(li_i);
				if (Character.isWhitespace(lc_c) || Character.isSpaceChar(lc_c))
					lsb_sb.deleteCharAt(li_i);
				else
					li_i++;
			}

			ls_s = lsb_sb.toString();
		}
		return ls_s;
	}

	public static String getStringNotEmpty(String as_s) {
		String ls_s = getStringNotNull(as_s);
		if (ls_s.length() < 1)
			ls_s = " ";
		return ls_s;
	}

	public static String getStringNotNull(String as_s) {
		return as_s != null ? as_s.trim() : "";
	}

	public static String getStringPadding(StringBuffer asb_sb, int ai_lenght, char ac_padd, boolean ab_left, boolean ab_fixed) {
		if (asb_sb == null)
			asb_sb = new StringBuffer();
		for (int li_i = asb_sb.length(); li_i < ai_lenght; li_i++)
			if (ab_left)
				asb_sb.insert(0, ac_padd);
			else
				asb_sb.append(ac_padd);

		if (ab_fixed)
			asb_sb.delete(ai_lenght, asb_sb.length());
		return asb_sb.toString();
	}

	public static String getStringRightPadding(StringBuffer asb_sb, int ai_lenght, char ac_padd) {
		return getStringPadding(asb_sb, ai_lenght, ac_padd, false, false);
	}

	public static String getStringRightPadding(StringBuffer asb_sb, int ai_lenght) {
		return getStringPadding(asb_sb, ai_lenght, ' ', false, false);
	}

	public static String getStringRightPadding(String as_s, int ai_lenght) {
		return getStringRightPadding(as_s, ai_lenght, ' ');
	}

	public static String getStringRightPadding(String as_s, int ai_lenght, char ac_padd) {
		StringBuffer lsb_sb = new StringBuffer();
		if (as_s != null)
			lsb_sb.append(as_s);
		return getStringPadding(lsb_sb, ai_lenght, ac_padd, false, false);
	}

	public static String getStringRightPaddingFixed(StringBuffer asb_sb, int ai_lenght, char ac_padd) {
		return getStringPadding(asb_sb, ai_lenght, ac_padd, false, true);
	}

	public static String getStringRightPaddingFixed(StringBuffer asb_sb, int ai_lenght) {
		return getStringPadding(asb_sb, ai_lenght, ' ', false, true);
	}

	public static String getStringRightPaddingFixed(String as_s, int ai_lenght, char ac_padd) {
		StringBuffer lsb_sb = new StringBuffer();
		if (as_s != null)
			lsb_sb.append(as_s);
		return getStringPadding(lsb_sb, ai_lenght, ac_padd, false, true);
	}

	public static String getStringTrim(String as_s) {
		String ls_s = null;
		if (as_s != null) {
			boolean lb_hasSpace = false;
			StringBuffer lsb_sb = new StringBuffer(getString(as_s));
			for (int li_i = 0; li_i < lsb_sb.length();) {
				char lc_c = lsb_sb.charAt(li_i);
				if (Character.isWhitespace(lc_c) || Character.isSpaceChar(lc_c)) {
					if (lb_hasSpace) {
						lsb_sb.deleteCharAt(li_i);
					} else {
						lsb_sb.replace(li_i, li_i + 1, " ");
						li_i++;
					}
					lb_hasSpace = true;
				} else {
					li_i++;
					lb_hasSpace = false;
				}
			}

			ls_s = lsb_sb.toString();
		}
		return ls_s;
	}

	public static String getStringTrimLowerCase(String as_s) {
		return getStringUpperCase(getStringTrim(as_s));
	}

	public static String getStringTrimUpperCase(String as_s) {
		return getStringUpperCase(getStringTrim(as_s));
	}

	public static String getStringUpperCase(String as_s) {
		return as_s != null ? as_s.trim().toUpperCase() : null;
	}

	public static String getStringZero(int ai_i, int ai_lenght) {
		StringBuffer lsb_sb = new StringBuffer();
		lsb_sb.append(ai_i);
		return getStringZero(lsb_sb, ai_lenght);
	}

	public static String getStringZero(String as_s, int ai_lenght) {
		return getStringLeftPadding(as_s, ai_lenght, '0');
	}

	public static String getStringZero(StringBuffer asb_sb, int ai_lenght) {
		return getStringLeftPadding(asb_sb, ai_lenght, '0');
	}

	public static boolean isValidString(String as_s) {
		return as_s != null && as_s.trim().length() > 0;
	}

	public static String format(String as_s) {
		Long ll_l = null;
		ll_l = new Long(as_s);
		DecimalFormat ldf_f = new DecimalFormat();
		ldf_f.setDecimalSeparatorAlwaysShown(false);
		ldf_f.setGroupingUsed(true);
		return ldf_f.format(ll_l);
	}

	public static String replace(String as_string, String as_pattern, String as_replace) {
		int li_pattern = as_pattern.length();
		int li_start = 0;
		StringBuffer lsb_result = new StringBuffer();
		int li_end;
		while ((li_end = as_string.indexOf(as_pattern, li_start)) >= 0) {
			lsb_result.append(as_string.substring(li_start, li_end));
			lsb_result.append(as_replace);
			li_start = li_end + li_pattern;
		}
		lsb_result.append(as_string.substring(li_start));
		return lsb_result.toString();
	}
	
	public static String insertSplit(String as_string, String as_split, int frequency) {
		String ls_string = as_string;
		String ls_newString = "";
		
		while (ls_string.length() > 0) {
			try {
				ls_newString += (!ls_string.substring(0, frequency).equals("0000") ? ls_string.substring(0, frequency) + as_split : "");
			} catch (Exception le_e) {
				ls_newString += ls_string.substring(0);
			}
			try {
				ls_string = ls_string.substring(frequency);
			} catch (Exception le_e) {
				ls_string = "";
			}
		}
		
		try {
			ls_newString = ls_newString.substring(0, ls_newString.lastIndexOf(as_split));
		} catch (Exception le_e) {
			
		}
		
		return (ls_newString);
	}
	
	public static String replaceZero(String as_string) {
		String newString = as_string;
		int li_count = 0;
		boolean ind = true;
		
		while ((ind) && (li_count < as_string.length())) {
			if (newString.substring(0, 1).equals("0")) {
				newString = newString.substring(1);
			} else {
				ind = false;
			}
		}
		
		return (newString);
	}
	
	public static String replaceXMLAnchor(String as_string) {
		String newString = "";
		
		newString = as_string.replaceAll("<", "&lt;");
		newString = newString.replaceAll(">", "&gt;");
		
		return (newString);
	}
}