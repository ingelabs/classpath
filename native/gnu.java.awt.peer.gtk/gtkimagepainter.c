#include "gtkpeer.h"
#include "gnu_java_awt_peer_gtk_GtkImagePainter.h"
#include <libart_lgpl/art_misc.h>
#include <libart_lgpl/art_rgb_affine.h>

#define SWAPU32(w) \
  (((w) << 24) | (((w) & 0xff00) << 8) | (((w) >> 8) & 0xff00) | ((w) >> 24))

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkImagePainter_drawPixels
(JNIEnv *env, jobject obj, jobject gc_obj, jint bg_red, jint bg_green, 
 jint bg_blue, jint x, jint y, jint width, jint height, jintArray jpixels, 
 jint offset, jint scansize, jdoubleArray jaffine)
{
  struct graphics *g;
  jint *pixels, *elems;
  guchar *packed;
  int i;
  jsize num_pixels;
  guchar *j_rgba, *c_rgb;

  g = (struct graphics *) NSA_GET_PTR (env, gc_obj);

  elems = (*env)->GetIntArrayElements (env, jpixels, NULL);
  num_pixels = (*env)->GetArrayLength (env, jpixels);
 
  /* get a copy of the pixel data so we can modify it */
  pixels = malloc (sizeof (jint) * num_pixels);
  memcpy (pixels, elems, sizeof (jint) * num_pixels);
 
  (*env)->ReleaseIntArrayElements (env, jpixels, elems, 0);

#ifndef WORDS_BIGENDIAN
  /* convert pixels from 0xBBGGRRAA to 0xAARRGGBB */
  for (i = 0; i < num_pixels; i++)
    pixels[i] = SWAPU32 ((unsigned)pixels[i]);
#endif

  packed = (guchar *) malloc (sizeof (guchar) * 3 * num_pixels);
  j_rgba = (guchar *) pixels;
  c_rgb = packed;

  /* copy over pixels in DirectColorModel format to 24 bit RGB image data,
     and process the alpha channel */
  for (i = 0; i < num_pixels; i++)
    {
      jint ialpha = *j_rgba++;

      switch (ialpha)
	{
	case 0:			/* full transparency */
	  *c_rgb++ = bg_red;
	  *c_rgb++ = bg_green;
	  *c_rgb++ = bg_blue;
	  j_rgba += 3;
	  break;
	case 255:		/* opaque */
	  *c_rgb++ = *j_rgba++;
	  *c_rgb++ = *j_rgba++;
	  *c_rgb++ = *j_rgba++;
	  break;
	default:		/* compositing required */
	  {
	    jfloat alpha = ialpha / 255.0;
	    jfloat comp_alpha = 1.0 - alpha;
	    
	    *c_rgb++ = *j_rgba++ * alpha + bg_red * comp_alpha;
	    *c_rgb++ = *j_rgba++ * alpha + bg_green * comp_alpha;
	    *c_rgb++ = *j_rgba++ * alpha + bg_blue * comp_alpha;
	  }
	  break;
	}
    }

  if (jaffine)
    {
      jdouble *affine;
      ArtAlphaGamma *alphagamma = NULL;
      art_u8 *dst;
      int new_width, new_height;
      int i;
      
      affine = (*env)->GetDoubleArrayElements (env, jaffine, NULL);

      new_width = abs (width * affine[0]);
      new_height = abs (height * affine[3]);

      dst = (art_u8 *) malloc (sizeof (art_u8) * 3 * (new_width * new_height));
      
      art_rgb_affine (dst, 
		      0, 0,
		      new_width, new_height,
		      new_width * 3,
		      (art_u8 *) packed + offset * 3,
		      width, height,
		      scansize * 3,
		      affine,
		      ART_FILTER_NEAREST,
		      alphagamma);

      (*env)->ReleaseDoubleArrayElements (env, jaffine, affine, JNI_ABORT);
      
      free (packed);
      packed = (guchar *) dst;

      width = scansize = new_width;
      height = new_height;
      offset = 0;
    }

  gdk_threads_enter ();

  gdk_draw_rgb_image (g->drawable,
		      g->gc,
		      x + g->x_offset, 
		      y + g->y_offset, 
		      width, height, GDK_RGB_DITHER_NORMAL,
		      packed + offset * 3, scansize * 3);

  gdk_threads_leave ();

  free (pixels); 
  free (packed);
}


