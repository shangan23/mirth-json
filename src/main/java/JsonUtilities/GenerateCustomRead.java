package JsonUtilities;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class GenerateCustomRead extends Utility {
	
	@SuppressWarnings("rawtypes")
	public static void CustomRead() throws FileNotFoundException {

		String basePath = "src/main/java/JsonUtilities/JsonFiles/";;

		String customReader = readFile(basePath + "custom.json");
		String fhirReader = readFile(basePath + "fhirR4.json");
		String mapReader = readFile(basePath + "mapper.json");

		Object customJson = JsonPath.parse(customReader).json();
		Object fhirJson = JsonPath.parse(fhirReader).json();
		Object mapJson = JsonPath.parse(mapReader).json();
		DocumentContext fhir = JsonPath.parse(customJson);

		LinkedHashMap<String, String> mapObj = JsonPath.parse(mapJson).read("$.read");
		Set writeSet = mapObj.entrySet();
		Iterator writeIterate = writeSet.iterator();
		while (writeIterate.hasNext()) {
			Map.Entry write = (Map.Entry) writeIterate.next();
			fhir = setJson(fhir, fhirJson,customJson, write.getValue().toString(), write.getKey().toString());
		}

		System.out.println(fhir.jsonString());
	}


}
