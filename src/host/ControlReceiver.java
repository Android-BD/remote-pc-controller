package host;

import utilities.*;

import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ControlReceiver implements Runnable {

	Socket socket;
	ObjectInputStream ois;
	Robot bot;
	Dimension screenSize;

	public ControlReceiver(Socket socket) throws Exception {
		this.socket = socket;
		this.ois = new ObjectInputStream(socket.getInputStream());
		this.bot = new Robot();
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	}

	@Override
	public void run() {
		try {
			while (true) {

				GenericEvent event = null;
				event = (GenericEvent) ois.readObject();

				if (event.getValue() == GenericEvent.MOUSE_PRESSED)
					mousePress((MouseEvent) event.getEvent(), event.getDim());
				if (event.getValue() == GenericEvent.MOUSE_RELEASED)
					mouseRelease((MouseEvent) event.getEvent(), event.getDim());
				if (event.getValue() == GenericEvent.MOUSE_CLICKED)
					mouseClick((MouseEvent) event.getEvent(), event.getDim());
				if (event.getValue() == GenericEvent.MOUSE_MOVED)
					mouseMove((MouseEvent) event.getEvent(), event.getDim());
				if (event.getValue() == GenericEvent.MOUSE_DRAGGED)
					mouseMove((MouseEvent) event.getEvent(), event.getDim());
				if (event.getValue() == GenericEvent.MOUSE_WHEEL)
					mouseScroll((MouseWheelEvent) event.getEvent(), event.getDim());
				if (event.getValue() == GenericEvent.KEY_PRESSED)
					keyPress(event.getKeyValue());
				if (event.getValue() == GenericEvent.KEY_RELEASED)
					keyRelease(event.getKeyValue());
			}
		} catch (Exception e) {
			System.out.println("Error in receiving event: " + e);
		}
	}

	private void mousePress(MouseEvent event, Dimension dim) {
		double x = (event.getX()*screenSize.getWidth())/dim.getWidth();
		double y = (event.getY()*screenSize.getHeight())/dim.getHeight();
		bot.mouseMove((int) x, (int) y);
		int mask = getMask(event);
		bot.mousePress(mask);
	}

	private void mouseRelease(MouseEvent event, Dimension dim) {
		double x = (event.getX()*screenSize.getWidth())/dim.getWidth();
		double y = (event.getY()*screenSize.getHeight())/dim.getHeight();
		bot.mouseMove((int) x, (int) y);
		int mask = getMask(event);
		bot.mouseRelease(mask);
	}

	private void mouseMove(MouseEvent event, Dimension dim) {
		double x = (event.getX()*screenSize.getWidth())/dim.getWidth();
		double y = (event.getY()*screenSize.getHeight())/dim.getHeight();
		bot.mouseMove((int) x, (int) y);
	}
	
	private void mouseClick(MouseEvent event, Dimension dim) {
		// Do nothing
	}
	
	private void mouseScroll(MouseWheelEvent event, Dimension dim) {
		bot.mouseWheel(event.getWheelRotation());
	}

	private void keyPress(int keycode) {
		System.out.println(keycode);
		bot.keyPress(keycode);
	}

	private void keyRelease(int keycode) {
		System.out.println(keycode);
		bot.keyRelease(keycode);
	}

	private int getMask(MouseEvent event) {
		int mask = 0;
		if (event.getButton() == MouseEvent.BUTTON1)
			mask = MouseEvent.getMaskForButton(MouseEvent.BUTTON1);
		if (event.getButton() == MouseEvent.BUTTON2)
			mask = MouseEvent.getMaskForButton(MouseEvent.BUTTON2);
		if (event.getButton() == MouseEvent.BUTTON3)
			mask = MouseEvent.getMaskForButton(MouseEvent.BUTTON3);
		return mask;
	}
}