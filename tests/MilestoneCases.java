/** Exercise level rewards from the canvas watcher without calling the combat update. */
public final class MilestoneCases extends JourneyCases {
    static void run() throws Exception {
        SJSCaeScqbiua auto=fresh(24001,19,false);progress(24001,3|4096|8192|64);
        body[10]=item(1305,5,10);body[11]=item(1282,5,11);
        check(!AutoPetLevel.intercept(auto),"prepared character must resume leveling");
        bag[2]=item(41,3,2);bag[24]=item(42,3,24);int uses=count("equip:");
        AutoPetLevel.watch();check(count("equip:")==uses,"ID 42 must wait to 20");
        level(20);AutoPetLevel.watch();
        check(TestTransport.last.equals("equip") && TestTransport.a==24 && count("equip:")==uses+1,"level watcher must use 42 despite old ID 41 progress marker");
        AutoPetLevel.watch();AutoPetLevel.watch();check(count("equip:")==uses+1 && bag[2]!=null,"pending use must neither repeat nor use ID 41");
        packet(10,new byte[]{24});AutoPetLevel.watch();check(bag[24]==null,"native item removal must acknowledge milestone");
        level(30);AutoPetLevel.watch();uses=count("equip:");bag[9]=item(44,3,9);AutoPetLevel.watch();
        check(TestTransport.last.equals("equip") && TestTransport.a==9 && count("equip:")==uses+1,"late ID 44 arrival must be used at 30 without another combat update");
        packet(10,new byte[]{9});AutoPetLevel.watch();
        bag[27]=item(48,3,27);level(49);uses=count("equip:");AutoPetLevel.watch();check(count("equip:")==uses,"ID 48 must wait to 50");
        level(50);AutoPetLevel.watch();check(TestTransport.last.equals("equip") && TestTransport.a==27,"watcher must use ID 48 at 50");
        packet(10,new byte[]{27});AutoPetLevel.watch();
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();auto=new SJSCaeScqbiua();AutoPetAccess.setAuto(auto);
        bag[17]=item(42,3,17);bag[18]=item(44,3,18);bag[19]=item(48,3,19);uses=count("equip:");
        check(!AutoPetLevel.intercept(auto),"completed milestones survive relog");AutoPetLevel.watch();
        check(count("equip:")==uses,"confirmed rewards must not be consumed twice after relog");
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();
        auto=fresh(24002,9,false);progress(24002,3|4096|8192);body[10]=item(1305,5,10);body[11]=item(1282,5,11);
        AutoPetLevel.intercept(auto);bag[3]=item(40,3,3);bag[4]=item(58,3,4);uses=count("equip:");AutoPetLevel.watch();
        check(count("equip:")==uses,"neither level-10 variant may be used at 9");
        level(10);AutoPetLevel.watch();check(TestTransport.last.equals("equip") && TestTransport.a==3,"primary 40 has priority when both variants exist");
        packet(10,new byte[]{3});AutoPetLevel.watch();uses=count("equip:");AutoPetLevel.watch();
        check(count("equip:")==uses && bag[4]!=null,"58 must stay unused after consuming 40 for this character");
        int[] levels={20,30,50},alternates={60,62,68},primaries={42,44,48};
        for(int i=0;i<levels.length;i++) {
            level(levels[i]);bag[20+i]=item(alternates[i],3,20+i);AutoPetLevel.watch();
            check(TestTransport.last.equals("equip") && TestTransport.a==20+i,"must accept alternate item "+alternates[i]+" at "+levels[i]);
            packet(10,new byte[]{(byte)(20+i)});AutoPetLevel.watch();uses=count("equip:");bag[10+i]=item(primaries[i],3,10+i);AutoPetLevel.watch();
            check(count("equip:")==uses,"primary must not be used again after alternate milestone was consumed");
        }
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();auto=new SJSCaeScqbiua();AutoPetAccess.setAuto(auto);uses=count("equip:");
        check(!AutoPetLevel.intercept(auto),"alternate milestones survive relog");AutoPetLevel.watch();check(count("equip:")==uses,"relog must not consume the other variant");
        auto=fresh(24003,10,false);progress(24003,3|4096|8192);body[10]=item(1305,5,10);body[11]=item(1282,5,11);bag[29]=item(58,3,29);
        AutoPetLevel.intercept(auto);check(TestTransport.last.equals("equip") && TestTransport.a==29,"level 10 may use 58 when 40 is absent");
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();
    }
}
