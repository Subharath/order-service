// src/pages/HomePage.jsx
import React, { useEffect, useState } from 'react';
import { useCart } from '../contexts/CartContext';

const HomePage = () => {
    const { addToCart } = useCart();
    const [menuItems, setMenuItems] = useState([]);

    useEffect(() => {
        // Hardcoded Dummy Menu Items
        const dummyItems = [
            { id: '1', name: 'Burger', description: 'Juicy beef burger', price: 5.99 },
            { id: '2', name: 'Pizza', description: 'Cheesy pepperoni pizza', price: 8.99 },
            { id: '3', name: 'Pasta', description: 'Creamy Alfredo pasta', price: 7.49 },
            { id: '4', name: 'Sushi', description: 'Fresh salmon sushi', price: 12.49 },
        ];
        setMenuItems(dummyItems);
    }, []);

    const handleAddToCart = (item) => {
        addToCart(item); // Call addToCart from Context
    };

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-6">Menu</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {menuItems.map((item) => (
                    <div key={item.id} className="border rounded-lg p-6 shadow hover:shadow-lg transition">
                        <h2 className="text-lg font-semibold">{item.name}</h2>
                        <p className="text-gray-600">{item.description}</p>
                        <p className="font-bold mt-2">${item.price.toFixed(2)}</p>
                        <button
                            onClick={() => handleAddToCart(item)}
                            className="mt-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                        >
                            Add to Cart
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default HomePage;
