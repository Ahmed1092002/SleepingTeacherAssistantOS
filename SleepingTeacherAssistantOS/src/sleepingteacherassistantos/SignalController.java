
package sleepingteacherassistantos;

/**
 *
 * @author Zeko
 */
public class SignalController {
    private boolean signal = false;

    // Used to send the signal.
    public synchronized void sendSignal() {
        this.signal = true;
        this.notify();
    }

    // Will wait until it receives a signal before continuing.
    public synchronized void waitForSignal() throws InterruptedException{
        while(!this.signal) wait();
        this.signal = false;
    }
}
