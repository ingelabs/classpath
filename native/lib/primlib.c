#include <jnilink.h>
#include <primlib.h>
#include <jcl.h>

static linkPtr nativeWrapClass[PRIMLIB_NUMTYPES] = {NULL,NULL,NULL, NULL,NULL,NULL,
						NULL,NULL,NULL, NULL,NULL,NULL};

static linkPtr nativeTypeClass[PRIMLIB_NUMTYPES] = {NULL,NULL,NULL, NULL,NULL,NULL,
						NULL,NULL,NULL, NULL,NULL,NULL};

static linkPtr nativeWrapClassConstructor[PRIMLIB_NUMTYPES] = {NULL,NULL,NULL, NULL,NULL,NULL,
						NULL,NULL,NULL, NULL,NULL,NULL};

static linkPtr nativeWrapClassAccessor[PRIMLIB_NUMTYPES] = {NULL,NULL,NULL, NULL,NULL,NULL,
						NULL,NULL,NULL, NULL,NULL,NULL};

static char * nativeWrapClassName[PRIMLIB_NUMTYPES] = {
							NULL,
							NULL,
							"java/lang/Boolean",
							"java/lang/Byte",
							"java/lang/Character",
							"java/lang/Short",
							"java/lang/Integer",
							"java/lang/Long",
							"java/lang/Float",
							"java/lang/Double",
							"java/lang/Void",
							NULL
							};

static char * nativeWrapClassConstructorSig[PRIMLIB_NUMTYPES] = {
							NULL,
							NULL,
							"(Z)V",
							"(B)V",
							"(C)V",
							"(S)V",
							"(I)V",
							"(J)V",
							"(F)V",
							"(D)V",
							"()V",
							NULL
							};

static char * nativeWrapClassAccessorName[PRIMLIB_NUMTYPES] = {
							NULL,
							NULL,
							"booleanValue",
							"byteValue",
							"charValue",
							"shortValue",
							"intValue",
							"longValue",
							"floatValue",
							"doubleValue",
							NULL,
							NULL
};

static char * nativeWrapClassAccessorSig[PRIMLIB_NUMTYPES] = {
							NULL,
							NULL,
							"()Z",
							"()B",
							"()C",
							"()S",
							"()I",
							"()J",
							"()F",
							"()D",
							NULL,
							NULL
};


JNIEXPORT jclass JNICALL PRIMLIB_GetNativeWrapClass(JNIEnv * env, int reflectType) {
	linkPtr retval = nativeWrapClass[reflectType];
	if(retval == NULL) {
		switch(reflectType) {
			case PRIMLIB_BOOLEAN:
			case PRIMLIB_BYTE:
			case PRIMLIB_CHAR:
			case PRIMLIB_SHORT:
			case PRIMLIB_INT:
			case PRIMLIB_LONG:
			case PRIMLIB_FLOAT:
			case PRIMLIB_DOUBLE:
			case PRIMLIB_VOID:
				retval = LINK_LinkClass(env, &(nativeWrapClass[reflectType]), nativeWrapClassName[reflectType]);
				break;
			case PRIMLIB_UNKNOWN:
			case PRIMLIB_OBJECT:
			case PRIMLIB_NULL:
			default:
				return NULL;
		}
	}
	return LINK_ResolveClass(env, retval);
}

JNIEXPORT jclass JNICALL PRIMLIB_GetNativeTypeClass(JNIEnv * env, int reflectType) {
	linkPtr retval = nativeTypeClass[reflectType];
	jfieldID typeField;
	jclass wrapClass;

	if(retval == NULL) {
		wrapClass = PRIMLIB_GetNativeWrapClass(env, reflectType);
		if((*env)->ExceptionOccurred(env)) {
			return NULL;
		}
		typeField = (*env)->GetStaticFieldID(env, wrapClass, "TYPE", "Ljava/lang/Class");
		if((*env)->ExceptionOccurred(env)) {
			return NULL;
		}
		switch(reflectType) {
			case PRIMLIB_BOOLEAN:
			case PRIMLIB_BYTE:
			case PRIMLIB_CHAR:
			case PRIMLIB_SHORT:
			case PRIMLIB_INT:
			case PRIMLIB_LONG:
			case PRIMLIB_FLOAT:
			case PRIMLIB_DOUBLE:
			case PRIMLIB_VOID:
				retval = LINK_LinkKnownClass(env, &(nativeTypeClass[reflectType]), (jclass)(*env)->GetStaticObjectField(env, wrapClass, typeField));
				break;
			case PRIMLIB_UNKNOWN:
			case PRIMLIB_OBJECT:
			case PRIMLIB_NULL:
			default:
				return NULL;
		}
	}
	return LINK_ResolveClass(env, retval);
}

