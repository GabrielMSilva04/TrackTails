import { useState } from 'react'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Layout from './components/Layout'
import LayoutAnimal from './components/LayoutAnimal'
import Home from './pages/Home'
import Register from './pages/Register'
import Login from './pages/Login'
import Map from './pages/Map'
import './App.css'
import {LayoutMap} from "./components/LayoutMap.jsx";

function App() {

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path="/map" element={<Map />} />

          {/* 404 Not Found Route */}
          <Route path="*" element={<h2>404 - Page Not Found</h2>} />
        </Route>
        <Route path="/map" element={<LayoutMap />}>
          <Route path="" element={<Map />} />
        </Route>
        <Route path="/test" element={<LayoutAnimal />}>

          </Route>
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
      </Routes>
    </Router>
  )
}

export default App
