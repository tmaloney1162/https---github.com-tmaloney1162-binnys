package test;

import java.util.ArrayList;
import java.util.List;

public class ListExample {

    public static void main(String[] args) {
        List<String> myList = new ArrayList<String>();
        myList.add("1");
        myList.add("2");
        myList.add("3");
        myList.add("11");
        myList.add("12");

        System.out.println("Inserted in 'order': ");
        printList(myList);
        System.out.println("\n");
        System.out.println("Inserted out of 'order': ");

        // Clear the list
        myList.clear();

        myList.add("2");
        myList.add("3");
        myList.add("11");
        myList.add("12");
        myList.add("1");

        printList(myList);
    }

    private static void printList(List<String> myList) {
        for (String string : myList) {
            System.out.println(string);
        }
    }
}