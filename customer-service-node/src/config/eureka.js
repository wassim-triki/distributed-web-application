const { Eureka } = require('eureka-js-client');

function registerWithEureka(PORT = 3001) {
  const eureka = new Eureka({
    instance: {
      app: 'CUSTOMER-SERVICE-NODE',
      instanceId: `customer-service-node:${PORT}`,
      hostName: 'localhost',
      ipAddr: '127.0.0.1',
      statusPageUrl: `http://localhost:${PORT}/info`,
      port: {
        $: PORT,
        '@enabled': true,
      },
      vipAddress: 'customer-service-node',
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

  eureka.start((error) => {
    if (error) {
      console.error('Eureka registration failed:', error);
    } else {
      console.log('Registered with Eureka!');
    }
  });
}

module.exports = registerWithEureka;
