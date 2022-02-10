package org.minueto.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.LinkedList;

import org.minueto.sample.fireinthesky.FireInTheSky;
import org.minueto.sample.image.AlphaImageDemo;
import org.minueto.sample.image.CircleDemo;
import org.minueto.sample.image.DrawInImageDemo;
import org.minueto.sample.image.GetSetPixelDemo;
import org.minueto.sample.image.ImageDemo;
import org.minueto.sample.image.LoadingFileDemo;
import org.minueto.sample.image.RectangleDemo;
import org.minueto.sample.image.text.FramerateDemo;
import org.minueto.sample.image.text.TextDemo;
import org.minueto.sample.image.text.TextDemo2;
import org.minueto.sample.image.transformation.CropDemo;
import org.minueto.sample.image.transformation.HighlightDemo;
import org.minueto.sample.image.transformation.RotateDemo;
import org.minueto.sample.image.transformation.RotateDemo2;
import org.minueto.sample.image.transformation.RotateDemo3;
import org.minueto.sample.image.transformation.ScaleDemo;
import org.minueto.sample.image.transformation.ScaleFlipDemo;
import org.minueto.sample.input.HandlerDemo;
import org.minueto.sample.input.HandlerDemo2;
import org.minueto.sample.input.HandlerDemo3;
import org.minueto.sample.input.TriangleRover;
import org.minueto.sample.input.TriangleRover2;
import org.minueto.sample.window.LineBenchmark;
import org.minueto.sample.window.MultiWindowDemo;
import org.minueto.sample.window.ResolutionChangeDemo;
import org.minueto.sample.window.ScreenshotDemo;

public class Demos {

	public Hashtable<String, Class> demos;
	public LinkedList<String> demoList;

	public Demos() {

		this.demos = new Hashtable<String, Class>();
		this.demoList = new LinkedList<String>();

		// Basic Demo
		this.addDemo("HelloWorld Demo", HelloWorld.class);
		this.addDemo("Line Demo", LineDemo.class);

		// Image Demo
		this.addDemo("Alpha Image Demo", AlphaImageDemo.class);
		this.addDemo("Circle Image Demo", CircleDemo.class);
		this.addDemo("Draw inside Image Demo", DrawInImageDemo.class);
		this.addDemo("Get Set Pxel Image Demo", GetSetPixelDemo.class);
		this.addDemo("Basic Image Demo", ImageDemo.class);
		this.addDemo("Loading Image File Demo", LoadingFileDemo.class);
		this.addDemo("Rectangle Image Demo", RectangleDemo.class);

		// Image Text
		this.addDemo("Text 1 Image Demo", TextDemo.class);
		this.addDemo("Text 2 Image Demo", TextDemo2.class);
		this.addDemo("Framerate Image Demo", FramerateDemo.class);

		// Image Transformation
		this.addDemo("Crop Image Demo", CropDemo.class);
		this.addDemo("Highlight Demo", HighlightDemo.class);
		this.addDemo("Rotate Image Demo", RotateDemo.class);
		this.addDemo("Rotate 2 Image Demo", RotateDemo2.class);
		this.addDemo("Rotate 3 Image Demo", RotateDemo3.class);
		this.addDemo("Scale Image Demo", ScaleDemo.class);
		this.addDemo("Scale Flip Image Demo", ScaleFlipDemo.class);

		// Windows
		this.addDemo("Line Benchmark Demo", LineBenchmark.class);
		this.addDemo("Multi Window Demo", MultiWindowDemo.class);
		this.addDemo("Resolution Change Demo", ResolutionChangeDemo.class);
		this.addDemo("Screenshot Demo", ScreenshotDemo.class);

		// Input
		this.addDemo("Handler Demo", HandlerDemo.class);
		this.addDemo("Handler 2 Demo", HandlerDemo2.class);
		this.addDemo("Handler 3 Demo", HandlerDemo3.class);
		this.addDemo("Triangle Rover Demo", TriangleRover.class);
		this.addDemo("Triangle Rover 2 Demo", TriangleRover2.class);

		//Big demos
		this.addDemo("Fire in the Sky", FireInTheSky.class);
	}
	
	private void addDemo(String name, Class demoClass) {
		
		this.demoList.add(name);
		this.demos.put(name, demoClass);
	}

	private void printList() {

		int i = 0;

		for(String key: this.demoList) {

			i++;

			System.out.println( i + ") " + key);

		}
	}

	private void printMenu() {

		System.out.println("Welcome to the Minueto Demo");
		System.out.println("---------------------------");

		System.out.println("## : Enter demo number to start.");
		System.out.println("p : Print list of demos.");
		System.out.println("q : Quit.");
		System.out.println("");
	}

	private String whichDemo(int i) {

		if (i < 1) return null;
		
		int j = 0;

		for(String key: this.demoList) {

			j++;

			if (i == j) return key;

		}

		return null;
	}
	
	private int getNumber(String s) {
		
		try {
			return Integer.parseInt(s);
		}
		catch (NumberFormatException nfe) {
			return -1;
		}
	}
	
	private void runDemo(String demoKey) {
		
		Class demo = this.demos.get(demoKey);
		Method mainMethod = null;
		
		for(Method method: demo.getMethods()) {
			if(method.getName().equals("main")) {
				mainMethod = method;
				break;
			}
		}
		
		System.out.println("Running " + demo.getName());
		
		if(mainMethod != null) {
		
			String[] params = new String[1];
			
			try {
				mainMethod.invoke(null, params);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			System.out.println("Error running demo " + demoKey + " can't find main.");
		}
		
	}

	public void run() {

		boolean stop = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(!stop) {

			this.printMenu();
			System.out.print("command> ");
			
			String command = "";
			
			try {
				command = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(getNumber(command) != -1) {

				String demoKey = whichDemo(getNumber(command));
				
				if (demoKey == null) {
					System.out.println("Invalid demo number " + command);
				} else {
					runDemo(demoKey);
					stop = true;	
				}				
				
			} else if ("p".equals(command)) {
				printList();
			} else if ("q".equals(command)) {			
				System.exit(-1);
			} else {
				System.out.println("Unknown command " + command);
			}			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Demos demos = new Demos();
		demos.run();

	}

}
