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
  else if (GTK_IS_LAYOUT (widget))
    {
      g->drawable = (GdkDrawable *) GTK_LAYOUT (widget)->bin_window;
    }
  else
    {
      g->drawable = (GdkDrawable *) widget->window;
    }

  g->cm = gtk_widget_get_colormap (widget);
  g->gc = gdk_gc_new (g->drawable);
  gdk_gc_copy (g->gc, widget->style->fg_gc[GTK_STATE_NORMAL]);
  g->x_offset = g->y_offset = 0;
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

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_translate
  (JNIEnv *env, jobject obj, jint x, jint y)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  g->x_offset += x;
  g->y_offset += y;
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
  gdk_draw_string (g->drawable, gdk_font_load (xlfd), g->gc, 
		   x + g->x_offset, y + g->y_offset, cstr);
  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, str, cstr);
  g_free (xlfd);
}


JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_drawLine
  (JNIEnv *env, jobject obj, jint x, jint y, jint x2, jint y2)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_line (g->drawable, g->gc, 
		 x + g->x_offset, y + g->y_offset, 
		 x2 + g->x_offset, y2 + g->y_offset);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_fillRect
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_rectangle (g->drawable, g->gc, TRUE, 
		      x + g->x_offset, y + g->y_offset, width, height);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_drawRect
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_rectangle (g->drawable, g->gc, FALSE, 
		      x + g->x_offset, y + g->y_offset, width, height);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_copyArea
  (JNIEnv *env, jobject obj, jint x, jint y, 
   jint width, jint height, jint dx, jint dy)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_window_copy_area ((GdkWindow *)g->drawable,
			g->gc,
			x + g->x_offset, y + g->y_offset,
			(GdkWindow *)g->drawable,
			x + g->x_offset + dx, y + g->y_offset + dy,
			width, height);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_clearRect
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_window_clear_area ((GdkWindow *)g->drawable, 
			 x + g->x_offset, y + g->y_offset, width, height);
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

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_drawArc
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height, 
   jint angle1, jint angle2)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_arc (g->drawable, g->gc, FALSE, 
		x + g->x_offset, y + g->y_offset, 
		width, height, angle1 << 6, angle2 << 6);
  gdk_threads_leave ();
}  

GdkPoint *
translate_points (JNIEnv *env, jintArray xpoints, jintArray ypoints, 
		  jint npoints, jint x_offset, jint y_offset)
{
  GdkPoint *points;
  jint *x, *y;
  int i;

  /* allocate one more point than necessary, in case we need to tack
     on an extra due to the semantics of Java polygons. */
  points = g_malloc (sizeof (GdkPoint) * (npoints + 1));
  
  x = (*env)->GetIntArrayElements (env, xpoints, NULL);
  y = (*env)->GetIntArrayElements (env, ypoints, NULL);

  for (i = 0; i < npoints; i++)
    {
      points[i].x = x[i] + x_offset;
      points[i].y = y[i] + y_offset;
    }

  (*env)->ReleaseIntArrayElements (env, xpoints, x, JNI_ABORT);
  (*env)->ReleaseIntArrayElements (env, ypoints, y, JNI_ABORT);

  return points;
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_drawPolyline
  (JNIEnv *env, jobject obj, jintArray xpoints, jintArray ypoints, 
   jint npoints)
{
  struct graphics *g;
  GdkPoint *points;

  g = (struct graphics *) NSA_GET_PTR (env, obj);
  points = translate_points (env, xpoints, ypoints, npoints,
			     g->x_offset, g->y_offset);

  gdk_threads_enter ();
  gdk_draw_lines (g->drawable, g->gc, points, npoints);
  gdk_threads_leave ();

  g_free (points);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_drawPolygon
  (JNIEnv *env, jobject obj, jintArray xpoints, jintArray ypoints, 
   jint npoints)
{
  struct graphics *g;
  GdkPoint *points;

  g = (struct graphics *) NSA_GET_PTR (env, obj);
  points = translate_points (env, xpoints, ypoints, npoints,
			     g->x_offset, g->y_offset);

  /* make sure the polygon is closed, per Java semantics.
     if it's not, we close it. */
  if (points[0].x != points[npoints-1].x || points[0].y != points[npoints-1].y)
    points[npoints++] = points[0];

  gdk_threads_enter ();
  gdk_draw_lines (g->drawable, g->gc, points, npoints);
  gdk_threads_leave ();

  g_free (points);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_fillPolygon
  (JNIEnv *env, jobject obj, jintArray xpoints, jintArray ypoints, 
   jint npoints)
{
  struct graphics *g;
  GdkPoint *points;

  g = (struct graphics *) NSA_GET_PTR (env, obj);
  points = translate_points (env, xpoints, ypoints, npoints,
			     g->x_offset, g->y_offset);
  gdk_threads_enter ();
  gdk_draw_polygon (g->drawable, g->gc, TRUE, points, npoints);
  gdk_threads_leave ();

  g_free (points);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_fillArc
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height, 
   jint angle1, jint angle2)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_arc (g->drawable, g->gc, TRUE, 
		x + g->x_offset, y + g->y_offset, 
		width, height, angle1 << 6, angle2 << 6);
  gdk_threads_leave ();
}  

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_drawOval
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_arc (g->drawable, g->gc, FALSE, 
		x + g->x_offset, y + g->y_offset, 
		width, height, 0, 23040);
  gdk_threads_leave ();
}  

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_fillOval
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
  struct graphics *g;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gdk_draw_arc (g->drawable, g->gc, TRUE, 
		x + g->x_offset, y + g->y_offset, 
		width, height, 0, 23040);
  gdk_threads_leave ();
}  

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_setClipRectangle
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
  struct graphics *g;
  GdkRectangle rectangle;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  rectangle.x = x + g->x_offset;
  rectangle.y = y + g->y_offset;
  rectangle.width = width;
  rectangle.height = height;

  gdk_threads_enter ();
  gdk_gc_set_clip_rectangle (g->gc, &rectangle);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GdkGraphics_clipRect
  (JNIEnv *env, jobject obj, jint x1, jint y1, jint width1, jint height1, 
   jint x2, jint y2, jint width2, jint height2)
{
  struct graphics *g;
  GdkRectangle cur_clip, req_clip, new_clip;

  g = (struct graphics *) NSA_GET_PTR (env, obj);

  cur_clip.x = x1;
  cur_clip.y = y1;
  cur_clip.width = width1;
  cur_clip.height = height1;
  
  req_clip.x = x2;
  req_clip.y = y2;
  req_clip.width = width2;
  req_clip.height = height2;

  gdk_rectangle_intersect (&cur_clip, &req_clip, &new_clip);
  
  new_clip.x += g->x_offset;
  new_clip.y += g->y_offset;

  gdk_threads_enter ();
  gdk_gc_set_clip_rectangle (g->gc, &new_clip);
  gdk_threads_leave ();
}
