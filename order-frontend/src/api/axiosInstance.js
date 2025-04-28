import axios from 'axios';

const instance = axios.create({
    baseURL: 'http://localhost:7002/api/v1', //order-service base URL
    headers: {
        'Content-Type': 'application/json',
    },
});

export default instance;
