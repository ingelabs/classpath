#include <stdlib.h>
#include <jni.h>
#include "native_state.h"

#define DEFAULT_TABLE_SIZE 97

struct state_table *
init_state_table_with_size (JNIEnv *env, jclass clazz, jint size)
{
  struct state_table *table;
  jfieldID hash;
  jclass clazz_g;

  hash = (*env)->GetFieldID (env, clazz, "native_state", "I");
  if (hash == NULL)
    return NULL;

  clazz_g = (*env)->NewGlobalRef (env, clazz);
  if (clazz_g == NULL)
    return NULL;

  table = (struct state_table *) malloc (sizeof (struct state_table));
  table->size = size;
  table->head = (struct state_node **) calloc (sizeof (struct state_node *),
					       table->size);
  table->hash = hash;
  table->clazz = clazz_g; 

  return table;
}

struct state_table *
init_state_table (JNIEnv *env, jclass clazz)
{
  return init_state_table_with_size (env, clazz, DEFAULT_TABLE_SIZE);
}

static
void *
remove_node (struct state_node **head, jint obj_id)
{
  struct state_node *back_ptr = NULL;
  struct state_node *node = *head;

  while (node != NULL)
    {
      if (node->key == obj_id)
	{
	  void *return_value;
	  if (back_ptr == NULL)
	    *head = node->next;
	  else
	    back_ptr->next = node->next;
	  return_value = node->c_state;
	  free (node);
	  return return_value;
	}
      back_ptr = node;
      node = node->next;
    }

  return NULL;
}
	    
static
void *
get_node (struct state_node **head, jint obj_id)
{
  struct state_node *back_ptr = NULL;
  struct state_node *node = *head;

  while (node != NULL)
    {
      if (node->key == obj_id)
	{
	  /* move the node we found to the front of the list */
	  if (back_ptr != NULL)
	    {
	      back_ptr->next = node->next;
	      node->next = *head;
	      *head = node;
	    }

	  /* return the match */
	  return node->c_state;
	}
  
      back_ptr = node;
      node = node->next;
    }

  return NULL;
}

static
void 
add_node (struct state_node **head, jint obj_id, void *state)
{
  struct state_node *node = *head;
  struct state_node *back_ptr = *head;

  struct state_node *new_node;
  new_node = (struct state_node *) malloc (sizeof (struct state_node));
  new_node->key = obj_id;
  new_node->c_state = state;

  /* insert into an empty slot */
  if (node == NULL)
    {
      new_node->next = NULL;
      *head = new_node;
      return;
    }

  /* collision resolution */
  /* try to find the old node, if it exists */
  while (node->next != NULL && obj_id != node->key) 
    {
      back_ptr = node;
      node = node->next;
    }

  /* if we're updating a node, setup to move it to the front of the list */
  if (node->key == obj_id)
    back_ptr->next = node->next;
  
  /* move node to the beginning */
  new_node->next = *head;
  *head = new_node;
}

void 
set_state_oid (JNIEnv *env, jobject lock, struct state_table *table, 
	       jint obj_id, void *state)
{
  jint hash;
  
  hash = obj_id % table->size;

  (*env)->MonitorEnter (env, lock);
  add_node (&table->head[hash], obj_id, state);
  (*env)->MonitorExit (env, lock);
}

void *
get_state_oid (JNIEnv *env, jobject lock, struct state_table *table,
	       jint obj_id)
{
  jint hash;
  void *return_value;
  
  hash = obj_id % table->size;

  (*env)->MonitorEnter (env, lock);
  return_value = get_node (&table->head[hash], obj_id);
  (*env)->MonitorExit (env, lock);

  return return_value;
}

void *
remove_state_oid (JNIEnv *env, jobject lock, struct state_table *table,
		  jint obj_id)
{
  jint hash;
  void *return_value;
  
  hash = obj_id % table->size;

  (*env)->MonitorEnter (env, lock);
  return_value = remove_node (&table->head[hash], obj_id);
  (*env)->MonitorExit (env, lock);

  return return_value;
}

int
set_state (JNIEnv *env, jobject obj, struct state_table *table, void *state)
{
  jint obj_id;
  obj_id = (*env)->GetIntField (env, obj, table->hash);

  if ((*env)->ExceptionOccurred (env) != NULL)
    return -1;

  set_state_oid (env, table->clazz, table, obj_id, state);
  return 0;
}

void *
get_state (JNIEnv *env, jobject obj, struct state_table *table)
{
  jint obj_id;
  obj_id = (*env)->GetIntField (env, obj, table->hash);

  if ((*env)->ExceptionOccurred (env) != NULL)
    return NULL;

  return get_state_oid (env, table->clazz, table, obj_id);
}

void *
remove_state_slot (JNIEnv *env, jobject obj, struct state_table *table)
{
  jint obj_id;
  obj_id = (*env)->GetIntField (env, obj, table->hash);

  if ((*env)->ExceptionOccurred (env) != NULL)
    return NULL;

  return remove_state_oid (env, table->clazz, table, obj_id);
}
