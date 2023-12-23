package xxl;

public class ComputeOperation extends Content {
    private final Content operand1;
    private final Content operand2;
    private String operator;

    public ComputeOperation(Content operand1, Content operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public Content getOperand1() {
        return operand1;
    }

    public Content getOperand2() {
        return operand2;
    }
    public ComputeOperation GenerateFunction(String operator) {
        this.operator = operator;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public String asString() {
        String operatorStr = "";
        switch (operator) {
            case "+":
                operatorStr = "ADD";
                break;
            case "-":
                operatorStr = "SUB";
                break;
            case "*":
                operatorStr = "MUL";
                break;
            case "/":
                operatorStr = "DIV";
                break;
        }

        if (operand1.getClass() == Reference.class && operand2.getClass() != Reference.class) {
            Reference refOperand1 = (Reference) operand1;
            int op1Row = refOperand1.get_row();
            int op1Col = refOperand1.get_column();
            if(value() == null) {
                return "#VALUE" + "=" + operatorStr + "(" + op1Row + ";" + op1Col + "," + operand2.asString() + ")";
            }
            return value().asString() + "=" + operatorStr + "(" + op1Row + ";" + op1Col + "," + operand2.asString() + ")";
        }

        if (operand2.getClass() == Reference.class && operand1.getClass() != Reference.class) {
            Reference refOperand2 = (Reference) operand2;
            int op2Row = refOperand2.get_row();
            int op2Col = refOperand2.get_column();
            if(value() == null ) {
                return "#VALUE" + "=" + operatorStr + "(" + operand1.asString() + "," + op2Row + ";" + op2Col + ")";
            }
            return value().asString() + "=" + operatorStr + "(" + operand1.asString() + "," + op2Row + ";" + op2Col + ")";
        }
        if (operand2.getClass() == Reference.class && operand1.getClass() == Reference.class) {
            Reference refOperand2 = (Reference) operand2;
            Reference refOperand1 = (Reference) operand1;
            int op1Row = refOperand1.get_row();
            int op1Col = refOperand1.get_column();
            int op2Row = refOperand2.get_row();
            int op2Col = refOperand2.get_column();
            if(value() == null ) {
                return "#VALUE" + "=" + operatorStr + "(" + op1Row + ";" + op1Col + "," + op2Row + ";" + op2Col + ")";
            }
            return value().asString() + "=" + operatorStr + "(" + op1Row + ";" + op1Col + "," + op2Row + ";" + op2Col + ")";
        }


        if(value() == null ) {
            return "#VALUE" + "=" + operatorStr + "(" + operand1.asString() + "," + operand2.asString() + ")";
        }
        return value().asString() + "=" + operatorStr + "(" + operand1.asString() + "," + operand2.asString() + ")";
    }
    @Override
    public Literal value() {
        // Calculate the value of the operation and return it as a Literal
        Literal op1Value = operand1.value();
        Literal op2Value = operand2.value();
        if(op1Value == null || op2Value == null || op1Value.getClass() == LiteralString.class || op2Value.getClass() == LiteralString.class){
            return null;
        }
        int result = 0;

        if (operator.equals("+")) {
            result = op1Value.asInt() + op2Value.asInt();
        } else if (operator.equals("-")) {
            result = op1Value.asInt() - op2Value.asInt();
        } else if (operator.equals("*")) {
            result = op1Value.asInt() * op2Value.asInt();
        } else if (operator.equals("/")) {
            result = op1Value.asInt() / op2Value.asInt();
        }
        return new LiteralInteger(result);
    }

    @Override
    public String toString() {
        return "ComputeOperation";
    }
}
