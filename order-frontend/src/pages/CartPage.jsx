import React from 'react';
import { useCart } from '../contexts/CartContext';
import { Link } from 'react-router-dom';

const CartPage = () => {
    const { cartItems, removeFromCart, clearCart } = useCart();

    const handleRemove = (menuItemId) => {
        removeFromCart(menuItemId);
    };

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Your Cart</h1>

            {cartItems.length === 0 ? (
                <p>Your cart is empty.</p>
            ) : (
                <div className="space-y-4">
                    {cartItems.map((item, index) => (
                        <div key={index} className="border p-4 rounded shadow">
                            <h2 className="text-xl font-semibold">{item.name}</h2>
                            <p>Quantity: {item.quantity}</p>
                            <p>Price: Rs. {item.price}</p>
                            {item.specialInstructions && (
                                <p>Special Instructions: {item.specialInstructions}</p>
                            )}
                            <button
                                onClick={() => handleRemove(item.menuItemId)}
                                className="mt-2 bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                            >
                                Remove
                            </button>
                        </div>
                    ))}

                    <div className="mt-6 flex gap-4">
                        <Link
                            to="/checkout"
                            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                        >
                            Proceed to Checkout
                        </Link>

                        <button
                            onClick={clearCart}
                            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                        >
                            Clear Cart
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CartPage;
