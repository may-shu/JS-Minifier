package com.paras.plugin.minifier;

import org.apache.maven.plugin.logging.Log;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

public class YuiErrorReporter
  implements ErrorReporter
{
  private Log logger;
  private String file;
  
  public YuiErrorReporter(Log logger, String file )
  {
    this.logger = logger;
    this.file = file;
  }
  
  public void warning(String message, String source, int line, String lineSource, int lineOffset)
  {
    if (line < 0) {
      this.logger.warn(message);
    } else {
      this.logger.warn("In " + file + " : At " + line + ":" + lineOffset + " | " + message);
    }
  }
  
  public void error(String message, String source, int line, String lineSource, int lineOffset)
  {
    if (line < 0) {
      this.logger.error(message);
    } else {
      this.logger.error("In " + file + " : At " + line + ":" + lineOffset + " | " + message);
    }
  }
  
  public EvaluatorException runtimeError(String message, String source, int line, String lineSource, int lineOffset)
  {
    error(message, source, line, lineSource, lineOffset);
    return new EvaluatorException(message);
  }
}