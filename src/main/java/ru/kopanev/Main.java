package ru.kopanev;

import ru.kopanev.ui.MenuUi;
import ru.kopanev.utils.ApplicationFactory;

public class Main {
    public static void main(String[] args) {
        ApplicationFactory factory = new ApplicationFactory();
        MenuUi ui = factory.createApplication();
        ui.start();
    }
}