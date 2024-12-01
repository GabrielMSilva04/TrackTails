import {useEffect, useState} from 'react'
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom'
import Layout from './components/Layout'
import LayoutAnimal from './components/LayoutAnimal'
import Profile from './pages/Profile'
import Home from './pages/Home'
import Pet from './pages/Pet'
import Register from './pages/Register'
import Login from './pages/Login'
import LayoutMapDetails from './components/LayoutMapDetails';
import LayoutMap from './components/LayoutMap';
import MyPets from "./pages/MyPets.jsx";
import RegisterPet from "./pages/RegisterPet.jsx";
import EditPet from "./pages/EditPet.jsx";
import HistoricalAnimalsData from './pages/HistoricalAnimalsData'
import Finders from './pages/Finders'
import {checkToken} from "./utils.js";
import './App.css'
import Notifications from "./pages/Notifications.jsx";
import { AnimalProvider } from './contexts/AnimalContext';
import { useNavigate } from 'react-router-dom'

const ProtectedRoute = ({ loggedIn, children }) => {
    return loggedIn ? children : <Navigate to="/login" />;
};

export default function App() {
    const [loggedUser, setLoggedUser] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if ( checkToken() ) {
            setLoggedUser(true);
        }
        setLoading(false);
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <Router>
            <Routes>
                {/* Public Pages */}
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                {/* Protected Pages */}
                <Route
                    path="*"
                    element={
                        <ProtectedRoute loggedIn={loggedUser}>
                            <AppRoutes />
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </Router>
    );
}

function AppRoutes() {
    const navigate = useNavigate()
    const [selectedAnimal, setSelectedAnimal] = useState('')
    const [selectedMetric, setSelectedMetric] = useState('')

    const handleSelectAnimal = (animal) => {
        console.log('Selected animal:', animal)
        setSelectedAnimal(animal)
    }

    const handleSelectMetric = (metric) => {
        setSelectedMetric(metric)
        // go to the historic page
        navigate('/animal/historic')
    }

    return (
        <AnimalProvider>
            <Routes>
                {/* Public Layout */}
                <Route path="/" element={<Layout />}>
                  <Route index element={<Home />} />
                  <Route path="/about" element={<h2>About</h2>} />
                  <Route path="/notifications" element={<Notifications />} />
                  <Route path="/mypets" element={<MyPets onAnimalSelect={handleSelectAnimal} />} />
                  <Route path="/profile" element={<Profile/>}/>
                  <Route path="/registerpet" element={<RegisterPet />} />
                  <Route path="/editpet" element={<EditPet />} />


                  {/* 404 Not Found Route */}
                  <Route path="*" element={<h2>404 - Page Not Found</h2>} />
                </Route>

                {/* Map and Details */}
                <Route path="/map" element={<LayoutMap />} />
                <Route path="/map/details" element={<LayoutMapDetails />} />

                {/* Animal Historic and Monitoring */}
                <Route
                    path="/animal/historic"
                    element={<LayoutAnimal showButtons="back-only" />}
                    >
                        <Route
                            path="/animal/historic"
                            element={<HistoricalAnimalsData animal={selectedAnimal} metric={selectedMetric} />}
                        />
                </Route>

                <Route
                    path="/animal/monitoring"
                    element={<LayoutAnimal showButtons="all" />}
                >
                    <Route
                        path="/animal/monitoring"
                        element={<Pet onMetricSelect={handleSelectMetric} />}
                    />
                </Route>

                {/* Finder Page */}
                <Route path="/finders" element={<LayoutAnimal showButtons="none"/>}>
                    <Route path="/finders" element={<Finders />} />
                </Route>
            </Routes>
        </AnimalProvider>
    );
}
