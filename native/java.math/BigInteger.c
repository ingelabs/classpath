#include <stdlib.h>
#define __USE_GNU
#include <stdio.h>
#include <unistd.h>
#include <endian.h>
#include <gmp2/gmp.h>
#include <math.h>
#include "BigInteger.h"
#include "native_state.h"

/* maximum value a jdouble can hold */
#define JDBL_MAX 1.7976931348623157e+308

#define SWAPU32(w) \
  (((w) << 24) | (((w) & 0xff00) << 8) | (((w) >> 8) & 0xff00) | ((w) >> 24))

struct state_table *table;
jmethodID constructor;

JNIEXPORT void JNICALL Java_java_math_BigInteger_initNativeState (JNIEnv * env, 
							jclass clazz)
{
  table = init_state_table (env, clazz);
  constructor = (*env)->GetMethodID (env, clazz, "<init>", "()V");
}

JNIEXPORT jboolean JNICALL Java_java_math_BigInteger_initFromString (JNIEnv *env, 
							   jobject obj, 
							   jstring str_obj,
							   jint radix)
{
  const char *str;
  mpz_t *integ;
  int ret;

  integ = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*integ);

  str = (*env)->GetStringUTFChars (env, str_obj, NULL);
  ret = mpz_set_str (*integ, str, radix);
  (*env)->ReleaseStringUTFChars (env, str_obj, str);

  if (ret == -1)		/* mpz_set_str signaled failure */
    {
      mpz_clear (*integ);
      return JNI_FALSE;
    }

  set_state (env, obj, table, integ);
  return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_java_math_BigInteger_initZero
  (JNIEnv *env, jobject obj)
{
  mpz_t *integ;

  integ = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*integ);
  set_state (env, obj, table, integ);
}

JNIEXPORT jbyteArray JNICALL Java_java_math_BigInteger_toByteArray
  (JNIEnv *env, jobject obj)
{
  mpz_t *this, pos;
  FILE *out;
  int negative, prepend;
  jsize array_size;
  jbyte *write_loc, *java_array_pointer;
  jbyteArray data;
  char *bp, *p;
  size_t size;
  int i;

  this = (mpz_t *) get_state (env, obj, table);
  mpz_init_set (pos, *this);
  negative = (mpz_sgn (pos) == -1);

  /* if we're negative, we need to take the 2's complement, in order to get
     a "usable" signed magnitude output from mpz_out_raw */
  if (negative)
    mpz_com (pos, pos);

  out = open_memstream (&bp, &size);
  mpz_out_raw (out, pos);
  mpz_clear (pos);
  fflush (out);
  if (size == 4)		/* bigint is zero : special case */
    {
      rewind (out);
      fwrite ("\000\000\000\001\000", 5, 1, out);
    }
  fclose (out);

  array_size = size - sizeof (jint);
  p = bp + sizeof (jint);	/* skip past the len header */

  prepend = ((negative && (~*p) >= 0) || ((!negative) && (*p < 0)));
  
  data = (*env)->NewByteArray (env, (prepend) ? array_size + 1 : array_size);
  write_loc = java_array_pointer = (*env)->GetByteArrayElements (env, 
								 data, NULL);

  if (prepend)
    {
      *write_loc = (negative) ? -1 : 0;
      write_loc++;
    }

  for (i = 0; i < array_size; i++)
    {
      *write_loc = (negative) ? ~*p : *p;
      write_loc++;
      p++;
    }

  (*env)->ReleaseByteArrayElements (env, data, java_array_pointer, 0);
  free (bp);

  return data;
}

JNIEXPORT void JNICALL Java_java_math_BigInteger_nativeFinalize
  (JNIEnv *env, jobject obj)
{
  mpz_t *integ;

  integ = remove_state_slot (env, obj, table);
  mpz_clear (*integ);
}

