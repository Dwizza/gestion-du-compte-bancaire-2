package com.demo1;

import com.demo1.Controllers.AuthController;

import java.sql.Connection;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("=== Bienvenue dans la Banque Digitale ===");
        Menus.showLoginMenu();
    }
}


