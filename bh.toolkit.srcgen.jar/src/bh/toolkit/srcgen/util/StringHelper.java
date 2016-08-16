package bh.toolkit.srcgen.util;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import bh.toolkit.srcgen.lang.GlobalConst;

public class StringHelper {

	/**
	 * 开始新的一行
	 * 
	 * @param sb
	 */
	public static void newLine(StringBuffer sb) {
		newLine(sb, "", 0);
	}

	/**
	 * 开始新的一行
	 * 
	 * @param sb
	 * @param maxWidth
	 */
	public static void newLine(StringBuffer sb, int maxWidth) {
		newLine(sb, "", 0, maxWidth);
	}

	/**
	 * 开始新的一行
	 * 
	 * @param sb
	 * @param indent
	 * @param countOfIndent
	 */
	public static void newLine(StringBuffer sb, String indent, int countOfIndent) {
		sb.append(GlobalConst.LINE_SEPARATOR);
		for (int j = 0; j < countOfIndent; j++) {
			sb.append(indent);
		}
	}

	/**
	 * 开始新的一行
	 * 
	 * @param sb
	 * @param indent
	 * @param countOfIndent
	 * @param maxWidth
	 */
	public static void newLine(StringBuffer sb, String indent, int countOfIndent, int maxWidth) {

		// To detect the last new line symbol.
		String currentStr = sb.toString();
		int i = lastIndexOf(currentStr, GlobalConst.LINE_SEPARATOR);

		// If found, compute from the last new line symbol.
		if (i != -1) {
			currentStr = currentStr.substring(i);
		}

		if (currentStr.length() > maxWidth) {
			sb.append(GlobalConst.LINE_SEPARATOR);

			for (int j = 0; j < countOfIndent; j++) {
				sb.append(indent);
			}
		}

	}

	/**
	 * @param str
	 * @param pattern
	 * @return int
	 */
	public static int indexOf(String str, String pattern) {
		return indexOf(str, pattern, false);
	}

	/**
	 * @param str
	 * @param pattern
	 * @param ignoreCase
	 * @return int
	 */
	public static int indexOf(String str, String pattern, boolean ignoreCase) {
		return indexOf(str, pattern, 0, ignoreCase);
	}

	/**
	 * @param str
	 * @param pattern
	 * @param fromIndex
	 * @param ignoreCase
	 * @return int
	 */
	public static int indexOf(String str, String pattern, int fromIndex, boolean ignoreCase) {

		if (ignoreCase == true) {
			str = str.toLowerCase();
			pattern = pattern.toLowerCase();

			return str.indexOf(pattern, fromIndex);
		} else {
			return str.indexOf(pattern, fromIndex);
		}

	}

	/**
	 * @param str
	 * @param pattern
	 * @return int
	 */
	public static int lastIndexOf(String str, String pattern) {
		return lastIndexOf(str, pattern, false);
	}

	/**
	 * @param str
	 * @param pattern
	 * @param ignoreCase
	 * @return int
	 */
	public static int lastIndexOf(String str, String pattern, boolean ignoreCase) {
		return lastIndexOf(str, pattern, str.length(), ignoreCase);
	}

	/**
	 * @param str
	 * @param pattern
	 * @param fromIndex
	 * @param ignoreCase
	 * @return int
	 */
	public static int lastIndexOf(String str, String pattern, int fromIndex, boolean ignoreCase) {

		if (ignoreCase == true) {
			str = str.toLowerCase();
			pattern = pattern.toLowerCase();

			return str.lastIndexOf(pattern, fromIndex);
		} else {
			return str.lastIndexOf(pattern, fromIndex);
		}

	}

	/**
	 * Get the minimum character within the <code>String</code> object.
	 * 
	 * @param input
	 *            - The input <code>String</code> object.
	 * @return int
	 */
	public static int minChar(String input) {

		if (input == null) {
			throw new NullPointerException();
		}

		int min = 0;
		int len = input.length();
		int ch = 0;

		for (int i = 0; i < len; i++) {
			ch = input.charAt(i);
			if (ch < min) {
				min = ch;
			}
		}

		return min;

	}

	/**
	 * Get the maximum character withing the <code>String</code> object.
	 * 
	 * @param input
	 *            - The input <code>String</code> object.
	 * @return int
	 */
	public static int maxChar(String input) {

		if (input == null) {
			throw new NullPointerException();
		}

		int max = 0;
		int len = input.length();
		int ch = 0;

		for (int i = 0; i < len; i++) {
			ch = input.charAt(i);
			if (ch > max) {
				max = ch;
			}
		}

		return max;

	}

