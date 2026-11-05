# AutoPet/LC r6 — luyện trong hành trang trước, dùng và nâng sau

Bản r6 sửa nhật ký chẩn đoán; chưa xác định được nguyên nhân Auto chờ trên máy chủ thật.
Luồng luyện/dùng/nâng của r5 được giữ nguyên.

Bản thử cho 251.jar (Java ME MIDP 2.1 / CLDC 1.1), theo luồng người dùng đã sửa:
**tự nhận → luyện trong hành trang → đủ chỉ số mới dùng → xác nhận trang bị → nâng cấp**.
Chưa xác minh trực tiếp trên máy chủ thật.

## Cách dùng

1. Tự nhận vật phẩm tại Tashino. Để **Pet Long ID 1305 ở ô 1** và
   **Long Châu Mika2 ID 1282 ở ô 2** hành trang; chưa sử dụng.
2. Đứng gần Tashino ở Làng Tone (map 22).
3. Menu → MenuAuto → AutoPet/LC. Màn hình phải ghi **AutoPet/LC r6**.
4. Bật Enable từng vật phẩm cần chạy, chọn chỉ số, Luyện/Nâng cấp, giới hạn lượt và cấp đích.
5. Bấm Lưu, rồi Auto dưới minimap. Bấm Auto lần nữa để dừng (`đã dừng auto`).

Không có tự nhận vật phẩm. Dòng nhận tự động cũ đã thay bằng hướng dẫn vị trí hành trang.
Cấu hình từng tab lưu riêng bằng RMS. Chưa Enable thì không sửa được các trường.
Chỉ kiểm tra các ô của vật phẩm đã Enable; sai hoặc thiếu bất kỳ ô nào thì dừng trước khi gửi lệnh.
Cấu hình cũ chỉ bật nhận sẽ được tắt Enable khi tải, để người dùng chọn lại Luyện/Nâng cấp.

## Quy tắc xử lý

- Đọc chỉ số thật của vật phẩm ở ô quy định trước khi bắt đầu.
- Luyện khi vật phẩm vẫn ở hành trang. Chưa đủ tất cả chỉ số đã chọn thì chưa được dùng.
- Hết lượt mà chưa đủ chỉ số: thông báo dừng, giữ vật phẩm trong hành trang, không nâng.
- Đủ chỉ số: sử dụng vật phẩm đúng ô. Chờ server cập nhật vào vùng trang bị và đọc lại
  thông tin mới; sau đó mới nâng tới cấp đích nếu đã chọn Nâng cấp.
- Chỉ bật Luyện thì vẫn dùng khi đã đủ chỉ số, sau đó hoàn tất mà không nâng.
- Hai vật phẩm xử lý tuần tự, giới hạn lượt và cấp đích độc lập.
- Mika2 yêu cầu nhân vật cấp 20 theo ảnh người dùng; máy chủ vẫn kiểm tra điều kiện sử dụng.
- Dừng khi sai ô, thiếu vật phẩm, nhận lỗi từ server, rời Tashino/map hoặc chờ một bước quá
  15 giây. Không tự gửi lại lệnh tiêu nguyên liệu. Dừng đóng hộp chờ gốc của game.
- Lệnh đã gửi trước khi bấm dừng có thể vẫn được máy chủ xử lý.

ID Tashino và vị trí menu lấy từ dữ liệu NPC đang tải. Đối chiếu nhãn Luyện Trang Bị /
Nâng cấp vật phẩm và tiêu đề cửa sổ. Nguồn danh sách của cửa sổ 52 là 32, giống hàm làm mới gốc.
Có thể dùng catalog đang hiển thị mà client đã tải sẵn, sau khoảng chờ ngắn. Chỉ số xem trước
trong catalog không được dùng làm kết quả luyện. Chỉ số ô giao diện và indexUI gửi server giữ riêng.

## Khi gặp lỗi

Nút **?** ở góc trên phải màn hình cấu hình mở nhật ký lần chạy. Nhật ký giữ giới hạn trong
bộ nhớ, ghim riêng bước đang chờ và các lệnh gửi/nhận, chỉ có mã gói và nguồn/ô vật phẩm: Q = đã xếp hàng, TX = gửi thành công
qua bộ ghi của client, RX = nhận và xử lý gói. Không lưu nội dung đăng nhập, chat hoặc giá trị
thông tin tài khoản. Khi dừng do lỗi, thông báo cũng kèm nhật ký để phân biệt lỗi client và
bước chờ phản hồi. Các gói nền -30 được đếm theo mã gói con thay vì ghi lặp từng dòng. Dừng Auto vẫn giữ
bước cuối cùng để xem lại; 1.200 gói nền liên tiếp không được làm mất thông tin gửi.
Nhật ký không tự gửi ra ngoài và không tồn tại sau khi đóng game.

## Build và kiểm tra

Giữ file gốc tại `input/251.jar`. Cần Java 21, Python 3 và rg.

```sh
python tools/bootstrap.py
bash build.sh input/251.jar
```

Đầu ra: `dist/251-AutoPetLC-Tashino-test.jar`, cấu hình hiển thị r6.
Công cụ tải qua TLS, kiểm tra SHA-256. Biên dịch Java 1.3 với API CLDC 1.1, tạo StackMap Java ME.

Các kiểm tra gồm cấu hình/động cơ, Canvas/RMS, nạp 319 lớp, kịch bản lớp game thật với bộ gửi
mô phỏng và kịch bản giữ nguyên EAVbpdxFGDnFcb (bộ tạo gói gốc), chỉ chặn tại hàng đợi mạng.
Kiểm tra cả frame nhị phân được bộ ghi gốc tạo, thứ tự luyện → dùng → nâng, ô 1/2, không tự nhận,
không dùng khi hết lượt/chưa đủ chỉ số, xác nhận trang bị, hủy phản hồi sau khi dừng và đóng hộp chờ
khi timeout. Đây là kiểm tra cục bộ, không chứng minh thao tác đã thành công trên máy chủ thật.

`build/runtime-fixture.jar`, `build/wire-fixture.jar`, TestTransport và các lớp test không được đóng
vào JAR phát hành. Mọi tài nguyên và lớp không vá phải giống file gốc theo byte.
Workspace cloud đã cô lập; dùng checkout hiện có, không tạo Git worktree. Không cần tiến trình nền.
