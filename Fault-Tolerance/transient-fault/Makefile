CC=gcc
CFLAGS=-g -Wall 
LFLAGS=-lm -lpthread

app: 	systeme.c
	${CC} ${CFLAGS} systeme.c ${LFLAGS} -o faulty-system 
testhighfail: faulty-system
	./faulty-system 2000 1800 900 450 
inject-error: 
	./faulty-system 2000 1800 900 450 &
	bash ./benchit.sh faulty-system 
