
// $Id: TologParser.java,v 1.16 2005/12/14 13:08:35 grove Exp $

package net.ontopia.topicmaps.query.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import net.ontopia.topicmaps.utils.ctm.Template;
import net.ontopia.topicmaps.utils.ctm.CTMLexer;
import net.ontopia.topicmaps.utils.ctm.CTMParser;
import net.ontopia.topicmaps.query.core.InvalidQueryException;

import antlr.Token;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;

/**
 * INTERNAL: The tolog query parser.
 */
public class TologParser {
  protected ParseContextIF context;
  protected ParseContextIF localcontext; // needed for CTM part of INSERT
  protected TologOptions options;
  private static final Pattern insertP =
    Pattern.compile("(^|\\s+)insert\\s+", Pattern.CASE_INSENSITIVE);
  private static final Pattern fromP =
    Pattern.compile("\\s+from\\s+", Pattern.CASE_INSENSITIVE);

  public TologParser(ParseContextIF context, TologOptions options) {
    this.context = context;
    this.options = options;
  }

  /**
   * Returns a parsed SELECT statement.
   */
  public TologQuery parseQuery(String query) throws InvalidQueryException {
    return parseQuery(new StringReader(query));
  }
  
  /**
   * Returns a parsed SELECT statement.
   */
  public TologQuery parseQuery(Reader queryReader) throws InvalidQueryException {
    try {
      RealTologParser parser = makeParser(queryReader);
      parser.setContext(new LocalParseContext(context));
      parser.query();
      return (TologQuery) parser.getStatement();
    }
    catch (AntlrWrapException ex) {
      Exception e = ex.getException();
      if (e instanceof InvalidQueryException)
        throw (InvalidQueryException)e;
      else 
        throw new InvalidQueryException(e);
    }
    catch (JFlexWrapException ex) {
      Exception e = ex.getException();
      if (e instanceof InvalidQueryException)
        throw (InvalidQueryException)e;
      else 
        throw new InvalidQueryException(e);
    }
    catch (RecognitionException ex) {
      throw new InvalidQueryException("Lexical error at " /*+ getBaseAddress().getAddress() + ":"*/ + ex.line + ":" + ex.column + ": "+ ex.getMessage());
    }
    catch (TokenStreamRecognitionException e) {
      RecognitionException ex = e.recog;
      throw new InvalidQueryException("Lexical error at " /*+ getBaseAddress().getAddress() + ":"*/ + ex.line + ":" + ex.column + ": "+ ex.getMessage());
    }
    catch (TokenStreamIOException ex) {
      throw new InvalidQueryException(ex.io.toString());
    }
    catch (TokenStreamException ex) {
      throw new InvalidQueryException("Lexical error: " + ex.getMessage());
    }
  }

  /**
   * Returns a parsed INSERT/UPDATE/MERGE/DELETE statement.
   */
  public TologStatement parseStatement(String query) throws InvalidQueryException {
    if (isInsertStatement(query)) {
      int insertpos = findInsert(query);
      int frompos = findFrom(query, insertpos);

      String ctm;
      if (frompos != -1) {
        ctm = query.substring(insertpos + 6, frompos);
        query = query.substring(0, insertpos + 6) + " " +
                query.substring(frompos);
      } else {
        ctm = query.substring(insertpos + 6);
        query = query.substring(0, insertpos + 6);
      }
        
      InsertStatement stmt = (InsertStatement) parseStatement(new StringReader(query));
      stmt.setCTMPart(ctm, localcontext);
      return stmt;
    } else
      return parseStatement(new StringReader(query));
  }
  
  /**
   * Returns a parsed INSERT/UPDATE/MERGE/DELETE statement.
   */
  private TologStatement parseStatement(Reader queryReader)
    throws InvalidQueryException {
    try {
      RealTologParser parser = makeParser(queryReader);
      localcontext = new LocalParseContext(context);
      parser.setContext(localcontext);
      parser.updatestatement();
      return parser.getStatement();
    }
    catch (AntlrWrapException ex) {
      Exception e = ex.getException();
      if (e instanceof InvalidQueryException)
        throw (InvalidQueryException)e;
      else 
        throw new InvalidQueryException(e);
    }
    catch (RecognitionException ex) {
      throw new InvalidQueryException("Lexical error at " /*+ getBaseAddress().getAddress() + ":"*/ + ex.line + ":" + ex.column + ": "+ ex.getMessage());
    }
    catch (TokenStreamRecognitionException e) {
      RecognitionException ex = e.recog;
      throw new InvalidQueryException("Lexical error at " /*+ getBaseAddress().getAddress() + ":"*/ + ex.line + ":" + ex.column + ": "+ ex.getMessage());
    }
    catch (TokenStreamIOException ex) {
      throw new InvalidQueryException(ex.io.toString());
    }
    catch (TokenStreamException ex) {
      throw new InvalidQueryException("Lexical error: " + ex.getMessage());
    }
  }
  
