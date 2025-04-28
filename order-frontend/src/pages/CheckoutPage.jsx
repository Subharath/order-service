import React, { useState, useCallback } from 'react';
import { useCart } from '../contexts/CartContext';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { GoogleMap, useLoadScript, Marker } from '@react-google-maps/api';

const CheckoutPage = () => {
    const { cartItems, clearCart } = useCart();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        userId: 'testuser1',
        customerEmail: '',
        customerPhoneNumber: '',
        street: '',
        city: '',
        postalCode: '',
        country: 'Sri Lanka',
        latitude: 6.9271,     // default to Colombo, Sri Lanka
        longitude: 79.8612,
    });

    const { isLoaded } = useLoadScript({
        googleMapsApiKey: 'AIzaSyAp7nhzYT1ikujsgPxmZOVTGMhxb_8R6qA',  // <<< --- Replace here
    });

    const handleChange = (e) => {
        const { name, value } = e.target;

        // Smart Validations
        if (name === "customerPhoneNumber" && !/^\d*$/.test(value)) return; // allow only digits
        if (name === "postalCode" && !/^\d*$/.test(value)) return; // allow only digits
        if (name === "customerEmail" && value.includes(' ')) return; // block spaces in email

        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!formData.customerEmail.includes('@') || !formData.customerPhoneNumber || !formData.street || !formData.city || !formData.postalCode) {
            alert('Please fill all fields correctly!');
            return;
        }

        try {
            await axios.post('http://localhost:7002/api/orders/checkout', formData);
            clearCart();
            alert('Order placed successfully! 🚀');
            navigate('/');
        } catch (error) {
            console.error('Checkout Error:', error);
            alert('Failed to place order. Please try again.');
        }
    };

    const handleMapClick = useCallback((event) => {
        setFormData(prev => ({
            ...prev,
            latitude: event.latLng.lat(),
            longitude: event.latLng.lng(),
        }));
    }, []);

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Checkout</h1>

            <form onSubmit={handleSubmit} className="grid gap-4 max-w-md">
                <input
                    type="email"
                    name="customerEmail"
                    placeholder="Email"
                    value={formData.customerEmail}
                    onChange={handleChange}
                    className="p-2 border rounded"
                    required
                />
                <input
                    type="text"
                    name="customerPhoneNumber"
                    placeholder="Phone Number (only digits)"
                    value={formData.customerPhoneNumber}
                    onChange={handleChange}
                    className="p-2 border rounded"
                    required
                    maxLength={10}
                />
                <input
                    type="text"
                    name="street"
                    placeholder="Street Address"
                    value={formData.street}
                    onChange={handleChange}
                    className="p-2 border rounded"
                    required
                />
                <input
                    type="text"
                    name="city"
                    placeholder="City"
                    value={formData.city}
                    onChange={handleChange}
                    className="p-2 border rounded"
                    required
                />
                <input
                    type="text"
                    name="postalCode"
                    placeholder="Postal Code"
                    value={formData.postalCode}
                    onChange={handleChange}
                    className="p-2 border rounded"
                    required
                />

                {/* Country Fixed */}
                <input
                    type="text"
                    name="country"
                    value="Sri Lanka"
                    disabled
                    className="p-2 border rounded bg-gray-100 cursor-not-allowed"
                />

                {/* Google Map Integration */}
                <div className="mt-4">
                    <p className="mb-2 font-semibold">Select Delivery Location on Map:</p>
                    {isLoaded ? (
                        <GoogleMap
                            mapContainerStyle={{ width: '100%', height: '300px' }}
                            center={{ lat: formData.latitude, lng: formData.longitude }}
                            zoom={10}
                            onClick={handleMapClick}
                        >
                            <Marker position={{ lat: formData.latitude, lng: formData.longitude }} />
                        </GoogleMap>
                    ) : (
                        <p>Loading Map...</p>
                    )}
                </div>

                <button
                    type="submit"
                    className="bg-blue-500 text-white p-2 rounded hover:bg-blue-600 mt-4"
                >
                    Place Order
                </button>
            </form>
        </div>
    );
};

export default CheckoutPage;
