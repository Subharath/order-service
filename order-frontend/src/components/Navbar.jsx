import React from 'react';
import { Link } from 'react-router-dom';
import { useCart, getCartItemCount } from '../contexts/CartContext';

const Navbar = () => {
    const { cartItems } = useCart();
    const itemCount = getCartItemCount(cartItems);

    return (
        <nav className="flex items-center justify-between p-4 bg-white shadow mb-4">
            <div className="text-xl font-bold">
                <Link to="/">SavorySwift</Link>
            </div>
            <div className="flex items-center gap-6">
                <Link to="/" className="hover:underline">Home</Link>
                <Link to="/cart" className="relative hover:underline">
                    Cart
                    {itemCount > 0 && (
                        <span className="ml-1 inline-flex items-center justify-center px-2 py-1 text-xs font-bold leading-none text-white bg-red-500 rounded-full">
              {itemCount}
            </span>
                    )}
                </Link>
                <Link to="/checkout" className="hover:underline">Checkout</Link>
            </div>
        </nav>
    );
};

export default Navbar;
