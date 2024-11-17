import { useState } from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Layout from './components/Layout'
import LayoutAnimal from './components/LayoutAnimal'
import Home from './pages/Home'
import Pet from './pages/Pet'
import Register from './pages/Register'
import Login from './pages/Login'
import MyPets from "./pages/MyPets.jsx";
import RegisterPet from "./pages/RegisterPet.jsx";
import EditPet from "./pages/EditPet.jsx";
import HistoricalAnimalsData from './pages/HistoricalAnimalsData'

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
          <Route path="/mypets" element={<MyPets />} />

          {/* 404 Not Found Route */}
          <Route path="*" element={<h2>404 - Page Not Found</h2>} />
        </Route>

        <Route path="/animal/historic" element={<LayoutAnimal showButtons='back-only' />}>
          <Route path="/animal/historic" element={<HistoricalAnimalsData animal={selectedAnimal} metric={selectedMetric} />} />
        </Route>
        <Route path="/animal/monitoring" element={<LayoutAnimal showButtons='all' />}>
          <Route path="/animal/monitoring" element={<Pet />} />
        </Route>

        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/registerpet" element={<RegisterPet />} />
        <Route path="/editpet" element={<EditPet />} />
      </Routes>
    </Router>
  )
}

export default App
