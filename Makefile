OUT = output
CC = javac
CFLAGS = -cp .:libs/* -Xlint:all -d $(OUT)
RFLAGS = -cp $(OUT):libs/* -Djava.library.path=native/linux
OBJ = $(OUT)/game.class

.PHONY: all clean run test

all: $(OUT)/Game.class

$(OUT)/%.class: %.java
	$(CC) $(CFLAGS) $<

test: test.class
	$(CC) $(CFLAGS) test.java

run: $(OUT)/Game.class
	java $(RFLAGS) Game

clean:
	rm -rf output/*
