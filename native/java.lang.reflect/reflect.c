#include "reflect.h"
#include <primlib.h>
#include <string.h>
#include <jcl.h>
#include <vmi.h>

#ifndef NO_VMI
#include <jvmdi.h>
#endif

#define SAME_CLASS       1
#define SAME_PACKAGE     2
#define SUBCLASS         3
#define UNRELATED        4

static jint StartMethodSignature(JNIEnv * env, char * signatureBuf, jobjectArray argTypes);

JNIEXPORT jclass JNICALL REFLECT_GetCallerClass(JNIEnv * env, jint callerStackPos) {
#ifndef NO_VMI
	jframeID callerFrame;
	jclass callerClass;
	vmiError vmiErr;
	jvmdiError jvmdiErr;
	jint i;

	vmiErr = VMI_GetThisFrame(env, &callerFrame);
	if(vmiErr != VMI_ERROR_NONE) {
			VMI_ThrowAppropriateException(env, vmiErr);
			return NULL;
	}

	for(i=0;i<callerStackPos;i++) {
		jvmdiErr = JVMDI_GetCallerFrame(env, callerFrame, &callerFrame);
		if(jvmdiErr != JVMDI_ERROR_NONE) {
			VMI_ThrowAppropriateException(env, jvmdiErr);
			return NULL;
		}
	}

	vmiErr = VMI_GetFrameClass(env, callerFrame, &callerClass);
	if(jvmdiErr != JVMDI_ERROR_NONE) {
		VMI_ThrowAppropriateException(env, jvmdiErr);
		return NULL;
	}
	return callerClass;
#else
	return NULL;
#endif
}

JNIEXPORT jboolean JNICALL REFLECT_CallerHasAccess(JNIEnv * env, jclass accessee, jint memberMods, jint callerStackPos) {
#ifndef NO_VMI
	jclass callerClass;
	callerClass = REFLECT_GetCallerClass(env, callerStackPos);
	if(callerClass == NULL) {
		return JNI_FALSE;
	}
	return REFLECT_HasLinkLevelAccessToMember(env, callerClass, accessee, memberMods);
#else
	return TRUE;
#endif
}

JNIEXPORT jboolean JNICALL REFLECT_HasLinkLevelAccessToMember(JNIEnv * env, jclass accessor, jclass accessee, jint memberMods) {
#ifndef NO_VMI
	jstring accessorNameUTF;
	jstring accesseeNameUTF;
	char * accessorName;
	char * accesseeName;
	jint classMods;
	int lastAccessorDot;
	int lastAccesseeDot;
	int comparison = UNRELATED;
	int accessorLen;
	int accesseeLen;
	jvmdiError jvmdiErr;

	jvmdiErr = JVMDI_GetClassModifiers(env, accessee, &classMods);
	if(jvmdiErr != JVMDI_ERROR_NONE) {
		VMI_ThrowAppropriateException(env, jvmdiErr);
		return JNI_FALSE;
	}
	jvmdiErr = JVMDI_GetClassName(env, accessor, &accessorNameUTF);
	if(jvmdiErr != JVMDI_ERROR_NONE) {
		VMI_ThrowAppropriateException(env, jvmdiErr);
		return JNI_FALSE;
	}
	jvmdiErr = JVMDI_GetClassName(env, accessee, &accesseeNameUTF);
	if(jvmdiErr != JVMDI_ERROR_NONE) {
		VMI_ThrowAppropriateException(env, jvmdiErr);
		return JNI_FALSE;
	}

	accessorName = JCL_jstring_to_cstring(env, accessorNameUTF);
	if(accessorName == NULL) {
		return JNI_FALSE;
	}

	accesseeName = JCL_jstring_to_cstring(env, accesseeNameUTF);
	if(accesseeName == NULL) {
		return JNI_FALSE;
	}

	accessorLen = strlen(accessorName);
	accesseeLen = strlen(accesseeName);

	if(accessorLen == accesseeLen) {
		if(!strcmp(accessorName,accesseeName)) {
			comparison = SAME_CLASS;
		}
	}

	if(comparison == UNRELATED) {
		lastAccessorDot = strlen(accessorName) - 1;
		while(lastAccessorDot > 0) {
			if(accessorName[lastAccessorDot] == '.') {
				break;
			}
			lastAccessorDot--;
		}
		lastAccesseeDot = strlen(accesseeName) - 1;
		while(lastAccesseeDot > 0) {
			if(accesseeName[lastAccesseeDot] == '.') {
				break;
			}
			lastAccesseeDot--;
		}
		if(lastAccessorDot == lastAccesseeDot) {
			if(strncmp(accessorName,accesseeName,lastAccessorDot) == 0) {
				comparison = SAME_PACKAGE;
			}
		}
	}
	if(comparison == UNRELATED) {
		if((*env)->IsAssignableFrom(env, accessor, accessee)) {
			comparison = SUBCLASS;
		}
	}

	JCL_free_cstring(env, accessorNameUTF, accessorName);
	JCL_free_cstring(env, accesseeNameUTF, accesseeName);

	switch(comparison) {
		case SAME_CLASS:
			return JNI_TRUE;
		case SAME_PACKAGE:
			return !((classMods & VMI_MOD_PRIVATE) || (memberMods & VMI_MOD_PRIVATE));
		case SUBCLASS:
			return ((classMods & VMI_MOD_PROTECTED) || (classMods & VMI_MOD_PUBLIC))
				&& ((memberMods & VMI_MOD_PROTECTED) || (memberMods & VMI_MOD_PUBLIC));
		case UNRELATED:
		default:
			return (classMods & VMI_MOD_PUBLIC) && (memberMods & VMI_MOD_PUBLIC);
	}
#else
	return TRUE;
#endif
}

