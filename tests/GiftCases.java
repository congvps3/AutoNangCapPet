/** Regression: silent gift redemption must not leave the journey on 200mem. */
public final class GiftCases extends JourneyCases {
    static void advanceNow(SJSCaeScqbiua auto) throws Exception {
        AutoPetLevel.intercept(auto);set("giftAdvanceAt",0);AutoPetLevel.intercept(auto);
    }
    static void serverText(int command,int npc,String text) throws Exception {
        java.io.ByteArrayOutputStream bytes=new java.io.ByteArrayOutputStream();java.io.DataOutputStream out=new java.io.DataOutputStream(bytes);
        if(command==38)out.writeShort(npc);if(command==53)out.writeUTF("Thông báo");out.writeUTF(text);out.flush();packet(command,bytes.toByteArray());
    }
    static void startCode(SJSCaeScqbiua auto,int token) throws Exception {
        AutoPetLevel.intercept(auto);menu(auto);input("Mã quà tặng",token);AutoPetLevel.intercept(auto);
    }
    static void run() throws Exception {
        SJSCaeScqbiua auto=fresh(23001,10,true);AutoPetLevel.intercept(auto);menu(auto);input("Mã quà tặng",601);
        AutoPetLevel.intercept(auto);check(TestTransport.giftCode.equals("200mem"),"first code must be 200mem");
        int first=count("gift:");Thread.sleep(2700);AutoPetLevel.intercept(auto);AutoPetLevel.intercept(auto);
        check(AutoPetAccess.currentAuto()==auto && TestTransport.last.equals("npc") && TestTransport.a==10,"silent server reply must advance to next code, not keep waiting on 200mem");
        check(count("gift:")==first,"silent code must not be submitted again");
        menu(auto);input("Mã quà tặng",602);AutoPetLevel.intercept(auto);
        check(TestTransport.giftCode.equals("CODETUAN4") && TestTransport.a==602,"second code must use its fresh input token");
        int submitted=count("gift:");advanceNow(auto);set("settleUntil",0);AutoPetLevel.intercept(auto);
        check(AutoPetRuntime.running() && TestTransport.last.equals("npc") && TestTransport.a==9,"both silent codes must finish prep and start Tashino");
        check(count("gift:")==submitted,"silent prep must send exactly two code requests");
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();body[10]=item(1305,5,10);body[11]=item(1282,5,11);
        AutoPetAccess.setAuto(auto);check(!AutoPetLevel.intercept(auto) && count("gift:")==submitted,"relogin must not repeat codes already sent without dialog acknowledgement");
        check((getFlags().intValue()&3)!=3,"silent TX must not be recorded as two confirmed successful redemptions");

        // A queued request is insufficient: only the exact native TX unlocks fallback.
        auto=fresh(23002,10,true);TestTransport.giftFlush=false;startCode(auto,701);submitted=count("gift:");
        set("giftAdvanceAt",0);AutoPetLevel.intercept(auto);check(TestTransport.last.equals("gift") && count("gift:")==submitted,"queued gift must wait and never replay");
        TestTransport.flushGift(702,"200mem");AutoPetLevel.intercept(auto);
        check(TestTransport.last.equals("gift"),"different token cannot acknowledge this code send");
        set("deadline",0);AutoPetLevel.intercept(auto);
        check(AutoPetAccess.currentAuto()==null && TestTransport.notice.indexOf("gửi mã 200mem")>=0 && count("gift:")==submitted,"failed or unflushed send must stop at send stage, not start code two");
        TestTransport.giftFlush=true;

        auto=fresh(23003,10,true);startCode(auto,801);bag[17]=item(829,3,17);AutoPetLevel.intercept(auto);AutoPetLevel.intercept(auto);
        check(TestTransport.last.equals("npc") && TestTransport.a==10,"new reward without success dialog must allow the next code immediately");
        menu(auto);input("Mã quà tặng",802);AutoPetLevel.intercept(auto);advanceNow(auto);set("settleUntil",0);AutoPetLevel.intercept(auto);
        check(TestTransport.last.equals("equip") && TestTransport.a==17,"after both codes use the actually present reward before Pet");
        int uses=count("equip:");set("giftAdvanceAt",0);AutoPetLevel.intercept(auto);
        check(count("equip:")==uses && TestTransport.last.equals("equip"),"code fallback must not bypass item consumption acknowledgement");
        set("deadline",0);AutoPetLevel.intercept(auto);check(AutoPetAccess.currentAuto()==null && count("equip:")==uses,"item timeout must still stop without replay");

        int[] channels={-24,-25,38,53};
        for(int i=0;i<channels.length;i++) {
            auto=fresh(23010+i,10,true);startCode(auto,901+i);
            serverText(channels[i],10,"Bạn đã nhập mã 200mem này rồi");
            for(int j=0;j<20;j++)serverText(-24,10,"+100 kinh nghiệm");
            AutoPetLevel.intercept(auto);AutoPetLevel.intercept(auto);
            check(TestTransport.last.equals("npc") && TestTransport.a==10,"NPC/system result must survive unrelated message flood: "+channels[i]);
        }
        auto=fresh(23020,10,true);startCode(auto,1001);
        serverText(38,9,"Bạn đã nhận quà từ mã 200mem");AutoPetLevel.intercept(auto);
        check(TestTransport.last.equals("gift"),"a different NPC must not finish code prep");
        serverText(-24,10,"Đã nhận CODETUAN4 thành công");AutoPetLevel.intercept(auto);
        check(TestTransport.last.equals("gift"),"text naming another code must not acknowledge 200mem");
        serverText(38,10,"Bạn đã nhận quà từ mã 200mem");AutoPetLevel.intercept(auto);AutoPetLevel.intercept(auto);
        check(TestTransport.last.equals("npc") && TestTransport.a==10,"matching NPC reply must advance 200mem");
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();
    }
    static Integer getFlags() throws Exception {
        java.lang.reflect.Field flags=AutoPetLevel.class.getDeclaredField("flags");flags.setAccessible(true);return (Integer)flags.get(null);
    }
}
