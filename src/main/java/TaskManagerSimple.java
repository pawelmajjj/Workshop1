import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import org.apache.commons.lang3.math.NumberUtils;

public class TaskManagerSimple {
    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};

    public static void main(String[] args) {
        String[][] elements = tasks();
        printOptions();
        chooseOptions(elements);
    }

    public static void printOptions() {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option:" + ConsoleColors.RESET);
        for (int i = 0; i < OPTIONS.length; i++) {
            System.out.println(OPTIONS[i]);
        }
    }

    public static String[][] chooseOptions(String[][] tasks) {

        Scanner scan = new Scanner(System.in);
        boolean flag = true;

        while (scan.hasNextLine()) {
            String input = scan.nextLine();

            switch (input) {

                case "add":
                    tasks = addTask(tasks);
                    printOptions();
                    break;

                case "remove":
                    tasks = removeTask(tasks);
                    printOptions();
                    break;

                case "list":
                    listTask(tasks);
                    printOptions();
                    break;

                case "exit":
                    exitTask(tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    return tasks;

                default:
                    System.out.println("Please select a correct option.");
            }
        }
        return tasks;
    }


    public static String[][] addTask(String[][] tasks) {
        Scanner scan = new Scanner(System.in);

        if (tasks.length == 0) { //if an array is empty - we create a new row, we'll update it later
            tasks = new String[1][3];
        } else {
            // if an array isn't empty - we increase its size by 1 row
            tasks = Arrays.copyOf(tasks, tasks.length + 1);

            // as it's a multiarray - we copy also the column size, so now we have a new row with 3 columns
            tasks[tasks.length - 1] = Arrays.copyOf(tasks[0], tasks[0].length);
        }

       /* String[][] tasksCopy = new String[tasks.length+1][tasks[0].length];
        System.out.println(tasksCopy[1].length);

        for(int i=0;i<tasks.length-1;i++){
            for(int j=0;j<tasks[i].length-1;j++) {
                tasksCopy[i][j] = tasks[i][j];
            }
        }*/

        // adding values to a new row
        System.out.println("Please add task description:");
        tasks[tasks.length - 1][0] = scan.nextLine();
        System.out.println("Please add task due date:");
        tasks[tasks.length - 1][1] = scan.nextLine();
        System.out.println("Is your task important?");
        tasks[tasks.length - 1][2] = scan.nextLine();

        System.out.println(ConsoleColors.RED + "The element has been succesfully added.");

        return tasks;

    }

    public static void listTask(String[][] tasks) {

        if (tasks.length == 0) {
            System.out.println("The task list is empty. Please try to add something to the list.");
        }
        for (int i = 0; i < tasks.length; i++) { //adding elements to the array
            System.out.print(i + ": ");
            for (int j = 0; j < tasks[i].length; j++) {
                System.out.print(tasks[i][j] + " ");
            }
            System.out.print("\n");
        }
    }


    public static String[][] removeTask(String[][] tasks) {
        String[][] tasks2 = tasks;
        int number = extractNumber(tasks, tasks.length);
        if (number < 0) {
            System.out.println("Nothing to remove. Please add a new task first before continuing.");
            return tasks;
        } else {
            //updating the array size - we delete one element, so the new one's size will be -1
            tasks = Arrays.copyOf(tasks, tasks.length - 1);

            // updating the new array with the original values

            for (int i = number; i < tasks.length; i++) {
                for (int j = 0; j < tasks[i].length; j++) {
                    tasks[i][j] = tasks2[i + 1][j];
                }
            }

            System.out.println(ConsoleColors.RED + "The element has been succesfully deleted.");

        }
        return tasks;
    }

    public static int extractNumber(String[][] tasks, int length) {
        Scanner scan = new Scanner(System.in);
        int number = -1; // there is no task[-1], so the -1 value means there is no task

        if (tasks.length > 0) { // if there is a task, we have to check if the user provides its correct number
            System.out.println("Please select a number to delete.");

            while (scan.hasNextLine()) {
                String value = scan.nextLine();

                try {
                    number = Integer.parseInt(value);

                    if (number < 0) {
                        System.out.println("The number is less than 0!");
                    } else if (number >= length) {
                        System.out.println("This number exceeds the task number!");

                    } else { //the number is correct - we delete it
                        return number;
                    }

                } catch (NumberFormatException e) {  //the user provided a value that is not an integer number
                    System.out.println("The value is incorrect. Please make sure you type in the correct task number!");
                }
            }
        }
        return number;
    }

    public static void exitTask(String[][] tasks) {
        String fileName = "tasks.csv";

        try (PrintWriter pw = new PrintWriter(fileName)) { // start writing
            if(tasks.length !=0) {
                for (int i = 0; i < tasks.length; i++) {
                    for (int j = 0; j < tasks[i].length; j++) {
                        pw.print(tasks[i][j]);
                        if (j < tasks[i].length - 1) {
                            pw.print("df "); // a really important step - we have to add a ',' separator as we use it while reading the file!!!
                        } else {
                            pw.print("\n");
                        }
                    }
                }
            } else {
                pw.println("Task example, date example, status example");
            }
            System.out.println(ConsoleColors.RED + "The data has been successfully updated.");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static String[][] tasks() {

        String[][] elements;
        String fileName = "tasks.csv";
        List<String> strings = new ArrayList<String>();

        try (Scanner scan = new Scanner(new File(fileName))) { //start reading lines from a file

            while (scan.hasNext()) { // fill a list with lines from the file
                strings.add(scan.nextLine());
            }

            /* now we have to create a multiarray: to do that we have to count the rows (list size)
            and columns - so differentiate how many parts divided by the "," separator are in a single row */

            //    if(strings.size() != 0) {
            elements = new String[strings.size()][strings.get(0).split(",").length];
            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(","); //dividing a single row into parts by ","
                for (int j = 0; j < split.length; j++) {
                    elements[i][j] = split[j]; // adding the split parts to our multiarray
                }
            }
            //   } else {
            //       elements = new String[1][3];
            //    }


        } catch (
                FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        return elements; //returning an array of file rows
    }
}
