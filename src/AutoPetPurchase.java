/** One Goosho purchase, using the native catalog and waiting for a real bag increase. */
public final class AutoPetPurchase {
    private static final int ROOT=1,SHOP=2,ACK=3;
    private static SJSCaeScqbiua owner;
    private static String character,tried,name;
    private static int npc,phase,menuBase,shopBase,listBase,shopType,confirmBase,noticeBase,menus;
    private static long deadline,readAt,cacheAt,before;
    private static boolean running,completed,requested;
    private static String character() {oFpffzUyucjDJX p=oFpffzUyucjDJX.rdmjdEJDQ();return p.BfndoYuX+":"+p.dvKEjoqBk;}
    private static void active() {
        if(!running || AutoPetAccess.currentAuto()!=owner || !character().equals(character) || AutoPetAccess.map()!=22)
            throw new RuntimeException("Đã dừng hoặc rời Goosho ở Làng Tone");
    }
    public static void start(SJSCaeScqbiua auto,int id) {
        synchronized(AutoPetRuntime.packetLock()) {
            owner=auto;npc=id;character=character();running=true;completed=false;tried="|";menus=0;
            before=AutoPetInventory.count(248);open();
        }
    }
    private static void open() {
        active();AutoPetAccess.dismiss();phase=ROOT;menuBase=AutoPetEvents.menus;shopBase=AutoPetEvents.shops;
        listBase=AutoPetEvents.catalogs;shopType=-1;noticeBase=AutoPetEvents.notices;deadline=System.currentTimeMillis()+15000;
        zxnsayePWpajv.oXAaoinDuwaN(npc);
    }
    private static boolean shopMenu(String text) {
        return text.equals("cua hang") || text.startsWith("cua hang ") || text.equals("mua") || text.startsWith("mua ")
            || text.equals("shop") || text.startsWith("shop ") || text.equals("giao dich") || text.equals("tap hoa") || text.equals("vat pham");
    }
    public static boolean completed() {synchronized(AutoPetRuntime.packetLock()){return completed;}}
    public static void cancel() {synchronized(AutoPetRuntime.packetLock()){running=false;completed=false;AutoPetAccess.dismiss();}}
    public static void tick() {
        synchronized(AutoPetRuntime.packetLock()) {
            active();long now=System.currentTimeMillis();
            if(now>=deadline)throw new RuntimeException("Quá thời gian "+(phase==ACK?"chờ mua ID 248; không gửi lại lệnh mua":"mở cửa hàng Goosho"));
            if(AutoPetEvents.notices>noticeBase) {
                noticeBase=AutoPetEvents.notices;String text=AutoPetRules.plain(AutoPetEvents.notice);
                if(text.indexOf("khong du")>=0 || text.indexOf("khong the")>=0 || text.indexOf("that bai")>=0)throw new RuntimeException(AutoPetEvents.notice);
            }
            if(phase==ACK) {
                if(AutoPetInventory.count(248)>before){completed=true;running=false;AutoPetAccess.dismiss();return;}
                if(AutoPetEvents.confirms>confirmBase) {
                    confirmBase=AutoPetEvents.confirms;String text=AutoPetRules.plain(AutoPetEvents.confirmText);
                    if(text.indexOf("mua")<0 || (text.indexOf("vat pham")<0 && (name.length()==0 || text.indexOf(name)<0)))
                        throw new RuntimeException("Xác nhận mua ID 248 không khớp");
                    active();NBAFgUXWGCkgPq.HmLENarbmj();EAVbpdxFGDnFcb.peMayxrZEBA().hFWcqhZjekJK(AutoPetEvents.confirmCode);
                }
                return;
            }
            if(AutoPetEvents.shops>shopBase) {
                shopBase=AutoPetEvents.shops;shopType=AutoPetEvents.shopSource;phase=SHOP;requested=false;
                readAt=now+300;cacheAt=now+1000;deadline=now+15000;
            }
            if(phase==ROOT && AutoPetEvents.menus>menuBase) {
                menuBase=AutoPetEvents.menus;kacyVKUNru choices=AutoPetEvents.menu;int selected=-1;
                for(int i=0;i<choices.size();i++) {
                    String label=AutoPetRules.plain(((bmxqULQDW)choices.elementAt(i)).oXAaoinDuwaN);
                    if(shopMenu(label) && tried.indexOf("|"+label+"|")<0){selected=i;tried+=label+"|";break;}
                }
                if(selected<0 || ++menus>8)throw new RuntimeException("Không thấy mục mua/cửa hàng có ID 248 tại Goosho");
                active();AutoPetAccess.click(choices,selected);return;
            }
            if(phase!=SHOP)return;
            int source=shopType==52?32:shopType;
            if(source<0 || source>255)throw new RuntimeException("Cửa hàng Goosho không được hỗ trợ: "+shopType);
            boolean fresh=AutoPetEvents.catalogRevisions[source]>listBase;
            if(!fresh) {
                if(!requested && now>=readAt){requested=true;EAVbpdxFGDnFcb.peMayxrZEBA().zmEhqDHwAKvzjM(source);}
                if(now<cacheAt)return;
            }
            wpiwfWAXuWze chosen=null;int ui=-1;
            for(int i=0;i<256;i++) {
                wpiwfWAXuWze it=AutoPetAccess.shopItem(shopType,i);
                if(it!=null && it.EgoDKTjkxEUMu!=null && it.EgoDKTjkxEUMu.QpKzrQoqbjfLc==248){chosen=it;ui=i;break;}
            }
            if(chosen==null){if(fresh)open();return;}
            active();name=AutoPetRules.plain(chosen.EgoDKTjkxEUMu.wLHNXXbvUSit);confirmBase=AutoPetEvents.confirms;
            AutoPetAccess.focusShop(ui);phase=ACK;deadline=now+15000;
            if(shopType==4) {
                if(chosen.tTqVmuHPEhAbg!=4)throw new RuntimeException("Nguồn ID 248 không khớp cửa hàng");
                AutoPetAccess.activateBox();
            } else {
                if(chosen.tTqVmuHPEhAbg==4)throw new RuntimeException("Không dùng lệnh mua cho cửa sổ 4");
                EAVbpdxFGDnFcb.peMayxrZEBA().BMWCZFcnBgzHkh(chosen.tTqVmuHPEhAbg,chosen.AZmTbjgvi,1);
            }
        }
    }
}
