import SocketServer
import RPi.GPIO as GPIO
import time

class GDORequestHandler(SocketServer.BaseRequestHandler):

    def handle(self):
        # Send the 1 (on) or 0 (off) to the GPIO port 17 with a quarter second delay
        data = self.request.recv(5)
        
        
        GPIO.output(17, True)
        time.sleep(0.25)
        GPIO.output(17, False)
        return
        

if __name__ == '__main__':
    import socket
    import threading
    
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(17, GPIO.OUT)

    address = ('localhost', 7654) 
    server = SocketServer.TCPServer(address, GDORequestHandler)
    ip, port = server.server_address # find out what port we were given

    t = threading.Thread(target=server.serve_forever)
    t.setDaemon(True) # don't hang on exit
    t.start()

    # Connect to the server
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((ip, port))
    print("Server on %s:%s", ip, port)

    # Send the data
    message = 'Toggle'
    print 'Sending : "%s"' % message
    len_sent = s.send(message)
    
    print ('Sleeping for 5 seconds')
    time.sleep(5)
    print('Cleaning Up')
    
    # Clean up
    print('Closing SocketServer')
    s.close()
    server.socket.close()
    print('Cleaning Up GPIO')
    GPIO.cleanup()
    print('Exiting')
