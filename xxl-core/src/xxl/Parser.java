package xxl;

import java.io.IOException;
import java.io.BufferedReader;

import xxl.exceptions.UnrecognizedEntryException;

import static java.lang.Integer.parseInt;

class Parser {

  private Spreadsheet _spreadsheet;
  private String[] _stringfields;

  Parser(Spreadsheet spreadsheet, String[] stringfields) throws UnrecognizedEntryException {
    _spreadsheet = spreadsheet;
    _stringfields = stringfields;
    parseLine();
  }
  private void parseLine() throws UnrecognizedEntryException {

    if (_stringfields.length == 1) // do nothing
      return;
    
    if (_stringfields.length == 2) {
      String[] address = _stringfields[0].split(";");
      Content content = parseContent();
      _spreadsheet.insertContents(parseInt(address[0]), parseInt(address[1]), content);
    } else
      throw new UnrecognizedEntryException("Wrong format in line: " + _stringfields[0] + _stringfields[1]);
  }

  // parse the beginning of an expression
  public Content parseContent() throws UnrecognizedEntryException{
    char c = _stringfields[1].charAt(0);

    if (c == '=') {
        return parseContentExpression();
    } else
      return parseLiteral(_stringfields[1]);
  }

  private Literal parseLiteral(String literalExpression) throws UnrecognizedEntryException {
    if (literalExpression.charAt(0) == '\'') {;
      LiteralString literalString = new LiteralString(literalExpression);
      return literalString;
    }
    else {
      try {
        int val = parseInt(literalExpression);
        LiteralInteger literalinteger = new LiteralInteger(val);
        return literalinteger;
      } catch (NumberFormatException nfe) {
        throw new UnrecognizedEntryException("Número inválido: " + literalExpression);
      }
    }
  }

  private Content parseContentExpression() throws UnrecognizedEntryException {
        if (_stringfields[1].contains("(")) {
            return parseFunction();
        }
        // It is a reference
        String[] address = _stringfields[1].split(";");
        Reference reference = new Reference(Integer.parseInt(address[0].substring(1)), Integer.parseInt(address[1]),  _spreadsheet);
        int g = 0;
        return reference;
        }
  private Content parseFunction() throws UnrecognizedEntryException  {
    String[] components = _stringfields[1].split("[()]");
    if (components[1].contains(","))
      return parseBinaryFunction(components[0], components[1]);

    return parseIntervalFunction(components[0], components[1]);
  }
  private Content parseBinaryFunction(String functionName, String args) throws UnrecognizedEntryException {
    String[] arguments = args.split(",");
    Content arg0 = parseArgumentExpression(arguments[0]);
    Content arg1 = parseArgumentExpression(arguments[1]);
    ComputeOperation op1 = new ComputeOperation(arg0, arg1);

    return switch (functionName.substring(1)) {
      case "ADD" -> op1.GenerateFunction( "+");
      case "SUB" -> op1.GenerateFunction( "-");
      case "MUL" -> op1.GenerateFunction( "*");
      case "DIV" -> op1.GenerateFunction( "/");
      default -> throw new UnrecognizedEntryException(functionName);
    };
  }
    private Content parseArgumentExpression(String argExpression) throws UnrecognizedEntryException {
      if (argExpression.contains(";")  && argExpression.charAt(0) != '\'') {
        String[] address = argExpression.split(";");
        Reference reference = new Reference(Integer.parseInt(address[0].trim()), Integer.parseInt(address[1]), _spreadsheet);
        return reference;
        // pode ser diferente do anterior em parseContentExpression
      } else
        return parseLiteral(argExpression);
    }
  private Content parseIntervalFunction(String functionName, String rangeDescription) throws UnrecognizedEntryException  {
    Range range = new Range(rangeDescription, _spreadsheet.get_rows(), _spreadsheet.get_columns());
    ComputeIntervalOperation op2 = new ComputeIntervalOperation(range, _spreadsheet);
    return switch (functionName.substring(1)) {
      case "CONCAT" -> op2.GenerateFunction("CONCAT");
      case "COALESCE" -> op2.GenerateFunction ("COALESCE");
      case "PRODUCT" -> op2.GenerateFunction("PRODUCT");
      case "AVERAGE" -> op2.GenerateFunction("AVERAGE");
      default -> throw new UnrecognizedEntryException(functionName);
    };
  }
}




  


