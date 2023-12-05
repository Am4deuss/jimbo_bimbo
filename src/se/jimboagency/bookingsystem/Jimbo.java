package se.jimboagency.bookingsystem;

import se.jimboagency.bookingsystem.ui.UIManager;
import se.jimboagency.bookingsystem.logic.LogicManager;

public class Jimbo {

    public static void main(String[] args) {
        LogicManager logic = new LogicManager();
        UIManager ui = new UIManager(logic);
        ui.show_menu();
    }

}