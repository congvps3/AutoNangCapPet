import javax.microedition.rms.RecordStore;

/** One acknowledged action at a time on the original Tan sat worker. */
public final class AutoPetLevel {
    private static final int TRAVEL_GIFT=1,MENU_GIFT=2,INPUT_GIFT=3,GIFT_ACK=4,SETTLE=5,USE_ACK=6,TRAVEL_PET=7,PET_RUN=8,LEVEL=9;
    private static final String[] CODES={"200mem","CODETUAN4"};
    private static final int[] USE_IDS={829,1399,41,44,48},USE_LEVELS={0,0,20,30,50},USE_FLAGS={4,8,64,128,256};
    private static volatile SJSCaeScqbiua owner;
    private static volatile int stage;
    private static boolean giftCustom;
    private static String character,key,useName;
    private static int flags,target,codeIndex,menuBase,inputBase,windowBase,giftBase,noticeBase,confirmBase,useId,useFlag;
    private static long deadline,settleUntil,useBefore;
    private static String character() {
        oFpffzUyucjDJX p=oFpffzUyucjDJX.rdmjdEJDQ();return p==null?"":p.BfndoYuX+":"+p.dvKEjoqBk;
    }
    private static void store(boolean write) throws Exception {
        RecordStore rs=null;
        try {
            rs=RecordStore.openRecordStore(key,true);
            if(write) {
                java.io.ByteArrayOutputStream bytes=new java.io.ByteArrayOutputStream();java.io.DataOutputStream out=new java.io.DataOutputStream(bytes);
                out.writeUTF(character);out.writeInt(flags);out.flush();byte[] data=bytes.toByteArray();
                if(rs.getNumRecords()==0)rs.addRecord(data,0,data.length);else rs.setRecord(1,data,0,data.length);
            } else {
                flags=0;
                if(rs.getNumRecords()>0) {
                    java.io.DataInputStream in=new java.io.DataInputStream(new java.io.ByteArrayInputStream(rs.getRecord(1)));
                    if(!in.readUTF().equals(character))throw new RuntimeException("Sai dấu tiến trình nhân vật");
                    flags=in.readInt();
                }
            }
        } finally {if(rs!=null)rs.closeRecordStore();}
    }
    private static void mark(int flag) throws Exception {flags|=flag;store(true);}
    private static boolean active() {return owner!=null && AutoPetAccess.currentAuto()==owner && character().equals(character);}
    private static void checkActive() {if(!active())throw new RuntimeException("Đã dừng Tàn sát hoặc đổi nhân vật");}
    private static void closeDialogs() {
        AutoPetAccess.dismiss();
        if((giftCustom || stage==INPUT_GIFT) && FcnKZwMhE.GAWjBFnKuf)FcnKZwMhE.okhVYHbLapzrW();
        giftCustom=false;
    }
    private static String waitingFor() {
        if(stage==TRAVEL_GIFT)return "đến Okanechan";
        if(stage==MENU_GIFT || stage==INPUT_GIFT || stage==GIFT_ACK)return "mã "+CODES[codeIndex];
        if(stage==SETTLE)return "cập nhật quà";
        if(stage==USE_ACK)return "sử dụng vật phẩm "+useId;
        return "làm "+AutoPetConfig.NAMES[target];
    }
    private static void stop(String message) {
        if(stage==PET_RUN && AutoPetRuntime.running())AutoPetRuntime.stop();
        if(owner!=null && AutoPetAccess.currentAuto()==owner)AutoPetAccess.setAuto(null);
        closeDialogs();owner=null;stage=0;if(message!=null)AutoPetBridge.notice(message);
    }
    private static gsxwWgJt npc(String name) {
        kacyVKUNru all=AutoPetAccess.npcs();
        if(all!=null)for(int i=0;i<all.size();i++) {
            gsxwWgJt n=(gsxwWgJt)all.elementAt(i);HAeSnYtYmns t=AutoPetAccess.template(n);
            if(t!=null && AutoPetRules.plain(t.wLHNXXbvUSit).equals(name))return n;
        }
        return null;
    }
    private static gsxwWgJt travel(SJSCaeScqbiua auto,String name) {
        if(System.currentTimeMillis()>=deadline)throw new RuntimeException("Chưa tới được NPC "+name+" ở Làng Tone");
        if(AutoPetAccess.map()!=22){auto.peMayxrZEBA(22,-1,-1,-1);return null;}
        gsxwWgJt n=npc(name);
        if(n!=null && !AutoPetAccess.nearNpc(n)){BMWCZFcnBgzHkh.peMayxrZEBA(n);return null;}
        return n;
    }
    private static void route(int next) {stage=next;deadline=System.currentTimeMillis()+180000;AutoPetAccess.clearCombat();}
    public static int codeResult(String text) {
        String s=AutoPetRules.plain(text);
        if(s.indexOf("khong hop le")>=0 || s.indexOf("khong ton tai")>=0 || s.indexOf("het han")>=0 || s.indexOf("that bai")>=0 || s.indexOf("khong du")>=0 || s.indexOf("khong thanh cong")>=0)return -1;
        if(s.indexOf("da su dung")>=0 || s.indexOf("da duoc su dung")>=0 || (s.indexOf("da nhan")>=0 && (s.indexOf("ma")>=0 || s.indexOf("code")>=0 || s.indexOf("qua")>=0)))return 1;
        if(s.indexOf("thanh cong")>=0 && (s.indexOf("nhan")>=0 || s.indexOf("ma")>=0 || s.indexOf("code")>=0 || s.indexOf("qua")>=0))return 1;
        return 0;
    }
    private static void codeDone() throws Exception {
        mark(1<<codeIndex);closeDialogs();
        if((flags&3)!=3){codeIndex=(flags&1)==0?0:1;route(TRAVEL_GIFT);}
        else {stage=SETTLE;settleUntil=System.currentTimeMillis()+1500;}
    }
    private static void sendCode(boolean custom) {
        checkActive();giftBase=AutoPetEvents.gifts;noticeBase=AutoPetEvents.notices;
        giftCustom=custom;stage=GIFT_ACK;deadline=System.currentTimeMillis()+15000;NBAFgUXWGCkgPq.HmLENarbmj();
        if(custom)EAVbpdxFGDnFcb.peMayxrZEBA().FdGQnmAerVxMi(CODES[codeIndex]);
        else EAVbpdxFGDnFcb.peMayxrZEBA().EgoDKTjkxEUMu((short)AutoPetEvents.inputCode,CODES[codeIndex]);
    }
    private static boolean useNext() {
        synchronized(AutoPetRuntime.packetLock()) {
            int level=oFpffzUyucjDJX.rdmjdEJDQ().vMyUkiiUKxerr;
            for(int i=0;i<USE_IDS.length;i++)if(level>=USE_LEVELS[i] && (flags&USE_FLAGS[i])==0) {
                wpiwfWAXuWze it=AutoPetInventory.bag(USE_IDS[i]);if(it==null)continue;
                if(it.AZmTbjgvi<0 || it.AZmTbjgvi>255)throw new RuntimeException("Ô hành trang ngoài phạm vi giao thức");
                checkActive();useId=USE_IDS[i];useFlag=USE_FLAGS[i];useBefore=AutoPetInventory.count(useId);useName=AutoPetRules.plain(it.EgoDKTjkxEUMu.wLHNXXbvUSit);
                noticeBase=AutoPetEvents.notices;confirmBase=AutoPetEvents.confirms;stage=USE_ACK;deadline=System.currentTimeMillis()+15000;
                AutoPetAccess.clearCombat();EAVbpdxFGDnFcb.peMayxrZEBA().jPtiHTBf(it.AZmTbjgvi);return true;
            }
            return false;
        }
    }
    public static synchronized boolean intercept(SJSCaeScqbiua auto) {
        try {
            if(AutoPetAccess.currentAuto()!=auto)return true;
            if(stage!=0 && !active()){stop(null);return true;}
            if(stage==0) {
                if(AutoPetRuntime.running())return true;
                owner=auto;character=character();key="APLC20_"+character.hashCode();store(false);
                if((flags&3)!=3){codeIndex=(flags&1)==0?0:1;route(TRAVEL_GIFT);}else stage=LEVEL;
            }
            if(stage!=TRAVEL_GIFT && stage!=TRAVEL_PET && stage!=LEVEL && stage!=PET_RUN && stage!=SETTLE && System.currentTimeMillis()>=deadline)
                throw new RuntimeException("Quá thời gian chờ máy chủ; không gửi lại thao tác đang chờ");
            if(stage==TRAVEL_GIFT) {
                gsxwWgJt n=travel(auto,"okanechan");if(n==null)return true;
                checkActive();menuBase=AutoPetEvents.menus;stage=MENU_GIFT;deadline=System.currentTimeMillis()+15000;
                zxnsayePWpajv.oXAaoinDuwaN(AutoPetAccess.template(n).mvjvhHcftNANDP);return true;
            }
            if(stage==MENU_GIFT && AutoPetEvents.menus>menuBase) {
                kacyVKUNru choices=AutoPetEvents.menu;int selected=-1;
                for(int i=0;i<choices.size();i++) {
                    String s=AutoPetRules.plain(((bmxqULQDW)choices.elementAt(i)).oXAaoinDuwaN);
                    if(s.equals("ma qua tang") || s.equals("giftcode") || s.equals("nhap giftcode")){if(selected>=0)throw new RuntimeException("Menu mã quà tặng trùng tên");selected=i;}
                }
                if(selected<0)throw new RuntimeException("Không thấy mục Mã quà tặng ở Okanechan");
                checkActive();inputBase=AutoPetEvents.inputs;windowBase=AutoPetEvents.giftWindows;stage=INPUT_GIFT;deadline=System.currentTimeMillis()+15000;
                AutoPetAccess.click(choices,selected);return true;
            }
            if(stage==INPUT_GIFT) {
                if(AutoPetEvents.inputs>inputBase) {
                    String title=AutoPetRules.plain(AutoPetEvents.inputTitle);
                    if((title.indexOf("ma")<0 && title.indexOf("code")<0) || title.indexOf("mat khau")>=0)throw new RuntimeException("Ô nhập không phải mã quà tặng");
                    sendCode(false);
                } else if(AutoPetEvents.giftWindows>windowBase)sendCode(true);
                return true;
            }
            if(stage==GIFT_ACK) {
                if(AutoPetEvents.gifts>giftBase) {
                    giftBase=AutoPetEvents.gifts;
                    if(AutoPetEvents.giftOK && CODES[codeIndex].equalsIgnoreCase(AutoPetEvents.giftCode)){codeDone();return true;}
                    if(!AutoPetEvents.giftOK){if(codeResult(AutoPetEvents.giftText)==1)codeDone();else throw new RuntimeException(AutoPetEvents.giftText);return true;}
                }
                if(AutoPetEvents.notices>noticeBase) {
                    noticeBase=AutoPetEvents.notices;int result=codeResult(AutoPetEvents.notice);
                    if(result==1)codeDone();if(result<0)throw new RuntimeException(AutoPetEvents.notice);
                }
                return true;
            }
            if(stage==SETTLE){if(System.currentTimeMillis()<settleUntil)return true;stage=LEVEL;}
            if(stage==USE_ACK) {
                if(AutoPetInventory.count(useId)<useBefore || AutoPetInventory.worn(useId)!=null){mark(useFlag);stage=LEVEL;}
                else {
                    if(AutoPetEvents.notices>noticeBase) {
                        noticeBase=AutoPetEvents.notices;String s=AutoPetRules.plain(AutoPetEvents.notice);
                        if(s.indexOf("khong the")>=0 || s.indexOf("khong du")>=0 || s.indexOf("chua du")>=0 || s.indexOf("that bai")>=0)throw new RuntimeException(AutoPetEvents.notice);
                    }
                    if(AutoPetEvents.confirms>confirmBase) {
                        confirmBase=AutoPetEvents.confirms;String s=AutoPetRules.plain(AutoPetEvents.confirmText);
                        if((s.indexOf("su dung")<0 && s.indexOf("mo ")<0) || (s.indexOf("vat pham")<0 && (useName.length()==0 || s.indexOf(useName)<0)))throw new RuntimeException("Xác nhận sử dụng vật phẩm không khớp");
                        checkActive();NBAFgUXWGCkgPq.HmLENarbmj();EAVbpdxFGDnFcb.peMayxrZEBA().hFWcqhZjekJK(AutoPetEvents.confirmCode);
                    }
                    return true;
                }
            }
            if(stage==PET_RUN) {
                if(AutoPetRuntime.running())return true;
                if(!AutoPetRuntime.completed()){stop(null);return true;}
                mark(target==0?16:32);stage=LEVEL;AutoPetAccess.resetLevel(auto);
            }
            if(stage==LEVEL) {
                if(AutoPetRuntime.running())return true;
                if(useNext())return true;
                int level=oFpffzUyucjDJX.rdmjdEJDQ().vMyUkiiUKxerr;
                target=(flags&16)==0 && AutoPetInventory.worn(AutoPetRules.PET)==null?0
                    :level>=21 && (flags&32)==0 && AutoPetInventory.worn(AutoPetRules.MIKA)==null?1:-1;
                if(target<0)return false;
                AutoPetConfig[] settings=AutoPetUI.settings();settings[target].validate();
                if(!settings[target].enabled)throw new RuntimeException("Hãy cấu hình và Enable "+AutoPetConfig.NAMES[target]+" trong AutoPet/LC rồi bật lại Tàn sát");
                route(TRAVEL_PET);
            }
            if(stage==TRAVEL_PET) {
                if(travel(auto,"tashino")==null)return true;
                checkActive();stage=PET_RUN;AutoPetRuntime.startTarget(target);
                if(!AutoPetRuntime.running() && !AutoPetRuntime.completed())stop(null);
                return true;
            }
        }catch(Exception e){stop("Dừng Auto ("+waitingFor()+"): "+e.getMessage());}
        return true;
    }
    public static void watch() {
        SJSCaeScqbiua active=owner;
        if(active!=null && (AutoPetAccess.currentAuto()!=active || !character().equals(character))) {
            if(stage==PET_RUN)AutoPetRuntime.stop();closeDialogs();owner=null;stage=0;
        }
    }
}
