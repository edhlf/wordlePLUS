package com.example.a1022project;

public class Wordle {
    public String guess;
    public String line1; //guess1
    public String line2;
    public String line3;
    public String line4;
    public String line5;
    public String line6;
    public String answer;
    public int currentLine = 1;
    public Wordle() {
        //this.answer ="APPLE";
    }
    public Wordle(String answer) {
        this.answer = answer;
    }
    public int getCLine() {
        return this.currentLine;
    }
    public void addLine(){
        if (this.currentLine < 6) {
            this.currentLine++;
        }
    }
    public void saveGuess(){
        if(this.currentLine == 1){
            this.line1 = this.guess;
        }else if(this.currentLine == 2){
            this.line2 = this.guess;
        }else if(this.currentLine == 3){
            this.line3 = this.guess;
        }else if(this.currentLine == 4){
            this.line4 = this.guess;
        }else if(this.currentLine == 5){
            this.line5 = this.guess;
        }
    }
    public void saveCompare(){

    }
    public String replaceChar(String str, char ch, int index) {
        return str.substring(0, index) + ch + str.substring(index+1);
    }
    public boolean inWordL(String word){ // METHOD TO COMPARE TO WORD LIST
        return true; // RETURN TRUE IF WORD IS IN LIST
    }
    public void setGuess(String guess){
        this.guess = guess;
    }
    public String getLine1(){
        return this.line1;
    }
    public String getAnswer(){
        return this.answer;
    }
    public String getGuess(){
        return this.guess;
    }
    public char[] compare(){
        char[] guessC = this.guess.toCharArray();
        char[] answerC = this.answer.toCharArray();
        String format = "";
        for(int c = 0; c <guessC.length; c++){
            format+="W";
        }
        for(int i = 0; i < guessC.length; i++) {
            for(int x = 0; x < guessC.length; x++){
                if (guessC[i] == answerC[x]){
                    format = replaceChar(format,'Y',i); // sets yellow
                }

            }
            if(guessC[i] == answerC[i]){
                format = replaceChar(format,'G',i); // sets green
            }
        }
        char[] formatC = format.toCharArray();
        return formatC;
    }
    public boolean checkWin() {
        String win = "";
        boolean hasWon = false;
        for(int c = 0; c <this.answer.length();c++){
            win+="G";
        }
        String str = String.valueOf(compare());
        if(str.equals(win)){
            hasWon = true;
        }
        return hasWon;
    }
    public boolean checkLost(){
        return this.currentLine == 6;
    }
}
//a -> c,r,a,n,e
//p -> c,r,a,n,e
//crane <- guess
//apple <- answer

// todo: add winner
