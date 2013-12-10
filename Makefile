CC = javac
CFLAGS = -cp .:lwjgl_jar/lwjgl.jar
RFLAGS = -Djava.library.path=lwjgl_native

.PHONY: all clean run test

all: World.class

%.class: %.java
	$(CC) $(CFLAGS) $<

test: test.class
	$(CC) $(CFLAGS) test.java

run: test_world.class
	java $(CFLAGS) $(RFLAGS) test_world

clean:
	rm -f *.class geomorph/*.class
