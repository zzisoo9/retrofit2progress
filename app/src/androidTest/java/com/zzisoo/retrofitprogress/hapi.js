'use strict';

const Hapi = require('hapi');
// Create a server with a host and port
const server = new Hapi.Server();
server.connection({
    host: '10.100.0.134',
    port: 8000
});

// Add the route
server.route({
    method: 'GET',
    path:'/hello',
    handler: function (request, reply) {

        return reply('hello world');
    }
});

  server.route({
       method: 'POST',
       path: '/convert',
       config: {
            payload: {
               output: 'file',
               maxBytes: 209715200,
               //allow: 'multipart/form-data',
               parse: true //or just remove this line since true is the default
            },
            handler:function (request, reply) {
               console.log('fileUpload path : ' + request.payload.fileUpload.path);
                return reply(true);
}
       },
   });

// Start the server
server.start((err) => {

    if (err) {
        throw err;
    }
    console.log('Server running at:', server.info.uri);
});