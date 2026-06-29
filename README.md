# Ứng Dụng Quản Lý Công Thức Nấu Ăn

Ứng dụng Android (Java) cho phép người dùng đăng ký/đăng nhập, thêm – sửa – xóa
công thức nấu ăn của riêng mình, tìm kiếm công thức và đánh dấu công thức yêu thích.
Dữ liệu được lưu trữ cục bộ bằng SQLite.

## 1. Yêu cầu hệ thống

- **Android Studio**: bản mới nhất (khuyến nghị Android Studio Ladybug hoặc mới hơn)
- **JDK**: 11 trở lên (Android Studio thường đã đi kèm sẵn)
- **Android SDK**: API level tối thiểu theo cấu hình trong `build.gradle` (thường là API 21+)
- **Thiết bị chạy thử**: máy ảo (Emulator) hoặc điện thoại Android thật đã bật **USB Debugging**

## 2. Cấu trúc dự án

```
app/
 └── src/main/
     ├── java/com/example/baitaplonmobile/
     │   ├── page_database.java          # Lớp quản lý SQLite (tạo bảng, CRUD, câu lệnh tham số hóa)
     │   ├── page_login.java             # Màn hình đăng nhập
     │   ├── page_register.java          # Màn hình đăng ký (hash mật khẩu trước khi lưu)
     │   ├── page_list_congthuc.java     # Màn hình danh sách công thức của tài khoản đang đăng nhập (tìm kiếm, yêu thích)
     │   ├── page_list_yeuthich.java     # Màn hình danh sách công thức yêu thích
     │   └── page_manager_congthuc.java  # Màn hình thêm / sửa / xóa công thức
     └── res/layout/
         ├── activity_page_login.xml
         ├── activity_page_register.xml
         ├── activity_list_congthuc.xml
         ├── activity_item_congthuc.xml
         ├── activity_list_yeuthich.xml
         └── activity_manager_congthuc.xml
```

## 3. Cơ sở dữ liệu

Ứng dụng dùng SQLite (`page_database.java`, file `QuanLyCongThuc.sqlite`) với 2 bảng:

| Bảng | Mô tả |
|------|-------|
| `TaiKhoan` | Lưu thông tin đăng ký / đăng nhập (MaTaiKhoan, Email, Password — mật khẩu được **hash** trước khi lưu, không lưu dạng văn bản thuần) |
| `CongThuc` | Lưu thông tin công thức nấu ăn (MaCongThuc, TenCongThuc, DoKho, NguyenLieu, CachLam, DuongDanAnh, YeuThich, MaTaiKhoan — dùng để xác định công thức thuộc tài khoản nào) |

Database được tự động tạo khi chạy ứng dụng lần đầu (không cần thao tác thủ công).

Mỗi tài khoản sau khi đăng nhập chỉ xem được danh sách công thức do chính tài khoản đó tạo (lọc theo `MaTaiKhoan`), không nhìn thấy công thức của tài khoản khác.

## 4. Bảo mật

- **Mật khẩu**: được hash trước khi lưu vào CSDL (không lưu dạng văn bản thuần), so khớp bằng cách hash mật khẩu nhập vào rồi đối chiếu với hash đã lưu.
- **Câu lệnh SQL**: toàn bộ các truy vấn (đăng nhập, đăng ký, thêm/sửa/xóa/tìm kiếm công thức) đều dùng tham số hóa (parameter binding) thay cho nối chuỗi trực tiếp, tránh lỗ hổng SQL Injection.
- **Phân quyền dữ liệu**: mỗi tài khoản chỉ truy vấn/thao tác trên công thức gắn với `MaTaiKhoan` của chính mình.

## 5. Hướng dẫn cài đặt và chạy dự án

### Bước 1: Lấy mã nguồn

- Giải nén/copy toàn bộ thư mục dự án vào máy, hoặc clone từ Git (nếu có repository):
  ```bash
  git clone <đường-dẫn-repository>
  ```

### Bước 2: Mở dự án bằng Android Studio

1. Mở **Android Studio**.
2. Chọn **File → Open...** rồi chọn đúng thư mục gốc của dự án (thư mục chứa file `build.gradle` cấp project).
3. Đợi Android Studio tự động **Sync Gradle** (đồng bộ thư viện). Nếu có thông báo cập nhật Gradle/SDK, chọn **Update/Install** theo gợi ý.

### Bước 3: Chuẩn bị thiết bị chạy thử

**Cách 1 – Dùng máy ảo (Emulator):**
1. Vào **Tools → Device Manager**.
2. Chọn **Create Device**, chọn dòng máy (ví dụ Pixel 6) → chọn phiên bản Android (image) → **Finish**.
3. Khởi động máy ảo vừa tạo.

**Cách 2 – Dùng điện thoại thật:**
1. Vào **Cài đặt → Thông tin điện thoại**, bấm liên tục vào **Số hiệu bản dựng (Build number)** 7 lần để mở **Developer options**.
2. Vào **Developer options**, bật **USB Debugging**.
3. Kết nối điện thoại với máy tính bằng cáp USB, chọn **Allow** khi điện thoại hỏi cấp quyền debug.

### Bước 4: Build và chạy ứng dụng

1. Trên thanh công cụ Android Studio, chọn thiết bị (máy ảo hoặc điện thoại) ở ô chọn thiết bị.
2. Bấm nút **Run ▶** (hoặc `Shift + F10`).
3. Android Studio sẽ build APK và tự động cài + mở ứng dụng trên thiết bị đã chọn.

## 6. Hướng dẫn sử dụng cơ bản

1. **Đăng ký**: ở màn hình đăng nhập, chọn chuyển sang **Đăng ký**, nhập Email + Mật khẩu + Xác nhận mật khẩu.
2. **Đăng nhập**: nhập Email và Mật khẩu đã đăng ký để vào màn hình danh sách công thức của riêng tài khoản đó.
3. **Xem / tìm kiếm công thức**: ở màn hình danh sách, gõ từ khóa vào ô tìm kiếm để lọc theo tên công thức (chỉ trong phạm vi công thức của tài khoản đang đăng nhập).
4. **Thêm công thức**: bấm nút thêm, điền thông tin (tên, độ khó, nguyên liệu, cách làm, chọn ảnh) rồi **Lưu Công Thức**.
5. **Sửa / Xóa công thức**: bấm vào một công thức trong danh sách để vào màn hình chỉnh sửa, có thể **Lưu** lại thay đổi hoặc bấm **Xóa** để xóa công thức.
6. **Yêu thích**: bấm icon ngôi sao trên mỗi công thức để đánh dấu/bỏ đánh dấu yêu thích; vào màn hình **Yêu thích** để xem danh sách các công thức đã đánh dấu.
7. **Đăng xuất**: chọn chức năng đăng xuất để quay lại màn hình đăng nhập.

## 7. Một số lưu ý

- Dữ liệu được lưu trên chính thiết bị (SQLite nội bộ), nếu gỡ ứng dụng thì dữ liệu sẽ mất.
- Khi chọn ảnh cho công thức, ứng dụng dùng URI ảnh từ thư viện máy; nếu đổi/xóa ảnh gốc trong máy thì ảnh hiển thị trong ứng dụng cũng có thể không còn.
- Nếu gặp lỗi khi build, thử **Build → Clean Project** rồi **Build → Rebuild Project**.

## 8. Tác giả dự án

| STT | Họ và tên | Mã sinh viên |
|-----|-----------|--------------|
| 1 | Phạm Kiều Oanh | 10123249 |
| 2 | Hoàng Thị Hồng Hạnh | 10123109 |
