all:
	jar cvmf0 Manifest.txt Mastermind.jar -C bin . -C resources .
	
clean:
	rm Mastermind.jar