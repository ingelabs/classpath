#include "gtkpeer.h"
#include "GtkCanvasPeer.h"

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkCanvasPeer_gtkCanvasNew
  (JNIEnv *env, jobject obj, jint width, jint height, jboolean visible)
{
  GtkWidget *drawing_area;

  gdk_threads_enter ();
  drawing_area = gtk_drawing_area_new ();
  
  connect_awt_hook (env, obj, drawing_area, 1, &drawing_area->window);
  gtk_drawing_area_size (GTK_DRAWING_AREA (drawing_area), width, height);
  gtk_widget_set_events (drawing_area,
			 gtk_widget_get_events (drawing_area) 
			 | GDK_EXPOSURE_MASK);
  set_visible (drawing_area, visible);
  gdk_threads_leave ();
  
  NSA_SET_PTR (env, obj, drawing_area);
}

