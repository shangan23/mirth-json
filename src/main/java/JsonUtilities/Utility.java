package JsonUtilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class Utility {
	@SuppressWarnings("unchecked")
	public static DocumentContext setJson(DocumentContext ctx, Object src, Object dest, String key, String val) {
		Object valObj;
		try {
			boolean isFound = val.indexOf(".StrToJson(") !=-1? true: false;
			if(isFound) {
				valObj = JsonPath.read(src, val.substring(0, val.indexOf(".StrToJson(")));
				valObj = JsonPath.parse(cleanString(valObj,2)).json();
				LinkedHashMap<String, String> mapObj = (LinkedHashMap<String, String>) valObj;
				Set writeSet = mapObj.entrySet();
				Iterator writeIterate = writeSet.iterator();
				while (writeIterate.hasNext()) {
					Map.Entry write = (Map.Entry) writeIterate.next();
					String cleansedJSONKey = cleanString(val.substring(val.indexOf(".StrToJson(")+1,val.length()).replace("StrToJson", ""),2);
					if(write.getKey().toString().equals(cleansedJSONKey)) {
						val = write.getValue().toString();
						break;
					}
				}
				ctx = ctx.set(key, val);
				return ctx;
			}
			valObj = JsonPath.read(src, val);
			switch (typeOf(JsonPath.read(dest, key))) {
			case "String":
				ctx = ctx.set(key, cleanString(valObj, 2));
				break;
			default:
				ctx = ctx.set(key, valObj);
			}
		} catch (Exception e) {
			ctx = ctx.set(key, "");
		}
		return ctx;
	}

	@SuppressWarnings("resource")
	public static String readFile(String filename) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String cleanString(Object object, Integer level) {
		String t = object.toString();
		Integer i;
		for (i = 1; i <= level; i++) {
			t = t.substring(1, t.length() - 1);
		}
		return t;
	}

	public static <T> String typeOf(T o) {
		// System.out.println("Instance Type >> " + o.getClass().getSimpleName());
		return o.getClass().getSimpleName();
	}
}