static jint StartMethodSignature(JNIEnv * env, char * signatureBuf, jobjectArray argTypes) {
	jint pos = 0;
	jint lengthOfFieldSig;
	jsize numArgs;
	jsize argIndex;
	jclass argType;

	signatureBuf[pos] = '(';
	pos++;

	numArgs = (*env)->GetArrayLength(env, argTypes);

	for(argIndex = 0; argIndex < numArgs; argIndex++) {
		argType = (*env)->GetObjectArrayElement(env, argTypes, argIndex);
		lengthOfFieldSig = REFLECT_GetFieldSignature(env, signatureBuf+pos, argType);
		if(lengthOfFieldSig == -1) {
			return -1;
		}
		pos += lengthOfFieldSig;
	}

	signatureBuf[pos] = ')';
	pos++;
	return pos;
}

JNIEXPORT jint JNICALL REFLECT_GetMethodSignature(JNIEnv * env, char * signatureBuf,
												  jobjectArray argTypes, jclass retType) {
	jint pos;
	jint lengthOfFieldSig;
	pos = StartMethodSignature(env, signatureBuf, argTypes);
	if(pos == -1) {
		return -1;
	}

	lengthOfFieldSig = REFLECT_GetFieldSignature(env, signatureBuf+pos, retType);
	if(lengthOfFieldSig == -1) {
		return -1;
	}
	pos += lengthOfFieldSig;
	return pos;
}

JNIEXPORT jint JNICALL REFLECT_GetConstructorSignature(JNIEnv * env, char * signatureBuf,
												  jobjectArray argTypes) {
	jint pos;
	pos = StartMethodSignature(env, signatureBuf, argTypes);
	if(pos == -1) {
		return -1;
	}
	signatureBuf[pos] = 'V';
	pos++;
	return pos;
}

JNIEXPORT jint JNICALL REFLECT_GetFieldSignature(JNIEnv * env, char * signatureBuf,
												 jclass fieldType) {
	jstring classNameUTF;
	char * className;
	jint reflectType;

	reflectType = PRIMLIB_GetReflectiveType(env, fieldType);

	switch(reflectType) {
	case PRIMLIB_BOOLEAN:
		signatureBuf[0] = 'Z';
		return 1;
	case PRIMLIB_BYTE:
		signatureBuf[0] = 'B';
		return 1;
	case PRIMLIB_CHAR:
		signatureBuf[0] = 'C';
		return 1;
	case PRIMLIB_SHORT:
		signatureBuf[0] = 'S';
		return 1;
	case PRIMLIB_INT:
		signatureBuf[0] = 'I';
		return 1;
	case PRIMLIB_LONG:
		signatureBuf[0] = 'J';
		return 1;
	case PRIMLIB_FLOAT:
		signatureBuf[0] = 'F';
		return 1;
	case PRIMLIB_DOUBLE:
		signatureBuf[0] = 'D';
		return 1;
	case PRIMLIB_VOID:
		signatureBuf[0] = 'V';
		return 1;
	case PRIMLIB_OBJECT:
		signatureBuf[0] = 'L';

#ifndef NO_VMI
		jvmdiErr = JVMDI_GetClassName(env, fieldType, &classNameUTF);
		if(jvmdiErr != JVMDI_ERROR_NONE) {
			VMI_ThrowAppropriateException(env, jvmdiErr);
			return -1;
		}
#else
		{
			jclass _c = (*env)->FindClass(env, "java/lang/Class");
			jmethodID _m = (*env)->GetMethodID(env, _c, "getName", "()java/lang/String");
			classNameUTF = (jstring)(*env)->CallObjectMethod(env, fieldType, _m);
		}
#endif

		className = JCL_jstring_to_cstring(env, classNameUTF);
		if(className == NULL) {
			return -1;
		}
		strcpy(signatureBuf+1,className);
		strcat(signatureBuf,";");
		return strlen(signatureBuf);
	case PRIMLIB_NULL:
	default:
		JCL_ThrowException(env, "java/lang/NullPointerException", "Null class in REFLECT_GetFieldSignature()");
		return -1;
	}
}

