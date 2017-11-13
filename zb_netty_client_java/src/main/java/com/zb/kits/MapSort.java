package com.zb.kits;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.sun.xml.internal.ws.util.StringUtils;

public class MapSort {

	/**
	 * 
	 * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
	 * 实现步骤: <br>
	 * 
	 * @param paraMap
	 *            要排序的Map对象
	 * @param urlEncode
	 *            是否需要URLENCODE
	 * @param keyToLower
	 *            是否需要将Key转换为全小写 true:key转化成小写，false:不转化
	 * @return
	 */
	public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

				@Override
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			// 构造URL 键值对的格式
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds) {
				
					String key = item.getKey();
					String val = item.getValue();
					if (urlEncode) {
						val = URLEncoder.encode(val, "utf-8");
					}
					if (keyToLower) {
						buf.append(key.toLowerCase() + "=" + val);
					} else {
						buf.append(key + "=" + val);
					}
					buf.append("&");
				

			}
			buff = buf.toString();
			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e) {
			return null;
		}
		return buff;
	}

	public static void main(String[] args) {

		Map<String, String> map = new HashMap<String, String>();

		map.put("Kb", "K");
		map.put("Wc", "W");
		map.put("Ab", "A");
		map.put("ad", "a");
		map.put("Fe", "N");
		map.put("Bf", "B");
		map.put("Cg", "C");
		map.put("Zh", "Z");

		Map<String, String> resultMap = sortMapByKey(map); // 按Key进行排序，根据首字母hashcode

		for (Map.Entry<String, String> entry : resultMap.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

	}

	/**
	 * 使用 Map按key首字母hashcode进行排序
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}

		Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());

		sortMap.putAll(map);

		return sortMap;
	}

	public static String toStringMap(Map m) {
		// 按map键首字母顺序进行排序
		m = MapSort.sortMapByKey(m);

		StringBuilder sbl = new StringBuilder();
		for (Iterator<Entry> i = m.entrySet().iterator(); i.hasNext();) {
			Entry e = i.next();
			Object o = e.getValue();
			String v = "";
			if (o == null) {
				v = "";
			} else if (o instanceof String[]) {
				String[] s = (String[]) o;
				if (s.length > 0) {
					v = s[0];
				}
			} else {
				v = o.toString();
			}
			if (!e.getKey().equals("sign") && !e.getKey().equals("reqTime") && !e.getKey().equals("tx")) {
				// try {
				// sbl.append("&").append(e.getKey()).append("=").append(URLEncoder.encode(v,
				// "utf-8"));
				// } catch (UnsupportedEncodingException e1) {
				// e1.printStackTrace();
				sbl.append("&").append(e.getKey()).append("=").append(v);
				// }
			}
		}
		String s = sbl.toString();
		if (s.length() > 0) {
			return s.substring(1);
		}
		return "";
	}
}

class MapKeyComparator implements Comparator<String> {

	public int compare(String str1, String str2) {

		return str1.compareTo(str2);
	}
}