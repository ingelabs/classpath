if JAPHAR
lib_LTLIBRARIES = libjaphar_javautil.la

libjaphar_javautil_la_SOURCES = TimeZone.c \
                                java_util_TimeZone.h \
                                ResourceBundle.c \
                                java_util_ResourceBundle.h

libjaphar_javautil_la_LDFLAGS = -version-info 0:0:0 
else
lib_LTLIBRARIES = libjavautil.la

libjavautil_la_SOURCES = TimeZone.c \
                         java_util_TimeZone.h \
                         ResourceBundle.c \
                         java_util_ResourceBundle.h

libjavautil_la_LDFLAGS = -version-info 0:0:0 
endif

if JAPHAR
INCLUDES +=  @JAPHAR_CFLAGS@ -I$(top_srcdir)
endif

