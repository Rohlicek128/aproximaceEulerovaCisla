public class Main {
    public static void main(String[] args) {

        /*try {
            EulersNumber e = new EulersNumber();
            Scanner input = new Scanner(System.in);

            System.out.print("NUMBER OF DIGITS OF e: ");
            int decimals = input.nextInt();
            //System.out.println(decimals + " DIGITS OF e");

            long startTime = System.currentTimeMillis();
            e.eulersAprx(decimals);
            long elapsedTime = System.currentTimeMillis() - startTime;

            System.out.println(e.tempNo.toString());
            System.out.println("TIME: " + elapsedTime + " ms");

            System.out.println(e.trueE);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }*/

        BigInt bn1 = new BigInt("555845138568445484763218598954216");
        BigInt bn2 = new BigInt("-5851412135468965411");
        BigInt bn3 = new BigInt("478213");

        BigDub bd1 = new BigDub("123.123456789");
        BigDub bd2 = new BigDub("0.0001");
        System.out.println(bd1);
        System.out.println(bd2);

        bd1.slowMultiply(bd2);
        System.out.println(bd1);

        BigInt.inverse(24f);

    }
}