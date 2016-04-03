package com.paras.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

import com.paras.plugin.minifier.CompressorOptions;
import com.paras.plugin.minifier.YuiErrorReporter;
import com.paras.plugin.utils.PathUtils;


/**
 * Maven Plugin to compress a single JavaScript file.
 * 
 * @author Gaurav
 */
@Mojo(name="minify-one")
public class MinifyOne extends AbstractMojo{

	/**
	 * File path to be minified.
	 */
	@Parameter
	private File source;

	/**
	 * Whether to obfuscate the source code.
	 */
	@Parameter
	private boolean obfuscate;

	/**
	 * List of excludes.
	 */
	@Parameter
	private List<String> excludes;
	
	private Log logger;

	public void execute() throws MojoExecutionException {
		logger = getLog();

		try{
			if( isExcluded( source )) {
				File target = new File( PathUtils.convert( source.getAbsolutePath() ));
				FileUtils.copyFile( source, target );
			} else {

				CompressorOptions options = new CompressorOptions();
				options.munge = obfuscate;


				Reader reader = new InputStreamReader( new FileInputStream( source ));
				JavaScriptCompressor compressor = new JavaScriptCompressor( reader, new YuiErrorReporter(logger, source.getName() ));

				reader.close();
				reader = null;

				File compressed = new File( PathUtils.convert( source.getAbsolutePath() ) );
				compressed.getParentFile().mkdirs();
				compressed.createNewFile();

				OutputStreamWriter writer = new OutputStreamWriter( new FileOutputStream( compressed ));
				compressor.compress( writer, options.lineBreakPos, options.munge, options.verbose, options.preserveSemiColons, options.preventOptimizatins);

				writer.flush();
				writer.close();
			}
		}catch (IOException ex) {
			logger.error("MinifyOne | execute | IOException caught | " + ex.getMessage());
			ex.printStackTrace();
		}
	}


	/**
	 * Check if file is in exclusion list.
	 */
	private boolean isExcluded( File file ) {
		boolean result = false;
		String name = file.getName();
		
		for( String str : excludes ) {
			if( name.equals( str )) {
				result = true;
				
				break;
			}
		}
		
		return result;
	}
}