JNIEXPORT jmethodID JNICALL PRIMLIB_GetNativeWrapClassConstructor(JNIEnv * env, int reflectType) {
	linkPtr retval = nativeWrapClassConstructor[reflectType];
	if(retval == NULL) {
		switch(reflectType) {
			case PRIMLIB_BOOLEAN:
			case PRIMLIB_BYTE:
			case PRIMLIB_CHAR:
			case PRIMLIB_SHORT:
			case PRIMLIB_INT:
			case PRIMLIB_LONG:
			case PRIMLIB_FLOAT:
			case PRIMLIB_DOUBLE:
			case PRIMLIB_VOID:
				retval =  LINK_LinkConstructor(env, &(nativeWrapClassConstructor[reflectType]), PRIMLIB_GetNativeWrapClass(env,reflectType), nativeWrapClassConstructorSig[reflectType]);
				break;
			case PRIMLIB_UNKNOWN:
			case PRIMLIB_OBJECT:
			case PRIMLIB_NULL:
			default:
				return NULL;
		}
	}
	return LINK_ResolveMethod(env, retval);
}

JNIEXPORT jmethodID JNICALL PRIMLIB_GetNativeWrapClassAccessor(JNIEnv * env, int reflectType) {
	jmethodID retval = nativeWrapClassAccessor[reflectType];
	if(retval == NULL) {
		switch(reflectType) {
			case PRIMLIB_BOOLEAN:
			case PRIMLIB_BYTE:
			case PRIMLIB_CHAR:
			case PRIMLIB_SHORT:
			case PRIMLIB_INT:
			case PRIMLIB_LONG:
			case PRIMLIB_FLOAT:
			case PRIMLIB_DOUBLE:
				retval = LINK_LinkMethod(env, &(nativeWrapClassAccessor[reflectType]), PRIMLIB_GetNativeWrapClass(env,reflectType), nativeWrapClassAccessorName[reflectType], nativeWrapClassAccessorSig[reflectType]);
				break;
			case PRIMLIB_VOID:
			case PRIMLIB_UNKNOWN:
			case PRIMLIB_OBJECT:
			case PRIMLIB_NULL:
			default:
				return NULL;
		}
	}
	return LINK_ResolveMethod(env, retval);
}



JNIEXPORT jobject JNICALL PRIMLIB_WrapBoolean(JNIEnv * env, jboolean b) {
	jmethodID construct = PRIMLIB_GetNativeWrapClassConstructor(env, PRIMLIB_BOOLEAN);
	JCL_RETHROW_EXCEPTION(env);
	return (*env)->NewObject(env, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BOOLEAN), construct, b);
}

JNIEXPORT jobject JNICALL PRIMLIB_WrapByte   (JNIEnv * env, jbyte b) {
	jmethodID construct = PRIMLIB_GetNativeWrapClassConstructor(env, PRIMLIB_BYTE);
	JCL_RETHROW_EXCEPTION(env);
	return (*env)->NewObject(env, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BYTE), construct, b); 
}

JNIEXPORT jobject JNICALL PRIMLIB_WrapChar   (JNIEnv * env, jchar c) {
	jmethodID construct = PRIMLIB_GetNativeWrapClassConstructor(env, PRIMLIB_CHAR);
	JCL_RETHROW_EXCEPTION(env);
	return (*env)->NewObject(env, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_CHAR), construct, c);
}

JNIEXPORT jobject JNICALL PRIMLIB_WrapShort  (JNIEnv * env, jshort s) {
	jmethodID construct = PRIMLIB_GetNativeWrapClassConstructor(env, PRIMLIB_SHORT);
	JCL_RETHROW_EXCEPTION(env);
	return (*env)->NewObject(env, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_SHORT), construct, s);
}

JNIEXPORT jobject JNICALL PRIMLIB_WrapInt    (JNIEnv * env, jint i) {
	jmethodID construct = PRIMLIB_GetNativeWrapClassConstructor(env, PRIMLIB_INT);
	JCL_RETHROW_EXCEPTION(env);
	return (*env)->NewObject(env, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_INT), construct, i);
}