	/**
	 * Convert the character at the given index to upper case.
	 * 
	 * @param value
	 *            - The object of <code>String</code> need to be converted.
	 * @param index
	 *            - The index of the character, beginning with 0.
	 * @return java.lang.String
	 */
	public static String toUpperCase(String value, int index) {

		if (StringUtils.isBlank(value) == true) {
			return "";
		}

		int len = value.length();
		String ch = value.substring(index, index + 1).toUpperCase();

		// If the character is not the last one of the string.
		if (index < len - 1) {
			value = value.substring(0, index) + ch + value.substring(index + 1, len);
		} else {
			value = value.substring(0, index) + ch;
		}

		return value;

	}

	/**
	 * Convert all the characters immediately after the given sub-string to upper case.
	 * 
	 * @param str
	 *            - The input <code>String</code> object.
	 * @param subStr
	 *            - The given sub-string.
	 * @return java.lang.String
	 */
	public static String toUpperCase(String str, String subStr) {

		int len = str.length();
		int subLen = subStr.length();
		int index = str.indexOf(subStr);

		int tempLen = 0;
		while (index != -1) {

			tempLen = index + subLen;

			str = str.substring(0, index) + subStr + str.substring(tempLen, tempLen + 1).toUpperCase() + str.substring(tempLen + 1, len);

			index = str.indexOf(subStr, tempLen + 1);
		}

		return str;

	}

	/**
	 * Convert the character of the specified string at the given index to lower case.
	 * 
	 * @param value
	 *            - The object of <code>String</code> need to be converted.
	 * @param index
	 *            - The index of the character, beginning with 0.
	 * @return java.lang.String
	 */
	public static String toLowerCase(String value, int index) {

		if (StringUtils.isBlank(value) == true) {
			return "";
		}

		int len = value.length();
		String ch = value.substring(index, index + 1).toLowerCase();

		// If the character is not the last one of the string.
		if (index < len - 1) {
			value = value.substring(0, index) + ch + value.substring(index + 1, len);
		} else {
			value = value.substring(0, index) + ch;
		}

		return value;

	}

	/**
	 * Convert all the characters immediately after the given sub-string to lower case.
	 * 
	 * @param str
	 *            - The input <code>String</code> object.
	 * @param subStr
	 *            - The given sub-string.
	 * @return java.lang.String
	 */
	public static String toLowerCase(String str, String subStr) {

		int len = str.length();
		int subLen = subStr.length();
		int index = str.indexOf(subStr);

		int tempLen = 0;
		while (index != -1) {

			tempLen = index + subLen;

			str = str.substring(0, index) + subStr + str.substring(tempLen, tempLen + 1).toLowerCase() + str.substring(tempLen + 1, len);

			index = str.indexOf(subStr, tempLen + 1);
		}

		return str;

	}

	/**
	 * @param str
	 * @param pattern
	 * @return true
	 */
	public static boolean startsWithIgnoreCase(String str, String pattern) {

		// For comparison, convert two strings to lower case.
		str = str.toLowerCase();
		pattern = pattern.toLowerCase();

		return str.startsWith(pattern);

	}

	/**
	 * @param str
	 * @param pattern
	 * @return true
	 */
	public static boolean endsWithIgnoreCase(String str, String pattern) {

		// For comparison, convert two strings to lower case.
		str = str.toLowerCase();
		pattern = pattern.toLowerCase();

		return str.endsWith(pattern);

	}

	/**
	 * Trim all unuseful characters, whose ascii value is smaller than 33 and bigger than 126, around the given <code>String</code> object.
	 * 
	 * @param input
	 *            - The given <code>String</code> object will be trimed.
	 * @return java.lang.String
	 */
	public static String trim(String input) {

		if (input == null) {
			return null;
		}

		int len = input.length();
		char ch = 0;

		// Trim the beginning.
		int i = 0;
		for (i = 0; i < len; i++) {
			ch = input.charAt(i);
			if ((ch >= 33) && (ch <= 126)) {
				// If useful character is found, then terminate the
				// current searching.
				break;
			}
		}

		// Trim the ending.
		int j = 0;
		for (j = len; j > i; j--) {
			ch = input.charAt(j - 1);
			if ((ch >= 33) && (ch <= 126)) {
				// If useful character is found, then terminate the
				// current searching.
				break;
			}
		}

		return input.substring(i, j);

	}

