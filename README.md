# AutoPet/LC — Tashino, 251.jar

**Bản thử có xử lý nhận/luyện/nâng, chưa được xác minh trên máy chủ thật.**
Đầu vào là Java ME MIDP 2.1 / CLDC 1.1. File gốc giữ nguyên trong `input/251.jar`.

## Cách sử dụng

1. Đứng gần **Tashino ở Làng Tone, map 22**.
2. Mở Menu → MenuAuto → AutoPet/LC, bật Enable cho vật phẩm cần chạy.
   AutoPet/LC cũng nằm ở đầu menu chính, nên không cần cuộn ngang để tìm.
3. Chọn chỉ số, thao tác nhận/luyện/nâng, giới hạn lượt luyện và cấp đích.
4. Bấm Lưu ở góc dưới phải, rồi bấm Auto dưới minimap.
5. Bấm Auto lần nữa để dừng; thông báo: `đã dừng auto`.

Hai tab lưu riêng bằng RMS. Chưa Enable thì các trường bị khóa. Đóng không lưu bỏ thay đổi.
Cuộn bằng kéo hoặc phím lên/xuống. Dừng Auto trước khi sửa cấu hình.

## Luồng xử lý

- **Pet Long: ID 1305**, **Long Châu Mika2: ID 1282**, theo ảnh người dùng.
- Tìm ID của Tashino từ NPC trong map; không cố định ID NPC.
- Chọn đúng nhãn `Nhận Vật Phẩm`, `Luyện Trang Bị`, `Nâng cấp vật phẩm`.
- Đọc hành trang/trang bị thật; không lấy chỉ số xem trước trong shop làm kết quả.
  Nếu có nhiều bản cùng vật phẩm, dừng để tránh chọn sai.
- Vật phẩm trong hành trang được sử dụng/trang bị bằng thao tác gốc trước khi luyện/nâng.
- Chọn template trong danh sách mới từ server, gửi thao tác shop gốc, đối chiếu nội dung
  xác nhận rồi dùng mã xác nhận server gửi. Đọc lại dữ liệu vật phẩm sau thao tác.
- Chỉ nâng khi đủ tất cả các dòng đã chọn. Hết lượt mà chưa đủ thì dừng, không nâng.
  Đạt cấp đích thì thông báo. Hai vật phẩm được xử lý tuần tự với cấu hình riêng.
- Dừng khi rời Tashino/map, gặp thông báo lỗi, nội dung xác nhận không khớp, dữ liệu
  không rõ ràng, hoặc quá 15 giây chờ một bước. Không tự gửi lại thao tác tiêu nguyên liệu.
- Bấm dừng chặn các bước tiếp theo; thao tác đã gửi trước đó có thể vẫn được server xử lý.

Option được nhận diện theo tên template tải từ server. Phân biệt tấn công số/phần trăm;
hồi HP và MP cần dòng kết hợp hoặc đủ cả hai dòng hồi. Nhãn khác không được tự suy đoán.

## Giới hạn xác minh

Ảnh xác nhận ID, menu, hộp luyện/nâng và yêu cầu trang bị. Hộp nâng Pet Long ghi cần
1.000 lượng, tỉ lệ 100%; không cố định giá/tỉ lệ đó trong mã. Ảnh Mika2 ghi yêu cầu cấp
nhân vật 20; máy chủ vẫn kiểm tra điều kiện này.

Chưa đăng nhập game trong môi trường, nên chưa xác nhận kết quả và thời điểm cập nhật
vật phẩm trên server đang chơi. Đây là bản thử; chưa có tuyên bố chạy thành công trên server thật.

## Build và kiểm tra

Cần Java 21, Python 3 và rg. Công cụ phát bytecode Java 1.3 với API CLDC 1.1;
ProGuard tạo StackMap Java ME. Công cụ tải qua TLS và kiểm tra SHA-256.

```sh
python tools/bootstrap.py
bash build.sh input/251.jar
```

Kết quả: `dist/251-AutoPetLC-Tashino-test.jar`.

Build kiểm tra:

- 40 assertion về cấu hình, nhãn từ ảnh và bộ điều khiển với server mô phỏng.
- 5 assertion Canvas/RMS trong MicroEmulator.
- Runtime dùng lớp game/accessor thật với transport giả: trang bị, menu, luyện/nâng,
  hết lượt, dừng khi chờ xác nhận, nhận đúng Mika2 và bảo toàn vị trí đọc packet.
- Preverification CLDC và nạp/kiểm tra 319 lớp bằng JVM.
- Tài nguyên và lớp không vá giống hệt file gốc theo byte.

`build/runtime-fixture.jar` và TestTransport chỉ để thử, không đóng vào JAR phát hành.
Cảnh báo thiếu hình của giả lập trong phép thử logic không chứng minh hiển thị game hoạt động;
phần hiển thị trong phiên chơi thật chưa được kiểm tra.

Workspace cloud đã cô lập; dùng checkout hiện có, không tạo Git worktree.
Không cần tiến trình nền để build. Chưa publish thay đổi lên GitHub.
