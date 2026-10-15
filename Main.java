import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// 1. EXCEPTION TỰ ĐỊNH NGHĨA (Bắt lỗi nghiệp vụ thư viện)
class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }
}

// 2. INTERFACE (Giao diện quy định hành vi mượn/trả)
interface IBorrowable {
    void borrowItem() throws LibraryException;
    void returnItem() throws LibraryException;
}

// 3. ABSTRACT CLASS (Lớp trừu tượng Tài liệu chung)
abstract class Document implements IBorrowable {
    private String id;
    private String title;
    private String author;
    private boolean isBorrowed;

    public Document(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isBorrowed = false; 
    }

   
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isBorrowed() { return isBorrowed; }

    @Override
    public void borrowItem() throws LibraryException {
        if (isBorrowed) {
            throw new LibraryException("Tài liệu này hiện đang có người mượn!");
        }
        isBorrowed = true;
    }

    @Override
    public void returnItem() throws LibraryException {
        if (!isBorrowed) {
            throw new LibraryException("Tài liệu này đang ở trong kho, chưa có ai mượn!");
        }
        isBorrowed = false;
    }

    // Phương thức trừu tượng bắt buộc lớp con phải viết lại (Override)
    public abstract void display();
}

// 4. INHERITANCE (Lớp Sách kế thừa từ Document)
class Book extends Document {
    private int pages; // Thuộc tính riêng của Sách: số trang

    public Book(String id, String title, String author, int pages) {
        super(id, title, author); // Gọi constructor lớp cha
        this.pages = pages;
    }

    @Override
    public void display() {
        String status = isBorrowed() ? "Đang mượn" : "Có sẵn";
        System.out.printf("[SÁCH]    Mã: %-6s | Tên: %-20s | Tác giả: %-15s | Số trang: %-4d | Trạng thái: %s%n",
                getId(), getTitle(), getAuthor(), pages, status);
    }
}

// 5. INHERITANCE (Lớp Tạp chí kế thừa từ Document)
class Magazine extends Document {
    private int issueNumber; // Thuộc tính riêng của Tạp chí: số phát hành

    public Magazine(String id, String title, String author, int issueNumber) {
        super(id, title, author);
        this.issueNumber = issueNumber;
    }

    @Override
    public void display() {
        String status = isBorrowed() ? "Đang mượn" : "Có sẵn";
        System.out.printf("[TẠP CHÍ] Mã: %-6s | Tên: %-20s | NXB: %-19s | Số PH: %-7d | Trạng thái: %s%n",
                getId(), getTitle(), getAuthor(), issueNumber, status);
    }
}

// CLASS CHÍNH QUẢN LÝ CHƯƠNG TRÌNH
public class Main {
    static ArrayList<Document> documents = new ArrayList<>();

    static HashMap<String, String> borrowedHistory = new HashMap<>();

    static Scanner sc = new Scanner(System.in);

