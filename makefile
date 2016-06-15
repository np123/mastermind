all:
	jar cvmf0 Manifest.txt Mastermind.jar -C bin .
	
clean:
	rm Mastermind.jar