	public static String leftTrim(String s) {

		if (s == null) {
			return s;
		} else if (StringUtils.isBlank(s) == true) {
			return s.trim();
		}

		int len = s.length();
		char whiteSpace = ' ';
		int i = 0;
		for (i = 0; i < len; i++) {
			if (s.charAt(i) == whiteSpace) {
				continue;
			} else {
				break;
			}
		}

		return s.substring(i);

	}

	public static String rightTrim(String s) {

		if (s == null) {
			return s;
		} else if (StringUtils.isBlank(s) == true) {
			return s.trim();
		}

		int len = s.length();
		char whiteSpace = ' ';
		int i = 0;
		for (i = len - 1; i >= 0; i--) {
			if (s.charAt(i) == whiteSpace) {
				continue;
			} else {
				i++;
				break;
			}
		}

		return s.substring(0, i);

	}

	public static String leftTrim(String input, String pattern) {

		if (StringUtils.isBlank(input) == true || StringUtils.isBlank(pattern) == true) {
			return input;
		}

		int idx = input.indexOf(pattern);
		if (idx != -1) {
			input = input.substring(pattern.length(), input.length());
		}

		return input;

	}

	/**
	 * Remove special char below: \t \r \n
	 * 
	 * @param source
	 * @return
	 */
	public static String removeBlank(String source) {

		String dest = "";
		if (source != null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(source);
			dest = m.replaceAll("");
		}

		return dest;

	}

