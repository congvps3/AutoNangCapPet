/** Server adapter must acknowledge each operation before another is sent. */
public final class AutoPetEngine {
    public static final int RECEIVE=1, TRAIN=2, UPGRADE=3, USE=4;
    public interface Port {
        void send(int run, int request, int item, int action);
        void notice(String message);
    }
    public static final class Item {
        public final boolean present,equipped;
        public final int options, level;
        public Item(boolean present,int options,int level) {
            this(present,options,level,true);
        }
        public Item(boolean present,int options,int level,boolean equipped) {
            this.present=present; this.options=options; this.level=level;
            this.equipped=equipped;
        }
    }
    private final Port port;
    private AutoPetConfig[] config;
    private Item[] items;
    private int run, request, item, used, pending;
    private boolean running;
    private long deadline;
    public AutoPetEngine(Port p) { port=p; }
    public boolean running() { return running; }
    public void cancel() {running=false;pending=0;++run;}
    public void start(AutoPetConfig[] settings, Item[] initial, long now) {
        if(running) throw new RuntimeException("Auto đang chạy");
        if(settings==null || initial==null || settings.length!=2 || initial.length!=2)
            throw new IllegalArgumentException("Thiếu dữ liệu vật phẩm");
        config=new AutoPetConfig[2]; items=new Item[2];
        boolean any=false;
        for(int i=0;i<2;i++) {
            settings[i].validate(); config[i]=settings[i].copy(); items[i]=initial[i];
            if(initial[i]==null) throw new IllegalArgumentException("Chưa nhận dữ liệu hành trang");
            any |= config[i].enabled;
        }
        if(!any) {port.notice("Chưa bật Enable cho vật phẩm nào"); return;}
        ++run; request=0; item=0; used=0; pending=0; running=true;
        advance(now);
    }
    public void stop() {
        if(running) {running=false; pending=0; ++run; port.notice("đã dừng auto");}
    }
    private void finish(String message) { running=false; pending=0; port.notice(message); }
    private void advance(long now) {
        while(running && item<2) {
            AutoPetConfig c=config[item]; Item s=items[item];
            if(!c.enabled) {++item; used=0; continue;}
            if(!c.train) {
                if(!s.present) {finish("Dừng auto: thiếu vật phẩm sau bước tự nhận");return;}
                if(!s.equipped) {send(USE,now);return;}
                if(c.upgrade && s.level<c.target) {send(UPGRADE,now);return;}
                port.notice(AutoPetConfig.NAMES[item]+" đã đủ cấp "+c.target);
                ++item;used=0;continue;
            }
            if(!s.present) {
                finish("Dừng auto: thiếu "+AutoPetConfig.NAMES[item]+" sau bước tự nhận");return;
            }
            boolean matches=(s.options & c.selected)==c.selected;
            if((c.train || c.upgrade) && !matches) {
                if(s.equipped) {finish("Dừng auto: vật phẩm đã sử dụng nhưng chưa đủ chỉ số; không luyện khi đang trang bị");return;}
                if(!c.train || used>=c.attempts) {
                    finish("Dừng auto: "+AutoPetConfig.NAMES[item]+" chưa đủ chỉ số sau "+used+" lượt luyện. Không nâng cấp.");return;
                }
                ++used; send(TRAIN,now); return;
            }
            if((c.train || c.upgrade) && !s.equipped) {send(USE,now);return;}
            if(c.upgrade && s.level<c.target) {send(UPGRADE,now);return;}
            port.notice(AutoPetConfig.NAMES[item]+(c.upgrade ? " đã đủ cấp "+c.target : " đã hoàn tất"));
            ++item; used=0;
        }
        if(running) {running=false;port.notice("AutoPet/LC đã hoàn tất");}
    }
    private void send(int action,long now) {
        pending=action; deadline=now+15000; ++request;
        port.send(run,request,item,action);
    }
    public void reply(int generation,int sequence,boolean success,Item updated,long now) {
        if(!running || generation!=run || sequence!=request || pending==0) return;
        int action=pending;pending=0;
        if(!success || updated==null || !updated.present) {
            finish("Dừng auto: máy chủ từ chối thao tác hoặc thiếu dữ liệu vật phẩm");return;
        }
        // A completed upgrade can fail or lower the level. A fresh item result,
        // rather than level progress, permits the next attempt until the target.
        if(action==USE && !updated.equipped) {finish("Dừng auto: chưa xác nhận sử dụng vật phẩm thành công");return;}
        items[item]=updated; advance(now);
    }
    public void tick(long now) {
        if(running && pending!=0 && now>=deadline)
            finish("Dừng auto: quá thời gian chờ máy chủ, không gửi lại thao tác");
    }
}
