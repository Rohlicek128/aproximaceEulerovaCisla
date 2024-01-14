import java.util.InputMismatchException;

public class BigDub {

    private BigInt preDecimal = new BigInt();
    private BigInt postDecimal = new BigInt();
    private boolean negative;

    public BigDub() {
    }
    // "-574894.00013555550101215"
    // " 543210.0123456789...    "
    public BigDub(String n){
        int negative = 0;
        String[] decimals = n.split("\\.");
        if (decimals.length == 1) throw new InputMismatchException();

        String[] pre = decimals[0].split("");
        String[] post = decimals[1].split("");
        if (pre[0].equals("-")) {
            this.negative = true;
            negative = 1;
        }
        else this.negative = false;

        try{
            for (int i = pre.length - 1; i >= negative; i--){
                preDecimal.addByte(Byte.parseByte(pre[i]));
            }
            for (int i = 0; i < post.length; i++){
                postDecimal.addByte(Byte.parseByte(post[i]));
            }
        }
        catch (Exception e){
            throw new InputMismatchException();
        }
    }

    public BigInt getPreDecimal() {
        return preDecimal;
    }
    public void setPreDecimal(BigInt preDecimal) {
        this.preDecimal = preDecimal;
    }

    public BigInt getPostDecimal() {
        return postDecimal;
    }
    public void setPostDecimal(BigInt postDecimal) {
        this.postDecimal = postDecimal;
    }

    public boolean isNegative() {
        return negative;
    }
    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    public void setBigDub(BigDub bd){
        setPreDecimal(bd.getPreDecimal());
        setPostDecimal(bd.getPostDecimal());
        setNegative(bd.isNegative());
    }

    public void addBytePre(Byte b){
        if (b > 9) throw new InputMismatchException();
        preDecimal.addByte(b);
    }

    public void addBytePost(Byte b){
        if (b > 9) throw new InputMismatchException();
        postDecimal.addByte(b);
    }

    // "-57489400013555550101215"
    // "           ...9876543210"
    public BigInt join(){
        BigInt bi = new BigInt();
        for (int i = postDecimal.getNumber().size() - 1; i >= 0; i--){
            bi.addByte(postDecimal.getNumber().get(i));
        }
        for (int i = 0; i < preDecimal.getNumber().size(); i++){
            bi.addByte(preDecimal.getNumber().get(i));
        }
        bi.setNegative(this.negative);
        return bi;
    }

    // "-57489400013555550101215"
    // 17
    public BigDub separate(BigInt bi, int decimal){
        BigDub bd = new BigDub();

        if (decimal == 0) bd.setPostDecimal(BigInt.ZERO());
        else {
            for (int i = decimal - 1; i >= 0; i--){
                bd.addBytePost(bi.getNumber().get(i));
            }
        }

        if (decimal >= bi.getNumber().size()){
            bd.setPreDecimal(BigInt.ZERO());
            bd.getPostDecimal().shiftToRight(decimal - bi.getNumber().size());
        }
        else {
            for (int i = decimal; i < bi.getNumber().size(); i++){
                bd.addBytePre(bi.getNumber().get(i));
            }
        }
        bd.setNegative(bi.isNegative());
        return bd;
    }

    public void add(BigDub bd2){
        int decimal1 = this.postDecimal.getNumber().size();
        int decimal2 = bd2.getPostDecimal().getNumber().size();
        BigInt joined1 = this.join();
        BigInt joined2 = bd2.join();

        int decDif = decimal1 - decimal2;
        if (decDif > 0) joined2.shiftToRight(decDif);
        if (decDif < 0) joined1.shiftToRight(-decDif);

        joined1.add(joined2);
        this.setBigDub(separate(joined1, Math.max(decimal1, decimal2)));
        this.preDecimal.format();
        this.postDecimal.format();
    }

    public void slowMultiply(BigDub bd2){
        int decimal1 = this.postDecimal.getNumber().size();
        int decimal2 = bd2.getPostDecimal().getNumber().size();
        BigInt joined1 = this.join();
        BigInt joined2 = bd2.join();

        int decDif = decimal1 - decimal2;
        if (decDif > 0) joined2.shiftToRight(decDif);
        if (decDif < 0) joined1.shiftToRight(-decDif);

        this.setBigDub(separate(BigInt.slowMultiply(joined1, joined2), Math.max(decimal1, decimal2) * 2));
        this.preDecimal.format();
        this.postDecimal.format();
    }



    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = postDecimal.getNumber().size() - 1; i >= 0; i--){
            str.append(postDecimal.getNumber().get(i));
        }
        str.append(".");
        for (int i = 0; i < preDecimal.getNumber().size(); i++){
            str.append(preDecimal.getNumber().get(i));
        }
        if (this.negative) str.append("-");
        return str.reverse().toString();
    }

}