JNIEXPORT jobject JNICALL PRIMLIB_WrapLong   (JNIEnv * env, jlong l) {
	jmethodID construct = PRIMLIB_GetNativeWrapClassConstructor(env, PRIMLIB_LONG);
	JCL_RETHROW_EXCEPTION(env);
	return (*env)->NewObject(env, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_LONG), construct, l);
}

JNIEXPORT jobject JNICALL PRIMLIB_WrapFloat  (JNIEnv * env, jfloat f) {
	jmethodID construct = PRIMLIB_GetNativeWrapClassConstructor(env, PRIMLIB_FLOAT);
	JCL_RETHROW_EXCEPTION(env);
	return (*env)->NewObject(env, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_FLOAT), construct, f);
}

JNIEXPORT jobject JNICALL PRIMLIB_WrapDouble (JNIEnv * env, jdouble d) {
	jmethodID construct = PRIMLIB_GetNativeWrapClassConstructor(env, PRIMLIB_DOUBLE);
	JCL_RETHROW_EXCEPTION(env);
	return (*env)->NewObject(env, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_DOUBLE), construct, d);
}


JNIEXPORT jboolean JNICALL PRIMLIB_UnwrapBoolean(JNIEnv * env, jobject obj) {
	if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BOOLEAN))) {
		return PRIMLIB_GetBooleanObjectValue(env, obj);
	} else {
		JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Argument not of correct type.");
		return JNI_FALSE;
	}
}

JNIEXPORT jbyte JNICALL PRIMLIB_UnwrapByte(JNIEnv * env, jobject obj) {
	if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BYTE))) {
		return PRIMLIB_GetByteObjectValue(env, obj);
	} else {
		JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Argument not of correct type.");
		return 0;
	}
}

JNIEXPORT jshort JNICALL PRIMLIB_UnwrapShort(JNIEnv * env, jobject obj) {
	if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_SHORT))) {
		return PRIMLIB_GetShortObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BYTE))) {
		return (jshort)PRIMLIB_GetByteObjectValue(env, obj);
	} else {
		JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Argument not of correct type.");
		return 0;
	}
}

JNIEXPORT jchar JNICALL PRIMLIB_UnwrapChar(JNIEnv * env, jobject obj) {
	if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_CHAR))) {
		return PRIMLIB_GetCharObjectValue(env, obj);
	} else {
		JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Argument not of correct type.");
		return 0;
	}
}

JNIEXPORT jint JNICALL PRIMLIB_UnwrapInt(JNIEnv * env, jobject obj) {
	if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_INT))) {
		return PRIMLIB_GetIntObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_SHORT))) {
		return (jint)PRIMLIB_GetShortObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_CHAR))) {
		return (jint)PRIMLIB_GetCharObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BYTE))) {
		return (jint)PRIMLIB_GetByteObjectValue(env, obj);
	} else {
		JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Argument not of correct type.");
		return 0;
	}
}

JNIEXPORT jlong JNICALL PRIMLIB_UnwrapLong(JNIEnv * env, jobject obj) {
	if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_LONG))) {
		return PRIMLIB_GetLongObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_INT))) {
		return (jlong)PRIMLIB_GetIntObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_SHORT))) {
		return (jlong)PRIMLIB_GetShortObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_CHAR))) {
		return (jlong)PRIMLIB_GetCharObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BYTE))) {
		return (jlong)PRIMLIB_GetByteObjectValue(env, obj);
	} else {
		JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Argument not of correct type.");
		return 0;
	}
}

JNIEXPORT jfloat JNICALL PRIMLIB_UnwrapFloat(JNIEnv * env, jobject obj) {
	if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_FLOAT))) {
		return PRIMLIB_GetFloatObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_LONG))) {
		return (jfloat)PRIMLIB_GetLongObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_INT))) {
		return (jfloat)PRIMLIB_GetIntObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_SHORT))) {
		return (jfloat)PRIMLIB_GetShortObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_CHAR))) {
		return (jfloat)PRIMLIB_GetCharObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BYTE))) {
		return (jfloat)PRIMLIB_GetByteObjectValue(env, obj);
	} else {
		JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Argument not of correct type.");
		return 0;
	}
}

JNIEXPORT jdouble JNICALL PRIMLIB_UnwrapDouble(JNIEnv * env, jobject obj) {
	if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_DOUBLE))) {
		return PRIMLIB_GetDoubleObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_FLOAT))) {
		return (jdouble)PRIMLIB_GetFloatObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_LONG))) {
		return (jdouble)PRIMLIB_GetLongObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_INT))) {
		return (jdouble)PRIMLIB_GetIntObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_SHORT))) {
		return (jdouble)PRIMLIB_GetShortObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_CHAR))) {
		return (jdouble)PRIMLIB_GetCharObjectValue(env, obj);
	} else if((*env)->IsInstanceOf(env, obj, PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BYTE))) {
		return (jdouble)PRIMLIB_GetByteObjectValue(env, obj);
	} else {
		JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Argument not of correct type.");
		return 0;
	}
}

