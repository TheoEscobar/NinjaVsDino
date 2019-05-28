package principal;

public class chrono {
	private long tempsDepart=0;
    private long tempsFin=0;
    private long pauseDepart=0;
    private long pauseFin=0;
    private long duree=0;

    public void start()
        {
        tempsDepart=System.currentTimeMillis();
        tempsFin=0;
        pauseDepart=0;
        pauseFin=0;
        duree=0;
        }

    public void stop()
        {
        if(tempsDepart==0) {return;}
        tempsFin=System.currentTimeMillis();
        duree=(tempsFin-tempsDepart) - (pauseFin-pauseDepart);
        tempsDepart=0;
        tempsFin=0;
        pauseDepart=0;
        pauseFin=0;
        }        

    public long getDureeSec()
        {
    	duree=(System.currentTimeMillis()-tempsDepart);
        return duree/1000;
        }
        
    public long getDureeMs()
        {
    	duree=(System.currentTimeMillis()-tempsDepart);
        return duree;
        }        

    public String getDureeTxt()
        {
    	duree=(System.currentTimeMillis()-tempsDepart);
        return timeToHMS(getDureeSec());
        }

    public static String timeToHMS(long tempsS) {

        int h = (int) (tempsS / 3600);
        int m = (int) ((tempsS % 3600) / 60);
        int s = (int) (tempsS % 60);

        String r="";

        if(h>0) {r+=h+":";}
        else {r+=0+":";}
        if(m>0) {r+=m+":";}
        else {r+=0+":";}
        if(s>0) {r+=s+"";}
        else {r+=0+"";}
        if(h<=0 && m<=0 && s<=0) {r="0:0:0";}

        return r;
        }

}
