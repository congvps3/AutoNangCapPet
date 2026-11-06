# AutoUpLv + AutoPet/LC r18

Bổ sung map ngoài: 19 (71), 36 (74/77), 54 (80), 37 (84/88), 55 (92), 58 (96/100).
Dưới cấp 60 giữ cách chọn map ngoài hiện có. Từ cấp 60 ưu tiên tuyến Ma Quỷ;
cấp nhân vật được kiểm tra thật, không đổi cấp hay cờ mở khóa của máy chủ.
Dùng tìm đường gốc của client (cùng hệ thống với gm), không giả lập gõ chat.

| Map Ma Quỷ | Cấp quái | Chuyển sang map kế khi nhân vật đạt |
|---|---|---|
| 139 | 64 / 68 | 66 |
| 140 | 74 / 77 | 76 |
| 141 | 83 / 88 | 85 |
| 142 | 92 / 96 | 94 |
| 143 | 103 / 107 | 105 |
| 144 | 114 / 117 | 116 |
| 145 | 123 / 126 | 125 |
| 146 | 131 / 137 | 133 |
| 147 | 142 / 148 | 144 |
| 148 | 153 / 159 | Không chuyển map; tiếp tục tàn sát tại 148 |

Trong map Ma Quỷ: nếu cấp quái cao nhất trừ cấp nhân vật >=10, chỉ đánh cấp
quái gần nhất trong danh sách map; khi chênh <=9 thì đánh mọi cấp quái.
Nút **TL** cạnh **Auto** dưới minimap bật/tắt chế độ Thủ lĩnh riêng trong Ma Quỷ.
Mặc định tắt; trạng thái lưu RMS. TL tắt: bỏ lọc chỉ chọn Thủ lĩnh, bỏ điểm ưu tiên
và bỏ lượt quét ưu tiên gốc. TL bật: ưu tiên Thủ lĩnh, đánh quái khác và chờ đủ
6 giây sau khi Thủ lĩnh chết rồi đổi khu nếu vẫn không có Thủ lĩnh. Vẫn lọc cấp quái, áp dụng cả mục tiêu đang giữ và đánh lan.
Khi TL tắt, không đổi khu vì thiếu Thủ lĩnh sau 6 giây; hết quái vẫn đổi khu.
Ví dụ: cấp 66 ở map 140 chỉ đánh quái 74; từ cấp 68 đánh 74/77; tới cấp 76 chuyển 141.
Map ngoài giữ quy tắc Thủ lĩnh/chờ 6 giây hiện có, không phụ thuộc nút TL.
Hai nút HUD bo góc, nền nâu khi tắt/xanh khi bật, viền vàng, chấm trạng thái
và hiệu ứng khi nhấn. Auto vẫn điều khiển AutoPet/LC; TL không bật/tắt AutoPet/LC.

Kiểm tra trang bị trước khi đọc cấu hình AutoPet/LC: nếu đã đeo đủ
Pet Long (1305) và Mika2 (1282), bật Tàn sát sẽ chạy thẳng AutoLV, kể cả sau
khi vào lại game ở cấp 41 và không còn cấu hình/dấu hoàn tất RMS. Chỉ tính
vật phẩm đang đeo, không tính vật phẩm trong hành trang. Không ghi dấu nâng
thành công từ kiểm tra này; vẫn có thể dùng AutoPet/LC bằng tay khi cần.

Ở map ngoài, AutoLV ưu tiên **Thủ lĩnh** (loại 2 theo nhãn trong client):
- Vừa vào khu có quái là đánh ngay, không đứng chờ.
- Không có Thủ lĩnh: đánh quái khác trong 6 giây; hết 6 giây vẫn không có thì đổi khu.
- Có Thủ lĩnh: ưu tiên đánh Thủ lĩnh. Sau khi Thủ lĩnh cuối cùng chết, đánh quái khác
  và bắt đầu lại đủ 6 giây chờ Thủ lĩnh, không dùng lại mốc lúc vào khu.
- Nếu Thủ lĩnh xuất hiện lại trong lúc chờ: đánh Thủ lĩnh và bắt đầu vòng 6 giây mới
  sau khi nó chết.
- Hết sạch quái: gửi đổi khu ở lượt cập nhật kế tiếp, không chờ hết 6 giây.

Chờ máy chủ xác nhận một lệnh chuyển khu trước khi gửi tiếp; khu mới không bị
cooldown của khu trước. Thời gian tải mạng vẫn phụ thuộc máy chủ.

Tích hợp trên đúng `AutoUpLv.jar` người dùng gửi, giữ bộ chọn map/quái theo cấp của bản đó, cập nhật
ưu tiên mục tiêu và thời điểm đổi khu. Bật **Tàn sát** để chạy chuỗi:

1. Dưới cấp 21: AutoLV chạy bình thường.
2. Từ cấp 21, nếu chưa đeo đủ hai món và chưa có dấu hoàn tất: tạm dừng đánh quái,
   dùng tìm đường gốc về Làng Tone (map 22),
   tìm Tashino theo tên NPC và đi tới gần.
3. Bật AutoPet/LC theo cấu hình đã lưu cho từng vật phẩm Enable.
4. Chỉ khi toàn bộ yêu cầu hoàn tất: hiện thông báo tự đóng sau 2 giây, tắt AutoPet/LC và tiếp tục
   ngay AutoLV trên cùng bộ điều khiển Tàn sát.