JNIEXPORT jint JNICALL PRIMLIB_GetReflectiveWrapperType(JNIEnv * env, jobject obj) {
	jclass typeClass;
	if(obj == NULL) {
		return PRIMLIB_NULL;
	}

	typeClass = PRIMLIB_GetNativeWrapClass(env, PRIMLIB_DOUBLE);
	if((*env)->IsInstanceOf(env, obj, typeClass)) {
		return PRIMLIB_DOUBLE;
	}
	typeClass = PRIMLIB_GetNativeWrapClass(env, PRIMLIB_FLOAT);
	if((*env)->IsInstanceOf(env, obj, typeClass)) {
		return PRIMLIB_FLOAT;
	}
	typeClass = PRIMLIB_GetNativeWrapClass(env, PRIMLIB_LONG);
	if((*env)->IsInstanceOf(env, obj, typeClass)) {
		return PRIMLIB_LONG;
	}
	typeClass = PRIMLIB_GetNativeWrapClass(env, PRIMLIB_INT);
	if((*env)->IsInstanceOf(env, obj, typeClass)) {
		return PRIMLIB_INT;
	}
	typeClass = PRIMLIB_GetNativeWrapClass(env, PRIMLIB_CHAR);
	if((*env)->IsInstanceOf(env, obj, typeClass)) {
		return PRIMLIB_CHAR;
	}
	typeClass = PRIMLIB_GetNativeWrapClass(env, PRIMLIB_SHORT);
	if((*env)->IsInstanceOf(env, obj, typeClass)) {
		return PRIMLIB_SHORT;
	}
	typeClass = PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BYTE);
	if((*env)->IsInstanceOf(env, obj, typeClass)) {
		return PRIMLIB_BYTE;
	}
	typeClass = PRIMLIB_GetNativeWrapClass(env, PRIMLIB_BOOLEAN);
	if((*env)->IsInstanceOf(env, obj, typeClass)) {
		return PRIMLIB_BOOLEAN;
	}
	typeClass = PRIMLIB_GetNativeWrapClass(env, PRIMLIB_VOID);
	if((*env)->IsInstanceOf(env, obj, typeClass)) {
		return PRIMLIB_VOID;
	}
	return PRIMLIB_OBJECT;
}

JNIEXPORT jint JNICALL PRIMLIB_GetReflectiveType(JNIEnv * env, jclass returnType) {
	jclass typeClass = PRIMLIB_GetNativeTypeClass(env, PRIMLIB_DOUBLE);
	if((*env)->IsAssignableFrom(env, returnType, typeClass)) {
		return PRIMLIB_DOUBLE;
	}
	typeClass = PRIMLIB_GetNativeTypeClass(env, PRIMLIB_FLOAT);
	if((*env)->IsAssignableFrom(env, returnType, typeClass)) {
		return PRIMLIB_FLOAT;
	}
	typeClass = PRIMLIB_GetNativeTypeClass(env, PRIMLIB_LONG);
	if((*env)->IsAssignableFrom(env, returnType, typeClass)) {
		return PRIMLIB_LONG;
	}
	typeClass = PRIMLIB_GetNativeTypeClass(env, PRIMLIB_INT);
	if((*env)->IsAssignableFrom(env, returnType, typeClass)) {
		return PRIMLIB_INT;
	}
	typeClass = PRIMLIB_GetNativeTypeClass(env, PRIMLIB_CHAR);
	if((*env)->IsAssignableFrom(env, returnType, typeClass)) {
		return PRIMLIB_CHAR;
	}
	typeClass = PRIMLIB_GetNativeTypeClass(env, PRIMLIB_SHORT);
	if((*env)->IsAssignableFrom(env, returnType, typeClass)) {
		return PRIMLIB_SHORT;
	}
	typeClass = PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BYTE);
	if((*env)->IsAssignableFrom(env, returnType, typeClass)) {
		return PRIMLIB_BYTE;
	}
	typeClass = PRIMLIB_GetNativeTypeClass(env, PRIMLIB_BOOLEAN);
	if((*env)->IsAssignableFrom(env, returnType, typeClass)) {
		return PRIMLIB_BOOLEAN;
	}
	typeClass = PRIMLIB_GetNativeTypeClass(env, PRIMLIB_VOID);
	if((*env)->IsAssignableFrom(env, returnType, typeClass)) {
		return PRIMLIB_VOID;
	}
	return PRIMLIB_OBJECT;
}