  public ParseContextIF parseDeclarations(String decls) throws InvalidQueryException {
    try {
      ParseContextIF ctxt = new LocalParseContext(context);
      RealTologParser parser = makeParser(new StringReader(decls));
      parser.setContext(ctxt);
      parser.declarations();
      return ctxt;
    }
    catch (AntlrWrapException ex) {
      Exception e = ex.getException();
      if (e instanceof InvalidQueryException)
        throw (InvalidQueryException)e;
      else 
        throw new InvalidQueryException(e);
    }
    catch (RecognitionException ex) {
      throw new InvalidQueryException("Lexical error at " /*+ getBaseAddress().getAddress() + ":"*/ + ex.line + ":" + ex.column + ": "+ ex.getMessage());
    }
    catch (TokenStreamRecognitionException e) {
      RecognitionException ex = e.recog;
      throw new InvalidQueryException("Lexical error at " /*+ getBaseAddress().getAddress() + ":"*/ + ex.line + ":" + ex.column + ": "+ ex.getMessage());
    }
    catch (TokenStreamIOException ex) {
      throw new InvalidQueryException(ex.io.toString());
    }
    catch (TokenStreamException ex) {
      throw new InvalidQueryException("Lexical error: " + ex.getMessage());
    }
  }  

  public void load(String ruleset) throws InvalidQueryException {
    load(new StringReader(ruleset));
  }

  public void load(Reader reader) throws InvalidQueryException {
    try {
      RealTologParser parser = makeParser(reader);
      parser.setContext(context);
      parser.ruleset();
    }
    catch (AntlrWrapException ex) {
      Exception e = ex.getException();
      if (e instanceof InvalidQueryException)
        throw (InvalidQueryException)e;
      else 
        throw new InvalidQueryException(e);
    }
    catch (RecognitionException ex) {
      throw new InvalidQueryException("Lexical error at " /*+ getBaseAddress().getAddress() + ":"*/ + ex.line + ":" + ex.column + ": "+ ex.getMessage());
    }
    catch (TokenStreamRecognitionException e) {
      RecognitionException ex = e.recog;
      throw new InvalidQueryException("Lexical error at " /*+ getBaseAddress().getAddress() + ":"*/ + ex.line + ":" + ex.column + ": "+ ex.getMessage());
    }
    catch (TokenStreamIOException ex) {
      throw new InvalidQueryException(ex.io.toString());
    }
    catch (TokenStreamException ex) {
      throw new InvalidQueryException("Lexical error: " + ex.getMessage());
    }
  }

  public ParseContextIF getContext() {
    return context;
  }
  
  // --- Internal methods

  private RealTologParser makeParser(Reader reader) throws InvalidQueryException {
    TologLexer lexer = new TologLexer(reader, options);
    RealTologParser parser = new RealTologParser(lexer);
    parser.init(lexer);
    return parser;
  }

  // --- Extra code to deal with INSERT statements

  private boolean isInsertStatement(String query) {
    Matcher matcher = insertP.matcher(query);
    return matcher.find();
  }

  private int findInsert(String query) {
    Reader reader = new StringReader(query);
    TologLexer lexer = new TologLexer(reader, options);
    Token token = lexer.nextToken();
    while (token.getType() != RealTologParser.INSERT)
      token = lexer.nextToken();
    return lexer.getStartOfToken();
  }

  private int findFrom(String query, int insertpos) {
    query = query.substring(insertpos + 6);
    Reader reader = new StringReader(query);
    CTMLexer lexer = new CTMLexer(reader);
    Token token = lexer.nextToken();
    while (!(token.getType() == CTMParser.IDENTIFIER &&
             token.getText().equalsIgnoreCase("from")) && 
           token.getType() != CTMParser.EOF)
      token = lexer.nextToken();

    if (token.getType() == CTMParser.EOF)
      return -1;
    else
      return lexer.getStartOfToken() + insertpos + 6;
  }
}
