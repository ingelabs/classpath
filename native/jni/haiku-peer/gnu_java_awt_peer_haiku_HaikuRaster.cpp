/* gnu_java_awt_peer_haiku_HaikuRaster.cpp -- native pixel loops for HaikuGraphics2D
   Copyright (C) 2026 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

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

/*
 * HaikuGraphics2D draws into int[] images (0x00RRGGBB or non-premultiplied
 * 0xAARRGGBB).  On Kaffe's interpreter, per-pixel Java loops are very slow,
 * so the common operations are done here, directly on the Java arrays:
 * rectangles, 1-pixel lines, paths (with or without anti-aliasing),
 * images (any affine transform), and copyArea.
 *
 * Paint: a solid colour, or a linear gradient given in user space with
 * the inverse of the device transform.  Clip: a device rectangle.
 */

#include <jni.h>

#include "gnu_java_awt_peer_haiku_HaikuRaster.h"

#include <math.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <vector>
#include <algorithm>

namespace {

/* The destination: pixels, scanline, size, alpha, clip. */
struct Target
{
  uint32_t *px;
  int scan;
  bool alpha;
  int cx0, cy0, cx1, cy1;  /* clip, exclusive end */
};

/* Non-premultiplied ARGB src over dst, with coverage a (0..255) already
   applied to the source alpha. */
inline uint32_t
blend(uint32_t s, uint32_t d, bool dstAlpha)
{
  uint32_t a = s >> 24;
  if (a == 255)
    return s;
  if (a == 0)
    return d;
  uint32_t na = 255 - a;
  uint32_t r = (((s >> 16) & 0xff) * a + ((d >> 16) & 0xff) * na + 127) / 255;
  uint32_t g = (((s >> 8) & 0xff) * a + ((d >> 8) & 0xff) * na + 127) / 255;
  uint32_t b = ((s & 0xff) * a + (d & 0xff) * na + 127) / 255;
  uint32_t oa = dstAlpha ? a + ((d >> 24) * na + 127) / 255 : 255;
  return (oa << 24) | (r << 16) | (g << 8) | b;
}

inline uint32_t
withAlpha(uint32_t argb, uint32_t cover)
{
  uint32_t a = ((argb >> 24) * cover + 127) / 255;
  return (a << 24) | (argb & 0xffffff);
}

/* A paint: solid colour or linear gradient. */
struct Paint
{
  bool gradient;
  uint32_t argb;
  /* Gradient: user-space points and colours, and the inverse transform
     (device to user). */
  double inv[6];
  double gx, gy, gdx, gdy, glen2;
  uint32_t c1, c2;
  bool cyclic;
  uint32_t xorBits;