Cần cài đặt và Lưu AutoPet/LC trước khi bật Tàn sát. Nếu bật Luyện thì tự nhận
Pet Long và Mika2 trước, đặt ở ô 1/2 hành trang; nếu chỉ nâng thì đeo sẵn vật phẩm.
Không có tự nhận vật phẩm. Có thể cấu hình khác nhau cho từng vật phẩm.
Nâng thất bại thì nâng tiếp tới cấp đích; hết lượt luyện mà thiếu chỉ số thì dừng.
Lỗi, thiếu vật phẩm, dừng Pet bằng tay hoặc tắt Tàn sát đều không tự bật lại AutoLV.
Không tới Tashino trong 3 phút thì dừng và báo lỗi.

Dấu hoàn tất được lưu RMS theo mã và tên nhân vật; cùng nhân vật sẽ không lặp
chuỗi cấp 21 khi mở lại game. Xóa dữ liệu ứng dụng sẽ xóa cả dấu hoàn tất và cấu hình.
Bật Tàn sát lần đầu khi đã trên cấp 21 vẫn chạy chuỗi nếu chưa có dấu hoàn tất
và chưa đeo đủ hai món.
Chưa thử trên máy chủ thật; kiểm thử dùng lớp game và gói tin gốc, chặn kết nối.

Bản thử cho AutoUpLv.jar (Java ME MIDP 2.1 / CLDC 1.1), theo luồng người dùng đã sửa:
**tự nhận → luyện trong hành trang → đủ chỉ số mới dùng → xác nhận trang bị → nâng cấp**.
Chưa xác minh trực tiếp trên máy chủ thật.

## Cách dùng

1. Nếu bật Luyện: để **Pet Long ID 1305 ở ô 1**, **Mika2 ID 1282 ở ô 2** hành trang.
   Nếu chỉ nâng: đeo sẵn vật phẩm, bỏ tích Luyện và bật Nâng cấp; không cần chọn chỉ số.
2. Đứng gần Tashino ở Làng Tone (map 22).
3. Menu → MenuAuto → AutoPet/LC. Màn hình phải ghi **AutoPet/LC r18**.
4. Bật Enable từng vật phẩm cần chạy, chọn chỉ số, Luyện/Nâng cấp, giới hạn lượt và cấp đích.
5. Bấm Lưu, rồi Auto dưới minimap. Bấm Auto lần nữa để dừng (`đã dừng auto`).

Không có tự nhận vật phẩm. Dòng nhận tự động cũ đã thay bằng hướng dẫn vị trí hành trang.
Cấu hình từng tab lưu riêng bằng RMS. Chưa Enable thì không sửa được các trường.
Chỉ kiểm tra vật phẩm đã Enable: bật Luyện thì kiểm tra ô hành trang, chỉ nâng thì kiểm tra trang bị.
Cấu hình cũ chỉ bật nhận sẽ được tắt Enable khi tải, để người dùng chọn lại Luyện/Nâng cấp.

## Quy tắc xử lý

- Bật Luyện: đọc chỉ số thật của vật phẩm ở ô hành trang quy định.
- Bỏ tích Luyện: chỉ đọc cấp của vật phẩm đang đeo, nâng tới cấp đích, bỏ qua chỉ số.
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
Cửa sổ 4 nhận danh sách qua gói 31; chỉ thực hiện khi tiêu đề khớp Luyện/Nâng cấp,
ID và nguồn vật phẩm khớp. Không gửi lệnh mua 13 cho nguồn 4.
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
bash build.sh input/AutoUpLv.jar
```

Đầu ra: `dist/AutoUpLv-AutoPetLC-r18.jar`, cấu hình hiển thị r18.
Công cụ tải qua TLS, kiểm tra SHA-256. Biên dịch Java 1.3 với API CLDC 1.1, tạo StackMap Java ME.

Các kiểm tra gồm cấu hình/động cơ, Canvas/RMS, nạp 325 lớp, kịch bản lớp game thật với bộ gửi
mô phỏng và kịch bản giữ nguyên EAVbpdxFGDnFcb (bộ tạo gói gốc), chỉ chặn tại hàng đợi mạng.
Kiểm tra cả frame nhị phân được bộ ghi gốc tạo, thứ tự luyện → dùng → nâng, ô 1/2, không tự nhận,
không dùng khi hết lượt/chưa đủ chỉ số, xác nhận trang bị, hủy phản hồi sau khi dừng và đóng hộp chờ
khi timeout. Kiểm tra thêm cấp 20/21, gọi hook Tàn sát thật, lệnh về Tone, tiếp cận NPC,
chờ đủ hai vật phẩm, tiếp tục AutoLV, lưu dấu hoàn tất và dừng/hủy chuỗi khi lỗi.
Đây là kiểm tra cục bộ, không chứng minh thao tác đã thành công trên máy chủ thật.

`build/runtime-fixture.jar`, `build/wire-fixture.jar`, TestTransport và các lớp test không được đóng
vào JAR phát hành. Mọi tài nguyên và lớp không vá phải giống file gốc theo byte.
Workspace cloud đã cô lập; dùng checkout hiện có, không tạo Git worktree. Không cần tiến trình nền.
