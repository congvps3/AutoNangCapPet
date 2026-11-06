/** AutoLV zone policy. Keeps native zone transport and map selection. */
public final class AutoPetHunt {
    private static int map=-1,zone=-1;
    private static long noLeaderSince=-1,lastAttempt=-1,retryAt;
    private static boolean seenPopulation,pending;
    private static wCmqWHpCSBaGX leader;

    public static void reset() {
        map=zone=-1;noLeaderSince=-1;lastAttempt=-1;retryAt=0;seenPopulation=false;pending=false;leader=null;
    }
    private static boolean alive(wCmqWHpCSBaGX mob) {
        return mob!=null && mob.ubmVeVhBeAG>0 && mob.XfAmeXqfjJLK!=0 && mob.XfAmeXqfjJLK!=1
            && !(mob.RULfytWrmUzzRn==202 && mob.XfAmeXqfjJLK==8);
    }
    public static boolean allowed(wCmqWHpCSBaGX mob) {
        if(!(AutoPetAccess.currentAuto() instanceof SJSCaeScqbiua)) return true;
        if(!alive(mob)) return false;
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
            if(!alive(mob)) continue;
            living=true;
            if(mob.QgPHSngFRpo==2) leader=mob;
        }
        if(living) seenPopulation=true;
        if(!living && seenPopulation) return change(auto,now,currentZone);
        if(leader!=null) {noLeaderSince=-1;return true;}
        // The leaderless interval starts anew after the last leader dies.
        // Ordinary mobs remain attackable throughout that interval.
        if(noLeaderSince<0) noLeaderSince=now;
        if(now-noLeaderSince>=6000) return change(auto,now,currentZone);
        return living;
    }
}
