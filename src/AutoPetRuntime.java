import java.io.DataInputStream;

/** Uses the existing NPC/shop protocol and actual inventory, never shop previews as results. */
public final class AutoPetRuntime implements AutoPetEngine.Port {
    private static final AutoPetRuntime INSTANCE=new AutoPetRuntime();
    private static final int BOOT=1,EQUIP=2,ROOT=3,SHOP=4,CONFIRM=5,ACK=6,REFRESH=7,CLAIM=8;
    private final AutoPetEngine engine=new AutoPetEngine(this);
    private AutoPetConfig[] config;
    private final AutoPetEngine.Item[] initial=new AutoPetEngine.Item[2];
    private boolean active;
    private boolean completed;
    private int phase,bootIndex,run,request,item,action,npcId,shopType=-1;
    private int infoSource,infoIndex,infoBaseline,listBaseline,shopBaseline,menuBaseline,confirmBaseline;
    private int infoRetries;
    private long infoRetryAt;
    private long deadline,nextInfo,shopRequestAt,shopCacheAt;
    private boolean shopListRequested;
    private String notices="";
    private static int eventCommand,eventSource,eventIndex;
    private static int infoVersion,infoEventSource,infoEventIndex,listVersion,shopVersion,openedShop,menuVersion,confirmVersion,confirmCode;
    private static final int[] listRevisions=new int[256];
    private static String confirmText;
    private static String serverError;
    private static kacyVKUNru menu;
    private static boolean auditing;
    private static long traceStart;
    private static String trace="";
    private static String lastRunState="Chưa chạy",actionQ="-",actionTX="-",infoQ="-",infoTX="-",lastReply="-";
    private static int eventSub=-999;
    private static final int[] subCounts=new int[256];

    /** Native packet parsing and Auto decisions must see the same complete inventory. */
    public static Object packetLock() {return AutoPetRuntime.class;}

