/*
 * java.lang.reflect.Method native functions.
 * Author: John Keiser
 * Version: 1.1.0
 * Date: 2 Jun 1998
 */

#include "java_lang_reflect_Method.h"
#include "reflect.h"
#include <jcl.h>
#include <vmi.h>
#include <primlib.h>
#include <native_state.h>

#include <malloc.h>

/** Native State functions **/

static struct state_table *table;

static jvalue * DoInitialCheckingAndConverting(JNIEnv * env, jobject invokeObj, jobjectArray args,
											   jclass declaringClass, jobjectArray targetArgTypes,
											   jint modifiers, jint argLength);
static jmethodID GetTheMethodID(JNIEnv * env, jobject thisObj, jclass declarer, jstring name,
									  jobjectArray targetArgTypes, jclass retType, int isStatic);

/*
Invoking the method:
N    If the calling class does not have link-level access to the method:
         Throw IllegalAccessException.
     If the method is not static:
         If the object is null:
             Throw NullPointerException.
N        If the object is not assignment-compatible (instanceof) with the Method's declaringClass:
             Throw IllegalArgumentException.
J    If the argument lists are different lengths:
         Throw IllegalArgumentException.
     For each argument in actualArgs:
J       If class of correspondng targetArgTypes is primitive:
N           Convert the wrapped argument to the primitive type using widening or identity conversion.
            If a widening or identity conversion could not occur, or if the object is null:
                Throw IllegalArgumentException.
J       Else:
N           If the class of the argument is not assignment-compatible (instanceof) with the corresponding targetArgType:
                Throw IllegalArgumentException.

    If the method is static,
N       Determine the methodID for the declaringClass, name and signature.
N       Invoke the appropriate static method based on the return type of the Method and and Method's declaringClass.
    Else
N       Determine the methodID for the object's class, and the Method's name and signature.
N       Invoke the appropriate static method, if the return type is primitive, wrap it.
    If an exception was thrown, return from invoke().
J   If the return value is a primitive type, wrap it in the appropriate wrapper type.


Java values to pass:
   object -- the object to invoke on
   actualArgs -- the arguments

   class -- the declaringClass
   methodName -- the name of the method
   argLengthsDifferent -- whether the argument lengths are different
   actualArgReflectiveTypes -- ints representing the types of the actualArgTypes
   actualArgTypes -- the actual classes representing the types of the method arguments

Native values to store:
   if(static) methodID -- the methodID
   else char*[2] argInfo -- the name and signature of the method as char*'s

*/

/* Initial checking portion:
N    If the calling class does not have link-level access to the method:
         Throw IllegalAccessException.
     If the method is not static:
         If the object is null:
             Throw NullPointerException.
N        If the object is not assignment-compatible (instanceof) with the Method's declaringClass:
             Throw IllegalArgumentException.
J    If the argument lists are different lengths:
         Throw IllegalArgumentException.
     For each argument in actualArgs:
J       If class of correspondng targetArgTypes is primitive:
N           Convert the wrapped argument to the primitive type using widening or identity conversion.
            If a widening or identity conversion could not occur, or if the object is null:
                Throw IllegalArgumentException.
J       Else:
N           If the class of the argument is not assignment-compatible (instanceof) with the corresponding targetArgType:
                Throw IllegalArgumentException.
*/
static jvalue * DoInitialCheckingAndConverting(JNIEnv * env, jobject invokeObj, jobjectArray args,
	jclass declaringClass, jobjectArray targetArgTypes, jint modifiers, jint argLength) {

	jvalue * retval;

	jobject obj;
	jclass c;

	jsize argNum;

	if(!REFLECT_CallerHasAccess(env, declaringClass, modifiers, 2)) {
		JCL_ThrowException(env, "java/lang/IllegalAccessException", "Cannot access reflected Method");
		return NULL;
	}

	if(!(modifiers & VMI_MOD_STATIC)) {
		if(invokeObj == NULL) {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Object to invoke on is null");
			return NULL;
		}
		if(!(*env)->IsInstanceOf(env, invokeObj, declaringClass)) {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Object to invoke on is not of appropriate type");
			return NULL;
		}
	}

	if(argLength == -1) {
		JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Incorrect number of arguments");
		return NULL;
	}

	if(argLength == 0) {
		return NULL;
	}

	retval = JCL_malloc(env, sizeof(jvalue) * argLength);
	if(retval == NULL) {
		return NULL;
	}

	for(argNum = 0; argNum < argLength; argNum++) {
		obj = (*env)->GetObjectArrayElement(env, args, argNum);
		c = (jclass)(*env)->GetObjectArrayElement(env, targetArgTypes, argNum);
		retval[argNum] = PRIMLIB_UnwrapJValue(env, obj, c);
		if((*env)->ExceptionOccurred(env)) {
			return NULL;
		}
	}
	return retval;
}



