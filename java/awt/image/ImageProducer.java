/*******************************************************************
 * ImageProducer.java -- Java interface for image production
 *
 * Copyright (c) 1999 Free Software Foundation, Inc.
 * Written by C. Brian Jones <cbj@gnu.org>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later verion.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 ********************************************************************/

package java.awt.image;

/**
 * An object implementing the <code>ImageProducer</code> interface can
 * produce data for images.  Each image has a corresponding
 * <code>ImageProducer</code> which is needed for things such as
 * resizing the image.
 *
 * @see ImageConsumer
 * @author C. Brian Jones (cbj@gnu.org) 
 */
public interface ImageProducer
{
    /**
     * Used to register an <code>ImageConsumer</code> with this
     * <code>ImageProducer</code>.  
     */
    public abstract void addConsumer(ImageConsumer ic);

    /**
     * Used to determine if the given <code>ImageConsumer</code> is
     * already registered with this <code>ImageProducer</code>.  
     */
    public abstract boolean isConsumer(ImageConsumer ic);

    /**
     * Used to remove an <code>ImageConsumer</code> from the list of
     * registered consumers for this <code>ImageProducer</code>.  
     */
    public abstract void removeConsumer(ImageConsumer ic);

    /**
     * Used to register an <code>ImageConsumer</code> with this
     * <code>ImageProducer</code> and then immediately start
     * reconstruction of the image data to be delivered to all
     * registered consumers.  
     */
    public abstract void startProduction(ImageConsumer ic);

    /**
     * Used to register an <code>ImageConsumer</code> with this
     * <code>ImageProducer</code> and then request that this producer
     * resend the image data in the order top-down, left-right.  
     */
    public abstract void requestTopDownLeftRightResend(ImageConsumer ic);
}