    // HÀM NHẬP CHUỖI KHÔNG RỖNG
    public static String inputString(String message) {
        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Lỗi: Không được để trống!");
        }
    }

    // HÀM NHẬP SỐ NGUYÊN DƯƠNG 
    public static int inputPositiveInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                int value = Integer.parseInt(sc.nextLine().trim());
                if (value >= 0) {
                    return value;
                }
                System.out.println("Lỗi: Vui lòng nhập số lớn hơn hoặc bằng 0!");
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }

    // TÌM TÀI LIỆU THEO MÃ
    public static Document findById(String id) {
        for (Document doc : documents) {
            if (doc.getId().equalsIgnoreCase(id)) {
                return doc;
            }
        }
        return null;
    }

    // NHẬP MÃ MỚI (Không trùng)
    public static String inputUniqueId() {
        while (true) {
            String id = inputString("Nhập mã tài liệu: ");
            if (findById(id) == null) {
                return id;
            }
            System.out.println("Lỗi: Mã tài liệu đã tồn tại!");
        }
    }

    // 1. THÊM SÁCH HOẶC TẠP CHÍ
    public static void addDocument() {
        System.out.println("\n----- THÊM TÀI LIỆU MỚI -----");
        System.out.println("1. Thêm Sách");
        System.out.println("2. Thêm Tạp chí");
        int type = inputPositiveInt("Chọn loại tài liệu (1 hoặc 2): ");

        if (type != 1 && type != 2) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        String id = inputUniqueId();
        String title = inputString("Nhập tên tài liệu: ");

        if (type == 1) {
            String author = inputString("Nhập tên tác giả: ");
            int pages = inputPositiveInt("Nhập số trang: ");

            documents.add(new Book(id, title, author, pages));
            System.out.println("Đã thêm Sách thành công!");
        } else {
            String publisher = inputString("Nhập tên nhà xuất bản: ");
            int issue = inputPositiveInt("Nhập số phát hành: ");

            documents.add(new Magazine(id, title, publisher, issue));
            System.out.println("Đã thêm Tạp chí thành công!");
        }
    }

    // 2. HIỂN THỊ DANH SÁCH
    public static void displayAll() {
        System.out.println("\n===== DANH SÁCH TÀI LIỆU TRONG THƯ VIỆN =====");
        if (documents.isEmpty()) {
            System.out.println("Thư viện hiện đang trống!");
            return;
        }
        for (Document doc : documents) {
            doc.display(); 
        }
    }

    // 3. MƯỢN TÀI LIỆU 
    public static void borrowDocument() {
        System.out.println("\n===== MƯỢN TÀI LIỆU =====");
        String id = inputString("Nhập mã tài liệu muốn mượn: ");
        Document doc = findById(id);

        if (doc == null) {
            System.out.println("Không tìm thấy tài liệu có mã: " + id);
            return;
        }

        try {
            String borrowerName = inputString("Nhập tên người mượn: ");
            doc.borrowItem(); 
            borrowedHistory.put(doc.getId().toUpperCase(), borrowerName);
            System.out.println("Mượn thành công! Người mượn: " + borrowerName);
        } catch (LibraryException e) {
            System.out.println("Không thể mượn: " + e.getMessage());
        }
    }

    // 4. TRẢ TÀI LIỆU 
    public static void returnDocument() {
        System.out.println("\n===== TRẢ TÀI LIỆU =====");
        String id = inputString("Nhập mã tài liệu cần trả: ");
        Document doc = findById(id);

        if (doc == null) {
            System.out.println("Không tìm thấy tài liệu có mã: " + id);
            return;
        }

        try {
            doc.returnItem();
            String borrower = borrowedHistory.remove(doc.getId().toUpperCase());
            System.out.println("Đã trả tài liệu thành công! (Người mượn trước đó: " + borrower + ")");
        } catch (LibraryException e) {
            System.out.println("Không thể trả: " + e.getMessage());
        }
    }

    // 5. XEM DANH SÁCH ĐANG MƯỢN 
    public static void displayBorrowedList() {
        System.out.println("\n===== DANH SÁCH TÀI LIỆU ĐANG ĐƯỢC MƯỢN =====");
        if (borrowedHistory.isEmpty()) {
            System.out.println("Hiện không có tài liệu nào đang được mượn.");
            return;
        }

        for (Map.Entry<String, String> entry : borrowedHistory.entrySet()) {
            String docId = entry.getKey();
            String borrower = entry.getValue();
            Document doc = findById(docId);
            if (doc != null) {
                System.out.printf("Mã: %-6s | Tên: %-20s | Người đang mượn: %s%n",
                        doc.getId(), doc.getTitle(), borrower);
            }
        }
    }

    // 6. TÌM KIẾM TÀI LIỆU THEO TÊN
    public static void searchByTitle() {
        System.out.println("\n===== TÌM KIẾM TÀI LIỆU =====");
        String keyword = inputString("Nhập từ khóa tên tài liệu: ").toLowerCase();
        boolean found = false;

        for (Document doc : documents) {
            if (doc.getTitle().toLowerCase().contains(keyword)) {
                doc.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Không tìm thấy tài liệu nào khớp với từ khóa!");
        }
    }

    // 7. XÓA TÀI LIỆU
    public static void deleteDocument() {
        System.out.println("\n===== XÓA TÀI LIỆU =====");
        String id = inputString("Nhập mã tài liệu cần xóa: ");
        Document doc = findById(id);

        if (doc == null) {
            System.out.println("Không tìm thấy tài liệu!");
            return;
        }

        if (doc.isBorrowed()) {
            System.out.println("Không thể xóa tài liệu đang có người mượn!");
            return;
        }

        documents.remove(doc);
        System.out.println("Đã xóa tài liệu thành công!");
    }

    // MENU CHÍNH
    public static void menu() {
        while (true) {
            System.out.println("\n========== HỆ THỐNG QUẢN LÝ THƯ VIỆN ==========");
            System.out.println("1. Thêm tài liệu mới (Sách / Tạp chí)");
            System.out.println("2. Hiển thị toàn bộ tài liệu");
            System.out.println("3. Mượn tài liệu");
            System.out.println("4. Trả tài liệu");
            System.out.println("5. Xem danh sách người đang mượn (HashMap)");
            System.out.println("6. Tìm kiếm tài liệu theo tên");
            System.out.println("7. Xóa tài liệu");
            System.out.println("0. Thoát chương trình");
            System.out.println("===============================================");

            int choice = inputPositiveInt("Nhập lựa chọn của bạn: ");

            switch (choice) {
                case 1:
                    addDocument();
                    break;
                case 2:
                    displayAll();
                    break;
                case 3:
                    borrowDocument();
                    break;
                case 4:
                    returnDocument();
                    break;
                case 5:
                    displayBorrowedList();
                    break;
                case 6:
                    searchByTitle();
                    break;
                case 7:
                    deleteDocument();
                    break;
                case 0:
                    System.out.println("Đã thoát chương trình Quản lý thư viện!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    public static void main(String[] args) {
        documents.add(new Book("B01", "Lập trình Java", "Nam Cao", 350));
        documents.add(new Magazine("M01", "Khoa học & Đời sống", "NXB Trẻ", 12));
        menu();
    }
}