static jmethodID GetTheMethodID(JNIEnv * env, jobject thisObj, jclass declarer, jstring name, jobjectArray targetArgTypes, jclass retType, int isStatic) {
	char * nameUTF;
	char * signature;
	jmethodID m;

	if(JCL_MonitorEnter(env, thisObj) != 0) {
		return NULL;
	}

	m = (jmethodID)get_state(env, thisObj, table);

	if(m == NULL) {
		nameUTF = JCL_jstring_to_cstring(env, name);
		if(nameUTF == NULL) {
			JCL_MonitorExit(env,thisObj);
			return NULL;
		}

		signature = JCL_malloc(env, sizeof(char) * MAX_SIGNATURE_SIZE);
		if(signature == NULL) {
			JCL_free_cstring(env, name, nameUTF);
			JCL_MonitorExit(env,thisObj);
			return NULL;
		}

		if(REFLECT_GetMethodSignature(env, signature, targetArgTypes, retType) == -1) {
			JCL_free(env, signature);
			JCL_free_cstring(env, name, nameUTF);
			JCL_MonitorExit(env,thisObj);
			JCL_ThrowException(env, "java/lang/NullPointerException", "Null class in argTypes[]");
			return NULL;
		}
		if(isStatic) {
			m = (*env)->GetStaticMethodID(env, declarer, nameUTF, signature);
		} else {
			m = (*env)->GetMethodID(env, declarer, nameUTF, signature);
		}

		JCL_free_cstring(env, name, nameUTF);
		JCL_free(env,signature);

		set_state(env, thisObj, table, m);
	}

	if(JCL_MonitorExit(env, thisObj) != 0) {
		return NULL;
	}

	return m;
}

/*
Checking for link-level access:
    If the method is public, link-level access is always granted.
    If the method is protected, the calling class must be assignment-compatible with the method's class, or it must
        have the same package.
    If the method is package-protected, the calling class must have the same package as the method's class.
    If the method is private, the calling class must be the same as the method's class.
*/


/*
 * Class:     java_lang_reflect_Method
 * Method:    initNativeState
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Method_initNativeState (JNIEnv * env, jclass clazz) {
  /* create the table to hold C state, for each instance of this class */
	table = init_state_table (env, clazz);
}

