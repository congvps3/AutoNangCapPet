import java.io.*;

/** Independent settings for Pet Long (0) and Long chau Mika2 (1). */
public final class AutoPetConfig {
    public static final String[] NAMES = {"Pet Long", "Long châu Mika2"};
    public static final String[] OPTIONS = {
        "Tấn công: +#", "Băng công: +#", "Hoả công: +#", "Phong lôi: +#",
        "Chí mạng: +#", "HP tối đa: #", "MP tối đa: +#", "Tấn công: +%#",
        "Miễn giảm sát thương: #%", "Mỗi nửa giây hồi phục # HP và MP",
        "Tăng #% kinh nghiệm khi đánh quái"
    };
    public boolean enabled, receive, train, upgrade;
    public int selected, attempts = 100, target = 1;

    public AutoPetConfig copy() {
        AutoPetConfig c = new AutoPetConfig();
        c.enabled=enabled; c.receive=receive; c.train=train; c.upgrade=upgrade;
        c.selected=selected; c.attempts=attempts; c.target=target;
        return c;
    }
    public void validate() {
        if (attempts < 0 || attempts > 1000000) throw new IllegalArgumentException("Số lần luyện: 0–1000000");
        if (target < 0 || target > 1000) throw new IllegalArgumentException("Cấp nâng: 0–1000");
        if ((selected & ~2047) != 0) throw new IllegalArgumentException("Chỉ số không hợp lệ");
        if (enabled && train && selected == 0)
            throw new IllegalArgumentException("Hãy chọn ít nhất một chỉ số");
        if (enabled && !train && !upgrade)
            throw new IllegalArgumentException("Hãy chọn thao tác cần chạy");
    }
    public static byte[] encode(AutoPetConfig[] cs) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream d = new DataOutputStream(b);
        d.writeInt(0x41504c43); d.writeInt(1);
        for(int i=0;i<2;i++) {
            AutoPetConfig c=cs[i]; c.validate();
            d.writeBoolean(c.enabled); d.writeBoolean(c.receive);
            d.writeBoolean(c.train); d.writeBoolean(c.upgrade);
            d.writeInt(c.selected); d.writeInt(c.attempts); d.writeInt(c.target);
        }
        d.flush(); return b.toByteArray();
    }
    public static AutoPetConfig[] decode(byte[] bytes) throws IOException {
        DataInputStream d=new DataInputStream(new ByteArrayInputStream(bytes));
        if(d.readInt()!=0x41504c43 || d.readInt()!=1) throw new IOException("Sai phiên bản cấu hình");
        AutoPetConfig[] cs=new AutoPetConfig[2];
        for(int i=0;i<2;i++) {
            AutoPetConfig c=new AutoPetConfig();
            c.enabled=d.readBoolean(); d.readBoolean(); c.receive=false;
            c.train=d.readBoolean(); c.upgrade=d.readBoolean();
            c.selected=d.readInt(); c.attempts=d.readInt(); c.target=d.readInt();
            if(c.enabled && !c.train && !c.upgrade) c.enabled=false;
            c.validate(); cs[i]=c;
        }
        if(d.available()!=0) throw new IOException("Dữ liệu thừa");
        return cs;
    }
}
