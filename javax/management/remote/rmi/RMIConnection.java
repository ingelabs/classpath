/* RMIConnection.java -- RMI object representing a MBean server connection.
   Copyright (C) 2007 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

package javax.management.remote.rmi;

import java.io.Closeable;
import java.io.IOException;

import java.rmi.MarshalledObject;
import java.rmi.Remote;

import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;

import javax.security.auth.Subject;

/**
 * <p>
 * RMI interface for forwarding requests to a remote
 * {@link javax.management.MBeanServer}.  This interface
 * parallels the {@link javax.management.MBeanServerConnection}
 * interface, providing a way of invoking those methods using
 * the RMI protocol.  When a client wishes to call a method
 * of an MBean server using RMI, the method is called on the stub
 * on the client side, which serializes the object parameters
 * and sends them to the server where they are deserialized and
 * an implementation of this interface forwards them to the
 * appropriate MBean server.  Return values follow the same
 * process, only in reverse.  Each client obtains its own
 * implementation of this interface from an {@link RMIServer}
 * instance.
 * </p>
 * <p>
 * Implementations of this interface do more than simply
 * forward requests directly to the server.  The arguments
 * of the server methods are wrapped in {@link MarshalledObject}
 * instances, so that the correct classloader can be used to
 * deserialize the arguments.  When a method is called, the
 * implementation must first retrieve the appropriate classloader
 * and then use it to deserialize the marshalled object.  Unless
 * explicitly specified in the documentation for the method,
 * a parameter of the type {@link MarshalledObject} or an array
 * of that type should not be {@code null}.
 * </p>
 * <p>
 * Security is also handled by this interface, as the methods
 * use an additional {@link javax.security.auth.Subject} parameter
 * for role delegation.
 * </p>
 *
 * @author Andrew John Hughes (gnu_andrew@member.fsf.org)
 * @since 1.5
 */
public interface RMIConnection
  extends Closeable, Remote
{

  /**
   * Handles {@link
   * MBeanServerConnection#addNotificationListener(ObjectName,
   * ObjectName, NotificationFilter, Object)} by
   * registering the supplied listener with the specified management
   * bean.  Notifications emitted by the management bean are forwarded
   * to the listener via the server, which will convert any MBean
   * references in the source to portable {@link ObjectName}
   * instances.  The notification is otherwise unchanged.  The filter
   * and handback object are wrapped in a {@link MarshalledObject}
   * so that they are deserialised using the bean's classloader.
   *
   * @param name the name of the management bean with which the listener
   *             should be registered.
   * @param listener the listener which will handle notifications from
   *                 the bean.
   * @param filter a wrapper containing a filter to apply to incoming
   *               notifications, or <code>null</code> if no filtering
   *               should be applied.
   * @param passback a wrapper containing an object to be passed to the
   *                 listener when a notification is emitted.
   * @param delegationSubject a {@link javax.security.auth.Subject} instance
   *                          containing the delegation principles or
   *                          {@code null} if authentication is used.
   * @throws InstanceNotFoundException if the name of the management bean
   *                                   could not be resolved.
   * @throws RuntimeOperationsException if the bean associated with the given
   *                                    object name is not a
   *                                    {@link NotificationListener}.  This
   *                                    exception wraps an
   *                                    {@link IllegalArgumentException}.
   * @throws SecurityException if the client or delegated subject (if any)
   *                           does not have permission to invoke this operation.
   * @throws IOException if an I/O error occurred in communicating with
   *                     the bean server.
   * @see #removeNotificationListener(ObjectName, ObjectName,
   *                                  javax.security.auth.Subject)
   * @see #removeNotificationListener(ObjectName, ObjectName,
   *                                  java.rmi.MarshalledObject,
   *                                  java.rmi.MarshalledObject,
   *                                  javax.security.auth.Subject)
   * @see #removeNotificationListeners(ObjectName, Integer[],
   *                                  javax.security.auth.Subject)
   * @see NotificationBroadcaster#addNotificationListener(NotificationListener,
   *                                                      NotificationFilter,
   *                                                      Object)
   */
  void addNotificationListener(ObjectName name, ObjectName listener,
			       MarshalledObject filter, MarshalledObject passback,
			       Subject delegationSubject)
    throws InstanceNotFoundException, IOException;

  /**
   * Handles {@link
   * MBeanServerConnection#addNotificationListener(ObjectName,
   * NotificationListener, NotificationFilter, Object)} by
   * registering for notifications from the specified management
   * beans.  The array of filters is assumed to be aligned with
   * the array of bean names, so that the notifications from each
   * bean are matched against the appropriate filter (or left as
   * is if the filter is {@code null}.  Notifications emitted by
   * the management beans are forwarded to a local listener created
   * by this method, via the server, which converts any MBean
   * references in the source to portable {@link ObjectName}
   * instances.  The notification is otherwise unchanged.
   * </p>
   * <p>
   * This local listener buffers the notifications for retrieval by
   * {@link #fetchNotifications(long,int,long).  This method returns
   * an array of listener identifiers which aligns with the supplied
   * array of beans so that the appropriate listener can be identified
   * by the client, which retains its own listener and handback object.
   * The filters are wrapped in {@link MarshalledObject}s so that they are
   * deserialised using the bean's classloader.
   * </p>
   *
   * @param names the names of the management bean whose notifications
   *              should be recorded.
   * @param filters an array of wrappers containing filters to apply to
   *                incoming notifications.  An element may be <code>null</code>
   *                if no filtering should be applied to a bean's notifications.
   * @param delegationSubjects an array of {@link javax.security.auth.Subject}
   *                          instances containing the delegation principles for
   *                          each listener.  An element may be {@code null} if
   *                          authentication is used instead, or the entire
   *                          argument itself may be {@code null}.  In the latter
   *                          case, this is treated as an array of {@code null}
   *                          values.
   * @return an array of integers which act as listener identifiers, so that
   *         notifications retrieved from {@link #fetchNotifications(long,int,long)
   *         can be matched to the beans they were emitted from.  The array is
   *         aligned against the array of beans supplied to this methods, so that
   *         the identifier in position 0 represents the bean in position 0 of the
   *         input array.
   * @throws IllegalArgumentException if the {@code names} or {@code filters} array
   *                                  is {@code null}, the {@code names} array contains
   *                                  a {@code null} value or the three arrays are not
   *                                  of the same size.
   * @throws ClassCastException if an element of the {@code filters} array unmarshalls
   *                            as a non-null object that is not a {@link NotificationFilter}.
   * @throws InstanceNotFoundException if the name of one of the management beans
   *                                   could not be resolved.
   * @throws SecurityException if, for one of the beans, the client or delegated subject
   *                           (if any) does not have permission to invoke this operation.
   * @throws IOException if an I/O error occurred in communicating with
   *                     the bean server.
   * @see #removeNotificationListener(ObjectName, ObjectName,
   *                                  javax.security.auth.Subject)
   * @see #removeNotificationListener(ObjectName, ObjectName,
   *                                  java.rmi.MarshalledObject,
   *                                  java.rmi.MarshalledObject,
   *                                  javax.security.auth.Subject)
   * @see #removeNotificationListeners(ObjectName, Integer[],
   *                                  javax.security.auth.Subject)
   * @see NotificationBroadcaster#addNotificationListener(NotificationListener,
   *                                                      NotificationFilter,
   *                                                      Object)
   */
  void addNotificationListeners(ObjectName[] names, MarshalledObject[] filters,
				Subject[] delegationSubjects)
    throws InstanceNotFoundException, IOException;
}
