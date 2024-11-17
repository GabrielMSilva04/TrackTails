import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import LayoutAnimal from './components/LayoutAnimal';
import LayoutMap from './components/LayoutMap';
import Home from './pages/Home';
import Register from './pages/Register';
import Login from './pages/Login';
import Map from './pages/MapPage.jsx';
import LayoutMapDetails from './components/LayoutMapDetails';
import MapDetails from './pages/MapDetails';
import './App.css';

function App() {
  return (
      <Router>
          <Routes>

              <Route path="/" element={<Layout />}>
                  <Route index element={<Home />} />
                  <Route path="register" element={<Register />} />
                  <Route path="login" element={<Login />} />
              </Route>

              <Route path="/map" element={<LayoutMap />}>
                  <Route index element={<Map />} />
              </Route>

              <Route path="/map/:animalName" element={<LayoutMapDetails />}>
                  <Route index element={<MapDetails />} />
              </Route>

              <Route path="*" element={<h2>404 - Page Not Found</h2>} />
          </Routes>
      </Router>
  );
}

export default App;

