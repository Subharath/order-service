import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import HomePage from './pages/HomePage';
import CartPage from './pages/CartPage';
import CheckoutPage from './pages/CheckoutPage';
import { CartProvider } from './contexts/CartContext';
import Navbar from './components/Navbar';

function App() {
    return (
        <CartProvider>
            <Router>
                {/*<nav className="bg-gray-800 p-4 text-white flex justify-between">
                    <Link to="/" className="font-bold text-xl">SavorySwift</Link>
                    <div className="flex gap-4">
                        <Link to="/">Home</Link>
                        <Link to="/cart">Cart</Link>
                    </div>
                </nav>*/}
                <Navbar /> {/* New Navbar */}
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/cart" element={<CartPage />} />
                    <Route path="/checkout" element={<CheckoutPage />} />
                </Routes>
            </Router>
        </CartProvider>
    );
}

export default App;
