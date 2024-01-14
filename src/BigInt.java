import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Pattern;

public class BigInt {

    private ArrayList<Byte> number = new ArrayList<>();
    private boolean negative;

    public BigInt(byte[] b){
        for (int i = 0; i < b.length; i++){
            if (b[i] > 9) throw new InputMismatchException();
            number.add(b[i]);
        }
        negative = false;
    }
    public BigInt(String n){
        int negative = 0;
        String[] num = n.split("");
        if (num[0].equals("-")) {
            this.negative = true;
            negative = 1;
        }
        else this.negative = false;

        try{
            for (int i = num.length - 1; i >= negative; i--){
                number.add(Byte.valueOf(num[i]));
            }
        }
        catch (Exception e){
            throw new InputMismatchException();
        }
    }
    public BigInt(){
    }
    public BigInt(List<Byte> b){
        this.number.addAll(b);
    }


    public ArrayList<Byte> getNumber() {
        return number;
    }
    public void setNumber(ArrayList<Byte> number) {
        this.number = number;
    }

    public boolean isNegative() {
        return negative;
    }
    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    public void addByte(byte b){
        if (b > 9) throw new InputMismatchException();
        number.add(b);
    }

    public byte max(BigInt n1, BigInt n2){
        if (n1.isNegative() && !n2.isNegative()) return -1;
        if (!n1.isNegative() && n2.isNegative()) return 1;
        int reverseResult = 0;
        if (n1.isNegative() && n2.isNegative()) reverseResult = 2;

        int size = n1.getNumber().size();
        for (int i = 1; i <= size; i++){
            byte temp = (byte) (n1.getNumber().get(size-i) - n2.getNumber().get(size-i));
            if (temp > 0) return (byte) (1 - reverseResult);
            if (temp < 0) return (byte) ((-1) + reverseResult);
        }
        return 0;
    }

    public void format(){
        int size = number.size();
        for (int i = 1; i < size; i++){
            if (number.get(size - i) == 0) number.remove(size - i);
            else break;
        }
    }

    public BigInt processLenght(BigInt number2){
        int sizeDiff = number.size() - number2.getNumber().size();

        if (sizeDiff < 0) {
            for (int i = 0; i < -sizeDiff; i++){
                number.add((byte) 0);
            }
            return number2;
        }
        else if (sizeDiff > 0) {
            for (int i = 0; i < sizeDiff; i++){
                number2.addByte((byte) 0);
            }
            return number2;
        }
        return number2;
    }

    public void add(BigInt number2){
        if ((this.negative && !number2.negative) || !this.negative && number2.negative) {
            subtract(number2);
            return;
        }
        number2 = processLenght(number2);

        int carry = 0;
        for (int i = 0; i < number.size(); i++){
            int value = number.get(i) + number2.getNumber().get(i) + carry;
            if (value > 9) {
                carry = 1;
                number.set(i, (byte) (value - 10));
            }
            else {
                carry = 0;
                number.set(i, (byte) (value));
            }
        }
        if (carry == 1) number.add((byte) 1);
    }

    public void subtract(BigInt number2){
        BigInt newNum = new BigInt(number2.getNumber());
        newNum = processLenght(newNum);
        switch (max(this, newNum)) {
            case 1 -> setNegative(false);
            case 0 -> {
                number = BigInt.ZERO().getNumber();
                return;
            }
            case (-1) -> {
                ArrayList<Byte> temp = getNumber();
                number = newNum.getNumber();
                newNum.setNumber(temp);
                setNegative(true);
            }
        }

        int carry = 0;
        for (int i = 0; i < number.size(); i++){
            int x = number.get(i) - carry;
            int y = newNum.getNumber().get(i);
            if (x < y){
                x += 10;
                carry = 1;
            }
            else{
                carry = 0;
            }
            number.set(i, (byte) (x - y));
        }
    }

    public void shiftToRight(int amount){
        Collections.rotate(number, amount);
        for (int i = 0; i < amount; i++){
            number.add(number.get(i));
            number.set(i, (byte) 0);
        }
    }

    public static BigInt oneDigitMultiply(BigInt number, Byte n){
        BigInt temp = new BigInt(number.getNumber());
        if (n > 9 || n < 0) throw new InputMismatchException();
        int carry = 0;
        for (int i = 0; i < temp.getNumber().size(); i++){
            int x = (temp.getNumber().get(i) * n) + carry;
            if (x > 9) {
                carry = (int) Math.floor(x / 10.0);
                x -= carry * 10;
            }
            else carry = 0;
            temp.getNumber().set(i, (byte) (x));
        }
        if (carry > 0) temp.getNumber().add((byte) carry);
        return temp;
    }

    public static BigInt slowMultiply(BigInt n1, BigInt n2){
        return recursiveMultiply(n1, n2, n2.getNumber().size() - 1);
    }

    public static BigInt recursiveMultiply(BigInt n1, BigInt n2, int i){
        BigInt answer = BigInt.oneDigitMultiply(n1, n2.getNumber().get(i));
        answer.shiftToRight(i);
        if (i > 0) answer.add(recursiveMultiply(n1, n2, i - 1));
        return answer;
    }

    /*public void inverse(){
        BigInt x1 = this;
        BigInt x2 = 0;
        float temp = num;
        float e = 0.001f;
        do {
            x1 = temp;
            x2 = x1 * (2 - num*x1);
            temp=x2;
        }
        while(Math.abs(x2-x1)> e);

        System.out.println(num + " ----> " + x2);
        return x2;
    }*/


    /*public static int karatsuba(BigInt number1, BigInt number2){
        if (number1.getNumber().size() == 1 || number2.getNumber().size() == 1){
            return number1.getNumber().get(0) * number2.getNumber().get(0);
        }

        int m =  Math.max(number1.getNumber().size(), number2.getNumber().size());
        int m2 = (int) Math.ceil(m / 2.0);

        number1.processLenght(number2);
        List<Byte> low1 = number1.getNumber().subList(0, m2);
        number1.processLenght(number2);
        List<Byte> high1 = number1.getNumber().subList(m2, m);
        number1.processLenght(number2);
        List<Byte> low2 = number2.getNumber().subList(0, m2);
        number1.processLenght(number2);
        List<Byte> high2 = number2.getNumber().subList(m2, m);

        int z0 = karatsuba(new BigInt(low1), new BigInt(low2));
        BigInt temp1 = new BigInt(low1);
        temp1.add(new BigInt(high1));
        BigInt temp2 = new BigInt(low2);
        temp1.add(new BigInt(high2));
        int z1 = karatsuba(temp1, temp2);
        int z2 = karatsuba(new BigInt(high1), new BigInt(high2));

        return (z2 * 10 ^ (m2 * 2)) + ((z1 - z2 - z0) * 10 ^ m2) + z0;
    }*/

    public static BigInt ZERO(){
        return new BigInt(new byte[]{0});
    }
    public static BigInt ONE(){
        return new BigInt(new byte[]{1});
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < number.size(); i++){
            str.append(number.get(i));
        }
        if (this.negative) str.append("-");
        return str.reverse().toString();
    }

    public static float inverse(float num) {
        float x1 = num;
        float x2 = 0;
        float temp = num;
        float e = 0.0000001f;
        do {
            x1 = temp;
            x2 = x1 * (2 - num*x1);
            temp=x2;
        }
        while(Math.abs(x2-x1)> e);

        System.out.println(num + " ----> " + x2);
        return x2;
    }

}