    public static synchronized boolean running() {return INSTANCE.active;}
    public static synchronized boolean completed() {return INSTANCE.completed;}
    public static synchronized void stop() {if(INSTANCE.active) toggle();}
    public static synchronized void toggle() {
        if(!INSTANCE.active){try{begin(AutoPetUI.settings());}catch(Exception e){INSTANCE.fail(e.getMessage());}return;}
        AutoPetRuntime r=INSTANCE;
        if(r.active) {
            r.completed=false;
            lastRunState=r.waitingFor();
            r.active=false;r.phase=0;r.notices="";AutoPetAccess.dismiss();
            if(r.engine.running()) r.engine.stop();else AutoPetBridge.notice("đã dừng auto");
            return;
        }
    }
    public static synchronized void startTarget(int target) {
        if(INSTANCE.active)throw new RuntimeException("AutoPet/LC đang chạy");
        AutoPetConfig[] settings=AutoPetUI.settings();settings[1-target].enabled=false;
        begin(settings);
    }
    private static void begin(AutoPetConfig[] settings) {
        AutoPetRuntime r=INSTANCE;
        try {
            r.completed=false;
            r.config=settings;boolean any=false;
            for(int i=0;i<2;i++) {r.config[i].receive=false;r.config[i].validate();any|=r.config[i].enabled;}
            if(!any) {AutoPetBridge.notice("Hãy bật Enable trong Menu > AutoPet/LC và lưu cấu hình");return;}
            if(AutoPetAccess.map()!=AutoPetRules.TONE) {AutoPetBridge.notice("Hãy đến Làng Tone (map 22), đứng gần Tashino rồi bấm Auto");return;}
            r.npcId=r.findNpc();
            if(r.npcId<0) {AutoPetBridge.notice("Chưa thấy Tashino trong bản đồ hiện tại");return;}
            r.active=true;r.notices="";serverError=null;r.bootIndex=0;r.phase=BOOT;r.deadline=System.currentTimeMillis()+15000;
            auditing=true;trace="";traceStart=System.currentTimeMillis();actionQ="-";actionTX="-";infoQ="-";infoTX="-";lastReply="-";
            for(int i=0;i<subCounts.length;i++)subCounts[i]=0;
            lastRunState=r.waitingFor();record("START r20");
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
    private wpiwfWAXuWze startItem(int target) {
        return locate(target,false);
    }
    private wpiwfWAXuWze bagItem(int target) {
        wpiwfWAXuWze it=AutoPetInventory.bag(templateId(target));
        if(it==null)throw new RuntimeException("Không thấy "+AutoPetConfig.NAMES[target]+" trong hành trang");
        return it;
    }
    private wpiwfWAXuWze locate(int target,boolean equippedOnly) {
        return equippedOnly?AutoPetInventory.worn(templateId(target)):AutoPetInventory.find(templateId(target));
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
        return new AutoPetEngine.Item(true,options,it.yKHpKzQyZroJ,it.tTqVmuHPEhAbg!=3);
    }
    private void boot() {
        while(bootIndex<2) {
            wpiwfWAXuWze it=config[bootIndex].enabled?startItem(bootIndex):null;
            initial[bootIndex]=snapshot(it);
            if(it!=null) {askInfo(it);return;}
            if(config[bootIndex].enabled) {
                if(!AutoPetInventory.room())throw new RuntimeException("Hành trang đầy, không nhận thêm vật phẩm");
                item=bootIndex;action=AutoPetEngine.RECEIVE;openNpc();return;
            }
            ++bootIndex;
        }
        phase=0;engine.start(config,initial,System.currentTimeMillis());
    }
    private void askInfo(wpiwfWAXuWze it) {
        infoSource=it.tTqVmuHPEhAbg;infoIndex=it.AZmTbjgvi;infoBaseline=infoVersion;
        it.ffWUPzwfbDA=false;
        deadline=System.currentTimeMillis()+15000;
        infoRetries=0;infoRetryAt=System.currentTimeMillis()+750;
        EAVbpdxFGDnFcb.peMayxrZEBA().uYrMAoZFCm(infoSource,infoIndex);
    }
    private wpiwfWAXuWze readItem(int target) {
        wpiwfWAXuWze[] all=AutoPetAccess.items(infoSource);
        wpiwfWAXuWze it=all!=null && infoIndex>=0 && infoIndex<all.length?all[infoIndex]:null;
        return it!=null && it.EgoDKTjkxEUMu!=null && it.EgoDKTjkxEUMu.QpKzrQoqbjfLc==templateId(target)
            && it.tTqVmuHPEhAbg==infoSource && it.AZmTbjgvi==infoIndex?it:null;
    }
    public void send(int generation,int sequence,int target,int operation) {
        record("TASK id="+templateId(target)+" op="+operation);
        run=generation;request=sequence;item=target;action=operation;
        deadline=System.currentTimeMillis()+15000;
        if(operation==AutoPetEngine.RECEIVE) {fail("Thiếu vật phẩm sau bước tự nhận");return;}
        if(operation==AutoPetEngine.TRAIN) {bagItem(item);openNpc();return;}
        if(operation==AutoPetEngine.USE) {
            wpiwfWAXuWze bag=bagItem(item);
            if(config[item].train && (snapshot(bag).options & config[item].selected)!=config[item].selected) {fail("Chưa đủ chỉ số, không sử dụng vật phẩm");return;}
            phase=EQUIP;EAVbpdxFGDnFcb.peMayxrZEBA().jPtiHTBf(bag.AZmTbjgvi);return;
        }
        if(locate(item,true)==null) {fail("Chưa xác nhận vật phẩm được sử dụng, không nâng cấp");return;}
        openNpc();
    }
    private void openNpc() {
        menuBaseline=menuVersion;phase=ROOT;deadline=System.currentTimeMillis()+15000;
        zxnsayePWpajv.oXAaoinDuwaN(npcId);
    }
    public void notice(String message) {
        if(notices.length()>0) notices+="\n";
        notices+=message;
        if(!engine.running()) {
            completed=message.equals("AutoPet/LC đã hoàn tất");active=false;phase=0;AutoPetAccess.dismiss();
            if(completed) AutoPetBridge.complete(notices);else AutoPetBridge.notice(notices);
            notices="";
        }
    }
    private void fail(String message) {
        completed=false;
        String report=diagnostics();
        active=false;phase=0;AutoPetAccess.dismiss();
        // Invalidate the engine before any queued server event can advance it.
        engine.cancel();notices="";AutoPetBridge.notice("Dừng auto: "+message+"\n\n"+report);
    }
    public static synchronized void tick() {
        AutoPetRuntime r=INSTANCE;
        if(!r.active) return;
        lastRunState=r.waitingFor();
        try {r.step(System.currentTimeMillis());if(r.active)lastRunState=r.waitingFor();} catch(Exception e) {r.fail(e.getMessage());}
    }
    private void step(long now) {
        if(serverError!=null) {String text=serverError;serverError=null;fail(text);return;}
        if(AutoPetAccess.map()!=AutoPetRules.TONE || findNpc()!=npcId) {fail("Đã rời Tashino ở Làng Tone");return;}
        if(now>=deadline) {fail("Quá thời gian chờ "+waitingFor()+"; không gửi lại thao tác");return;}
        if(phase==CLAIM) {
            if(locate(item,false)!=null){phase=BOOT;boot();return;}
            if(confirmVersion>confirmBaseline) {
                confirmBaseline=confirmVersion;String s=AutoPetRules.plain(confirmText);
                if(s.indexOf("nhan")<0 || (s.indexOf("vat pham")<0 && s.indexOf(item==0?"pet long":"long chau mika2")<0)) {fail("Xác nhận nhận vật phẩm không khớp");return;}
                NBAFgUXWGCkgPq.HmLENarbmj();EAVbpdxFGDnFcb.peMayxrZEBA().hFWcqhZjekJK(confirmCode);
            }
            return;
        }
        if(phase==BOOT || phase==REFRESH) {
            int target=phase==BOOT?bootIndex:item;
            wpiwfWAXuWze it=readItem(target);
            if(infoVersion>infoBaseline && infoEventSource==infoSource && infoEventIndex==infoIndex && it!=null && it.ffWUPzwfbDA) {
                if(phase==BOOT) {initial[bootIndex]=snapshot(it);++bootIndex;boot();}
                else {phase=0;engine.reply(run,request,true,snapshot(it),now);}
                return;
            }
            // Inventory replacement and delayed detail packets are transient.
            // Retry only the read, without extending the deadline or spending.
            if(now>=infoRetryAt && infoRetries<2) {
                ++infoRetries;infoRetryAt=now+750;infoBaseline=infoVersion;
                if(it!=null) it.ffWUPzwfbDA=false;
                record("INFO retry "+infoRetries+" src="+infoSource+" pos="+infoIndex);
                EAVbpdxFGDnFcb.peMayxrZEBA().uYrMAoZFCm(infoSource,infoIndex);
            }
            return;
        }
        if(phase==EQUIP) {
            wpiwfWAXuWze usedItem=locate(item,true);
            if(usedItem!=null) {phase=REFRESH;askInfo(usedItem);}
            return;
        }
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
            if(shopType==4 && (title==null || !AutoPetRules.shop(action,title))) {
                fail("Cửa sổ 4 không có tiêu đề phù hợp thao tác");return;
            }
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
            confirmBaseline=confirmVersion;phase=CONFIRM;
            if(action==AutoPetEngine.RECEIVE)phase=CLAIM;
            nextInfo=now+300;deadline=now+15000;
            if(shopType==4) {
                if(chosen.tTqVmuHPEhAbg!=4) {fail("Nguồn vật phẩm không khớp cửa sổ 4");return;}
                // The real Tashino screen is a custom box UI. Its native primary
                // action is 111101 -> hsFScEwVUfcV(index) -> packet 16, not buying.
                AutoPetAccess.activateBox();
            } else {
                if(chosen.tTqVmuHPEhAbg==4) {fail("Không dùng lệnh mua cho vật phẩm cửa sổ 4");return;}
                EAVbpdxFGDnFcb.peMayxrZEBA().BMWCZFcnBgzHkh(chosen.tTqVmuHPEhAbg,chosen.AZmTbjgvi,1);
            }
            return;
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
            wpiwfWAXuWze it=action==AutoPetEngine.TRAIN?bagItem(item):locate(item,true);
            if(it==null) {fail("Không tìm thấy vật phẩm sau thao tác");return;}
            phase=REFRESH;askInfo(it);
        }
    }
    private String waitingFor() {
        if(phase==ROOT) return "menu Tashino";
        if(phase==SHOP) return "danh sách "+(action==1?"nhận ":action==2?"luyện ":"nâng ")+AutoPetConfig.NAMES[item];
        if(phase==CONFIRM) return "xác nhận của máy chủ";
        if(phase==CLAIM) return "nhận "+AutoPetConfig.NAMES[item]+" vào hành trang";
        if(phase==EQUIP) return "trang bị "+AutoPetConfig.NAMES[item];
        if(phase==ACK) return "kết quả thao tác trên máy chủ";
        return "thông tin mới của vật phẩm";
    }
    public static synchronized void sawMenu(kacyVKUNru choices) {menu=choices;++menuVersion;AutoPetEvents.menu(choices);}
    public static synchronized void sawShop(int source) {openedShop=source;++shopVersion;record("SHOP "+source);}
    public static synchronized void sawConfirm(String text,int yes,Object code,int no) {
        if(yes!=8890 || no!=8891 || !(code instanceof Integer)) return;
        AutoPetEvents.confirm(text,((Integer)code).intValue());
        confirmText=text;confirmCode=((Integer)code).intValue();++confirmVersion;
    }
    public static synchronized void sawNotice(String text) {
        AutoPetEvents.notice(text);
        if(!INSTANCE.active || text==null) return;
        String s=AutoPetRules.plain(text);
        if(INSTANCE.action==AutoPetEngine.UPGRADE && (INSTANCE.phase==ACK || INSTANCE.phase==REFRESH)
            && AutoPetRules.upgradeFailure(text)) {record("UPGRADE failed; await fresh item");return;}
        if(s.indexOf("khong du")>=0 || s.indexOf("hay trang bi")>=0 || s.indexOf("hay nhan vat pham")>=0 || s.indexOf("khong the")>=0 || s.indexOf("that bai")>=0 || s.indexOf("chua du")>=0)
            serverError=text;
    }
    public static synchronized void beforePacket(WZKDTXfSh msg) {
        AutoPetEvents.before(msg);
        eventCommand=msg==null?0:msg.GAuWiEMckbFHeh;eventSource=-1;eventIndex=-1;eventSub=-999;
        if(eventCommand==31) {eventSource=4;return;}
        if(eventCommand!=33 && eventCommand!=42 && eventCommand!=-30) return;
        try {
            DataInputStream in=AutoPetAccess.input(msg);in.mark(3);
            try {
                if(eventCommand==-30) eventSub=in.readByte();
                else {eventSource=in.readUnsignedByte();if(eventCommand==42) eventIndex=in.readUnsignedByte();}
            } finally {in.reset();}
        } catch(Exception ignored) {eventSource=-1;}
    }
    public static synchronized void afterPacket() {
        AutoPetEvents.after();
        if(eventCommand==33 && eventSource>=0) {listRevisions[eventSource]=++listVersion;}
        if(eventCommand==31) {listRevisions[4]=++listVersion;}
        if(eventCommand==42 && eventSource>=0 && eventIndex>=0) {infoEventSource=eventSource;infoEventIndex=eventIndex;++infoVersion;}
        if(eventCommand==-30) {
            if(auditing && eventSub!=-999) ++subCounts[eventSub & 255];
            return;
        }
        if(eventCommand==13 || eventCommand==11 || eventCommand==30 || eventCommand==31 || eventCommand==33 || eventCommand==42 || eventCommand==107 || eventCommand==-26) {
            String line="RX "+eventCommand+(eventSource>=0?" src="+eventSource:"")+(eventIndex>=0?" pos="+eventIndex:"");
            if(auditing) lastReply=line;
            record(line);
        }
    }
    private static void record(String line) {
        if(!auditing) return;
        trace+=(System.currentTimeMillis()-traceStart)/1000+"s "+line+"\n";
        while(trace.length()>600) {int n=trace.indexOf('\n');if(n<0) break;trace=trace.substring(n+1);}
    }
    private static void wire(String stage,WZKDTXfSh msg) {
        if(!auditing || msg==null) return;
        int command=msg.GAuWiEMckbFHeh;
        // Never inspect login/chat payloads. These fields are only item source/index or confirmation code.
        if(command!=13 && command!=16 && command!=42 && command!=107 && command!=11 && command!=-30) return;
        byte[] bytes=msg.EgoDKTjkxEUMu();
        String line=null;
        if(command==-30) {
            if(bytes.length==2 && bytes[0]==-103) line=stage+" LIST src="+(bytes[1]&255);
        } else if((command==13 || command==42) && bytes.length>=2) line=stage+" "+command+" src="+(bytes[0]&255)+" pos="+(bytes[1]&255);
        else if(bytes.length>=1) line=stage+" "+command+(command==16?" pos=":" code=")+(bytes[0]&255);
        if(line==null)return;
        if(command==42) {if(stage.equals("Q"))infoQ=line;else infoTX=line;}
        else if(command==13 || command==16 || command==11 || command==107) {if(stage.equals("Q"))actionQ=line;else actionTX=line;}
        record(line);
    }
    public static synchronized void queued(WZKDTXfSh msg) {try {wire("Q",msg);}catch(Exception ignored) {}}
    public static synchronized void transmitted(WZKDTXfSh msg) {try {wire("TX",msg);}catch(Exception ignored) {}}
    public static synchronized String diagnostics() {
        String subs="";boolean[] seen=new boolean[256];
        for(int n=0;n<8;n++) {
            int best=-1;for(int i=0;i<256;i++)if(!seen[i] && subCounts[i]>0 && (best<0 || subCounts[i]>subCounts[best]))best=i;
            if(best<0)break;seen[best]=true;
            if(subs.length()>0)subs+=", ";subs+=(best>127?best-256:best)+"x"+subCounts[best];
        }
        return "r20 | "+lastRunState+" | ID "+INSTANCE.templateId(INSTANCE.item)+"\n"
            +actionQ+"\n"+actionTX+"\n"+infoQ+"\n"+infoTX+"\n"+lastReply+"\n"
            +"Nhóm -30: "+(subs.length()==0?"-":subs)+"\n"+trace;
    }
}
