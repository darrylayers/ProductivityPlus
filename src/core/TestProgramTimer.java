package core;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

/**
 * TestProgramTimer.java is used to test the timer feature found in
 * ProgramTimer.java
 * 
 * @author Austin Ayers
 * @version 9/12/18
 */

public class TestProgramTimer {

    @SuppressWarnings("static-access")
    @Test
    /*
     * This method tests ProgramTimer.java by launching four dummy
     * JFrame windows for a random amount of seconds then tests to see
     * if the timer recorded the window being open for the proper amount of time.
     */
    public void testFourApps() throws InterruptedException {

        // Start the timer
        ProgramTimer programTimer = new ProgramTimer();
        Thread t = new Thread(programTimer);
        t.start();

        Random rand = new Random();
        int time = rand.nextInt(10000) + 1000;

        // Create dummy window 1
        String window_name = "Window1 " + time / 1000 + " seconds";
        CreateDummyWindow window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());

        programTimer.printMap();

        // Dummy window 2
        time = rand.nextInt(10000) + 1000;
        window_name = "Window2 " + time / 1000 + " seconds";
        window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());

        programTimer.printMap();

        // Dummy window 3
        time = rand.nextInt(10000) + 1000;
        window_name = "Window3 " + time / 1000 + " seconds";
        window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());

        programTimer.printMap();

        // Dummy window 4
        time = rand.nextInt(10000) + 1000;
        window_name = "Window4 " + time / 1000 + " seconds";
        window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());

        programTimer.printMap();

    }

    /*    @SuppressWarnings("static-access")
    @Test
    
     * This method tests ProgramTimer.java by launching four dummy
     * JFrame windows for 0 seconds then tests to see
     * if the timer recorded the window being open for the proper amount of time.
     
    public void testFourApps0Secs() throws InterruptedException {
    
        // Start the timer
        ProgramTimer programTimer = new ProgramTimer();
        Thread t = new Thread(programTimer);
        t.start();
    
        int time = 500;
    
        // Create dummy window 1
        String window_name = "Window1_0sec " + time / 1000 + " seconds";
        CreateDummyWindow window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());
    
        programTimer.printMap();
        // Dummy window 2
    
        window_name = "Window2_0sec " + time / 1000 + " seconds";
        window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());
    
        programTimer.printMap();
        // Dummy window 3
    
        window_name = "Window3_0sec " + time / 1000 + " seconds";
        window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());
    
        programTimer.printMap();
        // Dummy window 4
    
        window_name = "Window4_0sec " + time / 1000 + " seconds";
        window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());
    
        programTimer.printMap();
    
    }
    
    @SuppressWarnings("static-access")
    @Test
    
     * This method tests ProgramTimer.java by launching four dummy
     * JFrame windows for 1 second then tests to see
     * if the timer recorded the window being open for the proper amount of time.
     
    public void testFourApps1Sec() throws InterruptedException {
    
        // Start the timer
        ProgramTimer programTimer = new ProgramTimer();
        Thread t = new Thread(programTimer);
        t.start();
    
        int time = 1000;
    
        // Create dummy window 1
        String window_name = "Window1_1sec " + time / 1000 + " seconds";
        CreateDummyWindow window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());
    
        programTimer.printMap();
        // Dummy window 2
    
        window_name = "Window2_1sec " + time / 1000 + " seconds";
        window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());
    
        programTimer.printMap();
        // Dummy window 3
    
        window_name = "Window3_1sec " + time / 1000 + " seconds";
        window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());
    
        programTimer.printMap();
        // Dummy window 4
    
        window_name = "Window4_1sec " + time / 1000 + " seconds";
        window = new CreateDummyWindow(window_name);
        Thread.sleep(time);
        window.frame.dispose();
        Thread.sleep(100);
        assertEquals(time / 1000,
            programTimer.appMap.get(window_name).longValue());
    
        programTimer.printMap();
    
    }*/

}
