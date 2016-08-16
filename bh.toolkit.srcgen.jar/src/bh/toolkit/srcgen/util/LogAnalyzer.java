package bh.toolkit.srcgen.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用以分析VO被调用过程中所输出的属性被访问的记录
 * 
 * @author panyongc
 */
public class LogAnalyzer {

	public static void main(String[] arg) {
		
		BufferedReader br = null;
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String placeHolder = "----Used----";
		try {
			
			String filePath = "C:/apache-tomcat-7.0.34/logs/pcc_purchmgmt.log";
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				int index = line.indexOf(placeHolder);
				if (index >= 0) {
					String temp = line.substring(index + placeHolder.length());
					String vo = temp.substring(0, temp.indexOf("."));
					String property = temp.substring(temp.indexOf(".") + 1);
					if (map.get(vo) == null) {
						map.put(vo, new ArrayList<String>());
					}
					if (!map.get(vo).contains(property))
						map.get(vo).add(property);
				}
			}
			for (String key : map.keySet()) {
				System.out.println(key + "使用到的属性有：");
				List<String> properties = map.get(key);
				for (String property : properties) {
					System.out.println("<includedAttr name=\"" + property + "\" />");
				}
			}
			
		} catch (Exception e) {
			try {
				br.close();
			} catch (IOException e1) {
			}
			e.printStackTrace();
		}
		
	}
	
}
