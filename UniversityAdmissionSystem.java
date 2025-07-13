import java.util.*;

public class UniversityAdmissionSystem {
    static Scanner scanner = new Scanner(System.in);
    static Map<String, Student> studentDB = new HashMap<>();
    static Map<String, String> voucherDB = new HashMap<>();
    static List<String> coreSubjects = Arrays.asList("Core Mathematics", "English Language", "Integrated Science", "Social Studies");
    static List<String> electiveSubjects = Arrays.asList("Physics", "Chemistry", "Biology", "Elective Mathematics", "Economics", "Geography", "Government", "Literature", "Business Management", "Accounting", "ICT");

    static class Student {
        String name;
        String id;
        Map<String, Integer> results = new HashMap<>();
        String emailOrPhone;
        boolean isAdmitted = false;
        String university = "";
        boolean hasPurchasedForm = false;
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== UNIVERSITY ADMISSION SYSTEM ===");
            System.out.println("1. Manually Enter Results");
            System.out.println("2. View Results and Admission Status");
            System.out.println("3. Purchase Admission Form");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> enterResults();
                case 2 -> viewResultsAndAdmission();
                case 3 -> purchaseForm();
                case 4 -> { System.out.println("Exiting system... Goodbye!"); return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void enterResults() {
        Student student = new Student();
        System.out.print("Enter your full name: ");
        student.name = scanner.nextLine();
        System.out.print("Enter your ID (used to retrieve results later): ");
        student.id = scanner.nextLine();
        System.out.print("Enter your email or phone number: ");
        student.emailOrPhone = scanner.nextLine();

        System.out.println("\nEnter your CORE subject marks:");
        for (String subject : coreSubjects) {
            student.results.put(subject, getMark(subject));
        }

        System.out.println("\nSelect 4 ELECTIVE subjects:");
        showElectives();
        for (int i = 1; i <= 4; i++) {
            System.out.print("Elective " + i + ": ");
            String elective = scanner.nextLine();
            while (!electiveSubjects.contains(elective)) {
                System.out.print("Invalid elective. Try again: ");
                elective = scanner.nextLine();
            }
            student.results.put(elective, getMark(elective));
        }

        String voucher = generateVoucher();
        studentDB.put(student.id, student);
        voucherDB.put(voucher, student.id);

        System.out.println("\nResults saved successfully.");
        System.out.println("Your result-check voucher is: " + voucher);
        System.out.println("Sent to: " + student.emailOrPhone);
    }

    static void viewResultsAndAdmission() {
        System.out.print("Enter your result-check voucher code: ");
        String voucher = scanner.nextLine();

        if (!voucherDB.containsKey(voucher)) {
            System.out.println("Invalid voucher!");
            return;
        }

        String studentId = voucherDB.get(voucher);
        Student student = studentDB.get(studentId);

        System.out.println("\n--- RESULTS ---");
        student.results.forEach((subject, mark) -> System.out.println(subject + ": " + mark + "%"));

        evaluateAdmission(student);

        System.out.println("\n--- ADMISSION STATUS ---");
        if (student.isAdmitted) {
            System.out.println("🎉 Congratulations! Admitted to: " + student.university);
        } else {
            System.out.println("❌ Sorry, you did not meet admission criteria.");
        }
    }

    static void purchaseForm() {
        System.out.print("Enter your student ID: ");
        String id = scanner.nextLine();

        if (!studentDB.containsKey(id)) {
            System.out.println("Student not found!");
            return;
        }

        Student student = studentDB.get(id);
        if (!student.isAdmitted) {
            System.out.println("You are not eligible to purchase a form. Admission not granted.");
            return;
        }

        if (student.hasPurchasedForm) {
            System.out.println("You have already purchased the admission form.");
        } else {
            System.out.println("Form purchased successfully for " + student.name + " (" + student.university + ")");
            student.hasPurchasedForm = true;
        }
    }

    static int getMark(String subject) {
        System.out.print("Enter marks for " + subject + ": ");
        int mark = scanner.nextInt(); scanner.nextLine();
        while (mark < 0 || mark > 100) {
            System.out.print("Invalid mark (0–100). Try again: ");
            mark = scanner.nextInt(); scanner.nextLine();
        }
        return mark;
    }

    static void evaluateAdmission(Student student) {
        double total = 0;
        for (int score : student.results.values()) {
            total += score;
        }
        double average = total / student.results.size();

        if (average >= 80) {
            student.isAdmitted = true;
            student.university = "University of Ghana (Legon)";
        } else if (average >= 70) {
            student.isAdmitted = true;
            student.university = "KNUST";
        } else if (average >= 60) {
            student.isAdmitted = true;
            student.university = "UCC";
        } else {
            student.isAdmitted = false;
        }
    }

    static void showElectives() {
        for (String subject : electiveSubjects) {
            System.out.println("- " + subject);
        }
    }

    static String generateVoucher() {
        return "VCH" + (100000 + new Random().nextInt(900000));
    }
}

