A simple webserver serving static files.

##### How to use:
Server has three parameters, two of which is optional:
6. `-r <rootDir>` will specify which directory server should serve files from. Better to use absolute paths.
6. `-c <numThreads>` will specify how many "worker" threads should be launched to serve statics. `1` by default. Optional.
6. `-p <port>` will specify which port server will listen to. `80` by default. Optional.