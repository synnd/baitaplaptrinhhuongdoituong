import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

// CLASS STUDENT
class Student {
    private String id;
    private String name;
    private int age;
    private double gpa;

    public Student(String id, String name, int age, double gpa) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gpa = gpa;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getGpa() { return gpa; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public void display() {
        System.out.printf("Mã SV: %-8s | Tên: %-20s | Tuổi: %-3d | GPA: %.2f%n", id, name, age, gpa);
    }
}

public class Main {
    static ArrayList<Student> students = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    // NHẬP CHUỖI KHÔNG RỖNG
    public static String inputString(String message) {
        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Không được để trống!");
        }
    }

    // NHẬP SỐ NGUYÊN
    public static int inputInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }

    // NHẬP TUỔI
    public static int inputAge() {
        while (true) {
            int age = inputInt("Nhập tuổi sinh viên (18 - 100): ");
            if (age >= 18 && age <= 100) return age;
            System.out.println("Tuổi phải nằm trong khoảng 18 đến 100!");
        }
    }

    // NHẬP GPA
    public static double inputGpa() {
        while (true) {
            try {
                System.out.print("Nhập GPA (0 - 10): ");
                double gpa = Double.parseDouble(sc.nextLine().trim());
                if (gpa >= 0 && gpa <= 10) return gpa;
                System.out.println("GPA phải từ 0.0 đến 10.0!");
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số thực hợp lệ!");
            }
        }
    }

    // TÌM SINH VIÊN THEO ID 
    public static Student findStudentById(String id) {
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                return student;
            }
        }
        return null;
    }

    // 1. THÊM SINH VIÊN
    public static void addStudent() {
        System.out.println("\n===== THÊM SINH VIÊN =====");
        String id;
        while (true) {
            id = inputString("Nhập mã sinh viên: ");
            if (findStudentById(id) == null) break;
            System.out.println("Mã sinh viên đã tồn tại, vui lòng nhập mã khác!");
        }

        String name = inputString("Nhập tên sinh viên: ");
        int age = inputAge();
        double gpa = inputGpa();

        students.add(new Student(id, name, age, gpa));
        System.out.println("Thêm sinh viên thành công!");
    }

    // 2. HIỂN THỊ DANH SÁCH
    public static void displayStudents() {
        System.out.println("\n===== DANH SÁCH SINH VIÊN =====");
        if (students.isEmpty()) {
            System.out.println("Danh sách hiện đang rỗng!");
            return;
        }
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-10s | %-22s | %-6s | %-6s%n", "MÃ SV", "HỌ VÀ TÊN", "TUỔI", "GPA");
        System.out.println("------------------------------------------------------------");
        for (Student student : students) {
            student.display();
        }
        System.out.println("------------------------------------------------------------");
    }

    // 3. TÌM THEO MÃ
    public static void searchById() {
        System.out.println("\n===== TÌM SINH VIÊN THEO MÃ =====");
        String id = inputString("Nhập mã sinh viên: ");
        Student s = findStudentById(id);
        if (s != null) {
            System.out.println("Tìm thấy sinh viên:");
            s.display();
        } else {
            System.out.println("Không tìm thấy sinh viên có mã: " + id);
        }
    }

    // 4. TÌM THEO TÊN
    public static void searchByName() {
        System.out.println("\n===== TÌM SINH VIÊN THEO TÊN =====");
        String name = inputString("Nhập tên cần tìm: ");
        boolean found = false;

        for (Student student : students) {
            if (student.getName().toLowerCase().contains(name.toLowerCase())) {
                student.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Không tìm thấy sinh viên phù hợp!");
        }
    }

    // 5. SẮP XẾP THEO GPA 
    public static void sortByGpa() {
        if (students.isEmpty()) {
            System.out.println("Danh sách đang rỗng!");
            return;
        }
        students.sort(Comparator.comparingDouble(Student::getGpa).reversed());
        System.out.println(">> Đã sắp xếp danh sách theo GPA giảm dần:");
        displayStudents();
    }

    // 6. SẮP XẾP THEO TÊN (A - Z)
    public static void sortByName() {
        if (students.isEmpty()) {
            System.out.println("Danh sách đang rỗng!");
            return;
        }
        students.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        System.out.println(">> Đã sắp xếp danh sách theo tên (A-Z):");
        displayStudents();
    }

    // 7. XÓA SINH VIÊN
    public static void deleteStudent() {
        System.out.println("\n===== XÓA SINH VIÊN =====");
        String id = inputString("Nhập mã sinh viên cần xóa: ");
        Student s = findStudentById(id);
        if (s != null) {
            students.remove(s);
            System.out.println("Xóa sinh viên thành công!");
        } else {
            System.out.println("Không tìm thấy sinh viên!");
        }
    }

    // 8. CẬP NHẬT SINH VIÊN
    public static void updateStudent() {
        System.out.println("\n===== CẬP NHẬT SINH VIÊN =====");
        String id = inputString("Nhập mã sinh viên cần cập nhật: ");
        Student s = findStudentById(id);
        if (s != null) {
            s.setName(inputString("Nhập tên mới: "));
            s.setAge(inputAge());
            s.setGpa(inputGpa());
            System.out.println("Cập nhật thông tin thành công!");
        } else {
            System.out.println("Không tìm thấy sinh viên!");
        }
    }

    // MENU
    public static void menu() {
        while (true) {
            System.out.println("\n========== QUẢN LÝ SINH VIÊN ==========");
            System.out.println("1. Thêm sinh viên");
            System.out.println("2. Hiển thị danh sách");
            System.out.println("3. Tìm sinh viên theo mã");
            System.out.println("4. Tìm sinh viên theo tên");
            System.out.println("5. Sắp xếp theo GPA (giảm dần)");
            System.out.println("6. Sắp xếp theo tên (A-Z)");
            System.out.println("7. Xóa sinh viên");
            System.out.println("8. Cập nhật sinh viên");
            System.out.println("0. Thoát");
            System.out.println("=======================================");

            int choice = inputInt("Nhập lựa chọn của bạn: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> displayStudents();
                case 3 -> searchById();
                case 4 -> searchByName();
                case 5 -> sortByGpa();
                case 6 -> sortByName();
                case 7 -> deleteStudent();
                case 8 -> updateStudent();
                case 0 -> {
                    System.out.println("Đã thoát chương trình!");
                    return;
                }
                default -> System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");
            }
        }
    }

    public static void main(String[] args) {
        menu();
    }
}