package visibleArithmeticExercise;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CalculateGenerator {
    private static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");

    private static final char PLUS = '+';
    private static final char MINUS = '-';
    private static final char MULTIPLY = '*';
    private static final char DIVIDE = '/';
    private static final char DISPLAY_DIVIDE = '÷';
    private static final char EQUALS = '=';
    private static final String DOT = ".";
    private static final String DOT_REGEX = "\\.";

    private static final char[] operators = {PLUS, MINUS, MULTIPLY, DIVIDE};
    private static final Integer MIN_OPERATOR_COUNT = 2;
    private static final Integer MAX_OPERATOR_COUNT = 4;
    private static final Integer MIN_CAL_VALUE = 0;
    private static final Integer MAX_CAL_VALUE = 100;

//    private static final String FILE_NAME = "result.txt";
//    private static final String STUDENT_NO = "STUDENT_NO";
//    private static final String NEW_LINE = "\r\n";

    private static Random random = new Random();
    public static List<String> answerList=new ArrayList<>();



    public  static ArrayList produceExercises() throws ScriptException {
//        System.out.println("请输入要生成的练习题数量：");
//        Scanner scanner = new Scanner(System.in);
//
//        Integer expressionCount = scanner.nextInt();

        List<String> expressionList = new ArrayList<>();
        for (int i = 0; i <10; i++) {
            expressionList.add(getNextExpression());
            System.out.println(String.format("正在生成第 %s 道题", i));
        }
        return (ArrayList) expressionList;
    }
    private static String getNextExpression() throws ScriptException {

        System.out.println("尝试生成下一道题");

        // 运算符数量
        int operatorCount = random.nextInt(MAX_OPERATOR_COUNT + 1 - MIN_OPERATOR_COUNT) + MIN_OPERATOR_COUNT;

        StringBuilder expression = new StringBuilder();

        // 运算符
        List<Character> operatorList = getOperatorList(operatorCount);

        // 运算数
        List<Integer> calValueList = getCalValueList(operatorCount + 1);

        for (int i = 0; i < operatorList.size(); i++) {
            Character operator = operatorList.get(i);

            Integer previousCalValue = calValueList.get(i);
            Integer nextCalValue = calValueList.get(i + 1);
            expression.append(previousCalValue);

            // 除法校验除数不为0且能被整除
            if (DIVIDE == operator) {
                calValueList.set(i + 1, getDivideCalValue(previousCalValue, nextCalValue));
            } else if (MINUS == operator) {
                // 减法校验被减数大于减数
                // 当包含小数点时向下取整
                String currentResultString = scriptEngine.eval(expression.toString()).toString();
                if (currentResultString.contains(DOT)) {
                    currentResultString = currentResultString.split(DOT_REGEX)[0];
                }
                Integer currentResult = Integer.valueOf(currentResultString);
                while (currentResult < nextCalValue) {
                    nextCalValue = random.nextInt(MAX_CAL_VALUE + 1);
                }
                calValueList.set(i + 1, nextCalValue);
            }

            expression.append(operator);
        }

        expression.append(calValueList.get(operatorCount));

        // 计算当前结果是否为正整数
        String result = scriptEngine.eval(expression.toString()).toString();



        System.out.println("第"+(answerList.size()+1)+"题答案："+result);




        if (result.contains(DOT) || Integer.valueOf(result) < 0) {
            System.out.println("当前题目不符合要求");
            return getNextExpression();
        }else{
            if(answerList.size()>9){
                System.out.println("集合满，清除！！！");
                answerList.clear();
            }
            answerList.add(result);

        }


        String currentExpression = expression.append(EQUALS)
                .toString()
                .replaceAll(String.valueOf(DIVIDE), String.valueOf(DISPLAY_DIVIDE));
        return currentExpression;
    }
    private static List<Character> getOperatorList(int operatorCount) {
        List<Character> operatorList = new ArrayList<>();
        for (int i = 0; i < operatorCount; i++) {
            Character operator = operators[random.nextInt(operators.length)];
            operatorList.add(operator);
        }
        return operatorList;
    }
    private static List<Integer> getCalValueList(int calValueCount) {
        List<Integer> calValueList = new ArrayList<>();
        for (int i = 0; i < calValueCount; i++) {
            calValueList.add(random.nextInt(MAX_CAL_VALUE + 1));
        }
        return calValueList;
    }
    private static int getDivideCalValue(Integer previousCalValue, Integer nextCalValue) {
        if (nextCalValue == 0 || previousCalValue % nextCalValue != 0) {
            nextCalValue = random.nextInt(MAX_CAL_VALUE) + 1;
            return getDivideCalValue(previousCalValue, nextCalValue);
        }
        return nextCalValue;
    }

}
