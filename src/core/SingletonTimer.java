package core;

public class SingletonTimer {
	private static SingletonTimer timer;
	private static boolean beenCalled = false;
	
	private SingletonTimer() {
		
	}
	
    public static SingletonTimer getInstance() {
    	if(timer == null) {
    		timer = new SingletonTimer();
    	}
    	return timer;
    }
    
    public void callTimer() {
    	if (beenCalled == false) {
        	beenCalled = true;
            ProgramTimer programTimer = new ProgramTimer();
            Thread t = new Thread(programTimer);
            t.start();
    	}

    }
    
    public static void setBeenCalled() {
    	beenCalled = false;
    }

}