/*
 * Class:     java_lang_reflect_Method
 * Method:    finalizeNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Method_finalizeNative(JNIEnv *env, jobject obj) {
	remove_state_slot (env, obj, table);
}



/*
 * Class:     java_lang_reflect_Method
 * Method:    invokeNative
 * Signature: (Ljava/lang/Object;[Ljava/lang/Object;Ljava/lang/Class;Ljava/lang/String;ILjava/lang/Class;[Ljava/lang/Class;I)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_java_lang_reflect_Method_invokeNative
  (JNIEnv * env, jobject thisObj, jobject invokeObj, jobjectArray args,
	jclass declarer, jstring name, jint modifiers,
	jclass returnType, jobjectArray parameterTypes,
	jint length) {

	jvalue * argVals;
	jint type;
	jmethodID m;

	jboolean z;
	jbyte b;
	jshort s;
	jchar c;
	jint i;
	jlong j;
	jfloat f;
	jdouble d;

	argVals = DoInitialCheckingAndConverting(env, invokeObj, args, declarer, parameterTypes, modifiers, length);

	if((*env)->ExceptionOccurred(env)) {
		return NULL;
	}

	type = PRIMLIB_GetReflectiveType(env, returnType);

	if(modifiers & VMI_MOD_STATIC) {
		m = GetTheMethodID(env, thisObj, declarer, name, parameterTypes, returnType,TRUE);
		JCL_RETHROW_EXCEPTION(env);
		switch(type) {
			case PRIMLIB_BOOLEAN:
				z = (*env)->CallStaticBooleanMethod(env, declarer, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapBoolean(env, z);
			case PRIMLIB_BYTE:
				b = (*env)->CallStaticByteMethod(env, declarer, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapByte(env, b);
			case PRIMLIB_CHAR:
				c = (*env)->CallStaticCharMethod(env, declarer, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapChar(env, c);
			case PRIMLIB_SHORT:
				s = (*env)->CallStaticShortMethod(env, declarer, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapShort(env, s);
			case PRIMLIB_INT:
				i = (*env)->CallStaticIntMethod(env, declarer, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapInt(env, i);
			case PRIMLIB_LONG:
				j = (*env)->CallStaticLongMethod(env, declarer, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapLong(env, j);
			case PRIMLIB_FLOAT:
				f = (*env)->CallStaticFloatMethod(env, declarer, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapFloat(env, f);
			case PRIMLIB_DOUBLE:
				d = (*env)->CallStaticDoubleMethod(env, declarer, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapDouble(env, d);
			case PRIMLIB_VOID:
				(*env)->CallStaticVoidMethod(env, declarer, m, argVals);
				return NULL;
			case PRIMLIB_OBJECT:
				return (*env)->CallStaticObjectMethod(env, declarer, m, argVals);
			default:
				JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Unknown primitive type!  Internal error.");
				return NULL;
		}
	} else {
		m = GetTheMethodID(env, thisObj, declarer, name, parameterTypes, returnType,FALSE);
		JCL_RETHROW_EXCEPTION(env);
		switch(type) {
			case PRIMLIB_BOOLEAN:
				z = (*env)->CallBooleanMethod(env, invokeObj, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapBoolean(env, z);
			case PRIMLIB_BYTE:
				b = (*env)->CallByteMethod(env, invokeObj, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapByte(env, b);
			case PRIMLIB_CHAR:
				c = (*env)->CallCharMethod(env, invokeObj, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapChar(env, c);
			case PRIMLIB_SHORT:
				s = (*env)->CallShortMethod(env, invokeObj, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapShort(env, s);
			case PRIMLIB_INT:
				i = (*env)->CallIntMethod(env, invokeObj, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapInt(env, i);
			case PRIMLIB_LONG:
				j = (*env)->CallLongMethod(env, invokeObj, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapLong(env, j);
			case PRIMLIB_FLOAT:
				f = (*env)->CallFloatMethod(env, invokeObj, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapFloat(env, f);
			case PRIMLIB_DOUBLE:
				(*env)->CallDoubleMethod(env, invokeObj, m, argVals);
				JCL_RETHROW_EXCEPTION(env);
				return PRIMLIB_WrapDouble(env, d);
			case PRIMLIB_VOID:
				(*env)->CallVoidMethod(env, invokeObj, m, argVals);
				return NULL;
			case PRIMLIB_OBJECT:
				return (*env)->CallObjectMethod(env, invokeObj, m, argVals);
			default:
				JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Unknown primitive type!  Internal error.");
				return NULL;
		}
	}
}

