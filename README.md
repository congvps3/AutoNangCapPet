# AutoUpLv + AutoPet/LC r20

[Bản JAR r20](dist/AutoUpLv-AutoPetLC-r20.jar) giữ chức năng bản r19 và thêm
chuỗi nhập mã, dùng vật phẩm, tự nhận Pet/Mika2. File r19 người dùng gửi đã được
đối chiếu SHA-256 với bản r19 trong repository; không sửa file gốc đã gửi.

## Chuỗi khi bật Tàn sát

1. Về **Làng Tone (map 22)**, tìm **Okanechan**, chọn **Mã quà tặng**.
   Nhập lần lượt **200mem** và **CODETUAN4**, chờ phản hồi từng mã trước khi gửi mã tiếp.
2. Quét hành trang, sử dụng **829**, rồi **1399** nếu có. Chờ hành trang xác nhận
   đã dùng một đơn vị trước khi tiếp tục.
3. Làm **Pet Long (1305)** ngay, kể cả khi nhân vật dưới cấp 21:
   kiểm tra trang bị và toàn bộ hành trang; thiếu thì tự nhận ở Tashino.
   Luyện khi vật phẩm còn trong hành trang; đủ chỉ số mới dùng, rồi mới nâng cấp.
4. Hoàn tất Pet thì tiếp tục AutoLV. **Từ cấp 21**, tạm dừng AutoLV, về Tashino
   làm **Long Châu Mika2 (1282)** theo cùng quy trình. Nếu bắt đầu khi đã từ cấp 21
   thì làm Pet và Mika2 lần lượt.
5. Hoàn tất từng phần: thông báo tự đóng sau **2 giây**, tắt AutoPet/LC và tiếp tục
   AutoLV ngay trên cùng bộ điều khiển Tàn sát.

Trong khi lên cấp, quét và dùng một lần các vật phẩm sau:

| Cấp nhân vật từ | ID vật phẩm |
|---|---|
| 20 | 41 |
| 30 | 44 |
| 50 | 48 |

Không có vật phẩm thì tiếp tục chạy, không ghi dấu đã dùng; khi vật phẩm xuất hiện
sau đó sẽ được kiểm tra lại. Bắt đầu ở cấp cao hơn mốc vẫn dùng nếu còn vật phẩm
và chưa ghi nhận đã dùng. Mã đã được nhận trước đó được tính là xong; mã hết hạn,
sai mã hoặc máy chủ từ chối sẽ dừng và báo nội dung lỗi.

Tiến trình từng mã, từng vật phẩm và từng phần Pet/Mika2 lưu RMS theo mã/tên nhân vật.
Vào lại game không tự lặp các bước đã xác nhận hoàn tất. Xóa dữ liệu ứng dụng sẽ
xóa tiến trình và cấu hình. Nếu đã đeo một món, chuỗi Tàn sát bỏ qua phần làm món đó;
đeo đủ hai món thì không cần Enable AutoPet/LC sau phần chuẩn bị mã/vật phẩm.
Nút Auto vẫn cho phép chạy nâng cấp bằng tay khi cần.

## Cài đặt AutoPet/LC

1. Mở **Menu → MenuAuto → AutoPet/LC**, kiểm tra tiêu đề **r20**.
2. Bật Enable và chọn Luyện/Nâng cấp, chỉ số, giới hạn lượt luyện và cấp nâng
   riêng cho Pet Long và Mika2. Bấm **Lưu**.
3. Bật **Tàn sát** để chạy toàn bộ chuỗi ở trên. Hoặc đứng gần Tashino ở Tone,
   bấm **Auto** dưới minimap để chỉ chạy AutoPet/LC theo các tab Enable.
4. Bấm Auto lần nữa khi AutoPet/LC đang chạy để dừng (`đã dừng auto`).

Không cần tự nhận hay xếp vật phẩm vào ô 1/2. Auto quét bằng ID trên toàn bộ hành trang
và vùng trang bị; đã có thì không nhận thêm. Khi thiếu, chọn mục Nhận Vật Phẩm
và đúng ID trong danh sách Tashino. Cần chừa ít nhất một ô trống để nhận.

