package org.minueto;


/**
 * The <code>MinuetoOptions</code> allows a programmer to activate 
 * experimental features in Minueto. For these features to work,
 * they should be activate early when the Java application start,
 * especially before any other calls to Minueto are made. In addition,
 * once these option are set, they should not be changed.
 * 
 * All the features found here are Expiremental. Be careful when using
 * them.
 * 
 * @author Alexandre Denault
 * @since 1.0
 */

public class MinuetoOptions {

	private static boolean transparency = false;

	/**
	 * Allows a programmer to enable alpha transparencies.
	 * Please note that this will dramaticly slow down
	 * the application unless the JVM uses some kind
	 * of acceleration.
	 * 
	 * @param value <code>boolean</code> denoting if alpha
	 *    transparencies should be turned on.
	 */
	public static void enableAlpha(boolean value) {
		
		transparency = value;		
	}
	
	/**
	 * Returns true if alpha transparencies are enabled.
	 * 
	 * @return <code>boolean</code> indicating if alpha 
	 *    transparencies are activated.
	 */
	public static boolean isAlphaEnabled() {
				
		return transparency;					
	}
	
	/**
	 * Enables OpenGL acceleration inside Minueto. If
	 * you are using Linux or Mac and have video drivers
	 * properly installed, this is a good option for a 
	 * speed boost.
	 * <p>
	 * To function, this should be one of the first call
	 * in your application.
	 * <p>
	 * Never enable OpenGL and Direct3D acceleration at
	 * the same time.
	 */
	public static void enableOpenGLAcceleration() {
		
	    System.setProperty("sun.java2d.opengl", "true");
	    System.setProperty("sun.java2d.accthreshold", "0");
	    System.setProperty("sun.java2d.translaccel", "true"); 
	}
	
	/**
	 * Enables DirectX/Direct3D acceleration inside Minueto.
	 * If you are using Windows and have video drivers
	 * properly installed, this is a good option for a 
	 * speed boost.
	 * <p>
	 * To function, this should be one of the first call
	 * in your application.
	 * <p>
	 * Never enable OpenGL and Direct3D acceleration at
	 * the same time.
	 */
	public static void enableDirectXAcceleration() {
		
	    System.setProperty("sun.java2d.d3d", "true");
	    System.setProperty("sun.java2d.d3dtexbpp", "32");
	    System.setProperty("sun.java2d.ddscale", "true");
	    System.setProperty("sun.java2d.accthreshold", "0");
	    System.setProperty("sun.java2d.translaccel", "true"); 
	}
	
}
