import { useState } from 'react'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Layout from './components/Layout'
import Home from './pages/Home'
import Register from './pages/Register'
import './App.css'

function App() {

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<h2>About</h2>} />

          {/* 404 Not Found Route */}
          <Route path="*" element={<h2>404 - Page Not Found</h2>} />
        </Route>
          <Route path="/register" element={<Register />} />
      </Routes>
    </Router>
  )
}

export default App
