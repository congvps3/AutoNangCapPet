import autouplv.MapSelector;

/** User supplied map levels. No server levels or unlock flags are modified. */
public final class AutoPetMaps {
    public static final int[][] WORLD={{19,71},{36,74},{36,77},{54,80},{37,84},{37,88},{55,92},{58,96},{58,100}};
    public static final int[][] REALM={{139,64,68},{140,74,77},{141,83,88},{142,92,96},{143,103,107},
        {144,114,117},{145,123,126},{146,131,137},{147,142,148},{148,153,159}};
    public static boolean realm(int map) {return map>=139 && map<=148;}
    public static int[][] extend(int[][] original) {
        int[][] all=new int[original.length+WORLD.length+REALM.length*2][];int n=0;
        for(int i=0;i<original.length;i++) all[n++]=original[i];
        for(int i=0;i<WORLD.length;i++) all[n++]=WORLD[i];
        for(int i=0;i<REALM.length;i++) {
            all[n++]=new int[]{REALM[i][0],REALM[i][1]};all[n++]=new int[]{REALM[i][0],REALM[i][2]};
        }
        return all;
    }
    public static int realmMap(int level) {
        if(level<60) return -1;
        for(int i=0;i<REALM.length;i++) if(level<REALM[i][1]+2) return REALM[i][0];
        return 148; // terminal map: keep leveling here without a level cap
    }
    public static boolean canContinue(int level,int monsterLevel,int map) {
        if(level>=60) return realm(map) && map==realmMap(level);
        return !realm(map) && MapSelector.canContinue(level,monsterLevel);
    }
    public static int selectExcluding(int level,int preferred,int excluded,int[][] maps) {
        if(level>=60) return realmMap(level);
        int count=0;
        for(int i=0;i<maps.length;i++) if(!realm(maps[i][0])) count++;
        int[][] outside=new int[count][];count=0;
        for(int i=0;i<maps.length;i++) if(!realm(maps[i][0])) outside[count++]=maps[i];
        return MapSelector.selectExcluding(level,preferred,excluded,outside);
    }
    public static int targetLevel(int level,int map,int[][] maps) {
        if(realm(map)) return level>=60?REALM[map-139][1]:-1;
        return MapSelector.targetLevel(level,map,maps);
    }
    /** -1=locked, 0=all levels, positive=the only allowed monster level. */
    public static int targetMobLevel(int level,int map) {
        if(!realm(map)) return 0;
        if(level<60) return -1;
        int[] row=REALM[map-139];
        if(row[2]-level<=9) return 0;
        return Math.abs(level-row[1])<=Math.abs(level-row[2])?row[1]:row[2];
    }
}
