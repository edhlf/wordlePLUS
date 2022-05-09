package dbUtil;

import java.util.Random;

public class wordGenerate {

    //Word Database
    public String four = "life,love,near,ness,ring,wolf,fish,five,king,else,Tree,over,time,able,have,sing,star,city,soul,rich";
    public String [] fourLettersWord  = four.split(",");
    //public String[] fourLettersWord = {"ache","acid","amen","arch","area","atom","baby","bare","belt"};

    public String five = "seven,world,about,again,heart,pizza,water,happy,sixty,board,month,Angel,death,green,music";
    public String [] fiveLettersWord  = five.split(",");
    //public String[] fiveLettersWord = {"Abuse","Apple","Award","Chair","Court","Crowd","Dream","Earth","Focus"};


    public String six = "banana,Africa,Monday,office,nature,eleven,Mumbai,animal,twenty,snitch,monday,Friday,Father,yellow,poetry,August";
    public String [] sixLettersWord  = six.split(",");
    //public String[] sixLettersWord = {"Better","Beyond","Bridge","Camera","Circle","Driver","Effort","Entire","Flight"};


    public String getEasyWord() throws Exception {
        int n = RandomNum(fourLettersWord.length);
        String result = fourLettersWord[n].toUpperCase();

        System.out.println(result);
        if(result.length() != 4) {
            throw new Exception("NotEasyWord!");
        }
        return result;
    }

    public String getNormalWord() throws Exception {
        int n = RandomNum(fiveLettersWord.length);
        String result = fiveLettersWord[n].toUpperCase();

        System.out.println(result);
        if(result.length() != 5) {
            throw new Exception("NotNormalWord!");
        }
        return result;
    }

    public String getHardWord() throws Exception {

        int n = RandomNum(sixLettersWord.length);
        String result = sixLettersWord[n].toUpperCase();

        System.out.println(result);
        if(result.length() != 6) {
            throw new Exception("NotHardWord!");
        }
        return result;
    }

    //generate a index of the array database
    public int RandomNum(int length) {
        Random rand = new Random();
        int index = rand.nextInt(length);

        return index;
    }



}
