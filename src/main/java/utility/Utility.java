package utility;



import java.util.HashMap;
import java.util.Random;

public class Utility extends Driver{


    public String getCurrentDirectory(){
        String currectDir =System.getProperty("user.dir");
        return  currectDir;
    }
    public String getRandomNumber(int digit){
        Random random = new Random();
        String randomNumber="";
        while(digit>0){
            randomNumber = randomNumber+String.valueOf(random.nextInt(10)); // Generates a number between 0 and 9
            digit--;
        }
        System.out.println("Random Number: " + randomNumber);
        return randomNumber;
    }




}
