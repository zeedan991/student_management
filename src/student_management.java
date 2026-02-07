

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class student_management {
    private static final ArrayList<Student_info> students = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);
    private static int nextId = 101;

    public static void main(String[] args) {
        loadFromDB(); // Load at start!
        menu();
        DBUtils.closeConnection();
    }

    public static void menu() {
        while (true) {
            System.out.println("\n--- STUDENT MANAGER ---");
            System.out.println("1. Add Student");
            System.out.println("2. Search Student");
            System.out.println("3. View All Students");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 6) break;

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    searchStudent();
                    break;
                case 3:
                    viewAllStudents();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter date of birth (DD/MM/YYYY): ");
        String dob = sc.nextLine();

        System.out.print("Enter blood group: ");
        String bloodGroup = sc.nextLine();

        System.out.print("Enter class: ");
        String studentClass = sc.nextLine();

        Student_info student = new Student_info(nextId++, name, age, dob, bloodGroup, studentClass);
        String query = "INSERT INTO students (id, name, age, dob, blood_group, class) VALUES (" +
                student.getId() + ", " +
                "'" + student.getName() + "', " +
                student.getAge() + ", " +
                "'" + student.getDob() + "', " +
                "'" + student.getBloodGroup() + "', " +
                "'" + student.getStudentClass() + "')";

        int result = DBUtils.executeQuery(query);

        if (result > 0) {
            students.add(student);
            System.out.println("Student added with ID: " + student.getId());
            System.out.println("Data saved to database successfully!");
        } else {
            System.out.println("Failed to save student to database!");
            nextId--; // Rollback the ID increment
        }
    }
    private static void searchStudent() {
        System.out.print("Enter student ID to search: ");
        int id = sc.nextInt();
        for (Student_info student : students) {
            if (student.getId() == id) {
                System.out.println("\n--- Student Details ---");
                System.out.println("ID: " + student.getId());
                System.out.println("Name: " + student.getName());
                System.out.println("Age: " + student.getAge());
                System.out.println("Date of Birth: " + student.getDob());
                System.out.println("Blood Group: " + student.getBloodGroup());
                System.out.println("Class: " + student.getStudentClass());
                return;
            }
        }
        System.out.println("Student not found!");
    }

    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
        } else {
            System.out.println("\n--- All Students ---");
            for (Student_info student : students) {
                System.out.println("ID: " + student.getId() +
                        " | Name: " + student.getName() +
                        " | Age: " + student.getAge() +
                        " | DOB: " + student.getDob() +
                        " | Blood Group: " + student.getBloodGroup() +
                        " | Class: " + student.getStudentClass());
            }
        }
    }

    private static void updateStudent() {
        System.out.print("Enter student ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Student_info student : students) {
            if (student.getId() == id) {
                System.out.print("Enter new name: ");
                String newName = sc.nextLine();

                System.out.print("Enter new age: ");
                int newAge = sc.nextInt();
                sc.nextLine();

                System.out.print("Enter new date of birth (DD/MM/YYYY): ");
                String newDob = sc.nextLine();

                System.out.print("Enter new blood group: ");
                String newBloodGroup = sc.nextLine();

                System.out.print("Enter new class: ");
                String newClass = sc.nextLine();

                System.out.print("Enter new ID (or press Enter to keep current): ");
                String idInput = sc.nextLine();

                int newId = id;
                if (!idInput.isEmpty()) {
                    newId = Integer.parseInt(idInput);
                    for (Student_info s : students) {
                        if (s.getId() == newId && s != student) {
                            System.out.println("Error: ID already exists!");
                            return;
                        }
                    }
                }

                // Update in database
                String query = "UPDATE students SET " +
                        "id = " + newId + ", " +
                        "name = '" + newName + "', " +
                        "age = " + newAge + ", " +
                        "dob = '" + newDob + "', " +
                        "blood_group = '" + newBloodGroup + "', " +
                        "class = '" + newClass + "' " +
                        "WHERE id = " + id;

                int result = DBUtils.executeQuery(query);

                if (result > 0) {
                    // Update in memory
                    student.setId(newId);
                    student.setName(newName);
                    student.setAge(newAge);
                    student.setDob(newDob);
                    student.setBloodGroup(newBloodGroup);
                    student.setStudentClass(newClass);
                    System.out.println("Student updated successfully!");
                } else {
                    System.out.println("Error updating student in database!");
                }
                return;
            }
        }
        System.out.println("Student not found!");
    }

    private static void deleteStudent() {
        System.out.print("Enter student ID to delete: ");
        int id = sc.nextInt();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                // Delete from database
                String query = "DELETE FROM students WHERE id = " + id;
                int result = DBUtils.executeQuery(query);

                if (result > 0) {
                    students.remove(i);
                    System.out.println("Student deleted successfully!");
                } else {
                    System.out.println("Error deleting student from database!");
                }
                return;
            }
        }
        System.out.println("Student not found!");
    }

    // Save students to database
    static void saveToDB() {
        try {
            for (Student_info s : students) {
                String query = "INSERT INTO students (id, name, age, dob, blood_group, class) VALUES (" +
                        s.getId() + ", " +
                        "'" + s.getName() + "', " +
                        s.getAge() + ", " +
                        "'" + s.getDob() + "', " +
                        "'" + s.getBloodGroup() + "', " +
                        "'" + s.getStudentClass() + "')";

                DBUtils.executeQuery(query);
            }
            System.out.println("Data saved to database successfully!");
        } catch (Exception ex) {
            System.out.println("Error saving data: " + ex.getMessage());
        }
    }

    // Load students from database
    static void loadFromDB() {
        students.clear();
        try {
            ResultSet rs = DBUtils.executeQueryForResult("SELECT * FROM students");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String dob = rs.getString("dob");
                String bloodGroup = rs.getString("blood_group");
                String studentClass = rs.getString("class");

                students.add(new Student_info(id, name, age, dob, bloodGroup, studentClass));

                // Update nextId to avoid conflicts
                if (id >= nextId) {
                    nextId = id + 1;
                }
            }

            System.out.println("✓ Loaded " + students.size() + " student(s) from database!");

        } catch (SQLException e) {
            System.out.println("Error loading from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class Student_info {
    private int id;
    private String name;
    private int age;
    private String dob;
    private String bloodGroup;
    private String studentClass;

    public Student_info(int id, String name, int age, String dob, String bloodGroup, String studentClass) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.dob = dob;
        this.bloodGroup = bloodGroup;
        this.studentClass = studentClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }
}