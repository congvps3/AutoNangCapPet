import java.io.DataInputStream;

/** Uses the existing NPC/shop protocol and actual inventory, never shop previews as results. */
public final class AutoPetRuntime implements AutoPetEngine.Port {
    private static final AutoPetRuntime INSTANCE=new AutoPetRuntime();
    private static final int BOOT=1,EQUIP=2,ROOT=3,SHOP=4,CONFIRM=5,ACK=6,REFRESH=7;
    private final AutoPetEngine engine=new AutoPetEngine(this);
    private AutoPetConfig[] config;
    private final AutoPetEngine.Item[] initial=new AutoPetEngine.Item[2];
    private boolean active;
    private int phase,bootIndex,run,request,item,action,npcId,shopType=-1;
    private int infoSource,infoIndex,infoBaseline,listBaseline,shopBaseline,menuBaseline,confirmBaseline;
    private long deadline,nextInfo,shopRequestAt,shopCacheAt;
    private boolean shopListRequested;
    private String notices="";
    private static int eventCommand,eventSource,eventIndex;
    private static int infoVersion,infoEventSource,infoEventIndex,listVersion,shopVersion,openedShop,menuVersion,confirmVersion,confirmCode;
    private static final int[] listRevisions=new int[256];
    private static String confirmText;
    private static String serverError;
    private static kacyVKUNru menu;

