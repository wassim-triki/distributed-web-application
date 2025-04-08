const { Eureka } = require('eureka-js-client');

function registerWithEureka(PORT = 4000) {
  const client = new Eureka({
    instance: {
      app: 'NOTIFICATION-SERVICE',
      instanceId: `notification-service-${PORT}`,
      hostName: 'localhost',
      ipAddr: '127.0.0.1',
      statusPageUrl: `http://localhost:${PORT}/info`,
      port: {
        $: PORT,
        '@enabled': true,
      },
      vipAddress: 'notification-service',
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
    },
    eureka: {
      host: 'localhost',
      port: 8761,
      servicePath: '/eureka/apps/',
    },
  });

  client.start((error) => {
    if (error) console.error('Eureka registration failed:', error);
    else console.log('✅ Registered with Eureka');
  });
}

module.exports = registerWithEureka;