  inline uint32_t at(int x, int y) const
  {
    if (!gradient)
      return argb;
    double dx = x + 0.5, dy = y + 0.5;
    double ux = inv[0] * dx + inv[2] * dy + inv[4];
    double uy = inv[1] * dx + inv[3] * dy + inv[5];
    double t = glen2 > 0 ? ((ux - gx) * gdx + (uy - gy) * gdy) / glen2 : 0;
    if (cyclic)
      {
        t = fmod(t, 2.0);
        if (t < 0) t += 2;
        if (t > 1) t = 2 - t;
      }
    else
      t = t < 0 ? 0 : t > 1 ? 1 : t;
    int k = (int) (t * 256);
    if (k > 256) k = 256;
    int nk = 256 - k;
    uint32_t a = (((c1 >> 24) & 0xff) * nk + ((c2 >> 24) & 0xff) * k) >> 8;
    uint32_t r = (((c1 >> 16) & 0xff) * nk + ((c2 >> 16) & 0xff) * k) >> 8;
    uint32_t g = (((c1 >> 8) & 0xff) * nk + ((c2 >> 8) & 0xff) * k) >> 8;
    uint32_t b = ((c1 & 0xff) * nk + (c2 & 0xff) * k) >> 8;
    return (a << 24) | (r << 16) | (g << 8) | b;
  }
};

/* Paints pixels x0..x1-1 of row y with coverage 255. */
inline void
span(const Target &t, const Paint &p, int y, int x0, int x1)
{
  if (x0 < t.cx0) x0 = t.cx0;
  if (x1 > t.cx1) x1 = t.cx1;
  if (y < t.cy0 || y >= t.cy1 || x0 >= x1)
    return;
  uint32_t *row = t.px + (size_t) y * t.scan;
  if (p.xorBits != 0)
    {
      for (int x = x0; x < x1; x++)
        row[x] ^= p.xorBits;
      return;
    }
  if (!p.gradient && (p.argb >> 24) == 255)
    {
      uint32_t v = p.argb;
      for (int x = x0; x < x1; x++)
        row[x] = v;
      return;
    }
  for (int x = x0; x < x1; x++)
    row[x] = blend(p.at(x, y), row[x], t.alpha);
}

/* One pixel with coverage (0..255). */
inline void
plot(const Target &t, const Paint &p, int x, int y, uint32_t cover)
{
  uint32_t *d = t.px + (size_t) y * t.scan + x;
  if (p.xorBits != 0)
    {
      if (cover >= 128)
        *d ^= p.xorBits;
      return;
    }
  *d = blend(withAlpha(p.at(x, y), cover), *d, t.alpha);
}

struct Edge
{
  float x0, y0, x1, y1;  /* y0 < y1 */
  float slope;           /* dx / dy */
  int dir;
};

/* Flattens a path (0 x y move, 1 x y line, 2 quad, 3 cubic, 4 close) to
   edges.  Tolerance about 0.2 pixels. */
void
flatten(const float *d, int n, std::vector<Edge> *edges)
{
  float sx = 0, sy = 0, cx = 0, cy = 0;
  bool open = false;
  auto line = [&](float x0, float y0, float x1, float y1) {
    if (y0 == y1)
      return;
    Edge e;
    if (y0 < y1)
      { e.x0 = x0; e.y0 = y0; e.x1 = x1; e.y1 = y1; e.dir = 1; }
    else
      { e.x0 = x1; e.y0 = y1; e.x1 = x0; e.y1 = y0; e.dir = -1; }
    e.slope = (e.x1 - e.x0) / (e.y1 - e.y0);
    edges->push_back(e);
  };
  int k = 0;
  while (k < n)
    {
      int op = (int) d[k++];
      switch (op)
        {
        case 0:
          if (open)
            line(cx, cy, sx, sy);
          sx = cx = d[k]; sy = cy = d[k + 1]; k += 2;
          open = true;
          break;
        case 1:
          line(cx, cy, d[k], d[k + 1]);
          cx = d[k]; cy = d[k + 1]; k += 2;
          break;
        case 2:
        case 3:
          {
            int np = op == 2 ? 2 : 3;
            float px[4] = { cx, d[k], d[k + 2], op == 3 ? d[k + 4] : 0 };
            float py[4] = { cy, d[k + 1], d[k + 3], op == 3 ? d[k + 5] : 0 };
            k += 2 * np;
            float len = 0;
            for (int i = 0; i < np; i++)
              len += hypotf(px[i + 1] - px[i], py[i + 1] - py[i]);
            int steps = (int) ceilf(sqrtf(len) * 1.2f);
            if (steps < 2) steps = 2;
            if (steps > 256) steps = 256;
            float lx = cx, ly = cy;
            for (int s = 1; s <= steps; s++)
              {
                float u = (float) s / steps, v = 1 - u, x, y;
                if (op == 2)
                  {
                    x = v * v * px[0] + 2 * v * u * px[1] + u * u * px[2];
                    y = v * v * py[0] + 2 * v * u * py[1] + u * u * py[2];
                  }
                else
                  {
                    x = v * v * v * px[0] + 3 * v * v * u * px[1]
                        + 3 * v * u * u * px[2] + u * u * u * px[3];
                    y = v * v * v * py[0] + 3 * v * v * u * py[1]
                        + 3 * v * u * u * py[2] + u * u * u * py[3];
                  }
                line(lx, ly, x, y);
                lx = x; ly = y;
              }
            cx = lx; cy = ly;
            break;
          }
        default: /* close */
          if (open)
            line(cx, cy, sx, sy);
          cx = sx; cy = sy;
          break;
        }
    }
  if (open)
    line(cx, cy, sx, sy);
}

/* Draws a 1-pixel line (Bresenham) with both end points. skipFirst leaves
   out the first point, so joined segments do not paint a point twice
   (that matters in XOR mode and with translucent colours). */
void
line1(const Target &t, const Paint &p, int x0, int y0, int x1, int y1,
      bool skipFirst)
{
  int dx = abs(x1 - x0), dy = -abs(y1 - y0);
  int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1;
  int err = dx + dy, x = x0, y = y0;
  bool first = true;
  for (;;)
    {
      if (!(first && skipFirst)
          && x >= t.cx0 && x < t.cx1 && y >= t.cy0 && y < t.cy1)
        {
          uint32_t *px = t.px + (size_t) y * t.scan + x;
          if (p.xorBits)
            *px ^= p.xorBits;
          else
            *px = blend(p.at(x, y), *px, t.alpha);
        }
      first = false;
      if (x == x1 && y == y1)
        break;
      int e2 = 2 * err;
      if (e2 >= dy) { err += dy; x += sx; }
      if (e2 <= dx) { err += dx; y += sy; }
    }
}

/* Strokes a path with 1-pixel lines: curves are flattened, points are
   rounded to pixels as java.awt does for thin lines. */
void
drawPathThin(const Target &t, const Paint &p, const float *d, int n)
{
  float sx = 0, sy = 0, cx = 0, cy = 0;
  int lastX = 0, lastY = 0;
  bool have = false;
  auto to = [&](float x, float y) {
    int ix = (int) floorf(x), iy = (int) floorf(y);
    if (have)
      line1(t, p, lastX, lastY, ix, iy, true);
    else
      line1(t, p, ix, iy, ix, iy, false);
    lastX = ix; lastY = iy; have = true;
  };
  int k = 0;
  while (k < n)
    {
      int op = (int) d[k++];
      if (op == 0)
        {
          sx = cx = d[k]; sy = cy = d[k + 1]; k += 2;
          have = false;
          to(cx, cy);
        }
      else if (op == 1)
        {
          cx = d[k]; cy = d[k + 1]; k += 2;
          to(cx, cy);
        }
      else if (op == 2 || op == 3)
        {
          int np = op == 2 ? 2 : 3;
          float px[4] = { cx, d[k], d[k + 2], op == 3 ? d[k + 4] : 0 };
          float py[4] = { cy, d[k + 1], d[k + 3], op == 3 ? d[k + 5] : 0 };
          k += 2 * np;
          float len = 0;
          for (int i = 0; i < np; i++)
            len += hypotf(px[i + 1] - px[i], py[i + 1] - py[i]);
          int steps = (int) ceilf(sqrtf(len) * 1.2f);
          if (steps < 2) steps = 2;
          if (steps > 256) steps = 256;
          for (int s = 1; s <= steps; s++)
            {
              float u = (float) s / steps, v = 1 - u, x, y;
              if (op == 2)
                {
                  x = v * v * px[0] + 2 * v * u * px[1] + u * u * px[2];
                  y = v * v * py[0] + 2 * v * u * py[1] + u * u * py[2];
                }
              else
                {
                  x = v * v * v * px[0] + 3 * v * v * u * px[1]
                      + 3 * v * u * u * px[2] + u * u * u * px[3];
                  y = v * v * v * py[0] + 3 * v * v * u * py[1]
                      + 3 * v * u * u * py[2] + u * u * u * py[3];
                }
              to(x, y);
            }
          cx = px[np]; cy = py[np];
        }
      else
        {
          if (have && ((int) floorf(sx) != lastX || (int) floorf(sy) != lastY))
            to(sx, sy);
          cx = sx; cy = sy;
        }
    }
}

struct Crossing
{
  float x;
  int dir;
  bool operator<(const Crossing &o) const { return x < o.x; }
};

/* Crossings of the edges with the horizontal line at sy, sorted. */
inline void
crossings(const std::vector<Edge> &edges, size_t first, float sy,
          std::vector<Crossing> *out)
{
  out->clear();
  for (size_t i = first; i < edges.size(); i++)
    {
      const Edge &e = edges[i];
      if (e.y0 > sy)
        break;  /* edges are sorted by y0 */
      if (sy < e.y1)
        {
          Crossing c = { e.x0 + (sy - e.y0) * e.slope, e.dir };
          out->push_back(c);
        }
    }
  std::sort(out->begin(), out->end());
}

void
fillPath(const Target &t, const Paint &p, const float *d, int n,
         bool evenOdd, bool aa)
{
  std::vector<Edge> edges;
  flatten(d, n, &edges);
  if (edges.empty())
    return;
  std::sort(edges.begin(), edges.end(),
            [](const Edge &a, const Edge &b) { return a.y0 < b.y0; });
  float ymin = edges[0].y0, ymax = 0;
  for (size_t i = 0; i < edges.size(); i++)
    if (edges[i].y1 > ymax)
      ymax = edges[i].y1;
  int y0 = (int) floorf(ymin), y1 = (int) ceilf(ymax);
  if (y0 < t.cy0) y0 = t.cy0;
  if (y1 > t.cy1) y1 = t.cy1;
  std::vector<Crossing> cr;
  if (!aa)
    {
      for (int y = y0; y < y1; y++)
        {
          float sy = y + 0.5f;
          crossings(edges, 0, sy, &cr);
          int w = 0;
          for (size_t i = 0; i + 1 < cr.size(); i++)
            {
              w += cr[i].dir;
              bool inside = evenOdd ? (i % 2 == 0) : w != 0;
              if (inside)
                span(t, p, y, (int) ceilf(cr[i].x - 0.5f),
                     (int) ceilf(cr[i + 1].x - 0.5f));
            }
        }
      return;
    }
  /* Anti-aliased: 4 sub-scanlines per row, exact horizontal coverage. */
  const int SS = 4;
  int cw = t.cx1 - t.cx0;
  if (cw <= 0)
    return;
  std::vector<float> acc(cw + 2, 0.0f);
  for (int y = y0; y < y1; y++)
    {
      int minx = cw, maxx = -1;
      for (int s = 0; s < SS; s++)
        {
          float sy = y + (s + 0.5f) / SS;
          crossings(edges, 0, sy, &cr);
          int w = 0;
          for (size_t i = 0; i + 1 < cr.size(); i++)
            {
              w += cr[i].dir;
              bool inside = evenOdd ? (i % 2 == 0) : w != 0;
              if (!inside)
                continue;
              float xa = cr[i].x - t.cx0, xb = cr[i + 1].x - t.cx0;
              if (xa < 0) xa = 0;
              if (xb > cw) xb = cw;
              if (xa >= xb)
                continue;
              int ia = (int) xa, ib = (int) xb;
              if (ia < minx) minx = ia;
              if (ib > maxx) maxx = ib < cw ? ib : cw - 1;
              if (ia == ib)
                acc[ia] += xb - xa;
              else
                {
                  acc[ia] += ia + 1 - xa;
                  for (int x = ia + 1; x < ib; x++)
                    acc[x] += 1;
                  if (ib < cw)
                    acc[ib] += xb - ib;
                }
            }
        }
      for (int x = minx; x <= maxx && x < cw; x++)
        {
          float a = acc[x];
          acc[x] = 0;
          if (a <= 0)
            continue;
          uint32_t cover = (uint32_t) (a * (255.0f / SS) + 0.5f);
          if (cover > 255) cover = 255;
          if (cover == 255 && !p.gradient && (p.argb >> 24) == 255
              && p.xorBits == 0)
            t.px[(size_t) y * t.scan + x + t.cx0] = p.argb;
          else
            plot(t, p, x + t.cx0, y, cover);
        }
    }
}

/* Reads the arguments that every call shares. */
struct Pinned
{
  JNIEnv *env;
  jintArray arr;
  jint *ptr;
  Pinned(JNIEnv *e, jintArray a) : env(e), arr(a)
  {
    ptr = (jint *) e->GetPrimitiveArrayCritical(a, NULL);
  }
  ~Pinned()
  {
    if (ptr != NULL)
      env->ReleasePrimitiveArrayCritical(arr, ptr, 0);
  }
};

Target
target(jint *px, jint scan, jboolean alpha, jintArray clip, JNIEnv *env)
{
  Target t;
  jint c[4];
  env->GetIntArrayRegion(clip, 0, 4, c);
  t.px = (uint32_t *) px;
  t.scan = scan;
  t.alpha = alpha;
  t.cx0 = c[0]; t.cy0 = c[1]; t.cx1 = c[0] + c[2]; t.cy1 = c[1] + c[3];
  return t;
}

Paint
solid(jint argb, jint xorBits)
{
  Paint p;
  memset(&p, 0, sizeof(p));
  p.argb = (uint32_t) argb;
  p.xorBits = (uint32_t) xorBits;
  return p;
}

/* g = x1, y1, x2, y2 (user space), then the inverse transform (6). */
Paint
gradient(JNIEnv *env, jdoubleArray g, jint c1, jint c2, jboolean cyclic)
{
  Paint p;
  memset(&p, 0, sizeof(p));
  jdouble v[10];
  env->GetDoubleArrayRegion(g, 0, 10, v);
  p.gradient = true;
  p.gx = v[0]; p.gy = v[1];
  p.gdx = v[2] - v[0]; p.gdy = v[3] - v[1];
  p.glen2 = p.gdx * p.gdx + p.gdy * p.gdy;
  for (int i = 0; i < 6; i++)
    p.inv[i] = v[4 + i];
  p.c1 = (uint32_t) c1;
  p.c2 = (uint32_t) c2;
  p.cyclic = cyclic;
  return p;
}

inline uint32_t
sampleNearest(const uint32_t *src, int scan, int w, int h, double u,
              double v, bool srcAlpha, bool *inside)
{
  int x = (int) floor(u), y = (int) floor(v);
  if (x < 0 || y < 0 || x >= w || y >= h)
    {
      *inside = false;
      return 0;
    }
  *inside = true;
  uint32_t s = src[(size_t) y * scan + x];
  return srcAlpha ? s : s | 0xff000000;
}

inline uint32_t
sampleBilinear(const uint32_t *src, int scan, int w, int h, double u,
               double v, bool srcAlpha, bool *inside)
{
  u -= 0.5; v -= 0.5;
  int x = (int) floor(u), y = (int) floor(v);
  if (x < -1 || y < -1 || x >= w || y >= h)
    {
      *inside = false;
      return 0;
    }
  *inside = true;
  int fx = (int) ((u - x) * 256), fy = (int) ((v - y) * 256);
  uint32_t q[4];
  for (int i = 0; i < 4; i++)
    {
      int xx = x + (i & 1), yy = y + (i >> 1);
      if (xx < 0) xx = 0;
      if (yy < 0) yy = 0;
      if (xx >= w) xx = w - 1;
      if (yy >= h) yy = h - 1;
      uint32_t s = src[(size_t) yy * scan + xx];
      q[i] = srcAlpha ? s : s | 0xff000000;
    }
  uint32_t out = 0;
  for (int sh = 0; sh < 32; sh += 8)
    {
      int a = (q[0] >> sh) & 0xff, b = (q[1] >> sh) & 0xff;
      int c = (q[2] >> sh) & 0xff, d = (q[3] >> sh) & 0xff;
      int top = a * (256 - fx) + b * fx, bot = c * (256 - fx) + d * fx;
      uint32_t r = (uint32_t) ((top * (256 - fy) + bot * fy) >> 16);
      out |= (r & 0xff) << sh;
    }
  return out;
}

} /* namespace */