	/**
	 * Remove non-eligible characters in phase. Please note that the order of regular expressions in this method could not be changed.
	 * 
	 * @param phase
	 * @return
	 */
	public static String removeNonEligibleChar(String phase) {

		String result = phase;

		// Replace invalid characters with " ".
		Pattern pattern = Pattern.compile("[-]|['\"_,%?./&():：]");
		Matcher matcher = pattern.matcher(result);
		result = matcher.replaceAll(" ");

		// Add "_" before number if number is the first character.
		pattern = Pattern.compile("^\\d");
		matcher = pattern.matcher(result);
		if (matcher.find()) {
			result = "_" + result;
		}

		// Replace "#" followed by number.
		pattern = Pattern.compile("#[ ]*[0-9]");
		Pattern subPat = Pattern.compile("#[ ]*");
		matcher = pattern.matcher(result);
		String currMat = null;
		Matcher subMat = null;
		StringBuffer sb = new StringBuffer();
		int start, end = 0;
		while (matcher.find()) {
			start = matcher.start();
			end = matcher.end();
			currMat = result.substring(start, end);
			subMat = subPat.matcher(currMat);
			matcher.appendReplacement(sb, subMat.replaceAll(""));
		}
		matcher.appendTail(sb);

		// Replace "#" followed by character with " Number ".
		pattern = Pattern.compile("#");
		matcher = pattern.matcher(result);
		result = matcher.replaceAll(" Number ");

		// Replace "Y/N" with " ".
		pattern = Pattern.compile("Y/N", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(result);
		result = matcher.replaceAll(" ");

		// Replace "A/C" with "Account".
		pattern = Pattern.compile("A/C", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(result);
		result = matcher.replaceAll("Acount");

		// Replace "DDMMYYYY" and "DDMMYY" with " ".
		pattern = Pattern.compile("DDMMY*", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(result);
		result = matcher.replaceAll(" ");

		// Replace "HHMMSS" with " ".
		pattern = Pattern.compile("HHMMSS", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(result);
		result = matcher.replaceAll(" ");

		// Remove extra white space.
		pattern = Pattern.compile("[ ]+");
		matcher = pattern.matcher(result);
		result = matcher.replaceAll(" ");

		return result.trim().replace("*", " ");

	}

	/**
	 * Sometimes the character cell is red as double, such as "140" is red as "140.0".
	 * 
	 * @param strVal
	 * @return
	 */
	public static String removeDot(String strVal) {

		if (StringUtils.isBlank(strVal) == true) {
			return strVal;
		}

		int dotIdx = strVal.indexOf(".");
		if (dotIdx != -1) {
			return strVal.substring(0, dotIdx);
		}

		return strVal;

	}

	public static String removeZeroFromDot(String phase) {

		String result = phase;

		Pattern pattern = Pattern.compile("\\.0*\\b");
		Matcher matcher = pattern.matcher(result);
		result = matcher.replaceAll("");

		return result;

	}

	public static String removePatterns(String source, String[] patterns) {

		String dest = "";
		if (source != null && patterns != null && patterns.length > 1) {

			StringBuffer pipedPat = new StringBuffer();
			String singlePat = null;
			for (int i = 0; i < patterns.length; i++) {
				singlePat = patterns[i];
				if (i > 0) {
					pipedPat.append("|");
				}
				pipedPat.append(singlePat);
			}

			Pattern p = Pattern.compile(pipedPat.toString());
			Matcher m = p.matcher(source);
			dest = m.replaceAll("");
		}

		return dest;

	}

	public static String replace(String input, String[] patterns, String replacement) {

		if (patterns == null || patterns.length == 0) {
			return input;
		}

		for (int i = 0; i < patterns.length; i++) {
			input = replace(input, patterns[i], replacement);
		}

		return input;

	}

	/**
	 * Replace every occurrence of str in input with pattern. If pattern is null or blank string, then every occrence of str is removed from input.
	 * 
	 * @param input
	 * @param str
	 * @param pattern
	 * @return java.lang.String
	 */
	public static String replace(String input, String patterns, String replacement) {

		if ((input == null) || (input.trim().length() == 0)) {
			return input;
		}

		int index = input.indexOf(patterns);
		int len = input.length();
		int strLen = patterns.length();

		if (replacement == null) {
			replacement = "";
		}

		while (index != -1) {

			input = input.substring(0, index) + replacement + input.substring(index + strLen, len);

			index = input.indexOf(patterns);
			len = input.length();
		}

		return input;

	}

	/**
	 * 
	 * @Fun: Given psResource, replace the content between of specified left&right delimiter
	 */
	// public static String replaceSubString(String input, int pnBegin, String psBegin, String psEnd, String replacement) {
	//
	// // System.out.println("original content:" + psResource);
	// int nBegin, nEnd;
	// String result = "";
	//
	// String sResource = input.substring(pnBegin);
	// nBegin = sResource.indexOf(psBegin, 0);
	// nEnd = sResource.indexOf(psEnd, nBegin);
	//
	// if (nBegin == -1) {
	// nBegin = 0;
	// } else if (nEnd == -1) {
	// nEnd = sResource.length();
	// }
	//
	// result = input.substring(0, pnBegin + nBegin + psBegin.length()) + replacement + input.substring(pnBegin + nEnd);
	// // System.out.println("result content:" + psResource);
	// return result;
	//
	// }

	/**
	 * Replace all unuseful charactor with given default character.
	 * 
	 * @param input
	 *            - The input <code>String</code> object.
	 * @return java.lang.String
	 */
	public static String filterSpace(String input) {

		StringBuffer output = new StringBuffer();
		int len = input.length();
		char ch = 0;

		for (int i = 0; i < len; i++) {
			ch = input.charAt(i);
			if ((ch >= 33) && (ch <= 126)) {
				output.append(ch);
			} else {
				// output.append(UNUSEFUL_CHAR);
			}
		}

		return output.toString();

	}

	public static String trimAndAppend(String input, String trimPat, String appendPat) {

		if ((input == null) || (input.trim().length() == 0)) {
			return input;
		}

		// If no trimPat occurred.
		int index = input.lastIndexOf(trimPat);
		if (index == -1) {
			return input;
		}

		input = input.substring(0, index);
		input = input + appendPat;

		return input;

	}

	/**
	 * Input the given patten at every length of characters.
	 * 
	 * @param inputStr
	 * @param everyLength
	 * @param pattern
	 * @return String
	 */
	public static String insert(String inputStr, int everyLength, String pattern) throws IllegalArgumentException {

		if (inputStr == null) {
			return null;
		}

		if (everyLength <= 0) {
			throw new IllegalArgumentException("The length can not be zero or negative.");
		}

		StringBuffer sb = new StringBuffer();
		int length = inputStr.length();

		if (everyLength >= length) {
			sb.append(inputStr);
			sb.append(pattern);
		} else {

			String everyPart = null;

			do {
				everyPart = inputStr.substring(0, everyLength);
				sb.append(everyPart);
				sb.append(pattern);

				inputStr = inputStr.substring(everyLength, length);
				length = inputStr.length();

			} while (everyLength < length);

			if (length > 0) {
				sb.append(inputStr);
				sb.append(pattern);
			}
		}

		return sb.toString();

	}

	public static void breakLine(StringBuffer paragraph, List<String> patternList, String indent, int countOfIndent, int maxWidth) {

		StringBuffer breakedParagraph = new StringBuffer();

		// To detect the last new line symbol.
		String currentStr = paragraph.toString();
		int idxOfLineSep = lastIndexOf(currentStr, GlobalConst.LINE_SEPARATOR);

		// If found, compute from the last new line symbol.
		if (idxOfLineSep != -1) {
			breakedParagraph.append(currentStr.substring(0, idxOfLineSep + 1));
			currentStr = currentStr.substring(idxOfLineSep + 1);
		}

		// If current string has exceed the maximum width.
		String line = null;
		String pattern = null;
		int lastPosOfPat = 0;
		int currPosOfPat = 0;
		if (currentStr.length() > maxWidth) {

			// Narrow down to the potential line.
			line = currentStr.substring(0, maxWidth);

			// Identify the rightmost of the given pattern in line.
			for (int i = 0; i < patternList.size(); i++) {
				pattern = patternList.get(i);
				currPosOfPat = line.lastIndexOf(pattern);
				if (currPosOfPat > lastPosOfPat) {
					lastPosOfPat = currPosOfPat;
				}
			}

			// Insert line separator before the identified pattern.
			breakedParagraph.append(line.substring(0, lastPosOfPat));
			breakedParagraph.append(GlobalConst.LINE_SEPARATOR);

			for (int i = 0; i < countOfIndent; i++) {
				breakedParagraph.append(indent);
			}

			breakedParagraph.append(currentStr.substring(lastPosOfPat));

			// Replace the given paragraph with the breaked one.
			paragraph.delete(0, paragraph.length());
			paragraph.append(breakedParagraph);

			// Invoke recursively.
			breakLine(paragraph, patternList, indent, countOfIndent, maxWidth);

		}

	}

	public static String getLastGroup(String pattern, String separator) {

		if (StringUtils.isBlank(pattern)) {
			return pattern;
		}

		int lastIdx = pattern.lastIndexOf(separator);
		if (lastIdx == -1) {
			return pattern;
		}

		return pattern.substring(lastIdx < pattern.length() - 1 ? lastIdx + 1 : pattern.length() - 1);

	}

	/**
	 * Add additional semi-quotation.
	 * 
	 * @param inputStr
	 *            - The input <code>String</code> object.
	 * @return java.lang.String
	 */
	public final static String doubleSemiQuotation(String inputStr) {

		if (inputStr == null) {
			return null;
		}

		int i = inputStr.indexOf("'");
		int l = inputStr.length();

		if (i != -1) {
			StringBuffer sb = new StringBuffer();
			sb.append(inputStr.substring(0, i));
			sb.append("''");
			sb.append(doubleSemiQuotation(inputStr.substring(i + 1, l)));

			return sb.toString();
		} else {
			return inputStr;
		}

	}

	public static String getRandomStr() {
		return new BigInteger(10 * 16, new Random()).toString(16);
	}

	public static String GBK2Unicode(String str) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char chr1 = (char) str.charAt(i);
			if (!isNeedConvert(chr1)) {
				result.append(chr1);
				continue;
			}
			result.append("\\u" + Integer.toHexString((int) chr1));
		}
		return result.toString();
	}

	public static boolean isNeedConvert(char para) {
		return ((para & (0x00FF)) != para);
	}

	public static void main(String[] args) {

		// String s = "1 A_B/C - . # 1 #A ok # 2 # 3# :：";
		// System.out.println(s);
		// System.out.println(removeNonEligibleChar(s));
		//
		// s = "12.000";
		// System.out.println(s);
		// System.out.println(removeZeroFromDot(s));
		// "['\"-_,%?./&():：]"

		// String result = "1 'A_B/,C - ._ # 1 #A (ok) # 2 # 3# :：";
		// System.out.println(result);
		//
		// Pattern pattern = Pattern.compile("[-]|['\"_,%?./&():：]");
		// Matcher matcher = pattern.matcher(result);
		// result = matcher.replaceAll("x");
		//
		// System.out.println(result);

		// Pattern p = Pattern.compile("cat");
		// Matcher m = p.matcher("one cat two cats in the yard");
		// StringBuffer sb = new StringBuffer();
		// while (m.find()) {
		// m.appendReplacement(sb, "dog");
		// }
		// m.appendTail(sb);
		// System.out.println(sb.toString());

	}

}
