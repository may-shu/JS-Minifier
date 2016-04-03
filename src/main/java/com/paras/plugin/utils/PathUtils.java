package com.paras.plugin.utils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class PathUtils {
	
	private static final String src = "src";
	
	/**
	 * Combine all string to make a part of the path.
	 */
	public static String combine( String ... paths ) {
		StringBuilder combined = new StringBuilder();
		
		for( String path : paths ){
			combined.append( path );
			combined.append( File.separator );
		}
		
		return combined.toString();
	}
	
	private static String removeLastSlash( String path ) {
		if( path.endsWith( File.separator )) {
			return path.substring(0, path.length() - 1 );
		}
		
		return path;
	}
	
	private static String getParent( String path ) {
		path = removeLastSlash( path );
		return path.substring( path.lastIndexOf( File.separator ) + 1);
	}
	
	public static String convert( String source ) {
		String beforeSource = StringUtils.substringBefore( source, File.separator + src );
		
		String parent = getParent( beforeSource );
		return removeLastSlash( combine( beforeSource, "target", parent, StringUtils.substringAfter( source, "webapp" + File.separator )));
	}
}
