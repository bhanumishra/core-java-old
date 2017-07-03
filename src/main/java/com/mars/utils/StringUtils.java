package com.mars.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	/**
	 * Utility method to replace each ' (quote sign) character with \'.
	 *
	 * @param str
	 *            In which ' to be replaced with \'.
	 * @return res All ' characters replaced with \'.
	 */
	public static String addslashes(final String str) {
		return (str != null && !str.isEmpty()) && str.contains("'") ? str.replace("'", "\\'") : str;
	}

	public static boolean areEquals(final String origin, final String checkWith) {
		return !isNullOrEmpty(origin) && origin.equals(checkWith);
	}

	public static String concat(final String[] strarr, final String c) {
		String res = null;

		for (String s : strarr) {
			if (res != null) {
				res += c + s;
			} else {
				res = s;
			}
		}
		return res;
	}

	public static byte[] convertCharsToBytes(char[] chars) {
		byte[] result = new byte[chars.length * 2];

		for (int i = 0; i < chars.length; ++i) {
			result[2 * i] = (byte) (chars[i] / 256);
			result[2 * i + 1] = (byte) (chars[i] % 256);
		}

		return result;
	}

	public static String deleteAllNonDigit(String s) {
		return s.replaceAll("\\D", "");
	}

	public static final String getChecksum(final String password, final String algo) {
		MessageDigest md = null;
		final StringBuilder sb = new StringBuilder(32);

		if (!isNullOrEmpty(password) && isNullOrEmpty(algo)) {
			try {
				if (areEquals(algo, "MD5") || areEquals(algo, "SHA1")) {
					md = MessageDigest.getInstance(algo);
				} else {
					md = MessageDigest.getInstance("MD5");
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			if (md != null) {
				md.update(password.getBytes());

				byte[] data = md.digest();

				for (int i = 0; i < data.length; i++) {
					sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
				}
			}
		}

		return sb.toString().toUpperCase();
	}

	/** Extract a only numbers from given String, if available */
	public static String getNumeric(final String input) {
		char[] numeric = new char[input.length()];
		int n = 0;

		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) >= 48 && input.charAt(i) <= 57) {
				numeric[n++] = input.charAt(i);
			}
		}
		return new String(numeric).trim();

		/* * * * * OR * * * * */
		/*
		 * StringBuffer numeric = new StringBuffer();
		 *
		 * for(int i = 0; i < input.length(); i++) { if(input.charAt(i) >= 48 && input.charAt(i) <= 57) {
		 * numeric.append(input.charAt(i)); } } return numeric.toString();
		 */
	}

	public static long getNumOnly(final String strarr) {
		return Long.parseLong(deleteAllNonDigit(strarr));
	}

	public static boolean isNullOrEmpty(String origin) {
		return (origin == null || origin.trim().isEmpty());
	}

	public static boolean isQuoted(String argument) {
		return argument.startsWith("\'") && argument.endsWith("\'")
				|| argument.startsWith("\"") && argument.endsWith("\"");
	}

	public static String left(String var0, int var1) {
		return var0 == null ? null : (var1 < 0 ? "" : (var0.length() <= var1 ? var0 : var0.substring(0, var1)));
	}

	public static String ltrim(final String str) {
		int i;
		int j;
		int x = 0;

		for (i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch != ' ') {
				break;
			}
		}

		final int len = (str.length() - i);
		char[] res = new char[len];
		for (j = i; j < str.length(); j++) {
			res[x++] = str.charAt(j);
		}

		return new String(res, 0, len);
	}

	public static String mid(String var0, int var1, int var2) {
		if (var0 == null) {
			return null;
		} else if (var2 >= 0 && var1 <= var0.length()) {
			if (var1 < 0) {
				var1 = 0;
			}

			return var0.length() <= var1 + var2 ? var0.substring(var1) : var0.substring(var1, var1 + var2);
		} else {
			return "";
		}
	}

	public static String removeChar(String s, char c) {
		StringBuffer buf = new StringBuffer(s.length());
		buf.setLength(s.length());
		int current = 0;
		for (int i = 0; i < s.length(); i++) {
			char cur = s.charAt(i);
			if (cur != c)
				buf.setCharAt(current++, cur);
		}
		return buf.toString();
	}

	public static String removeCharAt(String s, int i) {
		StringBuffer buf = new StringBuffer(s.length() - 1);
		buf.append(s.substring(0, i)).append(s.substring(i + 1));
		return buf.toString();
	}

	public static String removeCharsFromString(String word1, String word2) {
		StringBuilder sb = new StringBuilder(word1);

		System.out.println(sb);
		// char[] word2characters= word2.toCharArray();
		Map<Character, Integer> table = new HashMap<Character, Integer>();

		for (int i = 0; i < word2.length(); i++) {
			table.put(word2.charAt(i), 1);
		}

		int p = 0;
		for (int i = 0; i < word1.length(); i++) {
			if (table.containsKey(word1.charAt(i))) {
				if (p == 0) {
					sb.deleteCharAt(i);
					// p++;
				} else {
					sb.deleteCharAt(i - p);
				}
				// System.out.println(sb);
				p++;
			}
		}
		return sb.toString();
	}

	public static String[] removeEmptyStr(final String[] strarr) {
		List<String> list = new ArrayList<>();

		for (String s : strarr) {
			if (s != null && s.length() > 0) {
				list.add(s);
			}
		}
		return list.toArray(new String[list.size()]);

		// or Java 8 Lambda Expression
		// return Arrays.stream(firstArray).filter(s -> (s != null && s.length()
		// > 0)).toArray(String[]::new);
	}

	public static final String removeExtraSpaces(final String input) {
		final String regex = "\\s+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		String result = matcher.replaceAll(" ");

		return result;
	}

	public static String[] removeNulls(final String[] strArray) {
		// Calculate the size for the new array.
		int size = 0;
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i] != null) {
				size++;
			}
		}

		// Populate the new array.
		String[] elements = new String[size];
		int newIndex = 0;
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i] != null) {
				elements[newIndex] = strArray[i];
				newIndex++;
			}
		}
		// Return the new array.
		return elements;
	}

	public static String[] removeNullsAndEmptyString(final String[] strArray) {
		List<String> list = new ArrayList<String>(Arrays.asList(strArray));

		list.removeAll(Arrays.asList("", null));

		return list.toArray(new String[list.size()]);
	}

	public static String repalceAllChar(String s, String f, String r) {
		String temp = s.replace(f, r);
		return temp;
	}

	public static String replace(String origin, String replacement) {
		String value = null;

		if (origin == null || origin.isEmpty()) {
			value = replacement;
		} else {
			value = origin;
		}
		return value;
	}

	public static String replaceCharAt(String s, int i, char c) {
		StringBuffer buf = new StringBuffer(s);
		buf.setCharAt(i, c);
		return buf.toString();
	}

	public static String reverse(final String input) {
		char[] res = new char[input.length()];
		int c = 0;

		for (int i = input.length() - 1; i >= 0; --i) {
			res[c++] = input.charAt(i);
		}
		return new String(res);

		// METHOD 2
		/*
		 * StringBuffer output = new StringBuffer();
		 *
		 * for(int i = input.length() - 1; i >= 0; --i) { output.append(input.charAt(i)); }
		 *
		 * return output.toString();
		 */

		// METHOD 3
		/*
		 * String rev = "";
		 *
		 * for (int i = text.length() - 1; i >= 0; i--) { rev = rev + text.charAt(i); }
		 *
		 * return rev;
		 */
	}

	public static String reverse2(final String input) {
		char[] inputChars = input.toCharArray();
		int count = input.length() - 1;

		for (int i = 0; i < inputChars.length / 2; i++, count--) {
			if (i > count) {
				break;
			}

			char c = inputChars[i];
			inputChars[i] = inputChars[count];
			inputChars[count] = c;
		}

		return new String(inputChars, 0, inputChars.length);
	}

	public static String right(String var0, int var1) {
		return var0 == null ? null
				: (var1 < 0 ? "" : (var0.length() <= var1 ? var0 : var0.substring(var0.length() - var1)));
	}

	public static String rtrim(final String str) {
		int i, j;

		for (i = str.length() - 1; i >= 0; i--) {
			char ch = str.charAt(i);
			if (ch != ' ') {
				break;
			}
		}

		char[] res = new char[i + 1];

		for (j = 0; j <= i; j++) {
			res[j] = str.charAt(j);
		}
		return new String(res, 0, i + 1);
	}

	/**
	 * Separates the given String by capitalizing the first character and by inserting space before each capital letter.
	 *
	 * @param str
	 *            String to separate.
	 * @return Separated String, i.e., &quot;newName&quot; is converted to &quot;New Name&quot;
	 */
	public static final String separate(final String str) {
		StringBuilder sb = new StringBuilder("");

		if (str != null && !str.isEmpty()) {
			char[] arr = str.toCharArray();
			for (int i = 0; i < arr.length; i++) {
				if (i == 0) {
					sb.append(Character.toUpperCase(arr[i]));
				} else if (Character.isUpperCase(arr[i])) {
					sb.append(" ").append(arr[i]);
				} else {
					sb.append(arr[i]);
				}
			}
		}

		return sb.toString();
	}

	public static void trim(final String s) {
		int i = 0, j = 0, k = 0;

		for (i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (ch != ' ')
				break;
		}
		for (j = s.length() - 1; j >= 0; j--) {
			char ch = s.charAt(j);
			if (ch != ' ')
				break;
		}
		for (k = i; k < j + 1; k++)
			System.out.print(s.charAt(k));
	}

	public static byte[] xor(final byte[] input, final byte[] secret) {
		final byte[] output = new byte[input.length];
		if (secret.length == 0) {
			throw new IllegalArgumentException("Empty security key");
		}
		int spos = 0;
		for (int pos = 0; pos < input.length; ++pos) {
			output[pos] = (byte) (input[pos] ^ secret[spos]);
			++spos;
			if (spos >= secret.length) {
				spos = 0;
			}
		}
		return output;
	}

	private StringUtils() {
		System.out.println("StringUtils.StringUtils()");
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode());
	}
}
