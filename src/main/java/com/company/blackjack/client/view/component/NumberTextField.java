package com.company.blackjack.client.view.component;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

    public NumberTextField() {
        super();
    }

    @Override
    public void replaceText(int start, int stop, String text) {
        if(text.matches("[0-9]") || text.isEmpty()) {
            super.replaceText(start, stop, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        super.replaceSelection(text);
    }
}
