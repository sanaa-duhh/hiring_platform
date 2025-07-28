package com.example;

import com.example.Connection.ConnectionManager;
import com.example.Connection.DataBaseSetup;
import com.example.ResponseEntity.CandidateResponse;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            DataBaseSetup.setupDatabase(scanner);
            while (true) {
                printMenu();
                System.out.print("Enter your choice (0-5): ");
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice == 0) {
                        System.out.println("Exiting application. Goodbye!");
                        break;
                    }
                    handleUserChoice(choice, scanner);
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 0 and 5.");
                    scanner.nextLine(); // Clear the invalid input from the buffer
                } catch (SQLException e) {
                    System.err.println("A database error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            ConnectionManager.closeConnection();
        }
    }

    private static void printMenu() {
        System.out.println("\n###################################################");
        System.out.println("###   Recruitment Platform - Interactive Menu   ###");
        System.out.println("###################################################");
        System.out.println("1. List candidates in 'interview' stage for a job");
        System.out.println("2. Retrieve interview schedules for an interviewer");
        System.out.println("3. Find jobs with more than 50 applications");
        System.out.println("4. Show offer acceptance rate per department");
        System.out.println("5. Show status of all applications for a candidate");
        System.out.println("0. Exit");
    }

    private static void handleUserChoice(int choice, Scanner scanner) throws SQLException {
        switch (choice) {
            case 1:
                runQuery1(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please select an option from the menu.");
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void runQuery1(Scanner scanner) throws SQLException {
        System.out.print("Enter Job ID: ");
        int jobId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\n--- [Query 1] Listing candidates in 'interview' stage for Job ID: " + jobId + " ---");
        List<CandidateResponse> list = Queries.listCandidatesInInterviewStage(jobId);
        if (list.isEmpty()) {
            System.out.println("No candidates found.");
        } else {
            String format = "| %-15s | %-25s | %-10s | %-12s |%n";
            System.out.format("+-----------------+---------------------------+------------+--------------+%n");
            System.out.format(format, "Application ID", "Name", "Gender", "Experience");
            System.out.format("+-----------------+---------------------------+------------+--------------+%n");
            for (CandidateResponse candidate : list) {
                System.out.format(format, candidate.applicationId(), candidate.name(), candidate.gender(), candidate.experience());
                System.out.println("| Resume Path     : " + candidate.resumeLinkPath());
            }
            System.out.format("+-----------------+---------------------------+------------+--------------+%n");
        }
    }
}