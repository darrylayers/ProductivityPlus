package core;

import gui.ExploreDataGui;

public class UpdateBar implements Runnable {

    @Override
    public void run() {
        updateBar();
    }

    public void updateBar() {

        for (int i = 0; i < 10; i++) {
            ExploreDataGui.progressBar.setValue(i);
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ExploreDataGui.progressBar.setValue(10);

    }

}
