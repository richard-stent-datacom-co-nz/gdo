import SocketServer
import RPi.GPIO as GPIO
import time

class GDORequestHandler(SocketServer.BaseRequestHandler):

    def handle(self):
        # Send the 1 (on) and 0 (off) to the GPIO port 17 with a small delay to simulate pushing a springed button.
        data = self.request.recv(5)
        
        GPIO.output(17, True)
        time.sleep(0.15)
        GPIO.output(17, False)
        return
        

if __name__ == '__main__':
    import socket
    import threading
    
    GPIO.cleanup
    GPIO.setwarnings(False)
    
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(17, GPIO.OUT)

    address = ('localhost', 7654) 
    server = SocketServer.TCPServer(address, GDORequestHandler)
    ip, port = server.server_address # find out what port we were given

    t = threading.Thread(target=server.serve_forever)
    t.setDaemon(True) # don't hang on exit
    t.start()

    while 1:
        time.sleep(0.1)
    
