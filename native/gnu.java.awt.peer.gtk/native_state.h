#ifndef JCL_NATIVE_STATE
#define JCL_NATIVE_STATE

#include <jni.h>

struct state_table
{
  jint size;			/* number of slots, should be prime */
  jfieldID hash;		/* field containing System.identityHashCode(this) */
  jclass clazz;			/* lock aquired for reading/writing nodes */
  struct state_node **head;
};

struct state_node
{
  jint key;
  void *c_state;
  struct state_node *next;
};

struct state_table * init_state_table_with_size (JNIEnv *, jclass, jint);
struct state_table * init_state_table (JNIEnv *, jclass);

/* lowlevel api */
void set_state_oid (JNIEnv *, jobject, struct state_table *, jint, void *);
void * get_state_oid (JNIEnv *, jobject, struct state_table *, jint);
void * remove_state_oid (JNIEnv *, jobject, struct state_table *, jint);

/* highlevel api */
int set_state (JNIEnv *, jobject, struct state_table *, void *);
void * get_state (JNIEnv *, jobject, struct state_table *);
void * remove_state_slot (JNIEnv *, jobject, struct state_table *);

#endif
