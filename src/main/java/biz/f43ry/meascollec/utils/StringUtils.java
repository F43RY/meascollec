package biz.f43ry.meascollec.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtils {

	public static String extractXPath(String measObjLdn) {
		String xPath = null;
		xPath = Arrays.stream(measObjLdn.split(","))
	            .map(element -> element.split("=")[0])  
	            .collect(Collectors.joining("/"));  
		return xPath;
	}
}
