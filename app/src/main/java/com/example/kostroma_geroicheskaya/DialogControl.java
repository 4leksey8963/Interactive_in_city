package com.example.kostroma_geroicheskaya;

import java.util.ArrayList;
import java.util.List;

public class DialogControl {
    private List<String> stringList = new ArrayList<>();
    private int i = 0;
    private boolean forward=true, back=false;

    public void addToList(String text) {
        stringList.add(text);
    }

    public boolean getBackEnable() {
        return back;
    }

    public boolean getForwardEnable() {
        return forward;
    }

    public int getDialogSize() {return stringList.size()-1;}

    public void back() {
        i-=1;
        if (i-1 == -1) {
            back = (false);
        }
        else {
            back = (true);
        }
        forward = (true);
    }

    public void forward() {
        i+=1;
        if (i+1 == stringList.size()) {
            forward = (false);
        }
        else {
            forward = (true);
        }
        back = (true);
    }

    public int getI() {
        return i;
    }

    public String getText() {
        return stringList.get(i);
    }
}
