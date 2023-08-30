package de.emir.utils;

import de.emir.main.BuildFFACore;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InvBackground {
    public static int[] arraySecOne = new int[] { 0, 4, 8 };

    public static int[] arraySecTwo = new int[] { 0, 4, 8, 9, 13, 17 };

    public static int[] arraySecThree = new int[] { 0, 8, 10, 13, 16, 18, 26, 9, 17 };

    public static int[] arraySecFour = new int[] {
            0, 3, 5, 8, 10, 13, 16, 19, 22, 25,
            27, 30, 32, 35 };

    public static int[] arraySecFive = new int[] {
            0, 4, 8, 10, 13, 16, 19, 20, 21, 23,
            24, 25, 28, 31, 34, 36, 40, 44 };

    public static int[] arraySecSix = new int[] {
            0, 3, 4, 5, 8, 10, 16, 19, 20, 22,
            24, 25, 28, 29, 31, 33, 34, 37, 43, 45,
            48, 49, 50, 53 };

    public static int[] arrayBlackBackground = new int[] {
            10, 11, 12, 13, 14, 15, 16, 19, 20, 21,
            22, 23, 24, 25, 28, 29, 30, 31, 32, 33,
            34 };

    public static ItemStack blackBackground = BuildFFACore.instance.createItem(Material.STAINED_GLASS_PANE, 1, 15, " ");
}