JNIEXPORT void JNICALL Java_java_math_BigInteger_initFromTwosCompByteArray
  (JNIEnv *env, jobject obj, jbyteArray array)
{
  mpz_t *integ;
  jsize array_len, array_len_to_write;
  FILE *stream;
  jbyte *data;
  char *bp, *p;
  size_t size;
  int negative;
  int i;

  array_len = (*env)->GetArrayLength (env, array);
  p = data = (*env)->GetByteArrayElements (env, array, NULL);

  negative = (*p < 0);
  /* if we have an explicit sign bit, skip over it */
  if (*p == -1 || *p == 0)
    {
      p++;
      array_len--;
    }

  /* make sure we write out the array len in big endian format */
#if __BYTEORDER == __LITTE_ENDIAN
  array_len_to_write = SWAPU32 ((unsigned)array_len);
#else
  array_len_to_write = array_len;
#endif

  stream = open_memstream (&bp, &size);
  fwrite (&array_len_to_write, sizeof (array_len_to_write), 1, stream);

  for (i = 0; i < array_len; i++, p++)
    putc ((negative) ? ~*p : *p, stream);

  (*env)->ReleaseByteArrayElements (env, array, data, 0);
  fflush (stream);

  integ = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*integ);
  mpz_inp_raw (*integ, stream);
  if (negative)
    mpz_com (*integ, *integ);

  fclose (stream);
  free (bp);

  set_state (env, obj, table, integ);
}
    
JNIEXPORT void JNICALL Java_java_math_BigInteger_initFromSignedMagnitudeByteArray
  (JNIEnv *env, jobject obj, jint sign, jbyteArray magnitude)
{
  mpz_t *integ;
  FILE *stream;
  char *bp;
  size_t size;
  jsize array_len, array_len_to_write;
  jbyte *data;

  array_len = (*env)->GetArrayLength (env, magnitude);
  array_len_to_write = (sign < 0) ? -array_len : array_len;
  
  /* make sure we write out the array len in big endian format */
#if __BYTEORDER == __LITTE_ENDIAN
  array_len_to_write = SWAPU32 ((unsigned)array_len_to_write);
#endif

  stream = open_memstream (&bp, &size);
  fwrite (&array_len_to_write, sizeof (array_len_to_write), 1, stream);

  data = (*env)->GetByteArrayElements (env, magnitude, NULL);
  fwrite (data, sizeof (jbyte), array_len, stream);
  (*env)->ReleaseByteArrayElements (env, magnitude, data, 0);
  fflush (stream);

  integ = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*integ);
  mpz_inp_raw (*integ, stream);

  fclose (stream);
  free (bp);

  set_state (env, obj, table, integ);
}

JNIEXPORT void JNICALL Java_java_math_BigInteger_initFromLong (JNIEnv *env, 
						     jobject obj, 
						     jlong num)
{
  mpz_t *integ;
  mpz_t msw;
  jint msw_int;

  integ = (mpz_t *) malloc (sizeof (mpz_t));

  /* set the least significant word */
  mpz_init_set_ui (*integ, (jint) num);

  msw_int = (jint)(num >> 32);	/* grab the most significant word */
  if (msw_int != 0)
    {
      mpz_init_set_si (msw, msw_int);
      mpz_mul_2exp (msw, msw, 32);   /* move it back to the proper slot */
      mpz_ior (*integ, *integ, msw); /* msw | lsw */
    }

  set_state (env, obj, table, integ);
}
  

