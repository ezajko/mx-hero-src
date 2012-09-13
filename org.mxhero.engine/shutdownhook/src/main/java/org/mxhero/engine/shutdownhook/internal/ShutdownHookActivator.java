/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.shutdownhook.internal;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * Extension of the default OSGi bundle activator
 */
public final class ShutdownHookActivator
    implements BundleActivator
{
	
	private Thread shutdownHandler = null;
	
    /**
     * Called whenever the OSGi framework starts our bundle
     */
    public void start( BundleContext bc )
        throws Exception
    {
        System.out.println( "STARTING org.mxhero.shutdownhook" );
        shutdownHandler = new Thread(new ShutdownHandler(bc));
        Runtime.getRuntime().addShutdownHook(shutdownHandler);
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    public void stop( BundleContext bc )
        throws Exception
    {
        System.out.println( "STOPPING org.mxhero.shutdownhook" );
        if(shutdownHandler!=null){
        	try{
        		Runtime.getRuntime().removeShutdownHook(shutdownHandler);
        	}
        	catch(IllegalStateException e){
        		//this is ok, since ShutdownHandler will make this bundle to stop
        	}
        }
    }
    
    
    private static class ShutdownHandler implements Runnable{
    	
    	private BundleContext bc = null;
    	
    	public ShutdownHandler(BundleContext bc){
    		this.bc = bc;
    	}
    	
		public void run() {
			if(bc!=null){
				try {
					Bundle systemBundle = bc.getBundle(0);
					if(systemBundle!=null && systemBundle.getState()!=Bundle.RESOLVED){
						systemBundle.stop();
					}

					while(systemBundle.getState()!=Bundle.RESOLVED){
						Thread.sleep(100);
					}
					
					System.out.println(ShutdownHookActivator.class.getName()+" shutting down finished");
					Runtime.getRuntime().halt(0);
				} catch (Exception e) {
					System.out.println(ShutdownHookActivator.class.getName()+" error while shutting down");
				}
			}
		}
    
    }
}

