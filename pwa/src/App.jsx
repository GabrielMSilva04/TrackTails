import { useState } from 'react'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Layout from './components/Layout'
import Home from './pages/Home'
import './App.css'
import {MyPets} from "./pages/MyPets.jsx";
import {RegisterPet} from "./pages/RegisterPet.jsx";

function App() {

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route path="/" element={<Home />} />
                    <Route path="/about" element={<h2>About</h2>} />
                    <Route path="/mypets" element={<MyPets />} />
                    <Route path="/petregister" element={<RegisterPet />} />

                    {/* 404 Not Found Route */}
                    <Route path="*" element={<h2>404 - Page Not Found</h2>} />
                </Route>
            </Routes>
        </Router>
  )
}

export default App