static
jobject
mpz_t2obj (JNIEnv *env, jobject obj, mpz_t *val)
{
  jobject big_int;

  big_int = (*env)->NewObject (env, (*env)->GetObjectClass (env, obj),
			       constructor);

  set_state (env, big_int, table, val);

  return big_int;
}  

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_add (JNIEnv *env, jobject obj, 
					       jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);
  
  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_add (*result, *this, *value);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_subtract (JNIEnv *env, jobject obj, 
						    jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);
  
  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_sub (*result, *this, *value);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_multiply (JNIEnv *env, 
						    jobject obj, 
						    jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);
  
  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_mul (*result, *this, *value);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_abs (JNIEnv *env, jobject obj)
{
  mpz_t *this;
  mpz_t *result;

  this   = (mpz_t *) get_state (env, obj, table);

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_abs (*result, *this);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_not (JNIEnv *env, jobject obj)
{
  mpz_t *this;
  mpz_t *result;

  this = (mpz_t *) get_state (env, obj, table);

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_com (*result, *this);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_and (JNIEnv *env, jobject obj, 
					       jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);
  
  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_and (*result, *this, *value);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_andNot (JNIEnv *env, 
						  jobject obj, 
						  jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);
  
  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_com (*result, *value);
  mpz_and (*result, *result, *this);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_or (JNIEnv *env, jobject obj, 
					      jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);
  
  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_ior (*result, *this, *value);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_xor (JNIEnv *env, jobject obj,
					       jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;
  mpz_t tmp;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);
  
  mpz_init (tmp);
  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);

  /* ((this & (~value)) | ((~this) & value)) = (this ^ value) */
  mpz_com (tmp, *value);
  mpz_and (tmp, tmp, *this);

  mpz_com (*result, *this);
  mpz_and (*result, *result, *value);

  mpz_ior (*result, *result, tmp);

  mpz_clear (tmp);
  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jint JNICALL Java_java_math_BigInteger_getLowestSetBit
  (JNIEnv *env, jobject obj)
{
  mpz_t *this;

  this = (mpz_t *) get_state (env, obj, table);

  /* return -1, if this is 0 */
  return (mpz_sgn (*this) == 0) ? -1 : mpz_scan1 (*this, 0);
}

JNIEXPORT jint JNICALL Java_java_math_BigInteger_hashCode
  (JNIEnv *env, jobject obj)
{
  mpz_t *this;

  this = (mpz_t *) get_state (env, obj, table);
  return (jint) mpz_get_si (*this);
}

JNIEXPORT jstring JNICALL Java_java_math_BigInteger_toString
  (JNIEnv *env, jobject obj, jint radix)
{
  mpz_t *this;
  char *str = NULL;
  jstring ret;

  this = (mpz_t *) get_state (env, obj, table);

  if (radix < 2 || radix > 36)
    radix = 10;

  str = mpz_get_str (NULL, radix, *this);
  ret = (*env)->NewStringUTF (env, str);
  free (str);

  return ret;
} 

JNIEXPORT jdouble JNICALL Java_java_math_BigInteger_doubleValue
  (JNIEnv *env, jobject obj)
{
  mpz_t *this;
  mpz_t max_dbl;
  int cmp_val;

  this = (mpz_t *) get_state (env, obj, table);

  mpz_init_set_d (max_dbl, JDBL_MAX);
  cmp_val = mpz_cmp (*this, max_dbl);

  /* if we're larger than JDBL_MAX, return +infinity */
  if (cmp_val > 0)
    {
      mpz_clear (max_dbl);
      return HUGE_VAL;
    }

  mpz_neg (max_dbl, max_dbl);
  cmp_val = mpz_cmp (*this, max_dbl);
  mpz_clear (max_dbl);

  /* if we're more negative than -JDBL_MAX, return -infinity */
  if (cmp_val < 0)
    return -HUGE_VAL;

  return mpz_get_d (*this);
}
  
JNIEXPORT jlong JNICALL Java_java_math_BigInteger_longValue
  (JNIEnv *env, jobject obj)
{
  mpz_t *this;
  mpz_t result,num;
  jlong total = 0;
  int i, limbs_to_grab;
  
  this = (mpz_t *) get_state (env, obj, table);

  /* truncate values larger than MAX_LONG and MIN_LONG to MAX_LONG
     and MIN_LONG, respectively */
  if (mpz_sizeinbase (*this, 2) >= 64)
    return (mpz_sgn (*this) == 1) ? 
      ~((jlong)1 << 63) : (jlong)1 << 63;
 
  /* limbs are generally 32 bits, so we probably need to grab 2 limbs
     before we'll have enough data to fill up a 64 bit jlong */
  limbs_to_grab = sizeof (jlong) / sizeof (mp_limb_t);
  if (limbs_to_grab == 0)	/* word size of machine is > 64 bits */
    limbs_to_grab++;

  mpz_init (result);
  for (i = 0; i < limbs_to_grab; i++)
    {
      int bits = i*mp_bits_per_limb;

      mpz_tdiv_q_2exp (result, *this, bits);
      total |= ((jlong) mpz_get_ui (result)) << bits;
    }
  mpz_clear (result);

  return (mpz_sgn (num) == 1) ? total : -total;
}      

JNIEXPORT jint JNICALL Java_java_math_BigInteger_bitLength
  (JNIEnv *env, jobject obj)
{
  mpz_t *this;

  this = (mpz_t *) get_state (env, obj, table);
  return (mpz_sgn (*this) == 0) ? 0 : mpz_sizeinbase (*this, 2);
}

JNIEXPORT jint JNICALL Java_java_math_BigInteger_bitCount
  (JNIEnv *env, jobject obj)
{
  mpz_t *this;

  this = (mpz_t *) get_state (env, obj, table);

  /* for negative values, flip all the bits */
  if (mpz_sgn (*this) == -1)
    {
      mpz_t tmp;
      int ret_value;

      mpz_init (tmp);
      mpz_com (tmp, *this);
      ret_value = mpz_popcount (tmp);
      mpz_clear (tmp);
      return ret_value;
    }

  return mpz_popcount (*this);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_setBit
  (JNIEnv *env, jobject obj, jint n)
{
  mpz_t *this;
  mpz_t *result;

  if (n < 0)
    {
      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"attempt to set negative bit");
      return NULL;
    }

  this = (mpz_t *) get_state (env, obj, table);

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init_set (*result, *this);
  mpz_setbit (*result, n);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_clearBit
  (JNIEnv *env, jobject obj, jint n)
{
  mpz_t *this;
  mpz_t *result;

  if (n < 0)
    {
      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"attempt to clear negative bit");
      return NULL;
    }

  this = (mpz_t *) get_state (env, obj, table);

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init_set (*result, *this);
  mpz_clrbit (*result, n);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_flipBit
  (JNIEnv *env, jobject obj, jint n)
{
  mpz_t *this;
  mpz_t *result;

  if (n < 0)
    {
      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"attempt to flip negative bit");
      return NULL;
    }

  this = (mpz_t *) get_state (env, obj, table);

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init_set (*result, *this);
  (mpz_scan0 (*result, n) == n) ? mpz_setbit (*result, n) : 
                                  mpz_clrbit (*result, n);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jboolean JNICALL Java_java_math_BigInteger_testBit
  (JNIEnv *env, jobject obj, jint n)
{
  mpz_t *this;

  if (n < 0)
    {
      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"attempt to test negative bit");
      return JNI_FALSE;
    }

  this = (mpz_t *) get_state (env, obj, table);
  return (mpz_scan0 (*this, n) == n) ? JNI_FALSE : JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_java_math_BigInteger_nativeEquals
  (JNIEnv *env, jobject obj, jobject val)
{
  mpz_t *this;
  mpz_t *value;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);
  
  return (mpz_cmp (*this, *value) == 0) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_java_math_BigInteger_isProbablePrime
  (JNIEnv *env, jobject obj, jint certainty)
{
  mpz_t *this;
  int conv_certainty;

  this = (mpz_t *) get_state (env, obj, table);

  /* ln(.5) * certainty / ln(.25) */
  /* convert from (1/2 ** certainty) false positive to (1/4 ** certainty) */
  conv_certainty = (int) ceil (-.69314718055994530941 * certainty / 
			       -1.38629436111989061883);

  return (mpz_probab_prime_p (*this, conv_certainty)) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jint JNICALL Java_java_math_BigInteger_compareTo (JNIEnv *env, 
						  jobject obj, 
						  jobject val)
{
  mpz_t *this;
  mpz_t *value;
  int result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);

  result = mpz_cmp (*this, *value);
  if (result > 0) return 1;
  if (result < 0) return -1;
  return 0;
}

JNIEXPORT jint JNICALL Java_java_math_BigInteger_signum (JNIEnv *env, jobject obj)
{
  mpz_t *this;

  this = (mpz_t *) get_state (env, obj, table);
  return mpz_sgn (*this);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_negate (JNIEnv *env, jobject obj)
{
  mpz_t *this;
  mpz_t *result;

  this = (mpz_t *) get_state (env, obj, table);

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_neg (*result, *this);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_divide (JNIEnv *env, 
						  jobject obj, 
						  jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);

  if (mpz_sgn (*value) == 0)
    {
      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"divide by zero");
      return NULL;
    }

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_tdiv_q (*result, *this, *value);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_remainder (JNIEnv *env, 
						     jobject obj, 
						     jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);

  if (mpz_sgn (*value) == 0)
    {
      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"divide by zero");
      return NULL;
    }

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_tdiv_r (*result, *this, *value);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_pow (JNIEnv *env, 
					       jobject obj, 
					       jint exponent)
{
  mpz_t *this;
  mpz_t *result;

  if (exponent < 0)
    {
      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"exponent less than zero");
      return NULL;
    }

  this  = (mpz_t *) get_state (env, obj, table);

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_pow_ui (*result, *this, exponent);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_gcd (JNIEnv *env, 
					       jobject obj, 
					       jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_gcd (*result, *this, *value);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_modPow (JNIEnv *env, 
						  jobject obj, 
						  jobject exp_obj,
						  jobject m_obj)
{
  mpz_t *this;
  mpz_t *exp;
  mpz_t *m;
  mpz_t *result;

  this = (mpz_t *) get_state (env, obj, table);
  exp = (mpz_t *) get_state (env, exp_obj, table);
  m = (mpz_t *) get_state (env, m_obj, table);

  if (mpz_sgn (*m) <= 0)
    {
      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"mod undefined for less than or equals to zero");
      return NULL;
    }

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);

  if (mpz_sgn (*exp) == -1)	 /* special negative case */
    {
      mpz_t pow;
      int ret;

      mpz_init (pow);
      mpz_pow_ui (pow, *this, mpz_get_ui (*exp)); /* raise to exponent */

      /* perform a mod inverse */
      ret = mpz_invert (*result, pow, *m);
      mpz_clear (pow);

      if (ret == 0)		/* no multiplicative inverse mod m */
	{
	  mpz_clear (*result);	/* result is useless... */
	  free (result);	/* so get rid of it */

	  (*env)->ThrowNew (env,
			    (*env)->FindClass(env, 
					      "java/lang/ArithmeticException"),
			    "no multiplicative inverse mod m");
	  return NULL;
	}

      /* java doesn't allow negative results */
      if (mpz_sgn (*result) == -1)
	mpz_add (*result, *m, *result);
    }
  else
    mpz_powm (*result, *this, *exp, *m);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_modInverse
  (JNIEnv *env, jobject obj, jobject m_obj)
{
  mpz_t *this, *m, *result;
  int ret;

  this = (mpz_t *) get_state (env, obj, table);
  m    = (mpz_t *) get_state (env, m_obj, table);

  if (mpz_sgn (*m) <= 0)
    {
      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"mod undefined for less than or equals to zero");
      return NULL;
    }
  
  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);

  ret = mpz_invert (*result, *this, *m);

  if (ret == 0)			/* no multiplicative inverse mod m */
    {
      mpz_clear (*result);	/* result is useless... */
      free (result);		/* so get rid of it */

      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"no multiplicative inverse mod m");
      return NULL;
    }

  if (mpz_sgn (*result) == -1)  /* java spec does not allow a negative value */
    mpz_add (*result, *m, *result);
      
  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_mod
  (JNIEnv *env, jobject obj, jobject val)
{
  mpz_t *this;
  mpz_t *value;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);
  value = (mpz_t *) get_state (env, val, table);

  if (mpz_sgn (*value) <= 0)
    {
      (*env)->ThrowNew (env,
			(*env)->FindClass(env, 
					  "java/lang/ArithmeticException"),
			"mod undefined for less than or equals to zero");
      return NULL;
    }

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_mod (*result, *this, *value);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_shiftLeft
  (JNIEnv *env, jobject obj, jint n)
{
  mpz_t *this;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_mul_2exp (*result, *this, n);

  return mpz_t2obj (env, obj, result);
}

JNIEXPORT jobject JNICALL Java_java_math_BigInteger_shiftRight
  (JNIEnv *env, jobject obj, jint n)
{
  mpz_t *this;
  mpz_t *result;

  this  = (mpz_t *) get_state (env, obj, table);

  result = (mpz_t *) malloc (sizeof (mpz_t));
  mpz_init (*result);
  mpz_tdiv_q_2exp (*result, *this, n);

  return mpz_t2obj (env, obj, result);
}
