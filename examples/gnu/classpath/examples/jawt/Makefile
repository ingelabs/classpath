all: \
	DemoJAWT.h DemoJAWT.class libDemoJAWT.so

%.class: %.java
	gcj -C $<

%.h: %.class
	gcjh -jni $(basename $< .class)

DemoJAWT: DemoJAWT.java
	gcj -g --main=DemoJAWT -fjni -o DemoJAWT DemoJAWT.java

libDemoJAWT.so: DemoJAWT.c
	gcc -g -O0 -Wall -I. -I/usr/X11R6/include -shared -o $@ DemoJAWT.c -L. -ljawt -L/usr/X11R6/lib -lX11

run:
	LD_LIBRARY_PATH=$$LD_LIBRARY_PATH:. jamvm DemoJAWT

clean:
	rm -f DemoJAWT.h DemoJAWT.class DemoJAWT\$$1.class libDemoJAWT.so
