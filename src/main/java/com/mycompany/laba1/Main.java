/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.laba1;

import controller.AppController;
import model.Model;
import view.App;

/**
 *
 * @author vladshuvaev
 */
public class Main {

    public static void main(String[] args) {
         Model model = new Model();
          App view = new App();
          AppController controller = new AppController(view, model);
          view.setVisible(true);
    }
}
