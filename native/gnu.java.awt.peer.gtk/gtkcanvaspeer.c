#include "gtkpeer.h"
#include "GtkCanvasPeer.h"

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkCanvasPeer_gtkCanvasNew
  (JNIEnv *env, jobject obj, jobject parent_obj, 
   jint width, jint height)
{
  GtkWidget *drawing_area;
  void *parent;

  parent = NSA_GET_PTR (env, parent_obj);

  gdk_threads_enter ();

  drawing_area = gtk_drawing_area_new ();
  set_parent (drawing_area, GTK_CONTAINER (parent));
  gtk_widget_realize (drawing_area);
  connect_awt_hook (env, obj, drawing_area, 1, drawing_area->window);
  gtk_drawing_area_size (GTK_DRAWING_AREA (drawing_area), width, height);

  gdk_threads_leave ();
  
  NSA_SET_PTR (env, obj, drawing_area);
}

