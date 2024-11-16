import { useState } from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Layout from './components/Layout'
import LayoutAnimal from './components/LayoutAnimal'
import Home from './pages/Home'
import HistoricalAnimalsData from './pages/HistoricalAnimalsData'
import Register from './pages/Register'
import Login from './pages/Login'
import Pet from './pages/Pet'
import './App.css'

function App() {
  const [selectedAnimal, setSelectedAnimal] = useState('')
  const [selectedMetric, setSelectedMetric] = useState('')

  const handleSelectAnimal = (animal) => {
    setSelectedAnimal(animal)
  }

  const handleSelectMetric = (metric) => {
    setSelectedMetric(metric)
  }

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<h2>About</h2>} />

          {/* 404 Not Found Route */}
          <Route path="*" element={<h2>404 - Page Not Found</h2>} />
        </Route>
        <Route path="/animal" element={<LayoutAnimal />}>
          <Route path="/animal/historical" element={<HistoricalAnimalsData animal={selectedAnimal} metric={selectedMetric} />} />
        </Route>
        <Route path="/pet-monitoring" element={<LayoutAnimal />}>
          <Route path="" element={<Pet />} />
        </Route>
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </Router>
  )
}

export default App
