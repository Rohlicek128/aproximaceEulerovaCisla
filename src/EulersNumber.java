import java.math.BigDecimal;
import java.math.RoundingMode;

public class EulersNumber {

    BigDecimal tempNo = new BigDecimal("1.0");
    BigDecimal lastF = new BigDecimal("1");

    //BigDecimal trueE = new BigDecimal("2.718281828459045235360287471352662497757247093699959574966967627724076630353547594571382178525166427427466391932003059921817413596629043572900334295260595630738132328627943490763233829880753195251019011573834187930702154089149934884167509244761460668082264800168477411853742345442437107539077744992069");

    public void eulersAprx(int decimals) throws Exception {
        if (decimals < 1) throw new Exception("Must have 1 or more digits!");

        for (int n = 1; n <= decimals; n++){
            lastF = lastF.multiply(BigDecimal.valueOf(n));
            this.tempNo = this.tempNo.add(BigDecimal.ONE.divide(lastF, decimals, RoundingMode.DOWN));
        }
    }

    public BigDecimal factorial(int n){
        if (n <= 0) return new BigDecimal("1.0");
        return BigDecimal.valueOf(n).multiply(factorial(n - 1));
    }


}
