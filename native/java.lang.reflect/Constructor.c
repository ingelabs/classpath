/*
 * java.lang.reflect.Constructor native functions.
 * Author: John Keiser
 * Version: 1.1.0
 * Date: 2 Jun 1998
 */

#include "Constructor.h"
#include "reflect.h"
#include <jcl.h>
#include <native_state.h>
#include <jnilink.h>
#include <vmi.h>
#include <primlib.h>

#include <malloc.h>

static struct state_table * table;

static jmethodID
GetTheMethodID(JNIEnv * env, jobject thisObj, jclass declarer,
				jstring name, jobjectArray targetArgTypes);

static jmethodID
GetTheMethodID(JNIEnv * env, jobject thisObj, jclass declarer,
				jstring name, jobjectArray targetArgTypes) {
	linkPtr l;
	char * signature;
	jmethodID m;

	if(JCL_MonitorEnter(env, thisObj) != 0) {
		return NULL;
	}

	l = (linkPtr)get_state(env, thisObj, table);

	if(l == NULL) {
		signature = JCL_malloc(env, sizeof(char) * MAX_SIGNATURE_SIZE);
		if(signature == NULL) {
			return NULL;
		}

		if(REFLECT_GetConstructorSignature(env, signature, targetArgTypes) == -1) {
			return NULL;
		}
		if(LINK_LinkConstructor(env, &l, declarer, signature) == NULL) {
			return NULL;
		}

		free(signature);

		set_state(env, thisObj, table, l);
	}

	if(JCL_MonitorExit(env, thisObj) != 0) {
		return NULL;
	}

	m = LINK_ResolveMethod(env, l);
	return m;
}

static jvalue * DoInitialCheckingAndConverting(JNIEnv * env, jobjectArray args,
	jclass declaringClass, jobjectArray targetArgTypes, jint modifiers, jint argLength) {

	jvalue * retval;

	jobject obj;
	jclass c;

	jsize argNum;

	if(!REFLECT_CallerHasAccess(env, declaringClass, modifiers, 2)) {
		JCL_ThrowException(env, "java/lang/IllegalAccessException", "Cannot access reflected Method");
		return NULL;
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


/*
 * Class:     java_lang_reflect_Constructor
 * Method:    constructNative
 * Signature: ([Ljava/lang/Object;Ljava/lang/Class;Ljava/lang/String;I[Ljava/lang/Class;I)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL
Java_java_lang_reflect_Constructor_constructNative
(JNIEnv * env, jobject thisObj, jobjectArray args, jclass declarer, jstring name, jint modifiers, jobjectArray parameterTypes, jint length) {
	jvalue * argVals;
	jmethodID m;

	argVals = DoInitialCheckingAndConverting(env, args, declarer, parameterTypes, modifiers, length);

	if((*env)->ExceptionOccurred(env)) {
		return NULL;
	}

	m = GetTheMethodID(env, thisObj, declarer, name, parameterTypes);
	if(m == NULL)
		return NULL;

	return (*env)->NewObject(env, declarer, m, argVals);
}


/** Native State functions **/

JNIEXPORT void JNICALL Java_java_lang_reflect_Constructor_initNativeState (JNIEnv * env, jclass clazz) {
  /* create the table to hold C state, for each instance of this class */
	table = init_state_table (env, clazz);
}

JNIEXPORT void JNICALL Java_java_lang_reflect_Constructor_finalizeNative(JNIEnv *env, jobject obj) {
	jmethodID* m;
	m = (jmethodID*) remove_state_slot (env, obj, table);
	free(m);
}