Bật Luyện: chỉ dùng khi đủ tất cả chỉ số đã chọn; hết lượt mà thiếu chỉ số thì dừng,
giữ vật phẩm trong hành trang và không nâng. Bỏ tích Luyện: bỏ kiểm tra chỉ số,
sử dụng vật phẩm nếu đang ở hành trang rồi nâng; nếu đã đeo thì nâng thẳng tới cấp đích.
Nâng cấp thất bại hoặc tụt cấp vẫn tiếp tục sau khi đọc kết quả mới, đến khi đạt cấp đặt.

Chỉ các tab Enable được xử lý. Dưới cấp 21, chuỗi Tàn sát chỉ làm Pet; từ cấp 21 mới làm Mika2.
Nút Auto bằng tay vẫn giữ điều kiện cấp của game cho Mika2 (cấp 20); máy chủ quyết định
việc sử dụng thành công. Hai vật phẩm có giới hạn luyện và cấp nâng độc lập.

## Map và Thủ lĩnh

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
Hai nút Auto/TL dùng đúng hình tròn viền vàng và ảnh khi nhấn của nút Treo gốc
(`/hd/button.png`, `/hd/button2.png`), cùng phông chữ nhỏ có bóng. Chữ trắng khi
tắt và vàng khi bật; bỏ nền nâu/xanh và chấm trạng thái của bản trước. Auto vẫn điều khiển AutoPet/LC; TL không bật/tắt AutoPet/LC.

## Phản hồi và dừng

Mỗi thời điểm chỉ chờ một thao tác nhận/dùng/luyện/nâng cấp. Không gửi lặp thao tác
đang chờ; quá 15 giây sẽ dừng. Đọc thông tin vật phẩm được thử lại tối đa hai lần,
không tiêu thêm lượt luyện. Bộ đọc hành trang dùng cùng khóa với bộ phân tích gói tin gốc,
tránh đọc chỉ số khi máy chủ mới cập nhật một phần.

Lỗi, hết lượt luyện thiếu chỉ số, tắt Tàn sát hoặc dừng Pet bằng tay đều không tự bật
lại AutoLV. Di chuyển đến NPC quá 3 phút sẽ dừng. Lệnh đã gửi trước khi bấm dừng có
thể vẫn được máy chủ xử lý. Các bước đã xác nhận xong được lưu trước khi chuyển bước.

Cửa sổ Tashino loại 4 dùng thao tác gốc **111101 → gói 16**, không dùng gói mua 13.
Đọc ID/nguồn thật từ danh sách và kiểm tra tiêu đề trước khi chọn. Không dùng chỉ số
xem trước trong danh sách làm kết quả luyện. Nhập mã dùng giao thức có sẵn của client:
ô nhập 92 với token máy chủ, hoặc cửa sổ giftcode -33.

Nút **?** trên màn hình cài đặt mở nhật ký AutoPet/LC. Nhật ký chỉ giữ mã gói,
nguồn/ô vật phẩm và bước đang chờ; Q = xếp hàng, TX = gửi, RX = nhận/đã xử lý.
Gói nền -30 được tổng hợp để không che mất thao tác chính. Không lưu đăng nhập,
chat hay dữ liệu tài khoản; nhật ký chỉ ở bộ nhớ và không tự gửi ra ngoài.

## Build và kiểm tra

Giữ bản AutoLV gốc ở `input/AutoUpLv.jar`. Cần Java 21, Python 3 và rg.

```sh
python tools/bootstrap.py
bash build.sh input/AutoUpLv.jar
```

Đầu ra: `dist/AutoUpLv-AutoPetLC-r20.jar`. Công cụ tải qua TLS và kiểm tra SHA-256;
biên dịch Java 1.3 với API CLDC 1.1, tạo StackMap Java ME.

Kiểm tra gồm cấu hình/động cơ, luật map/Thủ lĩnh, Canvas/RMS, nạp 327 lớp,
chuỗi nhập mã → dùng thưởng → nhận/luyện/dùng/nâng Pet → AutoLV → Mika2 ở cấp 21,
mốc 20/30/50, ô hành trang bất kỳ, vào lại game, hủy và timeout.
Chạy cả bộ gửi mô phỏng và bộ tạo gói gốc của game (chỉ chặn ở hàng đợi mạng).
Các lớp test không được đóng vào JAR phát hành; tài nguyên và lớp không vá giống
file gốc theo byte. **Chưa thử đăng nhập và thao tác trên máy chủ thật.**

Workspace cloud dùng checkout hiện có; không cần tạo Git worktree hay chạy tiến trình nền.
