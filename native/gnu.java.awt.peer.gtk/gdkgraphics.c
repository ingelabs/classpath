#include "gtkpeer.h"
#include "GdkGraphics.h"

/* copy the native state of the peer (GtkWidget *) to the native state
   of the graphics object */
JNIEXPORT jintArray JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_initState
  (JNIEnv *env, jobject obj, jobject peer)
{
  struct graphics *g = (struct graphics *) malloc (sizeof (struct graphics));
  void *ptr;
  GtkWidget *widget;
  GdkColor color;
  jintArray array;
  jint *rgb;

  ptr = NSA_GET_PTR (env, peer);

  gdk_threads_enter ();

  widget = GTK_WIDGET (ptr);

  if (GTK_IS_WINDOW (widget))
    {
      g->drawable = find_gtk_layout (widget)->bin_window;
    }
  else
    {
      g->drawable = (GdkDrawable *) widget->window;
    }
  g->cm = gtk_widget_get_colormap (widget);
  g->gc = gdk_gc_new (g->drawable);
  gdk_gc_copy (g->gc, widget->style->fg_gc[GTK_STATE_NORMAL]);
  color = widget->style->fg[GTK_STATE_NORMAL];

  gdk_threads_leave ();

  array = (*env)->NewIntArray (env, 3);
  rgb = (*env)->GetIntArrayElements (env, array, NULL);
  rgb[0] = color.red >> 8;
  rgb[1] = color.green >> 8;
  rgb[2] = color.blue >> 8;
  (*env)->ReleaseIntArrayElements (env, array, rgb, 0);

  NSA_SET_PTR (env, obj, g);

  return array;
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_dispose
  (JNIEnv *env, jobject obj)
{
  struct graphics *g;

  g = (struct graphics *) NSA_DEL_PTR (env, obj);

  if (!g) return;		/* dispose has been called more than once */
  
  gdk_threads_enter ();
  gdk_gc_destroy (g->gc);
  gdk_threads_leave ();

  free (g);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_drawString
  (JNIEnv *env, jobject obj, jstring str, jint x, jint y, 
   jstring fname, jint size)
{
  struct graphics *g;
  const char *cfname, *cstr;
  gchar *xlfd;

  g = (struct graphics *) NSA_GET_PTR (env, obj);
  
  cfname = (*env)->GetStringUTFChars (env, fname, NULL);
  xlfd = g_strdup_printf (cfname, (size * 10));
  (*env)->ReleaseStringUTFChars (env, fname, cfname);

  cstr = (*env)->GetStringUTFChars (env, str, NULL);

  gdk_threads_enter ();
  gdk_draw_string (g->drawable, gdk_font_load (xlfd), g->gc, x, y, cstr);
  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, str, cstr);
  g_free (xlfd);
}


JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_drawLineNative
  (JNIEnv *env, jobject obj, jint x, jint y, jint x2, jint y2)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_line (g->drawable, g->gc, x, y, x2, y2);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_fillRectNative
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_rectangle (g->drawable, g->gc, TRUE, x, y, width, height);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_clearRectNative
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_window_clear_area ((GdkWindow *)g->drawable, x, y, width, height);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_setFunction
  (JNIEnv *env, jobject obj, jint func)
{
  struct graphics *g;
  g = (struct graphics *) NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
  gdk_gc_set_function (g->gc, func);
  gdk_threads_leave ();
}


JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_setFGColor
  (JNIEnv *env, jobject obj, jint red, jint green, jint blue)
{
  GdkColor color;
  struct graphics *g;

  color.red = red << 8;
  color.green = green << 8;
  color.blue = blue << 8;

  g = (struct graphics *) NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
  gdk_color_alloc (g->cm, &color);
  gdk_gc_set_foreground (g->gc, &color);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_drawArcNative
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height, 
   jint angle1, jint angle2)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_arc (g->drawable, g->gc, FALSE, x, y, width, height,
		angle1, angle2);
  gdk_threads_leave ();
}  
