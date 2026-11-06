/** AutoLV zone policy. Keeps native zone transport and map selection. */
public final class AutoPetHunt {
    private static int map=-1,zone=-1;
    private static long noLeaderSince=-1,lastAttempt=-1,retryAt;
    private static boolean seenPopulation,pending;
    private static wCmqWHpCSBaGX leader;
    private static volatile boolean realmLeaders;
    private static boolean preferenceLoaded;

    public static synchronized boolean realmLeaders() {
        if(!preferenceLoaded) {
            javax.microedition.rms.RecordStore rs=null;
            try {
                rs=javax.microedition.rms.RecordStore.openRecordStore("AutoPetLC.TL.v1",true);
                if(rs.getNumRecords()>0) {
                    byte[] data=rs.getRecord(1);
                    realmLeaders=data.length==2 && data[0]==1 && data[1]==1;
                }
            } catch(Exception ignored) {realmLeaders=false;}
            finally {if(rs!=null)try{rs.closeRecordStore();}catch(Exception ignored){}}
            preferenceLoaded=true;
        }
        return realmLeaders;
    }
    public static synchronized void toggleRealmLeaders() {
        boolean next=!realmLeaders();javax.microedition.rms.RecordStore rs=null;
        try {
            rs=javax.microedition.rms.RecordStore.openRecordStore("AutoPetLC.TL.v1",true);
            byte[] data={1,(byte)(next?1:0)};
            if(rs.getNumRecords()==0)rs.addRecord(data,0,data.length);else rs.setRecord(1,data,0,data.length);
            realmLeaders=next;
            if(realmAuto()) {noLeaderSince=-1;leader=null;AutoPetAccess.clearCombat();}
        } catch(Exception e) {throw new RuntimeException("Không lưu được nút TL");}
        finally {if(rs!=null)try{rs.closeRecordStore();}catch(Exception ignored){}}
    }

    public static void reset() {
        map=zone=-1;noLeaderSince=-1;lastAttempt=-1;retryAt=0;seenPopulation=false;pending=false;leader=null;
    }
    private static boolean realmAuto() {
        return AutoPetAccess.currentAuto() instanceof SJSCaeScqbiua && AutoPetMaps.realm(AutoPetAccess.map());
    }
    public static int scoreType(int type) {return realmAuto() && !realmLeaders()?0:type;}
    public static boolean leaderPreference(boolean enabled) {return enabled && (!realmAuto() || realmLeaders());}
    private static boolean alive(wCmqWHpCSBaGX mob) {
        return mob!=null && mob.ubmVeVhBeAG>0 && mob.XfAmeXqfjJLK!=0 && mob.XfAmeXqfjJLK!=1
            && !(mob.RULfytWrmUzzRn==202 && mob.XfAmeXqfjJLK==8);
    }
    private static boolean eligible(wCmqWHpCSBaGX mob) {
        if(!alive(mob)) return false;
        int target=AutoPetMaps.targetMobLevel(oFpffzUyucjDJX.rdmjdEJDQ().vMyUkiiUKxerr,AutoPetAccess.map());
        return target==0 || (target>0 && mob.aRzRUEjwca==target);
    }
    public static boolean allowed(wCmqWHpCSBaGX mob) {
        if(!(AutoPetAccess.currentAuto() instanceof SJSCaeScqbiua)) return true;
        if(!eligible(mob)) return false;
        if(realmAuto() && !realmLeaders()) return true;
        return !alive(leader) || mob.QgPHSngFRpo==2;
    }
    private static boolean change(SJSCaeScqbiua auto,long now,int currentZone) {
        if(AutoPetAccess.zoneCount()==1) return true; // no other zone exists
        if(now<retryAt) return false;
        AutoPetAccess.clearCombat();
        auto.EgoDKTjkxEUMu(currentZone);
        lastAttempt=now;retryAt=now+250;
        pending=auto.xNWwdhkGHYvrH>=0 && auto.xNWwdhkGHYvrH!=currentZone;
        return false;
    }
    public static boolean checkZone(SJSCaeScqbiua auto,long now,int trainingMap) {
        int currentMap=AutoPetAccess.map(),currentZone=SBnKDsbzS.GAWjBFnKuf;
        if(currentMap!=trainingMap) {reset();return true;}
        if(map!=currentMap || zone!=currentZone) {
            map=currentMap;zone=currentZone;noLeaderSince=now;
            seenPopulation=false;pending=false;retryAt=0;leader=null;
        }
        // Wait for acknowledgement instead of submitting another zone change
        // every update. This timeout does not delay entering the next new zone.
        if(pending) {
            if(now-lastAttempt>=8000) return change(auto,now,currentZone);
            return false;
        }
        kacyVKUNru mobs=AutoPetAccess.mobs();
        if(mobs==null) return false; // map data is not ready yet
        boolean living=false;leader=null;
        for(int i=0;i<mobs.size();i++) {
            wCmqWHpCSBaGX mob=(wCmqWHpCSBaGX)mobs.elementAt(i);
            if(!eligible(mob)) continue;
            living=true;
            if(mob.QgPHSngFRpo==2) leader=mob;
        }
        if(living) seenPopulation=true;
        if(!living && seenPopulation) return change(auto,now,currentZone);
        if(AutoPetMaps.realm(currentMap) && !realmLeaders()) {
            leader=null;
            if(living) {noLeaderSince=-1;return true;}
            // An initially empty/unpopulated zone can finish loading. This is
            // only an empty-zone grace, never a leader search while fighting.
            if(noLeaderSince<0) noLeaderSince=now;
            return now-noLeaderSince>=6000?change(auto,now,currentZone):false;
        }
        if(leader!=null) {noLeaderSince=-1;return true;}
        // The leaderless interval starts anew after the last leader dies.
        // Ordinary mobs remain attackable throughout that interval.
        if(noLeaderSince<0) noLeaderSince=now;
        if(now-noLeaderSince>=6000) return change(auto,now,currentZone);
        return living;
    }
}
