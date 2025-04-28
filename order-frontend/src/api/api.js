// src/api/api.js
import axios from 'axios';

const BASE_URL = 'http://localhost:8080'; // Your backend base URL

export const getMenuItems = async () => {
    const response = await axios.get(`${BASE_URL}/api/menu-items`);
    return response.data;
};
