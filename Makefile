SHELL = sh
SRC = bakagaiku.rb
DATA = *.txt.* *.erb.*
CGI_BIN = $(HOME)/public_html/cgi-bin
CGI_DATA = $(HOME)/public_html/cgi-data

all:
	install -m 755 -T $(SRC) $(CGI_BIN)/bakagaiku.rb
	install $(DATA) $(CGI_DATA)