extern "C" {

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_haiku_HaikuRaster_fillRect(JNIEnv *env, jclass,
                                                  jintArray dst, jint scan,
                                                  jboolean alpha,
                                                  jintArray clip, jint x,
                                                  jint y, jint w, jint h,
                                                  jint argb, jint xorBits)
{
  Pinned d(env, dst);
  if (d.ptr == NULL)
    return;
  Target t = target(d.ptr, scan, alpha, clip, env);
  Paint p = solid(argb, xorBits);
  for (int yy = y; yy < y + h; yy++)
    span(t, p, yy, x, x + w);
}

/* A 1-pixel line (Bresenham), both end points included. */
JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_haiku_HaikuRaster_drawLine(JNIEnv *env, jclass,
                                                  jintArray dst, jint scan,
                                                  jboolean alpha,
                                                  jintArray clip, jint x0,
                                                  jint y0, jint x1, jint y1,
                                                  jint argb, jint xorBits)
{
  Pinned d(env, dst);
  if (d.ptr == NULL)
    return;
  Target t = target(d.ptr, scan, alpha, clip, env);
  Paint p = solid(argb, xorBits);
  int dx = abs(x1 - x0), dy = -abs(y1 - y0);
  int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1;
  int err = dx + dy, x = x0, y = y0;
  for (;;)
    {
      if (x >= t.cx0 && x < t.cx1 && y >= t.cy0 && y < t.cy1)
        {
          uint32_t *px = t.px + (size_t) y * t.scan + x;
          if (p.xorBits)
            *px ^= p.xorBits;
          else
            *px = blend(p.argb, *px, t.alpha);
        }
      if (x == x1 && y == y1)
        break;
      int e2 = 2 * err;
      if (e2 >= dy) { err += dy; x += sx; }
      if (e2 <= dx) { err += dx; y += sy; }
    }
}

/* Strokes a device-space path with 1-pixel lines in a colour. */
JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_haiku_HaikuRaster_drawPathThin(JNIEnv *env, jclass,
                                                      jintArray dst,
                                                      jint scan,
                                                      jboolean alpha,
                                                      jintArray clip,
                                                      jfloatArray path,
                                                      jint n, jint argb,
                                                      jint xorBits)
{
  std::vector<float> d(n > 0 ? n : 1);
  env->GetFloatArrayRegion(path, 0, n, &d[0]);
  Pinned px(env, dst);
  if (px.ptr == NULL)
    return;
  Target t = target(px.ptr, scan, alpha, clip, env);
  drawPathThin(t, solid(argb, xorBits), &d[0], n);
}

/* Fills a device-space path with a colour. */
JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_haiku_HaikuRaster_fillPath(JNIEnv *env, jclass,
                                                  jintArray dst, jint scan,
                                                  jboolean alpha,
                                                  jintArray clip,
                                                  jfloatArray path, jint n,
                                                  jboolean evenOdd,
                                                  jboolean aa, jint argb,
                                                  jint xorBits)
{
  std::vector<float> d(n > 0 ? n : 1);
  env->GetFloatArrayRegion(path, 0, n, &d[0]);
  Pinned px(env, dst);
  if (px.ptr == NULL)
    return;
  Target t = target(px.ptr, scan, alpha, clip, env);
  fillPath(t, solid(argb, xorBits), &d[0], n, evenOdd, aa);
}

/* Fills a device-space path with a linear gradient. */
JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_haiku_HaikuRaster_fillPathGradient(JNIEnv *env, jclass,
  jintArray dst, jint scan, jboolean alpha,
  jintArray clip, jfloatArray path, jint n, jboolean evenOdd, jboolean aa,
  jdoubleArray g, jint c1, jint c2, jboolean cyclic)
{
  std::vector<float> d(n > 0 ? n : 1);
  env->GetFloatArrayRegion(path, 0, n, &d[0]);
  Paint p = gradient(env, g, c1, c2, cyclic);
  Pinned px(env, dst);
  if (px.ptr == NULL)
    return;
  Target t = target(px.ptr, scan, alpha, clip, env);
  fillPath(t, p, &d[0], n, evenOdd, aa);
}

/* Draws the source rectangle (sx, sy, sw, sh) of an int image through an
   affine transform.  inv maps device points to source points; box is
   the device area to visit.  extraAlpha is 0..255. */
JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_haiku_HaikuRaster_drawImage(JNIEnv *env, jclass,
  jintArray dst, jint scan, jboolean alpha,
  jintArray clip, jintArray src, jint sscan, jboolean srcAlpha, jint sx,
  jint sy, jint sw, jint sh, jdoubleArray inverse, jintArray box,
  jboolean bilinear, jint extraAlpha)
{
  jdouble m[6];
  env->GetDoubleArrayRegion(inverse, 0, 6, m);
  jint b[4];
  env->GetIntArrayRegion(box, 0, 4, b);
  Pinned dp(env, dst);
  if (dp.ptr == NULL)
    return;
  Target t = target(dp.ptr, scan, alpha, clip, env);
  Pinned sp(env, src);
  if (sp.ptr == NULL)
    return;
  const uint32_t *s = (const uint32_t *) sp.ptr + (size_t) sy * sscan + sx;
  int x0 = b[0] > t.cx0 ? b[0] : t.cx0, y0 = b[1] > t.cy0 ? b[1] : t.cy0;
  int x1 = b[0] + b[2] < t.cx1 ? b[0] + b[2] : t.cx1;
  int y1 = b[1] + b[3] < t.cy1 ? b[1] + b[3] : t.cy1;
  bool translate = m[0] == 1 && m[1] == 0 && m[2] == 0 && m[3] == 1
                   && m[4] == floor(m[4]) && m[5] == floor(m[5]);
  for (int y = y0; y < y1; y++)
    {
      uint32_t *row = t.px + (size_t) y * t.scan;
      if (translate && extraAlpha == 255)
        {
          /* Pure integer translation: copy or blend a row. */
          int v = y + (int) m[5];
          if (v < 0 || v >= sh)
            continue;
          int u0 = x0 + (int) m[4], u1 = x1 + (int) m[4];
          int xs = x0;
          if (u0 < 0) { xs -= u0; u0 = 0; }
          if (u1 > sw) u1 = sw;
          const uint32_t *srow = s + (size_t) v * sscan;
          if (!srcAlpha && !t.alpha)
            {
              if (u1 > u0)
                memcpy(row + xs, srow + u0, (u1 - u0) * 4);
            }
          else
            for (int u = u0; u < u1; u++, xs++)
              row[xs] = srcAlpha ? blend(srow[u], row[xs], t.alpha)
                                 : srow[u] | 0xff000000;
          continue;
        }
      for (int x = x0; x < x1; x++)
        {
          double dx = x + 0.5, dy = y + 0.5;
          double u = m[0] * dx + m[2] * dy + m[4];
          double v = m[1] * dx + m[3] * dy + m[5];
          bool inside;
          uint32_t c = bilinear
            ? sampleBilinear(s, sscan, sw, sh, u, v, srcAlpha, &inside)
            : sampleNearest(s, sscan, sw, sh, u, v, srcAlpha, &inside);
          if (!inside)
            continue;
          if (extraAlpha != 255)
            c = withAlpha(c, extraAlpha);
          row[x] = blend(c, row[x], t.alpha);
        }
    }
}

/* Copies a device rectangle by (dx, dy); source and target may overlap. */
JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_haiku_HaikuRaster_copyArea(JNIEnv *env, jclass,
                                                  jintArray dst, jint scan,
                                                  jint x, jint y, jint w,
                                                  jint h, jint dx, jint dy)
{
  Pinned d(env, dst);
  if (d.ptr == NULL || w <= 0 || h <= 0)
    return;
  uint32_t *p = (uint32_t *) d.ptr;
  if (dy > 0)
    for (int r = h - 1; r >= 0; r--)
      memmove(p + (size_t) (y + r + dy) * scan + x + dx,
              p + (size_t) (y + r) * scan + x, (size_t) w * 4);
  else
    for (int r = 0; r < h; r++)
      memmove(p + (size_t) (y + r + dy) * scan + x + dx,
              p + (size_t) (y + r) * scan + x, (size_t) w * 4);
}

} /* extern "C" */