    public static synchronized boolean running() {return INSTANCE.active;}
    public static synchronized void toggle() {
        AutoPetRuntime r=INSTANCE;
        if(r.active) {
            r.active=false;r.phase=0;r.notices="";AutoPetAccess.dismiss();
            if(r.engine.running()) r.engine.stop();else AutoPetBridge.notice("đã dừng auto");
            return;
        }
        try {
            r.config=AutoPetUI.settings();boolean any=false;
            for(int i=0;i<2;i++) {r.config[i].validate();any|=r.config[i].enabled;}
            if(!any) {AutoPetBridge.notice("Hãy bật Enable trong Menu > AutoPet/LC và lưu cấu hình");return;}
            if(AutoPetAccess.map()!=AutoPetRules.TONE) {AutoPetBridge.notice("Hãy đến Làng Tone (map 22), đứng gần Tashino rồi bấm Auto");return;}
            r.npcId=r.findNpc();
            if(r.npcId<0) {AutoPetBridge.notice("Chưa thấy Tashino trong bản đồ hiện tại");return;}
            r.active=true;r.notices="";serverError=null;r.bootIndex=0;r.phase=BOOT;r.deadline=System.currentTimeMillis()+15000;
            r.boot();
        } catch(Exception e) {r.fail("Không khởi động được: "+e.getMessage());}
    }
    private int findNpc() {
        kacyVKUNru ns=AutoPetAccess.npcs();
        if(ns==null) return -1;
        int found=-1;
        for(int i=0;i<ns.size();i++) {
            HAeSnYtYmns t=AutoPetAccess.template((gsxwWgJt)ns.elementAt(i));
            if(t!=null && AutoPetRules.plain(t.wLHNXXbvUSit).equals("tashino")) {
                if(found!=-1 && found!=t.mvjvhHcftNANDP) return -1;
                found=t.mvjvhHcftNANDP;
            }
        }
        return found;
    }
    private int templateId(int target) {return target==0?AutoPetRules.PET:AutoPetRules.MIKA;}
    private wpiwfWAXuWze locate(int target,boolean equippedOnly) {
        int[] sources=equippedOnly?new int[]{5,41,51,52,53}:new int[]{3,5,41,51,52,53};
        wpiwfWAXuWze result=null;
        for(int a=0;a<sources.length;a++) {
            wpiwfWAXuWze[] all=AutoPetAccess.items(sources[a]);
            if(all==null) continue;
            for(int i=0;i<all.length;i++) {
                wpiwfWAXuWze it=all[i];
                if(it==null || it.EgoDKTjkxEUMu==null || it.EgoDKTjkxEUMu.QpKzrQoqbjfLc!=templateId(target)) continue;
                if(result!=null && result!=it) throw new RuntimeException("Có nhiều "+AutoPetConfig.NAMES[target]+", hãy chỉ giữ một vật phẩm để Auto chọn đúng");
                result=it;
            }
        }
        return result;
    }
    private AutoPetEngine.Item snapshot(wpiwfWAXuWze it) {
        if(it==null) return new AutoPetEngine.Item(false,0,0);
        int options=0,hpmp=0;
        if(it.ScymBikfyop!=null) for(int i=0;i<it.ScymBikfyop.size();i++) {
            eNSzLVSc op=(eNSzLVSc)it.ScymBikfyop.elementAt(i);
            if(op==null || op.peMayxrZEBA==null) continue;
            String label=op.peMayxrZEBA.wLHNXXbvUSit;
            int bit=AutoPetRules.option(label);
            if(bit>=0) options|=1<<bit;
            String s=AutoPetRules.plain(label);
            if(s.indexOf("hoi phuc")>=0 && s.indexOf("nua giay")>=0) {
                if(s.indexOf("hp")>=0) hpmp|=1;
                if(s.indexOf("mp")>=0) hpmp|=2;
            }
        }
        if(hpmp==3) options|=1<<9;
        return new AutoPetEngine.Item(true,options,it.yKHpKzQyZroJ);
    }
    private void boot() {
        while(bootIndex<2) {
            wpiwfWAXuWze it=config[bootIndex].enabled?locate(bootIndex,false):null;
            initial[bootIndex]=snapshot(it);
            if(it!=null) {askInfo(it);return;}
            ++bootIndex;
        }
        phase=0;engine.start(config,initial,System.currentTimeMillis());
    }
    private void askInfo(wpiwfWAXuWze it) {
        infoSource=it.tTqVmuHPEhAbg;infoIndex=it.AZmTbjgvi;infoBaseline=infoVersion;
        it.ffWUPzwfbDA=false;
        deadline=System.currentTimeMillis()+15000;
        EAVbpdxFGDnFcb.peMayxrZEBA().uYrMAoZFCm(infoSource,infoIndex);
    }
    public void send(int generation,int sequence,int target,int operation) {
        run=generation;request=sequence;item=target;action=operation;
        deadline=System.currentTimeMillis()+15000;
        wpiwfWAXuWze equipped=locate(item,true);
        if(operation!=AutoPetEngine.RECEIVE && equipped==null) {
            wpiwfWAXuWze bag=locate(item,false);
            if(bag==null || bag.tTqVmuHPEhAbg!=3) {fail("Không tìm thấy vật phẩm cần trang bị");return;}
            phase=EQUIP;EAVbpdxFGDnFcb.peMayxrZEBA().jPtiHTBf(bag.AZmTbjgvi);return;
        }
        openNpc();
    }
    private void openNpc() {
        menuBaseline=menuVersion;phase=ROOT;deadline=System.currentTimeMillis()+15000;
        zxnsayePWpajv.oXAaoinDuwaN(npcId);
    }
    public void notice(String message) {
        if(notices.length()>0) notices+="\n";
        notices+=message;
        if(!engine.running()) {active=false;phase=0;AutoPetAccess.dismiss();AutoPetBridge.notice(notices);notices="";}
    }
    private void fail(String message) {
        active=false;phase=0;AutoPetAccess.dismiss();
        // Invalidate the engine before any queued server event can advance it.
        engine.cancel();notices="";AutoPetBridge.notice("Dừng auto: "+message);
    }
    public static synchronized void tick() {
        AutoPetRuntime r=INSTANCE;
        if(!r.active) return;
        try {r.step(System.currentTimeMillis());} catch(Exception e) {r.fail(e.getMessage());}
    }
    private void step(long now) {
        if(serverError!=null) {String text=serverError;serverError=null;fail(text);return;}
        if(AutoPetAccess.map()!=AutoPetRules.TONE || findNpc()!=npcId) {fail("Đã rời Tashino ở Làng Tone");return;}
        if(now>=deadline) {fail("Quá thời gian chờ "+waitingFor()+"; không gửi lại thao tác");return;}
        if(phase==BOOT || phase==REFRESH) {
            if(infoVersion<=infoBaseline || infoEventSource!=infoSource || infoEventIndex!=infoIndex) return;
            int target=phase==BOOT?bootIndex:item;
            wpiwfWAXuWze it=locate(target,false);
            if(it==null || it.tTqVmuHPEhAbg!=infoSource || it.AZmTbjgvi!=infoIndex || !it.ffWUPzwfbDA) {fail("Không đọc được thông tin mới của vật phẩm");return;}
            if(phase==BOOT) {initial[bootIndex]=snapshot(it);++bootIndex;boot();}
            else {phase=0;engine.reply(run,request,true,snapshot(it),now);}
            return;
        }
        if(phase==EQUIP) {if(locate(item,true)!=null) openNpc();return;}
        if(phase==ROOT && menuVersion>menuBaseline) {
            menuBaseline=menuVersion;int selected=-1;
            for(int i=0;i<menu.size();i++) if(AutoPetRules.menu(((bmxqULQDW)menu.elementAt(i)).oXAaoinDuwaN)==action) {
                if(selected>=0) {fail("Menu Tashino có nhiều lựa chọn trùng tên");return;}selected=i;
            }
            if(selected<0) {fail("Không có mục "+(action==1?"Nhận Vật Phẩm":action==2?"Luyện Trang Bị":"Nâng cấp vật phẩm")+" trong menu Tashino");return;}
            // Capture before clicking: both list-before-open and open-before-list are valid.
            phase=SHOP;shopBaseline=shopVersion;shopType=-1;listBaseline=listVersion;
            shopListRequested=false;deadline=now+15000;
            AutoPetAccess.click(menu,selected);return;
        }
        if(phase==SHOP) {
            if(shopVersion>shopBaseline) {
                shopBaseline=shopVersion;shopType=openedShop;shopRequestAt=now+300;shopCacheAt=now+1000;
            }
            if(shopType<0) return;
            int listType=shopType==52?32:shopType;
            if(listType<0 || listType>=listRevisions.length) {fail("Loại danh sách vật phẩm không được hỗ trợ");return;}
            boolean freshList=listRevisions[listType]>listBaseline;
            if(!freshList) {
                // The native screen may already have requested it. Only one read-only fallback.
                if(!shopListRequested && now>=shopRequestAt) {
                    shopListRequested=true;
                    // Native DPftvpqHuSPmve refreshes window 52 using list source 32.
                    EAVbpdxFGDnFcb.peMayxrZEBA().zmEhqDHwAKvzjM(listType);
                }
                // Native shops deliberately reuse their loaded catalog. A fresh packet is
                // not a prerequisite for selecting an item shown in a newly opened shop.
                if(now<shopCacheAt) return;
            }
            String title=AutoPetAccess.shopTitle();
            if(title!=null && title.length()>0 && !AutoPetRules.shop(action,title)) {
                fail("Cửa sổ hiện tại không đúng thao tác đã chọn: "+title);return;
            }
            wpiwfWAXuWze chosen=null;int chosenIndex=-1;
            for(int i=0;i<256;i++) {
                wpiwfWAXuWze it=AutoPetAccess.shopItem(shopType,i);
                if(it!=null && it.EgoDKTjkxEUMu!=null && it.EgoDKTjkxEUMu.QpKzrQoqbjfLc==templateId(item)) {
                    if(chosen!=null && chosen!=it) {fail("Danh sách có nhiều vật phẩm cùng ID");return;}chosen=it;chosenIndex=i;
                }
            }
            if(chosen==null) {
                if(freshList) fail("Không thấy "+AutoPetConfig.NAMES[item]+" trong danh sách Tashino");
                return;
            }
            AutoPetAccess.focusShop(chosenIndex);
            confirmBaseline=confirmVersion;phase=action==AutoPetEngine.RECEIVE?ACK:CONFIRM;
            nextInfo=now+300;deadline=now+15000;
            EAVbpdxFGDnFcb.peMayxrZEBA().BMWCZFcnBgzHkh(chosen.tTqVmuHPEhAbg,chosen.AZmTbjgvi,1);return;
        }
        if(phase==CONFIRM && confirmVersion>confirmBaseline) {
            confirmBaseline=confirmVersion;
            if(!AutoPetRules.confirmation(action,confirmText) || confirmCode==0) {fail("Nội dung xác nhận không khớp thao tác đã chọn");return;}
            if(action==AutoPetEngine.UPGRADE) {
                String s=AutoPetRules.plain(confirmText);
                String name=item==0?"pet long":"long chau mika2";
                if(s.indexOf(name)<0) {fail("Hộp xác nhận nâng cấp không đúng vật phẩm");return;}
            }
            phase=ACK;nextInfo=now+300;deadline=now+15000;
            NBAFgUXWGCkgPq.HmLENarbmj();
            EAVbpdxFGDnFcb.peMayxrZEBA().hFWcqhZjekJK(confirmCode);return;
        }
        if(phase==ACK && now>=nextInfo) {
            wpiwfWAXuWze it=locate(item,false);
            if(it==null) return; // receiving must actually create the item
            phase=REFRESH;askInfo(it);
        }
    }
    private String waitingFor() {
        if(phase==ROOT) return "menu Tashino";
        if(phase==SHOP) return "danh sách "+(action==1?"nhận ":action==2?"luyện ":"nâng ")+AutoPetConfig.NAMES[item];
        if(phase==CONFIRM) return "xác nhận của máy chủ";
        if(phase==EQUIP) return "trang bị "+AutoPetConfig.NAMES[item];
        if(phase==ACK) return "kết quả thao tác trên máy chủ";
        return "thông tin mới của vật phẩm";
    }
    public static synchronized void sawMenu(kacyVKUNru choices) {menu=choices;++menuVersion;}
    public static synchronized void sawShop(int source) {openedShop=source;++shopVersion;}
    public static synchronized void sawConfirm(String text,int yes,Object code,int no) {
        if(yes!=8890 || no!=8891 || !(code instanceof Integer)) return;
        confirmText=text;confirmCode=((Integer)code).intValue();++confirmVersion;
    }
    public static synchronized void sawNotice(String text) {
        if(!INSTANCE.active || text==null) return;
        String s=AutoPetRules.plain(text);
        if(s.indexOf("khong du")>=0 || s.indexOf("hay trang bi")>=0 || s.indexOf("khong the")>=0 || s.indexOf("that bai")>=0 || s.indexOf("chua du")>=0)
            serverError=text;
    }
    public static synchronized void beforePacket(WZKDTXfSh msg) {
        eventCommand=msg==null?0:msg.GAuWiEMckbFHeh;eventSource=-1;eventIndex=-1;
        if(eventCommand!=33 && eventCommand!=42) return;
        try {
            DataInputStream in=AutoPetAccess.input(msg);in.mark(3);
            eventSource=in.readUnsignedByte();if(eventCommand==42) eventIndex=in.readUnsignedByte();in.reset();
        } catch(Exception ignored) {eventSource=-1;}
    }
    public static synchronized void afterPacket() {
        if(eventCommand==33 && eventSource>=0) {listRevisions[eventSource]=++listVersion;}
        if(eventCommand==42 && eventSource>=0 && eventIndex>=0) {infoEventSource=eventSource;infoEventIndex=eventIndex;++infoVersion;}
    }
}
