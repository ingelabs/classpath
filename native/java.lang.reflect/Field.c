#include "Field.h"
#include "reflect.h"
#include <primlib.h>
#include <native_state.h>
#include <jcl.h>
#include <vmi.h>

#include <malloc.h>
static struct state_table* table;

static jfieldID GetTheFieldID(JNIEnv * env, jobject thisObj,
									jstring name, jclass declarer, jclass type, int isStatic);

static void DoInitialChecking(JNIEnv * env, jobject invokeObj,
								  jclass declaringClass, jint modifiers);

/*
 * Class:     java_lang_reflect_Field
 * Method:    initNativeState
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_initNativeState
(JNIEnv * env, jclass thisClass) {
  /* create the table to hold C state, for each instance of this class */
	table = init_state_table (env, thisClass);
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    finalizeNative
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_finalizeNative
(JNIEnv * env, jobject thisObj) {
	remove_state_slot (env, thisObj, table);
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    getNative
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_java_lang_reflect_Field_getNative
(JNIEnv * env, jobject thisObj, jobject getObj, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,getObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return NULL;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BOOLEAN))) {
			return PRIMLIB_WrapBoolean(env, (*env)->GetStaticBooleanField(env, declaringClass, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return PRIMLIB_WrapByte(env, (*env)->GetStaticByteField(env, declaringClass, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return PRIMLIB_WrapChar(env, (*env)->GetStaticCharField(env, declaringClass, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return PRIMLIB_WrapShort(env, (*env)->GetStaticShortField(env, declaringClass, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			return PRIMLIB_WrapInt(env, (*env)->GetStaticIntField(env, declaringClass, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			return PRIMLIB_WrapLong(env, (*env)->GetStaticLongField(env, declaringClass, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			return PRIMLIB_WrapFloat(env, (*env)->GetStaticFloatField(env, declaringClass, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			return PRIMLIB_WrapDouble(env, (*env)->GetStaticDoubleField(env, declaringClass, f));
		} else {
			return (*env)->GetStaticObjectField(env, declaringClass, f);
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BOOLEAN))) {
			return PRIMLIB_WrapBoolean(env, (*env)->GetBooleanField(env, getObj, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return PRIMLIB_WrapByte(env, (*env)->GetByteField(env, getObj, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return PRIMLIB_WrapChar(env, (*env)->GetCharField(env, getObj, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return PRIMLIB_WrapShort(env, (*env)->GetShortField(env, getObj, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			return PRIMLIB_WrapInt(env, (*env)->GetIntField(env, getObj, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			return PRIMLIB_WrapLong(env, (*env)->GetLongField(env, getObj, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			return PRIMLIB_WrapFloat(env, (*env)->GetFloatField(env, getObj, f));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			return PRIMLIB_WrapDouble(env, (*env)->GetDoubleField(env, getObj, f));
		} else {
			return (*env)->GetObjectField(env, getObj, f);
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    getBooleanNative
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)Z
 */
JNIEXPORT jboolean JNICALL Java_java_lang_reflect_Field_getBooleanNative
(JNIEnv * env, jobject thisObj, jobject getObj, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,getObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return JNI_FALSE;
	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BOOLEAN))) {
			return (*env)->GetStaticBooleanField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Tried to access non-boolean field as boolean.");
			return JNI_FALSE;
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BOOLEAN))) {
			return (*env)->GetBooleanField(env, getObj, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Tried to access non-boolean field as boolean.");
			return JNI_FALSE;
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    getByteNative
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)B
 */
JNIEXPORT jbyte JNICALL Java_java_lang_reflect_Field_getByteNative
(JNIEnv * env, jobject thisObj, jobject getObj, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,getObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return 0;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (*env)->GetStaticByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Tried to access non-byte field as byte.");
			return 0;
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (*env)->GetByteField(env, getObj, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Tried to access non-byte field as byte.");
			return 0;
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    getCharNative
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)C
 */
JNIEXPORT jchar JNICALL Java_java_lang_reflect_Field_getCharNative
(JNIEnv * env, jobject thisObj, jobject getObj, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,getObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return 0;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return (*env)->GetStaticCharField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Tried to access non-char field as char.");
			return 0;
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return (*env)->GetCharField(env, getObj, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Tried to access non-char field as char.");
			return 0;
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    getShortNative
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)S
 */
JNIEXPORT jshort JNICALL Java_java_lang_reflect_Field_getShortNative
(JNIEnv * env, jobject thisObj, jobject getObj, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,getObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return 0;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return (*env)->GetStaticShortField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (*env)->GetStaticByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert field to double.");
			return 0;
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return (*env)->GetShortField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (*env)->GetByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert field to double.");
			return 0;
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    getIntNative
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)I
 */
JNIEXPORT jint JNICALL Java_java_lang_reflect_Field_getIntNative
(JNIEnv * env, jobject thisObj, jobject getObj, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,getObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return 0;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			return (*env)->GetStaticIntField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return (*env)->GetStaticCharField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return (*env)->GetStaticShortField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (*env)->GetStaticByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert field to double.");
			return 0;
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			return (*env)->GetIntField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return (*env)->GetCharField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return (*env)->GetShortField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (*env)->GetByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert field to double.");
			return 0;
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    getLongNative
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)J
 */
JNIEXPORT jlong JNICALL Java_java_lang_reflect_Field_getLongNative
(JNIEnv * env, jobject thisObj, jobject getObj, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,getObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return 0;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			return (*env)->GetStaticLongField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			return (*env)->GetStaticIntField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return (*env)->GetStaticCharField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return (*env)->GetStaticShortField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (*env)->GetStaticByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert field to double.");
			return 0;
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			return (*env)->GetLongField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			return (*env)->GetIntField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return (*env)->GetCharField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return (*env)->GetShortField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (*env)->GetByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert field to double.");
			return 0;
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    getFloatNative
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)F
 */
JNIEXPORT jfloat JNICALL Java_java_lang_reflect_Field_getFloatNative
(JNIEnv * env, jobject thisObj, jobject getObj, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,getObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return 0;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			return (*env)->GetStaticFloatField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			return (jfloat)(*env)->GetStaticLongField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			return (jfloat)(*env)->GetStaticIntField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return (jfloat)(*env)->GetStaticCharField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return (jfloat)(*env)->GetStaticShortField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (jfloat)(*env)->GetStaticByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert field to double.");
			return 0;
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			return (*env)->GetFloatField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			return (jfloat)(*env)->GetLongField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			return (jfloat)(*env)->GetIntField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return (jfloat)(*env)->GetCharField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return (jfloat)(*env)->GetShortField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (jfloat)(*env)->GetByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert field to double.");
			return 0;
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    getDoubleNative
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)D
 */
JNIEXPORT jdouble JNICALL Java_java_lang_reflect_Field_getDoubleNative
(JNIEnv * env, jobject thisObj, jobject getObj, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,getObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return 0;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			return (*env)->GetStaticDoubleField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			return (jdouble)(*env)->GetStaticFloatField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			return (jdouble)(*env)->GetStaticLongField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			return (jdouble)(*env)->GetStaticIntField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return (jdouble)(*env)->GetStaticCharField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return (jdouble)(*env)->GetStaticShortField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (jdouble)(*env)->GetStaticByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert field to double.");
			return 0;
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			return (*env)->GetDoubleField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			return (jdouble)(*env)->GetFloatField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			return (jdouble)(*env)->GetLongField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			return (jdouble)(*env)->GetIntField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			return (jdouble)(*env)->GetCharField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			return (jdouble)(*env)->GetShortField(env, declaringClass, f);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			return (jdouble)(*env)->GetByteField(env, declaringClass, f);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert field to double.");
			return 0;
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    setNative
 * Signature: (Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_setNative
(JNIEnv * env, jobject thisObj, jobject setObj, jobject val, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,setObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BOOLEAN))) {
			(*env)->SetStaticBooleanField(env, declaringClass, f, PRIMLIB_UnwrapBoolean(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BYTE))) {
			(*env)->SetStaticByteField(env, declaringClass, f, PRIMLIB_UnwrapByte(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_CHAR))) {
			(*env)->SetStaticCharField(env, declaringClass, f, PRIMLIB_UnwrapChar(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_SHORT))) {
			(*env)->SetStaticShortField(env, declaringClass, f, PRIMLIB_UnwrapShort(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_INT))) {
			(*env)->SetStaticIntField(env, declaringClass, f, PRIMLIB_UnwrapInt(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_LONG))) {
			(*env)->SetStaticLongField(env, declaringClass, f, PRIMLIB_UnwrapLong(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetStaticFloatField(env, declaringClass, f, PRIMLIB_UnwrapFloat(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetStaticDoubleField(env, declaringClass, f, PRIMLIB_UnwrapDouble(env, val));
		} else {
			(*env)->SetStaticObjectField(env, declaringClass, f, val);
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BOOLEAN))) {
			(*env)->SetBooleanField(env, setObj, f, PRIMLIB_UnwrapBoolean(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BYTE))) {
			(*env)->SetByteField(env, setObj, f, PRIMLIB_UnwrapByte(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_CHAR))) {
			(*env)->SetCharField(env, setObj, f, PRIMLIB_UnwrapChar(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_SHORT))) {
			(*env)->SetShortField(env, setObj, f, PRIMLIB_UnwrapShort(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_INT))) {
			(*env)->SetIntField(env, setObj, f, PRIMLIB_UnwrapInt(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_LONG))) {
			(*env)->SetLongField(env, setObj, f, PRIMLIB_UnwrapLong(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetFloatField(env, setObj, f, PRIMLIB_UnwrapFloat(env, val));
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetDoubleField(env, setObj, f, PRIMLIB_UnwrapDouble(env, val));
		} else {
			(*env)->SetObjectField(env, setObj, f, val);
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    setBooleanNative
 * Signature: (Ljava/lang/Object;ZLjava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_setBooleanNative
(JNIEnv * env, jobject thisObj, jobject setObj, jboolean val, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,setObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BOOLEAN))) {
			(*env)->SetStaticBooleanField(env, declaringClass, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Tried to access non-boolean field as a boolean.");
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BOOLEAN))) {
			(*env)->SetBooleanField(env, setObj, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Tried to access non-boolean field as a boolean.");
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    setByteNative
 * Signature: (Ljava/lang/Object;BLjava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_setByteNative
(JNIEnv * env, jobject thisObj, jobject setObj, jbyte val,jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,setObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetStaticDoubleField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetStaticFloatField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			(*env)->SetStaticLongField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			(*env)->SetStaticIntField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			(*env)->SetStaticShortField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			(*env)->SetStaticByteField(env, declaringClass, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert byte to field type.");
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetDoubleField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetFloatField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			(*env)->SetLongField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			(*env)->SetIntField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			(*env)->SetShortField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			(*env)->SetCharField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE))) {
			(*env)->SetByteField(env, setObj, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert byte to field type.");
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    setCharNative
 * Signature: (Ljava/lang/Object;CLjava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_setCharNative
(JNIEnv * env, jobject thisObj, jobject setObj, jchar val, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,setObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetStaticDoubleField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetStaticFloatField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			(*env)->SetStaticLongField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			(*env)->SetStaticIntField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			(*env)->SetStaticCharField(env, declaringClass, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert char to field type.");
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetDoubleField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetFloatField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			(*env)->SetLongField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			(*env)->SetIntField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR))) {
			(*env)->SetCharField(env, setObj, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert char to field type.");
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    setShortNative
 * Signature: (Ljava/lang/Object;SLjava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_setShortNative
(JNIEnv * env, jobject thisObj, jobject setObj, jshort val, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,setObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetStaticDoubleField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetStaticFloatField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			(*env)->SetStaticLongField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			(*env)->SetStaticIntField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			(*env)->SetStaticShortField(env, declaringClass, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert short to field type.");
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetDoubleField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetFloatField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			(*env)->SetLongField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			(*env)->SetIntField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT))) {
			(*env)->SetShortField(env, setObj, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert short to field type.");
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    setIntNative
 * Signature: (Ljava/lang/Object;ILjava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_setIntNative
(JNIEnv * env, jobject thisObj, jobject setObj, jint val, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,setObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetStaticDoubleField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetStaticFloatField(env, declaringClass, f, (jfloat)val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			(*env)->SetStaticLongField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			(*env)->SetStaticIntField(env, declaringClass, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert int to field type.");
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetDoubleField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetFloatField(env, setObj, f, (jfloat)val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			(*env)->SetLongField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT))) {
			(*env)->SetIntField(env, setObj, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert int to field type.");
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    setLongNative
 * Signature: (Ljava/lang/Object;JLjava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_setLongNative
(JNIEnv * env, jobject thisObj, jobject setObj, jlong val, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,setObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetStaticDoubleField(env, declaringClass, f, (jdouble)val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetStaticFloatField(env, declaringClass, f, (jfloat)val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			(*env)->SetStaticLongField(env, declaringClass, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert long to field type.");
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetDoubleField(env, setObj, f, (jdouble)val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetFloatField(env, setObj, f, (jfloat)val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG))) {
			(*env)->SetLongField(env, setObj, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert long to field type.");
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    setFloatNative
 * Signature: (Ljava/lang/Object;FLjava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_setFloatNative
(JNIEnv * env, jobject thisObj, jobject setObj, jfloat val, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,setObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetStaticDoubleField(env, declaringClass, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetStaticFloatField(env, declaringClass, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert float to field type.");
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetDoubleField(env, setObj, f, val);
		} else if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT))) {
			(*env)->SetFloatField(env, setObj, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert float to field type.");
		}
	}
}

/*
 * Class:     java_lang_reflect_Field
 * Method:    setDoubleNative
 * Signature: (Ljava/lang/Object;DLjava/lang/String;Ljava/lang/Class;Ljava/lang/Class;I)V
 */
JNIEXPORT void JNICALL Java_java_lang_reflect_Field_setDoubleNative
(JNIEnv * env, jobject thisObj, jobject setObj, jdouble val, jstring fieldName, jclass declaringClass, jclass type, jint modifiers) {
	jfieldID f;
	DoInitialChecking(env,setObj,declaringClass,modifiers);
	if((*env)->ExceptionOccurred(env))
		return;

	if(modifiers & VMI_MOD_STATIC) {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,TRUE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetStaticDoubleField(env, declaringClass, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert double to field type.");
		}
	} else {
		f = GetTheFieldID(env,thisObj,fieldName,declaringClass,type,FALSE);
		if((*env)->IsAssignableFrom(env, type, PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE))) {
			(*env)->SetDoubleField(env, setObj, f, val);
		} else {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Cannot convert double to field type.");
		}
	}
}




static jfieldID GetTheFieldID(JNIEnv * env, jobject thisObj, jstring name, jclass declarer, jclass type, int isStatic) {
	char * nameUTF;
	char * signature;
	jfieldID f;

	if(JCL_MonitorEnter(env, thisObj) != 0) {
		return NULL;
	}

	f = (jfieldID)get_state(env, thisObj, table);

	if(f == NULL) {
		nameUTF = JCL_jstring_to_cstring(env, name);
		if(nameUTF == NULL) {
			JCL_MonitorExit(env, thisObj);
			return NULL;
		}

		signature = JCL_malloc(env, sizeof(char) * MAX_SIGNATURE_SIZE);
		if(signature == NULL) {
			JCL_free_cstring(env, name, nameUTF);
			JCL_MonitorExit(env, thisObj);
			return NULL;
		}

		if(REFLECT_GetFieldSignature(env, signature, type) == -1) {
			JCL_free_cstring(env, name, nameUTF);
			JCL_free(env,signature);
			JCL_MonitorExit(env, thisObj);
			JCL_ThrowException(env, "java/lang/NullPointerException", "Null class in argTypes[]");
			return NULL;
		}
		if(isStatic) {
			f = (*env)->GetStaticFieldID(env, declarer, nameUTF, signature);
		} else {
			f = (*env)->GetFieldID(env, declarer, nameUTF, signature);
		}

		JCL_free_cstring(env, name, nameUTF);
		JCL_free(env,signature);

		set_state(env, thisObj, table, f);
	}

	if(JCL_MonitorExit(env, thisObj) != 0) {
		return NULL;
	}

	return f;
}

static void DoInitialChecking(JNIEnv * env, jobject invokeObj,
								  jclass declaringClass, jint modifiers) {
	if(!REFLECT_CallerHasAccess(env, declaringClass, modifiers, 2)) {
		JCL_ThrowException(env, "java/lang/IllegalAccessException", "Cannot access reflected Method");
		return;
	}

	if(!(modifiers & VMI_MOD_STATIC)) {
		if(invokeObj == NULL) {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Object to invoke on is null");
			return;
		}
		if(!(*env)->IsInstanceOf(env, invokeObj, declaringClass)) {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Object to invoke on is not of appropriate type");
			return;
		}
	}
	return;
}
