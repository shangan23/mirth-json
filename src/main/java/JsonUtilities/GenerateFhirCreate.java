package JsonUtilities;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class GenerateFhirCreate extends Utility {
	
	@SuppressWarnings("rawtypes")
	public static void CreateFhirbyFile() throws FileNotFoundException {

		String basePath = "src/main/java/JsonUtilities/JsonFiles/conditions/";;

		String customReader = readFile(basePath + "customCreate.json");
		String fhirReader = readFile(basePath + "fhirCreate.json");
		String mapReader = readFile(basePath + "mapper.json");

		Object customJson = JsonPath.parse(customReader).json();
		Object fhirJson = JsonPath.parse(fhirReader).json();
		Object mapJson = JsonPath.parse(mapReader).json();
		DocumentContext fhir = JsonPath.parse(fhirJson);

		LinkedHashMap<String, String> mapObj = JsonPath.parse(mapJson).read("$.write");
		Set writeSet = mapObj.entrySet();
		Iterator writeIterate = writeSet.iterator();
		while (writeIterate.hasNext()) {
			Map.Entry write = (Map.Entry) writeIterate.next();
			fhir = setJson(fhir, customJson, fhirJson, write.getKey().toString(), write.getValue().toString());
		}

		System.out.println(fhir.jsonString());
	}
	
	@SuppressWarnings("rawtypes")
	public static String CreateFhir(String custom, String fhir, String mapper) throws FileNotFoundException {
		
	Object customJson = JsonPath.parse(custom).json();
		Object fhirJson = JsonPath.parse(fhir).json();
		Object mapJson = JsonPath.parse(mapper).json();
		DocumentContext fhirCtx = JsonPath.parse(fhirJson);

		LinkedHashMap<String, String> mapObj = JsonPath.parse(mapJson).read("$.write");
		Set writeSet = mapObj.entrySet();
		Iterator writeIterate = writeSet.iterator();
		while (writeIterate.hasNext()) {
			Map.Entry write = (Map.Entry) writeIterate.next();
			fhirCtx = setJson(fhirCtx, customJson, fhirJson, write.getKey().toString(), write.getValue().toString());
		}

		return fhirCtx.jsonString();
	}


}
