CC = javac
CFLAGS = -cp .:libs/*
RFLAGS = -Djava.library.path=native/linux

.PHONY: all clean run test

all: Game.class

%.class: %.java
	$(CC) $(CFLAGS) $<

test: test.class
	$(CC) $(CFLAGS) test.java

run: Game.class
	java $(CFLAGS) $(RFLAGS) Game

clean:
	rm -f *.class geomorph/*.class graphics/*.class physics/*.class physics/material/*.class creature/*.class world/*.class
