#include "gtkpeer.h"
#include "GdkGraphics.h"

struct graphics
{
  GdkDrawable *drawable;
  GdkGC *gc;
};

/* copy the native state of the peer (GtkWidget *) to the native state
   of the graphics object */
JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_initState
  (JNIEnv *env, jobject obj, jobject peer)
{
  struct graphics *g = (struct graphics *) malloc (sizeof (struct graphics));
  void *ptr;
  GtkWidget *widget;

  ptr = NSA_GET_PTR (env, peer);

  gdk_threads_enter ();

  widget = GTK_WIDGET (ptr);
  if (GTK_WIDGET_DRAWABLE (widget))
    g->drawable = (GdkDrawable *) widget->window;
  else
    g->drawable = NULL;

  g->gc = widget->style->fg_gc[GTK_STATE_NORMAL];

  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, g);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_drawLine
  (JNIEnv *env, jobject obj, jint x, jint y, jint x2, jint y2)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_line (g->drawable, g->gc, x, y, x2, y2);
  gdk_threads_leave ();
}

		 


