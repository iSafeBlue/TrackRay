package com.trackray.base.utils;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author fre1Ray
 * */
public class StrUtils {



	/**
	 * 单位缩进字符串。
	 */
	private static String SPACE = "   ";

	/**
	 * 返回格式化JSON字符串。
	 *
	 * @param json 未格式化的JSON字符串。
	 * @return 格式化的JSON字符串。
	 */
	public static String formatJson(String json)
	{
		StringBuffer result = new StringBuffer();

		int length = json.length();
		int number = 0;
		char key = 0;
		//遍历输入字符串。
		for (int i = 0; i < length; i++)
		{
			//1、获取当前字符。
			key = json.charAt(i);

			//2、如果当前字符是前方括号、前花括号做如下处理：
			if((key == '[') || (key == '{') )
			{
				//（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
				if((i - 1 > 0) && (json.charAt(i - 1) == ':'))
				{
					result.append('\n');
					result.append(indent(number));
				}

				//（2）打印：当前字符。
				result.append(key);

				//（3）前方括号、前花括号，的后面必须换行。打印：换行。
				result.append('\n');

				//（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
				number++;
				result.append(indent(number));

				//（5）进行下一次循环。
				continue;
			}

			//3、如果当前字符是后方括号、后花括号做如下处理：
			if((key == ']') || (key == '}') )
			{
				//（1）后方括号、后花括号，的前面必须换行。打印：换行。
				result.append('\n');

				//（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
				number--;
				result.append(indent(number));

				//（3）打印：当前字符。
				result.append(key);

				//（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
				if(((i + 1) < length) && (json.charAt(i + 1) != ','))
				{
					result.append('\n');
				}

				//（5）继续下一次循环。
				continue;
			}

			//4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
			if((key == ','))
			{
				result.append(key);
				result.append('\n');
				result.append(indent(number));
				continue;
			}

			//5、打印：当前字符。
			result.append(key);
		}

		return result.toString();
	}

	/**
	 * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
	 *
	 * @param number 缩进次数。
	 * @return 指定缩进次数的字符串。
	 */
	private static String indent(int number)
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < number; i++)
		{
			result.append(SPACE);
		}
		return result.toString();
	}





	/**
	 * @param urlparam 带分隔的url参数
	 * @return
	 */
	public static Map<String,String> paramToMap(String urlparam){
		Map<String,String> map = new HashMap<String,String>();
		String[] param =  urlparam.split("&");
		for(String keyvalue:param){
			String[] pair = keyvalue.split("=");
			if(pair.length==2){
				map.put(pair[0], pair[1]);
			}
		}
		return map;
	}


	/**
	 * 获取字符串的unicode编码
	 * 汉字“木”的Unicode 码点为Ox6728
	 *
	 * @param s 木
	 * @return \ufeff\u6728  \ufeff控制字符 用来表示「字节次序标记（Byte Order Mark）」不占用宽度
	 * 在java中一个char是采用unicode存储的 占用2个字节 比如 汉字木 就是 Ox6728 4bit+4bit+4bit+4bit=2字节
	 */
	public static String stringToUnicode(String s) {
		try {
			StringBuffer out = new StringBuffer("");
			//直接获取字符串的unicode二进制
			byte[] bytes = s.getBytes("unicode");
			//然后将其byte转换成对应的16进制表示即可
			for (int i = 0; i < bytes.length - 1; i += 2) {
				out.append("\\u");
				String str = Integer.toHexString(bytes[i + 1] & 0xff);
				for (int j = str.length(); j < 2; j++) {
					out.append("0");
				}
				String str1 = Integer.toHexString(bytes[i] & 0xff);
				out.append(str1);
				out.append(str);
			}
			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Unicode转 汉字字符串
	 *
	 * @param str \u6728
	 * @return '木' 26408
	 */
	public static String unicodeToString(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			//group 6728
			String group = matcher.group(2);
			//ch:'木' 26408
			ch = (char) Integer.parseInt(group, 16);
			//group1 \u6728
			String group1 = matcher.group(1);
			str = str.replace(group1, ch + "");
		}
		return str.replaceAll("[\\u0000-\\u0002\b]","");//转义字符 回车等范围
	}
}
