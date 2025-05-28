/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ssoms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Personnels {

    private static final Personnels instance = new Personnels();

    private ObservableList<HomepageController.Person> personnelList = FXCollections.observableArrayList();

    private Personnels() {
    }

    public static Personnels getInstance() {
        return instance;
    }

    public ObservableList<HomepageController.Person> getPersonnelList() {
        return personnelList;
    }
}
