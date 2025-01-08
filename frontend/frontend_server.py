import http.server
import ssl

# Create an HTTP server
ip = '10.7.94.121'
server_address = (ip, 8000)
httpd = http.server.HTTPServer(server_address, http.server.SimpleHTTPRequestHandler)

# Wrap the HTTP server with SSL
httpd.socket = ssl.wrap_socket(httpd.socket,
                               keyfile="D:\B.tech_cse\sem 7\MAP\Map_Innovative/key.pem",
                               certfile="D:\B.tech_cse\sem 7\MAP\Map_Innovative\cert.pem",
                               server_side=True)

print(f"Serving on https://{ip}:8000")
httpd.serve_forever()