JNIEXPORT jboolean JNICALL PRIMLIB_GetBooleanObjectValue(JNIEnv * env, jobject obj) {
	jmethodID acc = PRIMLIB_GetNativeWrapClassAccessor(env, PRIMLIB_BOOLEAN);
	return (*env)->CallBooleanMethod(env, obj, acc);
}

JNIEXPORT jbyte JNICALL PRIMLIB_GetByteObjectValue(JNIEnv * env, jobject obj) {
	jmethodID acc = PRIMLIB_GetNativeWrapClassAccessor(env, PRIMLIB_BYTE);
	return (*env)->CallByteMethod(env, obj, acc);
}

JNIEXPORT jshort JNICALL PRIMLIB_GetShortObjectValue(JNIEnv * env, jobject obj) {
	jmethodID acc = PRIMLIB_GetNativeWrapClassAccessor(env, PRIMLIB_SHORT);
	return (*env)->CallShortMethod(env, obj, acc);
}

JNIEXPORT jchar JNICALL PRIMLIB_GetCharObjectValue(JNIEnv * env, jobject obj) {
	jmethodID acc = PRIMLIB_GetNativeWrapClassAccessor(env, PRIMLIB_CHAR);
	return (*env)->CallCharMethod(env, obj, acc);
}

JNIEXPORT jint JNICALL PRIMLIB_GetIntObjectValue(JNIEnv * env, jobject obj) {
	jmethodID acc = PRIMLIB_GetNativeWrapClassAccessor(env, PRIMLIB_INT);
	return (*env)->CallIntMethod(env, obj, acc);
}

JNIEXPORT jlong JNICALL PRIMLIB_GetLongObjectValue(JNIEnv * env, jobject obj) {
	jmethodID acc = PRIMLIB_GetNativeWrapClassAccessor(env, PRIMLIB_LONG);
	return (*env)->CallLongMethod(env, obj, acc);
}

JNIEXPORT jfloat JNICALL PRIMLIB_GetFloatObjectValue(JNIEnv * env, jobject obj) {
	jmethodID acc = PRIMLIB_GetNativeWrapClassAccessor(env, PRIMLIB_FLOAT);
	return (*env)->CallFloatMethod(env, obj, acc);
}

JNIEXPORT jdouble JNICALL PRIMLIB_GetDoubleObjectValue(JNIEnv * env, jobject obj) {
	jmethodID acc = PRIMLIB_GetNativeWrapClassAccessor(env, PRIMLIB_DOUBLE);
	return (*env)->CallDoubleMethod(env, obj, acc);
}



JNIEXPORT jvalue JNICALL PRIMLIB_UnwrapJValue(JNIEnv* env, jobject obj, jclass classType) {
	jvalue retval;
	jint objType = PRIMLIB_GetReflectiveType(env, classType);
	if(objType == PRIMLIB_BOOLEAN) {
		retval.z = PRIMLIB_UnwrapBoolean(env,obj);
	} else if(objType == PRIMLIB_BYTE) {
		retval.b = PRIMLIB_UnwrapByte(env,obj);
	} else if(objType == PRIMLIB_CHAR) {
		retval.c = PRIMLIB_UnwrapChar(env,obj);
	} else if(objType == PRIMLIB_SHORT) {
		retval.s = PRIMLIB_UnwrapShort(env,obj);
	} else if(objType == PRIMLIB_INT) {
		retval.i = PRIMLIB_UnwrapInt(env,obj);
	} else if(objType == PRIMLIB_LONG) {
		retval.j = PRIMLIB_UnwrapLong(env,obj);
	} else if(objType == PRIMLIB_FLOAT) {
		retval.f = PRIMLIB_UnwrapFloat(env,obj);
	} else if(objType == PRIMLIB_DOUBLE) {
		retval.d = PRIMLIB_UnwrapDouble(env,obj);
	} else {
		if(obj != NULL && !(*env)->IsInstanceOf(env, obj, classType)) {
			JCL_ThrowException(env, "java/lang/IllegalArgumentException", "Argument not of correct object type.");
			return retval;
		}
		retval.l = obj;
	}
	return retval;
}

