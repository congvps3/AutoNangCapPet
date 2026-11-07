import java.io.*;

/** End-to-end level journey against the original classes and native packet creators. */
public class JourneyCases extends RuntimeSmokeMidlet {
    static HAeSnYtYmns okane() {HAeSnYtYmns t=new HAeSnYtYmns();t.mvjvhHcftNANDP=10;t.wLHNXXbvUSit="Okanechan";return t;}
    static void level(int n) throws Exception {field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(n));}
    static void map(int n) throws Exception {field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)n));}
    static void set(String name,long n) throws Exception {field(AutoPetLevel.class,null,name,long.class,Long.valueOf(n));}
    static SJSCaeScqbiua fresh(int id,int lv,boolean enabled) throws Exception {
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();if(AutoPetRuntime.running())AutoPetRuntime.stop();
        reset(enabled,enabled,3);bag[0]=bag[1]=null;
        field(oFpffzUyucjDJX.class,player,"BfndoYuX",int.class,Integer.valueOf(id));
        field(oFpffzUyucjDJX.class,player,"dvKEjoqBk",String.class,"journey");
        field(oFpffzUyucjDJX.class,player,"wLHNXXbvUSit",int.class,Integer.valueOf(100));
        field(oFpffzUyucjDJX.class,player,"SgQHNwMh",int.class,Integer.valueOf(100));level(lv);map(22);
        SJSCaeScqbiua auto=new SJSCaeScqbiua();AutoPetAccess.setAuto(auto);return auto;
    }
    static void packet(int command,byte[] payload) {new yRvgxykm().WRmEtfNMLdMb(new WZKDTXfSh((byte)command,payload));}
    static void menu(SJSCaeScqbiua auto) {
        check(TestTransport.last.equals("npc") && TestTransport.a==10,"gift preparation must open Okanechan, not Tashino");
        kacyVKUNru choices=new kacyVKUNru();choices.addElement(new bmxqULQDW("Mã quà tặng",new TestTransport(),104,null));
        AutoPetRuntime.sawMenu(choices);check(AutoPetLevel.intercept(auto) && TestTransport.last.equals("menu") && TestTransport.a==104,"gift menu callback");
    }
    static void input(String title,int token) throws Exception {
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();DataOutputStream out=new DataOutputStream(bytes);out.writeUTF(title);out.writeShort(token);out.flush();packet(92,bytes.toByteArray());
    }
    static void notice(String text) throws Exception {
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();DataOutputStream out=new DataOutputStream(bytes);out.writeUTF(text);out.flush();packet(-26,bytes.toByteArray());
    }
    static void customWindow() {FcnKZwMhE.GAWjBFnKuf=true;packet(-33,new byte[]{0,0,0});}
    static void customAck(boolean ok,String value) throws Exception {
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();DataOutputStream out=new DataOutputStream(bytes);out.writeByte(1);out.writeByte(ok?1:0);out.writeUTF(value);
        if(ok){out.writeInt(0);out.writeInt(0);out.writeInt(0);out.writeByte(0);}out.flush();packet(-33,bytes.toByteArray());
    }
    static void redeem(SJSCaeScqbiua auto,String code,boolean custom,String reply) throws Exception {
        menu(auto);if(custom)customWindow();else input("Mã quà tặng",321);
        int sent=count("gift:");AutoPetLevel.intercept(auto);
        check(count("gift:")==sent+1 && TestTransport.giftCode.equals(code) && TestTransport.a==(custom?-1:321),"gift must use observed native protocol/token in requested order: "+code);
        AutoPetLevel.intercept(auto);AutoPetLevel.intercept(auto);
        check(count("gift:")==sent+1,"pending gift must never be submitted twice");
        if(custom)customAck(true,code);else notice(reply);
        AutoPetLevel.intercept(auto);
        check(!FcnKZwMhE.GAWjBFnKuf,"acknowledged gift must close native reward window before next step");
    }
    static void settle(SJSCaeScqbiua auto) throws Exception {set("settleUntil",0);AutoPetLevel.intercept(auto);}
    static void used(SJSCaeScqbiua auto,int slot) {bag[slot]=null;AutoPetLevel.intercept(auto);}
    static wpiwfWAXuWze claim(int id,int slot) throws Exception {
        check(AutoPetRuntime.running() && TestTransport.last.equals("npc") && TestTransport.a==9,"claim must start at Tashino");
        rootAndShop();check(TestTransport.a==(id==1305?0:1),"claim must select matching item ID in native box");
        int claims=count("box:");AutoPetRuntime.tick();AutoPetRuntime.tick();check(count("box:")==claims,"pending claim must not be resent");
        wpiwfWAXuWze it=bag[slot]=item(id,3,slot);AutoPetRuntime.tick();
        check(TestTransport.last.equals("info") && TestTransport.a==3 && TestTransport.b==slot,"new claim must be re-read at actual bag slot");
        info(it);return it;
    }
    static void trainUseUpgrade(wpiwfWAXuWze it,int slot,int equipped) throws Exception {
        int uses=count("equip:");rootAndShop();confirm(false);refreshNow();
        check(TestTransport.a==3 && TestTransport.b==slot,"training must refresh actual bag slot");
        options(it,3);info(it);check(TestTransport.last.equals("equip") && TestTransport.a==slot && count("equip:")==uses+1,"matching stats must use correct arbitrary slot");
        useReply(slot,equipped);rootAndShop();
        if(it.EgoDKTjkxEUMu.QpKzrQoqbjfLc==1305)confirm(true);
        else {AutoPetRuntime.sawConfirm("Nâng cấp: Long Châu Mika2\nXác nhận nâng cấp?",8890,Integer.valueOf(7),8891);AutoPetRuntime.tick();}
        refreshNow();it.yKHpKzQyZroJ=1;info(it);
        check(AutoPetRuntime.completed() && !AutoPetRuntime.running() && TestTransport.noticeTimeout==2000,"target stage must finish with two-second notice");
    }
    static void run() throws Exception {
        check(AutoPetLevel.codeResult("Nhận mã không thành công")==-1,"negative redemption must never count as success");
        int giftsBefore=AutoPetEvents.gifts;
        ByteArrayOutputStream badBytes=new ByteArrayOutputStream();DataOutputStream badOut=new DataOutputStream(badBytes);
        badOut.writeByte(1);badOut.writeByte(1);badOut.writeUTF("200mem");badOut.flush();
        WZKDTXfSh truncated=new WZKDTXfSh((byte)-33,badBytes.toByteArray());AutoPetRuntime.beforePacket(truncated);
        check(AutoPetAccess.input(truncated).available()==badBytes.size(),"gift observer must reset incomplete packet stream");AutoPetRuntime.afterPacket();
        check(AutoPetEvents.gifts==giftsBefore,"incomplete gift result must not publish an acknowledgement");
        SJSCaeScqbiua auto=fresh(22001,10,true);bag[8]=item(829,3,8);bag[14]=item(1399,3,14);
        map(1);auto.LAEAunqbxr();check(TestTransport.last.equals("travel") && TestTransport.a==22,"native Tan sat must route to Tone before leveling at level 10");
        map(22);field(oFpffzUyucjDJX.class,player,"wLHNXXbvUSit",int.class,Integer.valueOf(1000));
        check(AutoPetLevel.intercept(auto) && TestTransport.last.equals("walk"),"must approach Okanechan");
        field(oFpffzUyucjDJX.class,player,"wLHNXXbvUSit",int.class,Integer.valueOf(100));AutoPetLevel.intercept(auto);
        redeem(auto,"200mem",false,"Nhận quà thành công");AutoPetLevel.intercept(auto);
        redeem(auto,"CODETUAN4",true,null);settle(auto);
        check(TestTransport.last.equals("equip") && TestTransport.a==8,"use 829 after both codes, before Pet");
        int uses=count("equip:");AutoPetLevel.intercept(auto);check(count("equip:")==uses,"pending reward use must not repeat");
        used(auto,8);check(TestTransport.last.equals("equip") && TestTransport.a==14,"1399 follows acknowledged 829");
        used(auto,14);pet=claim(1305,7);trainUseUpgrade(pet,7,10);
        check(!AutoPetLevel.intercept(auto) && AutoPetAccess.currentAuto()==auto && body[11]==null,"Pet completion below 21 must immediately resume AutoLV without Mika");
        level(20);bag[13]=item(41,3,13);bag[13].WKutVuVwuGT=2;
        check(AutoPetLevel.intercept(auto) && TestTransport.last.equals("equip") && TestTransport.a==13,"level 20 must use ID 41");
        uses=count("equip:");bag[13].WKutVuVwuGT=1;
        check(!AutoPetLevel.intercept(auto),"one consumed unit acknowledges milestone use");AutoPetLevel.intercept(auto);
        check(count("equip:")==uses,"milestone must not consume remaining stack again");
        level(21);check(AutoPetLevel.intercept(auto) && AutoPetRuntime.running(),"21 must pause AutoLV and start Mika automatically");
        wpiwfWAXuWze mika=claim(1282,25);check(body[10]==pet,"Mika claim must not disturb worn Pet");trainUseUpgrade(mika,25,11);
        check(!AutoPetLevel.intercept(auto) && AutoPetAccess.currentAuto()==auto,"Mika completion must resume same AutoLV worker");
        level(30);bag[9]=item(44,3,9);AutoPetLevel.intercept(auto);check(TestTransport.last.equals("equip") && TestTransport.a==9,"level 30 must use ID 44");used(auto,9);
        level(49);bag[4]=item(48,3,4);uses=count("equip:");check(!AutoPetLevel.intercept(auto) && count("equip:")==uses,"ID 48 must wait until level 50");
        level(50);AutoPetLevel.intercept(auto);check(TestTransport.last.equals("equip") && TestTransport.a==4,"level 50 must use ID 48");used(auto,4);
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();SJSCaeScqbiua next=new SJSCaeScqbiua();AutoPetAccess.setAuto(next);bag[8]=item(829,3,8);
        int commands=TestTransport.log.size();check(!AutoPetLevel.intercept(next) && TestTransport.log.size()==commands,"relogin must preserve each acknowledged code/item/Pet stage per character");

        // Old characters with worn gear do not need Pet configuration. Gift prep still runs once.
        auto=fresh(22002,41,false);body[10]=item(1305,5,10);body[11]=item(1282,5,11);AutoPetLevel.intercept(auto);
        redeem(auto,"200mem",false,"Mã đã được sử dụng");AutoPetLevel.intercept(auto);redeem(auto,"CODETUAN4",false,"Bạn đã nhận quà từ mã này");
        settle(auto);check(!AutoPetLevel.intercept(auto) && AutoPetAccess.currentAuto()==auto,"worn Pet/Mika must bypass disabled settings after code prep");
        bag[23]=item(41,3,23);AutoPetLevel.intercept(auto);check(TestTransport.last.equals("equip") && TestTransport.a==23,"missing milestone item must remain eligible when it arrives later");used(auto,23);
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();AutoPetAccess.setAuto(auto);commands=TestTransport.log.size();
        check(!AutoPetLevel.intercept(auto) && TestTransport.log.size()==commands,"worn relog must not repeat prep");
        body[11]=null;bag[29]=item(1282,3,29);
        check(AutoPetLevel.intercept(auto) && AutoPetAccess.currentAuto()==null,"bag Mika cannot bypass disabled configuration as worn");
        body[11]=item(1305,5,11);AutoPetAccess.setAuto(auto);check(AutoPetLevel.intercept(auto) && AutoPetAccess.currentAuto()==null,"two Pet cannot substitute for Pet plus Mika");
        body[10].tTqVmuHPEhAbg=3;body[11]=item(1282,5,11);AutoPetAccess.setAuto(auto);
        check(AutoPetLevel.intercept(auto) && AutoPetAccess.currentAuto()==null,"incorrect source alias cannot count as worn");
        body[10].tTqVmuHPEhAbg=5;body[11]=null;wpiwfWAXuWze[] special=new wpiwfWAXuWze[3];special[2]=item(1282,53,2);
        field(oFpffzUyucjDJX.class,player,"BMWCZFcnBgzHkh",wpiwfWAXuWze[].class,special);AutoPetAccess.setAuto(auto);
        check(!AutoPetLevel.intercept(auto),"Mika worn in special equipment array must bypass settings");
        field(oFpffzUyucjDJX.class,player,"BMWCZFcnBgzHkh",wpiwfWAXuWze[].class,null);

        auto=fresh(22003,10,true);AutoPetLevel.intercept(auto);menu(auto);input("Mã quà tặng",456);AutoPetLevel.intercept(auto);
        int gifts=count("gift:");set("deadline",0);AutoPetLevel.intercept(auto);
        check(AutoPetAccess.currentAuto()==null && count("gift:")==gifts && TestTransport.notice.indexOf("Quá thời gian")>=0,"unacknowledged gift must stop without duplicate submission");
        check(AutoPetLevel.intercept(auto) && AutoPetAccess.currentAuto()==null,"stale worker must not restart canceled prep");
        auto=fresh(22004,10,true);AutoPetLevel.intercept(auto);menu(auto);input("Mật khẩu",789);gifts=count("gift:");AutoPetLevel.intercept(auto);
        check(AutoPetAccess.currentAuto()==null && count("gift:")==gifts,"unrelated input must not receive a gift code");
        auto=fresh(22005,10,true);AutoPetLevel.intercept(auto);menu(auto);customWindow();AutoPetLevel.intercept(auto);
        customAck(true,"SOME_OTHER_CODE");AutoPetLevel.intercept(auto);check(AutoPetAccess.currentAuto()==auto && !AutoPetRuntime.running(),"unrelated success code must not advance prep");
        customAck(false,"Mã đã hết hạn");AutoPetLevel.intercept(auto);check(AutoPetAccess.currentAuto()==null && !FcnKZwMhE.GAWjBFnKuf && TestTransport.notice.indexOf("hết hạn")>=0,"expired code must stop with real server error");
        auto=fresh(22006,10,true);map(1);AutoPetLevel.intercept(auto);set("deadline",0);AutoPetLevel.intercept(auto);
        check(AutoPetAccess.currentAuto()==null && TestTransport.notice.indexOf("NPC okanechan")>=0,"unreachable Tone must time out");

        auto=fresh(22007,10,true);AutoPetLevel.intercept(auto);redeem(auto,"200mem",false,"Mã đã sử dụng");AutoPetLevel.intercept(auto);
        redeem(auto,"CODETUAN4",false,"Mã đã sử dụng");settle(auto);check(AutoPetRuntime.running(),"Pet starts after gift preparation");
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();check(!AutoPetRuntime.running() && !AutoPetRuntime.completed(),"turning off Tan sat must cancel Pet without success");
        check(TestTransport.noticeTimeout==javax.microedition.lcdui.Alert.FOREVER,"manual cancellation keeps acknowledgement popup");
        AutoPetAccess.setAuto(auto);AutoPetLevel.intercept(auto);check(AutoPetRuntime.running(),"incomplete Pet resumes on explicit restart without repeating gift codes");
        AutoPetRuntime.stop();check(AutoPetLevel.intercept(auto) && AutoPetAccess.currentAuto()==null,"manual Pet stop must leave AutoLV stopped");
        // Receive cancellation, stale confirmations, full bag, and Mika level gate.
        map(22);level(21);reset(true,false,2);bag[0]=null;for(int i=0;i<bag.length;i++)bag[i]=item(41,3,i);
        commands=TestTransport.log.size();AutoPetRuntime.toggle();check(!AutoPetRuntime.running() && commands==TestTransport.log.size() && TestTransport.notice.indexOf("đầy")>=0,"full bag must reject claim before spending");
        reset(true,false,2);bag[0]=null;AutoPetRuntime.toggle();rootAndShop();int claims=count("box:");
        AutoPetRuntime.sawConfirm("Xác nhận nhận vật phẩm Pet Long?",8890,Integer.valueOf(5),8891);AutoPetRuntime.tick();check(TestTransport.last.equals("confirm") && TestTransport.a==5,"claim may confirm only fresh matching server prompt");
        java.lang.reflect.Field instance=AutoPetRuntime.class.getDeclaredField("INSTANCE");instance.setAccessible(true);field(AutoPetRuntime.class,instance.get(null),"deadline",long.class,Long.valueOf(0));AutoPetRuntime.tick();
        check(!AutoPetRuntime.running() && count("box:")==claims && TestTransport.notice.indexOf("nhận Pet Long")>=0,"claim timeout must not repeat action");
        reset(false,true,2);level(20);AutoPetRuntime.toggle();check(AutoPetRuntime.running() && TestTransport.last.equals("info") && TestTransport.b==1,"manual Auto keeps Mika's original level-20 use; Tan sat journey waits to 21");AutoPetRuntime.stop();
        reset(true,false,2);bag[0]=null;AutoPetRuntime.toggle();rootAndShop();AutoPetRuntime.stop();int confirms=TestTransport.confirms;
        AutoPetRuntime.sawConfirm("Xác nhận nhận vật phẩm Pet Long?",8890,Integer.valueOf(5),8891);AutoPetRuntime.tick();
        check(TestTransport.confirms==confirms && !AutoPetRuntime.running(),"manual stop must reject late claim confirmation");
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();level(21);
    }
}
