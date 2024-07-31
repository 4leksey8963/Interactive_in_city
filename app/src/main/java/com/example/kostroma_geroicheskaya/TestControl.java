package com.example.kostroma_geroicheskaya;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestControl {


    private boolean[] boolOtvArray = new boolean[4];
    private String[] stringOtvArray = new String[4];
    private int randomIndex;

    private String question;

    public TestControl() {
        boolOtvArray[0] = false;
        boolOtvArray[1] = false;
        boolOtvArray[2] = false;
        boolOtvArray[3] = false;
        setTrueRandom();
    }

    public void setTrueRandom() {
        Random random = new Random();
        randomIndex = random.nextInt(4);
        boolOtvArray[randomIndex] = true;
    }

    public boolean getOtv1() {
        return boolOtvArray[0];
    }

    public boolean getOtv2() {
        return boolOtvArray[1];
    }

    public boolean getOtv3() {
        return boolOtvArray[2];
    }

    public boolean getOtv4() {
        return boolOtvArray[3];
    }

    public int getTrueIndex() {
        return randomIndex;
    }

    public String getStrOtv1() {
        return stringOtvArray[0];
    }
    public void setQuestion(String Question) {
        question = Question;
    }
    public String getQuestion() {
        return question;
    }

    public String getStrOtv2() {
        return stringOtvArray[1];
    }

    public String getStrOtv3() {
        return stringOtvArray[2];
    }

    public String getStrOtv4() {
        return stringOtvArray[3];
    }

    public void setTrueOtv(String tru) {
        stringOtvArray[randomIndex] = tru;
    }

    public String getTrueOtv() {
        return stringOtvArray[randomIndex];
    }

    public String[] getFalseOtv() {
        String[] mas = new String[3];
        for (int j = 0; j < stringOtvArray.length; j++) {
            if(j == randomIndex) {
                j+=1;
            }
            mas[j] = stringOtvArray[j];
        }
        return mas;
    }

    public void addFalseOtv(String[] fals) {
        int i = 0;
        for (int j = 0; j < stringOtvArray.length; j++) {
            if(j != 4 && j == randomIndex) {
                j+=1;
            }
            if (i<3 && i < fals.length)
                stringOtvArray[j] = fals[i++];
        }
    }

    public void randomOtv() {
        String old_string = stringOtvArray[randomIndex];
        List<String> list = Arrays.asList(stringOtvArray);
        Collections.shuffle(list);
        list.toArray(stringOtvArray);
        for (int j = 0; j < stringOtvArray.length; j += 1) {
            if (stringOtvArray[j].equals(old_string)) {
                for (int i = 0; i < boolOtvArray.length; i++) {
                    boolOtvArray[i] = false;
                }
                randomIndex = j;
                boolOtvArray[j] = true;
                break;
            }
        }
    }
}
