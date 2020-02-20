package JsonUtilities;

import java.io.BufferedReader;
import java.io.FileReader;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class Utility {
	public static DocumentContext setJson(DocumentContext ctx, Object src, Object dest, String key, String val) {
		try {
			switch (typeOf(JsonPath.read(dest, key))) {
			case "String":
				ctx = ctx.set(key, cleanString(JsonPath.read(src, val), 2));
				break;
			default:
				ctx = ctx.set(key, JsonPath.read(src, val));
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
