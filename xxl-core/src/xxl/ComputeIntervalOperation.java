package xxl;

import java.util.ArrayList;
import java.util.List;

public class ComputeIntervalOperation extends Content {
    private Range rangeCells;
    private String operator;
    private Spreadsheet spreadsheet;

    public ComputeIntervalOperation(Range range, Spreadsheet spreadsheet){
        this.rangeCells = range;
        this.spreadsheet = spreadsheet;
    }
    public ComputeIntervalOperation GenerateFunction(String operator) {
        this.operator = operator;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public String asString() {
        List<Integer> listRows = rangeCells.getListRows();
        List<Integer> listCols = rangeCells.getListCols();
        Literal value = value();
        if (value == null) {
            return "#VALUE" + "=" + operator + "(" +
                    listRows.get(0) + ";" + listCols.get(0) + ":" +
                    listRows.get(listRows.size() - 1) + ";" + listCols.get(listCols.size() - 1) + ")";
        }
        return value.asString() + "=" + operator + "(" +  listRows.get(0) + ";" + listCols.get(0) + ":" +
                listRows.get(listRows.size() - 1) + ";" + listCols.get(listCols.size() - 1) + ")";
    }
    @Override
    public Literal value() {
        // Check if the range has columns specified
        if (rangeCells.getListCols() == null) {
            return null;
        }

        String operatorLowerCase = operator.toLowerCase();

        // Initialize result variables based on the operator
        int resultInt = 0;
        String resultString = "'";

        int numValues = 0;  // Number of values in the range
        boolean hasString = false;  // Indicates if any string value is found

        for (int row : rangeCells.getListRows()) {
            for (int col : rangeCells.getListCols()) {
                // Get the cell content and its value
                Cell cell = spreadsheet.getGrid()[row][col];
                if (cell != null) {
                    Content content = cell.getContent();
                    if(content == null){
                        if(operatorLowerCase.equals("concat") || operatorLowerCase.equals("coalesce")){
                            continue;
                        }
                        if(operatorLowerCase.equals("average") || operatorLowerCase.equals("product")){
                            return null;
                        }
                    }
                    Literal cellValue = content.value();

                    if (cellValue != null) {
                        // Update the appropriate result variables based on the operator
                        switch (operatorLowerCase) {
                            case "average":
                                if(cellValue.getClass() != LiteralInteger.class){
                                    return null;
                            }
                                numValues++;
                                resultInt += cellValue.asInt();
                                break;
                            case "product":
                                if(cellValue.getClass() != LiteralInteger.class){
                                    return null;
                                }
                                numValues++;
                                if (numValues == 1) {
                                    resultInt = 1;
                                }
                                resultInt *= cellValue.asInt();
                                break;
                            case "concat":
                                if(cellValue.getClass() != LiteralString.class){
                                    return null;
                                }
                                if (content instanceof LiteralString) {
                                    String cellValueString = cellValue.asString().replaceFirst("'", "");
                                    resultString = (resultString != null) ?
                                            resultString + cellValueString : cellValueString;
                                    hasString = true;
                                }
                                break;
                            case "coalesce":
                                if (!hasString && content instanceof LiteralString) {
                                    resultString = cellValue.asString();
                                    hasString = true;
                                }
                                break;
                            }
                        }
                    }
                }
            }


        // Process the final result based on the operator
        if (operatorLowerCase.equals("average")) {
            if (numValues > 0) {
                // Calculate the integer division (truncate towards zero)
                resultInt = resultInt / numValues;
            } else {
                // No values found, return null
                return null;
            }
            return new LiteralInteger(resultInt);
        } else if (operatorLowerCase.equals("product")) {
            // If no values found, return null
            return numValues > 0 ? new LiteralInteger(resultInt) : null;
        } else if (operatorLowerCase.equals("concat") || operatorLowerCase.equals("coalesce")) {
            // If no string found, return null
            return hasString ? new LiteralString(resultString) : null;
        }

        // Return null if the operator is not recognized
        return null;
    }
    @Override
    public String toString() {
        return "ComputeIntervalOperation";
    }
}
