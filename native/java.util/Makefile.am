if JAPHAR
lib_LTLIBRARIES = libjaphar_javautil.la

libjaphar_javautil_la_SOURCES = TimeZone.c \
                                java_util_TimeZone.h \
                                ResourceBundle.c \
                                java_util_ResourceBundle.h

else
lib_LTLIBRARIES = libjavautil.la

libjavautil_la_SOURCES = TimeZone.c \
                         java_util_TimeZone.h \
                         ResourceBundle.c \
                         java_util_ResourceBundle.h

endif

libjaphar_javautil_la_LDFLAGS = -version-info 0:0:0 
libjavautil_la_LDFLAGS = -version-info 0:0:0 

INCLUDES +=  @JAPHAR_CFLAGS@ -I$(top_srcdir)/include

