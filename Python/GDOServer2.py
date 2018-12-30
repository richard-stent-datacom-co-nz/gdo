import SocketServer
import RPi.GPIO as GPIO
import time

class GDORequestHandler(SocketServer.BaseRequestHandler):

    def handle(self):
        # Send the 1 (on) or 0 (off) to the GPIO port 17
        data = self.request.recv(1024)
        
        if data == "True":
            GPIO.output(17, True)
            print 'Got On'
            return
            
        if data == "False":
            GPIO.output(17, False)
            print 'Got Off'
            return
        

if __name__ == '__main__':
    import socket
    import threading
    
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(17, GPIO.OUT)

    address = ('localhost', 0) # let the kernel give us a port
    server = SocketServer.TCPServer(address, GDORequestHandler)
    ip, port = server.server_address # find out what port we were given

    t = threading.Thread(target=server.serve_forever)
    t.setDaemon(True) # don't hang on exit
    t.start()

    # Connect to the server
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((ip, port))

    # Send the data
    message = 'True'
    print 'Sending : "%s"' % message
    len_sent = s.send(message)
    time.sleep(5)

    # Send the data
    message = 'False'
    print 'Sending : "%s"' % message
    len_sent = s.send(message)
    
    time.sleep(2)

    # Clean up
    s.close()
    server.socket.close()
    GPIO.cleanup()
