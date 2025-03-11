const axios = require('axios');

const backendApi = axios.create({
    baseURL: process.env.BACKEND_URL || 'http://localhost:8081',
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
});

module.exports = backendApi;
